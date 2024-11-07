package org.example.logic;

import org.eclipse.milo.opcua.sdk.client.OpcUaClient;
import org.eclipse.milo.opcua.sdk.client.api.UaClient;
import org.eclipse.milo.opcua.sdk.client.nodes.UaVariableNode;
import org.eclipse.milo.opcua.stack.core.types.builtin.DataValue;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;

import java.util.Objects;
import java.util.concurrent.CompletableFuture;

import org.example.utils.Nodes;
import org.example.utils.STATES;
import org.example.utils.Statifyer;

public class Operations {

    Nodes node;
    static Operations operator;

    Settings settings;

    UaVariableNode variableNodeState;


    public Operations() {
        this.node = new Nodes();
    }

    private void reset(UaClient client) throws Exception {
        System.out.println("In Reset");
        client.writeValue(node.cntrlCmd, DataValue.valueOnly(new Variant(1)));
        client.writeValue(node.cmdChange, DataValue.valueOnly(new Variant(true)));
        System.out.println(Statifyer.showState(Integer.parseInt(variableNodeState.readValue().getValue().getValue().toString())));
    }

    public void clear(UaClient client) throws Exception {
        client.writeValue(node.cntrlCmd, DataValue.valueOnly(new Variant(5)));
        client.writeValue(node.cmdChange, DataValue.valueOnly(new Variant(true)));
    }

    public void execute(OpcUaClient client) throws Exception {

        variableNodeState = client.getAddressSpace().getVariableNode(node.stateCurrent);
        //starts the production
        client.writeValue(node.cntrlCmd, DataValue.valueOnly(new Variant(2)));
        client.writeValue(node.cmdChange, DataValue.valueOnly(new Variant(true)));
        STATES states = Statifyer.showState(Integer.parseInt(variableNodeState.readValue().getValue().getValue().toString()));
        STATES currentState = states;
        System.out.println(currentState);
        while (states != STATES.COMPLETE) {
            while (currentState == states) {
                states = Statifyer.showState(Integer.parseInt(variableNodeState.readValue().getValue().getValue().toString()));
            }
            System.out.println(states);
            currentState = states;
            operator.checkStatus(client);
        }

    }


    public void loadSettings(OpcUaClient client) {

        settings = new Settings(0, 1, 300);

        //chooses the type of beer
        client.writeValue(node.parameter1, DataValue.valueOnly(new Variant(settings.getBeerType())));

        //chooses the amount of beer
        client.writeValue(node.parameter2, DataValue.valueOnly(new Variant(settings.getBeerAmount())));

        //switches speed of the product
        client.writeValue(node.machSpeed, DataValue.valueOnly(new Variant(settings.getMachSpeed())));

        client.writeValue(node.parameter0, DataValue.valueOnly(new Variant(2)));

    }


    public static void main(String[] args) throws Exception {
        String endpointUrl = "opc.tcp://localhost:4840";  // Change to your server's URL

        // Build OPC-UA client
        OpcUaClient client = OpcUaClient.create(endpointUrl);

        // Connect to the server
        CompletableFuture<UaClient> connectFuture = client.connect();

        // Block until connection is established
        connectFuture.get();

        System.out.println("Client connected to server: " + endpointUrl);
        operator = new Operations();

        operator.checkStatus(client);
        operator.reset(client);
        operator.loadSettings(client);
        operator.execute(client);



    }

    private void checkStatus(OpcUaClient client) throws Exception {
        variableNodeState = client.getAddressSpace().getVariableNode(node.stateCurrent);
        STATES states = Statifyer.showState(Integer.parseInt(variableNodeState.readValue().getValue().getValue().toString()));
        switch (Objects.requireNonNull(states)) {
            case ABORTED ->
                System.out.println("Machine production was aborted");

            case COMPLETE, STOPPED -> {
                System.out.println("Production complete. Resetting...");
                operator.reset(client);
            }
            case IDLE ->
                operator.execute(client);
        }
    }
}
