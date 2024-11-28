package org.example.logic;

import org.eclipse.milo.opcua.sdk.client.OpcUaClient;
import org.example.data.NodeRepository;
import org.example.data.OPCUAServerConnection;
import org.example.utils.Nodes;
import org.example.utils.STATES;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.json.JSONObject;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;


import java.io.IOException;
import java.util.concurrent.ExecutionException;

import static org.example.logic.Operations.logger;
import static org.example.utils.Converter.showBeerType;


@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://127.0.0.1:8000")  // Adjust as needed for CORS


public class BrewingController {
    static String endpointUrl = "opc.tcp://localhost:4840";
    private static final Logger logger = LoggerFactory.getLogger(Operations.class);
    Operations operations;
    OPCUAServerConnection connection;
    OpcUaClient client;
    SubscriptionService subscriptionService;
    NodeRepository nodeRepository;


    @PostMapping("/start")
    public ResponseEntity<String> startBrewing() {
        connection = OPCUAServerConnection.getInstance(endpointUrl);
        client = connection.getClient();
        nodeRepository = new NodeRepository(client);

        if (client != null && connection.isConnected()) {
            try {
                operations = new Operations(client);
                operations.clear();
                operations.start();  // Ensure this method properly initiates the brewing process

                STATES states = operations.checkStatus();
                if (states.equals(STATES.STOPPED)) {
                    operations.handleStoppedStatus(states);
                    Thread.sleep(1500);
                    operations.reset();
                    Thread.sleep(1500);
                    operations.start();
                }
                if (states.equals(STATES.EXECUTE)) {
                    System.out.println("Brewing process has been started!");
                    return ResponseEntity.ok("Brewing process started successfully!");
                }
                if (states.equals(STATES.COMPLETE)) {
                    return ResponseEntity.ok("Brewing has finished. " +
                            "BatchID:" + nodeRepository.readNodeValue(Nodes.cmdBatchId) +
                            "\nTotal Beers produced: " + nodeRepository.readNodeValue(Nodes.produced) +
                            "\nDefective amount: " + nodeRepository.readNodeValue(Nodes.prodDefectiveCount));
                }

                return ResponseEntity.ok("Brewing" +
                        "Beer Type: " + showBeerType(Float.parseFloat(nodeRepository.readNodeValue(Nodes.cmdBeerType).getValue().getValue().toString())) +
                        "\nAmount: " + nodeRepository.readNodeValue(Nodes.cmdAmountOfBeer).getValue().getValue().toString() +
                        "\nSpeed: " + nodeRepository.readNodeValue(Nodes.cmdMachSpeed).getValue().getValue().toString());
            } catch (Exception e) {
                System.err.println("Error during brewing start: " + e.getMessage());
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error starting brewing process: " + e.getMessage());
            }
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to start brewing process: Client is not connected.");
        }
    }

    // Stand-in for setting settings
    @PostMapping("/pause")
    public ResponseEntity<String> resetBrewery() throws Exception {
        connection = OPCUAServerConnection.getInstance(endpointUrl);
        client = connection.getClient();
        if (client != null && connection.isConnected()) {
            try {
                nodeRepository = new NodeRepository(client);
                subscriptionService = new SubscriptionService(client);
                operations = new Operations(client);


                // Subscribe to changes on status
                subscriptionService.subscribeToNode(Nodes.stateCurrent, dataValue -> {
                    //System.out.println("New value received for Current State: " + dataValue);
                    logger.info("New value received for Current State: {}", dataValue);
                });
                // Subscribe to changes on produced items
                subscriptionService.subscribeToNode(Nodes.produced, dataValue -> {
                    //System.out.println("New value received for Produced Items: " + dataValue);
                    logger.info("New value received for Produced Items: {}", dataValue);
                });
                subscriptionService.subscribeToNode(Nodes.statusRelativeHumidity, dataValue -> {
                    //System.out.println("New value received for Current State: " + dataValue);
                    logger.info("New value received for Current State: {}", dataValue);
                });
                // Subscribe to changes on produced items
                subscriptionService.subscribeToNode(Nodes.statusTemperature, dataValue -> {
                    //System.out.println("New value received for Produced Items: " + dataValue);
                    logger.info("New value received for Produced Items: {}", dataValue);
                });


                operations.reset();
                System.out.println("Status has been reset!");
                return ResponseEntity.ok("Brewery has been reset!");
            } catch (Exception e) {
                System.err.println("Error during reset: " + e.getMessage());
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error during reset: " + e.getMessage());
            }
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to Error during reset: Client is not connected.");
        }

    }

