package org.example.data;

import org.eclipse.milo.opcua.sdk.client.OpcUaClient;
import org.eclipse.milo.opcua.sdk.client.api.config.OpcUaClientConfig;
import org.eclipse.milo.opcua.sdk.client.api.config.OpcUaClientConfigBuilder;
import org.eclipse.milo.opcua.sdk.client.api.identity.AnonymousProvider;
import org.eclipse.milo.opcua.stack.client.DiscoveryClient;
import org.eclipse.milo.opcua.stack.core.types.structured.EndpointDescription;
import org.eclipse.milo.opcua.stack.core.util.EndpointUtil;

import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import org.example.utils.EndpointUrl;

public class OPCUAServerConnection {
    private static OPCUAServerConnection instance;
    private OpcUaClient client;
    private final String endpointUrl;
    private boolean isConnected = false;
    //private String selectedEndpointMachine = "192.168.0.122";
    //private String getSelectedEndpointSimulation = "localhost";

    String selectedEndpointMachine = EndpointUrl.getSelectedEndpoint();
    private OPCUAServerConnection(String endpointUrl) {
        this.endpointUrl = endpointUrl;
    }
    public static synchronized OPCUAServerConnection getInstance(String endpointUrl) {
        if (instance == null) {
            instance = new OPCUAServerConnection(endpointUrl);
        }
        return instance;
    }

    public synchronized OpcUaClient connect() throws Exception {
        try {

            if (client == null || !isConnected) {
                if (client != null) {
                    disconnect();
                }


                // Discover endpoints using the provided URL
                List<EndpointDescription> endpoints = DiscoveryClient.getEndpoints(endpointUrl).get();

                // Optionally filter for "No Security"
                EndpointDescription selectedEndpoint = endpoints.stream()
                        .filter(e -> e.getSecurityPolicyUri().equals("http://opcfoundation.org/UA/SecurityPolicy#None"))
                        .findFirst()
                        .orElseThrow(() -> new Exception("No 'No Security' endpoints found"));

                // Update the endpoint URL to ensure it matches the local configuration
                EndpointDescription updatedEndpoint = EndpointUtil.updateUrl(selectedEndpoint, selectedEndpointMachine, 4840);

                // Configure the client
                OpcUaClientConfig config = OpcUaClientConfig.builder()
                        .setEndpoint(updatedEndpoint)
                        .setApplicationUri("urn:example:client")
                        .setIdentityProvider(new AnonymousProvider())
                        .build();

                // Create and connect the client
                client = OpcUaClient.create(config);
                client.connect().get();

                isConnected = true;
            }
        } catch (Exception e) {
            isConnected = false;
            scheduleReconnect();  // schedule a reconnection attempt
            throw e;  // rethrow exception to indicate initial connection failure
        }
        return client;
    }

    public synchronized OpcUaClient getClient() {
        try {
            if (client == null || !isConnected) {
                connect();
            }
        } catch (Exception e) {
            System.err.println("Failed to connect to OPC UA server: " + e.getMessage());
            // Optionally retry or handle error as needed
            isConnected = false;
        }
        return client;
    }

    private synchronized void scheduleReconnect() {
        Executors.newSingleThreadScheduledExecutor().schedule(() -> {
            System.out.println("Attempting to reconnect...");
            try {
                connect();
            } catch (Exception e) {
                System.err.println("Reconnection attempt failed: " + e.getMessage());
            }
        },10, TimeUnit.SECONDS);
    }

        public synchronized void disconnect() {
        if (client != null && isConnected) {
            client.disconnect().join();
            isConnected = false;
        }
    }

    public synchronized boolean isConnected() {
        return isConnected;
    }

//
//    public OpcUaClient connect() throws Exception {
//        if (client == null || !isConnected) {
//            if (client != null) {
//                disconnect();
//            }
//
//            // Discover endpoints
//            List<EndpointDescription> endpoints = DiscoveryClient.getEndpoints("opc.tcp://127.0.0.1:4840").get();
//            EndpointDescription configPoint = EndpointUtil.updateUrl(endpoints.get(0), "127.0.0.1", 4840);
//
//            OpcUaClientConfigBuilder cfg = new OpcUaClientConfigBuilder();
//            cfg.setEndpoint(configPoint);
//
//            this.client = OpcUaClient.create(cfg.build());
//            client.connect().get(); // Connect to the server
//            isConnected = true;
//        }
//        return client;
//    }
}
