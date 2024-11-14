package org.example.data;

import org.eclipse.milo.opcua.sdk.client.OpcUaClient;
import org.eclipse.milo.opcua.stack.core.Identifiers;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.enumerated.TimestampsToReturn;

import java.util.concurrent.ExecutionException;

public class OPCUAServerConnection {
    private static OPCUAServerConnection instance;
    private OpcUaClient client;
    private final String endpointUrl;
    private static boolean isConnected = false;
    private NodeRepository nodeRepository;


    private OPCUAServerConnection(String endpointUrl) {
        this.endpointUrl = endpointUrl;
    }

    public static synchronized OPCUAServerConnection getInstance(String endpointUrl) throws UaException, ExecutionException, InterruptedException {
        if (instance == null) {
            instance = new OPCUAServerConnection(endpointUrl);
            instance.connect();
        }
        return instance;
    }

    public OpcUaClient connect() throws ExecutionException, InterruptedException, UaException {

        if (client == null || !isConnected) {
            if (client != null) { // client is not connected but exists, safely run disconnect
                disconnect();
            }
            // Create and connect client if it doesn't exist
            client = OpcUaClient.create(endpointUrl);
            client.connect().get();
            isConnected = true;  // Set the flag once connected
            if (nodeRepository != null) {
                nodeRepository.setClient(client); // neither client nor nodeRepository is null
            }
        }
        return client;
    }

    public void setNodeRepository(NodeRepository nodeRepository) {
        this.nodeRepository = nodeRepository;
        if (isConnected() && client != null)  {
            nodeRepository.setClient(client);
        }
    }

    public void disconnect() {
        if (client != null && isConnected) {
            try {
                client.disconnect().get();
                isConnected = false;
                System.out.println("Disconnected from OPC UA server.");
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                System.out.println("Disconnect was interrupted.");
            } catch (ExecutionException e) {
                System.out.println("Failed to disconnect: " + e.getCause().getMessage());
            }
        }
    }

    public boolean isConnected() {
        if (client == null) {
            return false;
        }
        try {
            // Attempt to read a basic attribute of the Server Node, like the Server's Status
            NodeId nodeId = Identifiers.Server_ServerStatus; // Standard node ID for the ServerStatus
            client.readValue(0, TimestampsToReturn.Neither, nodeId).get();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

}