    @PostMapping("/stop")
    public ResponseEntity<String> stopBrewing() {
        connection = OPCUAServerConnection.getInstance(endpointUrl);
        client = connection.getClient();
        if (client != null && connection.isConnected()) {
            try {
                operations = new Operations(client);
                operations.stop();
                System.out.println("Brewing process stopped successfully!");
                return ResponseEntity.ok("Brewing process stopped successfully!");
            } catch (Exception e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error stopping brewing process: " + e.getMessage());
            }
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to stop brewing process: Client is not connected.");
        }
    }

    @PostMapping("/status")
    public ResponseEntity<String> getBrewingStatus() {
        connection = OPCUAServerConnection.getInstance(endpointUrl);
        client = connection.getClient();
        if (client != null && connection.isConnected()) {
            try {
                Operations operator = new Operations(client);
                STATES currentStatus = operator.checkStatus();
                return ResponseEntity.ok("Current Brewing Status: " + currentStatus);
            } catch (Exception e) {
                logger.error("Error fetching brewing status: ", e);
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error fetching brewing status: " + e.getMessage());
            }
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Client is not connected.");
        }
    }

    @PostMapping("/settings")
    public ResponseEntity<String> getSettings(@RequestBody String payload) throws Exception {
        connection = OPCUAServerConnection.getInstance(endpointUrl);
        client = connection.getClient();
        if (client != null && connection.isConnected()) {
            try {
                operations = new Operations(client);
                System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!" + payload);

                // Converting

//
//                JSONObject json = new JSONObject(payload);
//                System.out.println(json.getString("beerType"));
//                System.out.println(json.optString("beerType"));
//                Float beerType = showBeerType(json.getString("beerType"));
//                System.out.println(beerType);
//                if (beerType == null) {
//                    beerType = 0.0f;
//                }

                JSONObject json = new JSONObject(payload);
                String beerTypeStr = json.optString("beerType", "IPA");
                System.out.println("Beer Type: " + beerTypeStr);
                Float beerType = showBeerType(beerTypeStr);
                System.out.println("Converted Beer Type: " + beerType);

                float amount = json.optFloat("amount", 1000.0f);   // Default to 1000.0 if not present
                float speed = json.optFloat("speed", 300.0f);      // Default to 300.0 if not present

                System.out.println("Setting");
                Settings settings = new Settings(beerType, amount, speed);
                operations.loadSettings(settings);
                System.out.println("Done");
                return ResponseEntity.ok("Brewing settings configured successfully!");
            } catch (Exception e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error stopping brewing process: " + e.getMessage());
            }
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to stop brewing process: Client is not connected.");
        }
    }


    @GetMapping("/brew-status-stream")
    public SseEmitter streamBrewStatus() throws ExecutionException, InterruptedException {
        final SseEmitter emitter = new SseEmitter();
        // Assuming you have a service to handle subscription
        subscriptionService.subscribeToNode(Nodes.produced, dataValue -> {
            try {
                emitter.send(SseEmitter.event().name("update").data(dataValue.getValue().getValue().toString()));
            } catch (IOException e) {
                emitter.completeWithError(e);
                return;
            }
        });
        emitter.onCompletion(() -> {
            try {
                subscriptionService.unsubscribeNode(Nodes.produced);
            } catch (ExecutionException e) {
                throw new RuntimeException(e);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });
        emitter.onTimeout(emitter::complete);
        return emitter;

    }

}
