package org.example.data;

import org.eclipse.milo.opcua.sdk.client.OpcUaClient;
import org.eclipse.milo.opcua.sdk.client.api.UaClient;
import org.eclipse.milo.opcua.stack.core.UaException;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class OPCUAServerConnection {
    private OpcUaClient client;
    private final String endpointUrl;
    private boolean isConnected = false;


    public OPCUAServerConnection(String endpointUrl) {
        this.endpointUrl = endpointUrl;
    }

    public OpcUaClient connect() throws ExecutionException, InterruptedException, UaException {
        // Logic to connect and manage OPC UA client
        // String endpointUrl = "opc.tcp://localhost:4840";  // Change to your server's URL
        if (client == null || isConnected) {
            if (client != null) {
                disconnect();
            }
            // Create and connect client if it doesn't exist
            client = OpcUaClient.create(endpointUrl);
            CompletableFuture<UaClient> connectFuture = client.connect();
            connectFuture.get();
            isConnected = true;  // Set the flag once connected
        }
        return client;
    }
    public void disconnect() {
        if (client != null && isConnected) {
            try {
                CompletableFuture<OpcUaClient> disconnectFuture = client.disconnect();
                disconnectFuture.get(); // Ensures completion
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
        return isConnected;
    }
}
