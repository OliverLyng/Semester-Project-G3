package org.example.logic;
//
//import org.eclipse.milo.opcua.sdk.client.OpcUaClient;
//import org.example.data.NodeRepository;
//import org.example.data.OPCUAServerConnection;
//import org.example.utils.Nodes;
//import org.example.utils.STATES;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.http.HttpEntity;
//import org.springframework.http.HttpHeaders;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.MediaType;
//import org.springframework.web.bind.annotation.*;
//import org.springframework.web.client.RestTemplate;
//import org.springframework.http.ResponseEntity;
//import org.json.JSONObject;
//import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
//
//import java.io.IOException;
//import java.util.concurrent.ExecutionException;
//
//public class BatchReport{
//
//    SubscriptionService subscriptionService;
//    static String endpointUrl = "opc.tcp://localhost:4840";
//    Operations operations;
//    OPCUAServerConnection connection;
//    OpcUaClient client;
//    NodeRepository nodeRepository;
//    private static final Logger logger = LoggerFactory.getLogger(Operations.class);
//
//
//
//    //public void sendReportData(String produced, String batchID, String defectiveProduce, String productType){
//
//    public static void main(String[] args){
//
//        OPCUAServerConnection serverConnection = null;
//        OpcUaClient client = null;
//
//        String produced = null;
//        String batchID = null;
//        String defectiveProduce = null;
//        String productType = null;
//
//
//        try{
//            serverConnection = OPCUAServerConnection.getInstance(endpointUrl);
//            client = serverConnection.connect();
//            SubscriptionService subscriptionService = new SubscriptionService(client);
//            NodeRepository nodeRepository = new NodeRepository(client);
//
//            produced = nodeRepository.readNodeValue(Nodes.produced).getValue().getValue().toString();
//            batchID = nodeRepository.readNodeValue(Nodes.cmdBatchId).getValue().getValue().toString();
//            defectiveProduce = nodeRepository.readNodeValue(Nodes.prodDefectiveCount).getValue().getValue().toString();
//            productType = nodeRepository.readNodeValue(Nodes.cmdBeerType).getValue().getValue().toString();
//
//
//        }catch (Exception e) {
//            System.err.println("Error during settings start: " + e.getMessage());
//        }
//
//
//        RestTemplate restTemplate = new RestTemplate();
//
//        // URL of the API
//        String url = "http://localhost:8000/api/batch-report";
//
//        // Create headers
//        HttpHeaders headers = new HttpHeaders();
//        headers.setContentType(MediaType.APPLICATION_JSON);
//
//        String jsonBody = "{\"produced\": \""+produced+"\",\"batchID\": \""+batchID+"\", \"defectiveProduce\": \""+defectiveProduce+"\", \"productType\": \""+productType+"\"}";
//
//        // Create HttpEntity
//        HttpEntity<String> request = new HttpEntity<>(jsonBody, headers);
//
//        // Send POST request
//        String response = restTemplate.postForObject(url, request, String.class);
//
//        // Print the response
//        System.out.println("Response: " + response);

//    }
//
//}
//


import org.eclipse.milo.opcua.sdk.client.OpcUaClient;
import org.eclipse.milo.opcua.stack.core.types.builtin.DataValue;
import org.example.data.OPCUAServerConnection;
import org.example.utils.Nodes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class BatchReport {
    private static final Logger logger = LoggerFactory.getLogger(BatchReport.class);

    public static void main(String[] args) {
        OPCUAServerConnection serverConnection;
        OpcUaClient client;
        SubscriptionService subscriptionService;

        try {
            serverConnection = OPCUAServerConnection.getInstance("opc.tcp://localhost:4840");
            client = serverConnection.connect();
            subscriptionService = new SubscriptionService(client);
            CompletableFuture<DataValue> producedFuture = subscriptionService.readNodeAsync(Nodes.produced);
            CompletableFuture<DataValue> defectiveProduceFuture = subscriptionService.readNodeAsync(Nodes.prodDefectiveCount);
            CompletableFuture<DataValue> productTypeFuture = subscriptionService.readNodeAsync(Nodes.cmdBeerType);

            CompletableFuture.allOf(producedFuture, defectiveProduceFuture, productTypeFuture).thenRun(() -> {
                try {
                    String produced = producedFuture.get().getValue().toString();
                    String defectiveProduce = defectiveProduceFuture.get().getValue().toString();
                    String productType = productTypeFuture.get().getValue().toString();

                    sendReportData(produced, defectiveProduce, productType);
                } catch (InterruptedException | ExecutionException e) {
                    logger.error("Failed to retrieve values", e);
                }
            }).exceptionally(ex -> {
                logger.error("Error in reading values", ex);
                return null;
            });

        } catch (Exception e) {
            logger.error("Error during settings start: " + e.getMessage());
        }
    }



    public static void sendReportData(String produced, String defectiveProduce, String productType) {
        RestTemplate restTemplate = new RestTemplate();

        // URL of the API
        String url = "http://localhost:8000/api/batch-report";

        // Create headers
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        String jsonBody = "{\"produced\": \""+produced+"\", \"defectiveProduce\": \""+defectiveProduce+"\", \"productType\": \""+productType+"\"}";

        System.out.println(jsonBody);
        // Create HttpEntity
        HttpEntity<String> request = new HttpEntity<>(jsonBody, headers);

        // Send POST request
        String response = restTemplate.postForObject(url, request, String.class);

        // Print the response
        System.out.println("Response: " + response);

    }
}


