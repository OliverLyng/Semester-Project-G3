package org.example.logic;

import org.eclipse.milo.opcua.sdk.client.OpcUaClient;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.example.data.NodeRepository;
import org.example.data.OPCUAServerConnection;
import org.example.utils.Converter;
import org.example.utils.EndpointUrl;
import org.example.utils.Nodes;
import org.example.utils.STATES;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.jackson.JsonComponentModule;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.json.JSONObject;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;


import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ExecutionException;


import static org.example.utils.Converter.showBeerType;
import org.example.logic.BatchReport;


@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://127.0.0.1:8000")  // Adjust as needed for CORS


public class BrewingController {
    static String endpointUrl = EndpointUrl.getEndpointUrl();
    //static String endpointUrl = "opc.tcp://localhost:4840";
    private static final Logger logger = LoggerFactory.getLogger(Operations.class);
    private final JsonComponentModule jsonComponentModule;
    Operations operations;
    OPCUAServerConnection connection;
    OpcUaClient client;
    SubscriptionService subscriptionService;
    NodeRepository nodeRepository;
    BatchReport report = new BatchReport();

    public BrewingController(JsonComponentModule jsonComponentModule) {
        this.jsonComponentModule = jsonComponentModule;
    }


    @PostMapping("/start")
    public ResponseEntity<String> startBrewing() throws UaException {
        connection = OPCUAServerConnection.getInstance(endpointUrl);
        client = connection.getClient();
        nodeRepository = new NodeRepository(client);
        int beerTypeInt = (Math.round(Float.parseFloat(nodeRepository.readNodeValue(Nodes.cmdBeerType).getValue().getValue().toString())));
        String beerType = "Beer Type: " + showBeerType(beerTypeInt);
        String beer = showBeerType(beerTypeInt).toString(); // For report
        String amount = "Amount: " + nodeRepository.readNodeValue(Nodes.cmdAmountOfBeer).getValue().getValue().toString();
        String produced = nodeRepository.readNodeValue(Nodes.cmdAmountOfBeer).getValue().getValue().toString(); // For report
        String speed = "Speed: " + nodeRepository.readNodeValue(Nodes.cmdMachSpeed).getValue().getValue().toString();
        final String[] defect = {"Defect: "};
        if (client != null && connection.isConnected()) {
            try {
                operations = new Operations(client);
                //operations.clear();
                operations.start();  // Ensure this method properly initiates the brewing process

                subscriptionService.subscribeToNode(Nodes.stateCurrent, dataValue -> {
                    try {
                        String status = dataValue.getValue().getValue().toString();
                        STATES currentState = operations.checkStatus(status);
                        if (currentState.equals(STATES.COMPLETE)) {

                            BatchReport batchReport = new BatchReport();
                            batchReport.sendReportData(
                                    produced,
                                    nodeRepository.readNodeValue(Nodes.prodDefectiveCount).getValue().getValue().toString(),
                                    beer);
                        }


                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                    System.out.println("New value received for Current State: " + dataValue);
                    logger.info("New value received for Current State: {}", dataValue);
                });

//                STATES states = operations.checkStatus();
//                if (states.equals(STATES.STOPPED)) {
//                    operations.handleStoppedStatus(states);
//                    Thread.sleep(1500);
//                    operations.reset();
//                    Thread.sleep(1500);
//                    operations.start();
//                }
//                if (states.equals(STATES.EXECUTE)) {
//                    System.out.println("Brewing process has been started!");
//                    return ResponseEntity.ok("Brewing process started successfully!");
//                }

            } catch (Exception e) {
                System.err.println("Error during brewing start: " + e.getMessage());
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error starting brewing process: " + e.getMessage());
            }
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to start brewing process: Client is not connected.");
        }
        return ResponseEntity.status(HttpStatus.OK).body(beerType + "\n" + amount + "\n"  + speed);
    }

    // Stand-in for setting settings
    @PostMapping(value = "/reset", consumes = "application/json")
    public ResponseEntity<String> resetBrewery(@RequestBody(required = false) Map<String, Object> payload) throws Exception {
        connection = OPCUAServerConnection.getInstance(endpointUrl);
        client = connection.getClient();
        if (client != null && connection.isConnected()) {
            try {
                nodeRepository = new NodeRepository(client);
                subscriptionService = new SubscriptionService(client);
                operations = new Operations(client);

//
//                // Subscribe to changes on status
//                subscriptionService.subscribeToNode(Nodes.stateCurrent, dataValue -> {
//                    //System.out.println("New value received for Current State: " + dataValue);
//                    logger.info("New value received for Current State: {}", dataValue);
//                });
//                // Subscribe to changes on produced items
//                subscriptionService.subscribeToNode(Nodes.produced, dataValue -> {
//                    //System.out.println("New value received for Produced Items: " + dataValue);
//                    logger.info("New value received for Produced Items: {}", dataValue);
//                });
//                subscriptionService.subscribeToNode(Nodes.statusRelativeHumidity, dataValue -> {
//                    //System.out.println("New value received for Current State: " + dataValue);
//                    logger.info("New value received for Current State: {}", dataValue);
//                });
//                // Subscribe to changes on produced items
//                subscriptionService.subscribeToNode(Nodes.statusTemperature, dataValue -> {
//                    //System.out.println("New value received for Produced Items: " + dataValue);
//                    logger.info("New value received for Produced Items: {}", dataValue);
//                });
//

                operations.reset();
                return ResponseEntity.ok("Ready to brew!!");
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
    public ResponseEntity<Map<String,String>> getSettings(@RequestBody String payload) throws Exception {
        connection = OPCUAServerConnection.getInstance(endpointUrl);
        client = connection.getClient();
        if (client != null && connection.isConnected()) {
            try {
                operations = new Operations(client);

                JSONObject json = new JSONObject(payload);
                String beerTypeStr = json.optString("beerType", "IPA");
                System.out.println("Beer Type: " + beerTypeStr);
                Float beerType = showBeerType(beerTypeStr);
                System.out.println("Converted Beer Type: " + beerType);

                float amount = json.optFloat("amount", 100.0f);   // Default to 1000.0 if not present
                float speed = json.optFloat("speed", 300.0f);      // Default to 300.0 if not present

                System.out.println("Setting");
                Settings settings = new Settings(beerType, amount, speed);
                operations.loadSettings(settings);
                //System.out.println("Done");
                Map<String, String> response = new HashMap<>();
                response.put("message", "Brewing settings configured successfully!");
                return ResponseEntity.ok(response);
            } catch (Exception e) {
                Map<String, String> errorResponse = new HashMap<>();
                errorResponse.put("error", "Error stopping brewing process: " + e.getMessage());
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
            }
        } else {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "Failed to stop brewing process: Client is not connected.");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    @GetMapping("/brew-status-stream")
    public SseEmitter streamBrewStatus() throws ExecutionException, InterruptedException {
        final SseEmitter emitter = new SseEmitter(0L);
        // Assuming you have a service to handle subscription
        subscriptionService.subscribeToNode(Nodes.produced, dataValue -> {
            try {
                emitter.send(SseEmitter.event().name("produced").data(dataValue.getValue().getValue().toString()));
            } catch (IOException e) {
                emitter.completeWithError(e);
            }
        });
        subscriptionService.subscribeToNode(Nodes.temperature, dataValue -> {
            try {
                emitter.send(SseEmitter.event().name("temperature").data(dataValue.getValue().getValue().toString()));
            } catch (IOException e) {
                emitter.completeWithError(e);
            }
        });
        subscriptionService.subscribeToNode(Nodes.humidity, dataValue -> {
            try {
                emitter.send(SseEmitter.event().name("humidity").data(dataValue.getValue().getValue().toString()));
            } catch (IOException e) {
                emitter.completeWithError(e);
            }
        });
        subscriptionService.subscribeToNode(Nodes.stateCurrent, dataValue -> {
            try {
                emitter.send(SseEmitter.event().name("state")
                        .data(Objects.requireNonNull(
                                Converter.showState(
                                        Integer.parseInt(nodeRepository.readNodeValue(
                                                Nodes.stateCurrent).getValue().getValue().toString())))));
            } catch (IOException e) {
                emitter.completeWithError(e);
            } catch (UaException e) {
                throw new RuntimeException(e);
            }
        });
        subscriptionService.subscribeToNode(Nodes.prodDefectiveCount, dataValue -> {
            try {
                emitter.send(SseEmitter.event().name("defective").data(dataValue.getValue().getValue().toString()));
            } catch (IOException e) {
                emitter.completeWithError(e);
            }
        });
        subscriptionService.subscribeToNode(Nodes.barley, dataValue -> {
            try {
                emitter.send(SseEmitter.event().name("barley").data(dataValue.getValue().getValue().toString()));
            } catch (IOException e) {
                emitter.completeWithError(e);
            }
        });
        subscriptionService.subscribeToNode(Nodes.malt, dataValue -> {
            try {
                emitter.send(SseEmitter.event().name("malt").data(dataValue.getValue().getValue().toString()));
            } catch (IOException e) {
                emitter.completeWithError(e);
            }
        });
        subscriptionService.subscribeToNode(Nodes.yeast, dataValue -> {
            try {
                emitter.send(SseEmitter.event().name("yeast").data(dataValue.getValue().getValue().toString()));
            } catch (IOException e) {
                emitter.completeWithError(e);
            }
        });
        subscriptionService.subscribeToNode(Nodes.wheat, dataValue -> {
            try {
                emitter.send(SseEmitter.event().name("wheat").data(dataValue.getValue().getValue().toString()));
            } catch (IOException e) {
                emitter.completeWithError(e);
            }
        });
        subscriptionService.subscribeToNode(Nodes.wheat, dataValue -> {
            try {
                emitter.send(SseEmitter.event().name("wheat").data(dataValue.getValue().getValue().toString()));
            } catch (IOException e) {
                emitter.completeWithError(e);
            }
        });
        subscriptionService.subscribeToNode(Nodes.hops, dataValue -> {
            try {
                emitter.send(SseEmitter.event().name("hops").data(dataValue.getValue().getValue().toString()));
            } catch (IOException e) {
                emitter.completeWithError(e);
            }
        });

        emitter.onCompletion(emitter::complete);
        emitter.onTimeout(emitter::complete);

        return emitter;
    }

}
