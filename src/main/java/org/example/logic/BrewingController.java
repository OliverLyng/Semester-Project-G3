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

import static org.example.logic.Operations.logger;


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
        if (client != null && connection.isConnected()) {
            try {
                operations = new Operations(client);
                operations.clear();
                operations.start();  // Ensure this method properly initiates the brewing process
                STATES states = operations.checkStatus();
                if (states.equals(STATES.STOPPED)) {
                    operations.handleStoppedStatus(states);
                    operations.reset();
                    operations.start();
                }

                System.out.println("Brewing process has been started!");
                return ResponseEntity.ok("Brewing process started successfully!");
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
    public ResponseEntity<String> pauseBrewing() throws Exception {
        connection = OPCUAServerConnection.getInstance(endpointUrl);
        client = connection.getClient();
        if (client != null && connection.isConnected()) {
            try {
                subscriptionService = new SubscriptionService(client);
                nodeRepository = new NodeRepository(client);

                Operations operator = new Operations(client);

                // Subscribe to changes on status
                subscriptionService.subscribeToNode(Nodes.stateCurrent, dataValue -> {
                    System.out.println("New value received for Current State: " + dataValue);
                    logger.info("New value received for Current State: {}", dataValue);
                });
                // Subscribe to changes on produced items
                subscriptionService.subscribeToNode(Nodes.produced, dataValue -> {
                    System.out.println("New value received for Produced Items: " + dataValue);
                    logger.info("New value received for Produced Items: {}", dataValue);
                });
                operations = new Operations(client);
                operations.loadSettings();
                System.out.println("Settings has been set!");
                return ResponseEntity.ok("Settings has been set!");
            } catch (Exception e) {
                System.err.println("Error during settings start: " + e.getMessage());
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error during settings start: " + e.getMessage());
            }
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to Error during settings start: Client is not connected.");
        }


//        return ResponseEntity.ok("Settings has been set!");
    }

    @PostMapping("/stop")
    public ResponseEntity<String> stopBrewing() {
        connection = OPCUAServerConnection.getInstance(endpointUrl);
        client = connection.getClient();
        if (client != null && connection.isConnected()) {
            try {
                operations = new Operations(client);
                operations.stop();
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

    @PostMapping("/configure")
    public ResponseEntity<String> configureBrewSettings(@RequestBody Settings settings) {
        // Validate settings
        // Apply settings to the brewing process
        return ResponseEntity.ok("Brewing settings configured successfully!");
    }


}
