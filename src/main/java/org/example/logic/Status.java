package org.example.logic;

import org.eclipse.milo.opcua.sdk.client.OpcUaClient;
import org.eclipse.milo.opcua.sdk.client.api.UaClient;
import org.eclipse.milo.opcua.sdk.client.nodes.UaVariableNode;
import org.example.utils.Converter;
import org.example.utils.Nodes;
import org.example.utils.STATES;

import java.util.Objects;
import java.util.concurrent.CompletableFuture;

public class Status {


    UaVariableNode variableNodeState;

    static Status status;


    public static void main(String[] args) throws Exception {
        String endpointUrl = "opc.tcp://localhost:4840";  // Change to your server's URL

        // Build OPC-UA client
        OpcUaClient client = OpcUaClient.create(endpointUrl);

        // Connect to the server
        CompletableFuture<UaClient> connectFuture = client.connect();

        // Block until connection is established
        connectFuture.get();

        System.out.println("Client connected to server: " + endpointUrl);


        status.statusViewer(client);


    }

    public void statusViewer(OpcUaClient client) throws Exception {
        variableNodeState = client.getAddressSpace().getVariableNode(Nodes.stateCurrent);
        UaVariableNode uaVariableNode;
        STATES states = Converter.showState(Integer.parseInt(variableNodeState.readValue().getValue().getValue().toString()));
        switch (Objects.requireNonNull(states)) {
            case ABORTED ->
                    System.out.println("Machine production was aborted");

            case COMPLETE, STOPPED -> {
                System.out.println("Production complete. Resetting...");

            }
            case EXECUTE -> {
                uaVariableNode = client.getAddressSpace().getVariableNode(Nodes.statusMachSpeedState);
                System.out.println("Speed: " + uaVariableNode.readValue().getValue().getValue().toString());

            }
//            case IDLE ->
//                    operator.execute(client);
        }
    }

}
