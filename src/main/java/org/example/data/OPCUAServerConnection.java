package org.example.data;

import org.eclipse.milo.opcua.sdk.client.OpcUaClient;
import org.eclipse.milo.opcua.sdk.client.api.config.OpcUaClientConfig;
import org.eclipse.milo.opcua.sdk.client.api.config.OpcUaClientConfigBuilder;
import org.eclipse.milo.opcua.sdk.client.api.identity.AnonymousProvider;
import org.eclipse.milo.opcua.stack.client.DiscoveryClient;
import org.eclipse.milo.opcua.stack.core.types.structured.EndpointDescription;
import org.eclipse.milo.opcua.stack.core.util.EndpointUtil;

import java.util.List;

public class OPCUAServerConnection {
    private OpcUaClient client;
    private final String endpointUrl;
    private boolean isConnected = false;

    public OPCUAServerConnection(String endpointUrl) {
        this.endpointUrl = endpointUrl;
    }

    public OpcUaClient connect() throws Exception {

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
            EndpointDescription updatedEndpoint = EndpointUtil.updateUrl(selectedEndpoint, "127.0.0.1", 4840);

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
        return client;
    }

    public void disconnect() {
        if (client != null && isConnected) {
            client.disconnect().join();
            isConnected = false;
        }
    }

    public boolean isConnected() {
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
