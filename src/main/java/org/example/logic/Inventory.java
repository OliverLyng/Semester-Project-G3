package org.example.logic;

import org.eclipse.milo.opcua.sdk.client.OpcUaClient;
import org.example.data.NodeRepository;
import org.example.data.OPCUAServerConnection;
import org.example.utils.Nodes;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Inventory {

    SubscriptionService subscriptionService;
    static String endpointUrl = "opc.tcp://localhost:4840";
    Operations operations;
    OPCUAServerConnection connection;
    OpcUaClient client;
    NodeRepository nodeRepository;
    private static final Logger logger = LoggerFactory.getLogger(Operations.class);

        public static void main(String[] args) {

            String barley = null;
            String wheat = null;
            String hops = null;
            String yeast = null;
            String malt = null;
            
            OPCUAServerConnection serverConnection = null;
            OpcUaClient client = null;
            
            try{
                serverConnection = OPCUAServerConnection.getInstance(endpointUrl);
                client = serverConnection.connect();
                SubscriptionService subscriptionService = new SubscriptionService(client);
                NodeRepository nodeRepository = new NodeRepository(client);

                barley = nodeRepository.readNodeValue(Nodes.barley).getValue().getValue().toString();
                wheat = nodeRepository.readNodeValue(Nodes.wheat).getValue().getValue().toString();
                malt = nodeRepository.readNodeValue(Nodes.malt).getValue().getValue().toString();
                yeast = nodeRepository.readNodeValue(Nodes.yeast).getValue().getValue().toString();
                hops = nodeRepository.readNodeValue(Nodes.barley).getValue().getValue().toString();


            }catch (Exception e) {
                System.err.println("Error during settings start: " + e.getMessage());
            }

            


            // Create RestTemplate instance
            RestTemplate restTemplate = new RestTemplate();
    
            // URL of the API
            String url = "http://localhost:8000/api/update-inventory";
    
            // Create headers
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
    
            // Create JSON body
            String jsonHops = "{\"name\": \"hops\", \"quantity\": \""+hops+"\", \"id\": 2}";
            String jsonWheat = "{\"name\": \"wheat\", \"quantity\": \""+wheat+"\", \"id\": 4}";
            String jsonBarley = "{\"name\": \"barley\", \"quantity\": \""+barley+"\", \"id\": 1}";
            String jsonMalt = "{\"name\": \"malt\", \"quantity\": \""+malt+"\", \"id\": 3}";
            String jsonYeast = "{\"name\": \"yeast\", \"quantity\": \""+yeast+"\", \"id\": 5}";

            String[] jsonBody = {jsonBarley,jsonHops,jsonMalt,jsonWheat,jsonYeast};
    
            for(int i=0 ; i < jsonBody.length ; i++){
            // Create HttpEntity
            HttpEntity<String> request = new HttpEntity<>(jsonBody[i], headers);
    
            // Send PUT request
            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.PUT, request, String.class);
    
            // Print the response
            System.out.println("Response: " + response.getBody());
            }
        }
    }