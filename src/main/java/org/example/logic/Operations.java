package org.example.logic;

import org.eclipse.milo.opcua.sdk.client.OpcUaClient;
import org.eclipse.milo.opcua.sdk.client.api.UaClient;
import org.eclipse.milo.opcua.sdk.client.nodes.UaVariableNode;
import org.eclipse.milo.opcua.stack.core.types.builtin.DataValue;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;

import java.util.concurrent.CompletableFuture;

import org.example.utils.Nodes;

public class Operations {

    Nodes node;

    Settings settings;

    public Operations() {
        this.node = new Nodes();
    }

    public void start(UaClient client) throws Exception {
        client.writeValue(node.cntrlCmd, DataValue.valueOnly(new Variant(1)));
        client.writeValue(node.cmdChange, DataValue.valueOnly(new Variant(true)));

        // Adds a short delay for the server to process the commands
        Thread.sleep(500);

        //Verify that the state have been reset
        UaVariableNode stateNode = client.getAddressSpace().getVariableNode(node.stateCurrent);
        System.out.println("The state after resetting is: " + stateNode.readValue().getValue().getValue());
        
    }

    public void loadSettings(OpcUaClient client) throws Exception {

        settings = new Settings(0, 3, 300);

        // chooses the type of beer
        client.writeValue(node.parameter1, DataValue.valueOnly(new Variant(settings.getBeerType())));

        // chooses the amount of beer
        client.writeValue(node.parameter2, DataValue.valueOnly(new Variant(settings.getBeerAmount())));

        // switches speed of the product
        client.writeValue(node.machSpeed, DataValue.valueOnly(new Variant(settings.getMachSpeed())));

        // Sets the batchId
        client.writeValue(node.parameter0, DataValue.valueOnly(new Variant(2)));

    }

    public void execute(OpcUaClient client, UaVariableNode variableNode) throws Exception {

        variableNode = client.getAddressSpace().getVariableNode(node.stateCurrent);

        // starts the production
        client.writeValue(node.cntrlCmd, DataValue.valueOnly(new Variant(2)));
        client.writeValue(node.cmdChange, DataValue.valueOnly(new Variant(true)));

        while (true) {
            // Reads the current production count
            int stateCurrent = Integer.parseInt(variableNode.readValue().getValue().getValue().toString());
            System.out.println("The current state is: " + stateCurrent);

            // Check if production target is met
            if (stateCurrent == 17) {
                System.out.println("Production is now complete");
                break;
            }

            // Adds delay to reduce the amount of print statements in the terminal
            Thread.sleep(2000);
        }

        // resets the machine
        client.writeValue(node.cntrlCmd, DataValue.valueOnly(new Variant(1)));
        client.writeValue(node.cmdChange, DataValue.valueOnly(new Variant(true)));

        Thread.sleep(500);

        // Verify that the stateCurrent node has reset
        UaVariableNode stateNode = client.getAddressSpace().getVariableNode(node.stateCurrent);
        int currentState = Integer.parseInt(stateNode.readValue().getValue().getValue().toString());
        System.out.println("The current state after resetting is: " + currentState);

    }

    public static void main(String[] args) throws Exception {
        String endpointUrl = "opc.tcp://localhost:4840"; // Change to your server's URL

        // Build OPC-UA client
        OpcUaClient client = OpcUaClient.create(endpointUrl);

        // Connect to the server
        CompletableFuture<UaClient> connectFuture = client.connect();

        // Block until connection is established
        connectFuture.get();

        System.out.println("Client connected to server: " + endpointUrl);

        Operations operator = new Operations();
        operator.node = new Nodes();
        UaVariableNode uaVariableNode = client.getAddressSpace().getVariableNode(operator.node.produced);

        operator.start(client);
        operator.loadSettings(client);
        operator.execute(client, uaVariableNode);

        // Examples

        /*Nodes nodes = new Nodes();

        UaVariableNode stateCurrentNode = client.getAddressSpace().getVariableNode(nodes.stateCurrent);

        // Write values
        client.writeValue(nodes.cntrlCmd, DataValue.valueOnly(new Variant(2)));
        client.writeValue(nodes.cmdChange, DataValue.valueOnly(new Variant(true)));

        Thread.sleep(500);

        System.out.println("The current state after restarting production is: " + stateCurrentNode.readValue().getValue().getValue());
        
        */


    }
}
