package org.example.logic;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://127.0.0.1:8000")
public class BrewingController {

    private static final Logger log = LoggerFactory.getLogger(BrewingController.class);

    @PostMapping("/start")
    public ResponseEntity<String> startBrewing() {
        try {
            Operations.main(null);
            log.info("Brewing process has been started!");
            return ResponseEntity.ok("Brewing process started successfully!");
        } catch (Exception e) {
            log.error("Failed to start brewing", e);
            return ResponseEntity.internalServerError().body("Failed to start brewing: " + e.getMessage());
        }
    }

    @PostMapping("/pause")
    public ResponseEntity<String> pauseBrewing() {
        try {
            // Antag at der er logik her for at pause brygningen
            log.info("Brewing process has been paused!");
            return ResponseEntity.ok("Brewing process paused successfully!");
        } catch (Exception e) {
            log.error("Failed to pause brewing", e);
            return ResponseEntity.internalServerError().body("Failed to pause brewing: " + e.getMessage());
        }
    }

    @PostMapping("/stop")
    public ResponseEntity<String> stopBrewing() {
        try {
            // Antag at der er logik her for at stoppe brygningen
            log.info("Brewing process has been stopped!");
            return ResponseEntity.ok("Brewing process stopped successfully!");
        } catch (Exception e) {
            log.error("Failed to stop brewing", e);
            return ResponseEntity.internalServerError().body("Failed to stop brewing: " + e.getMessage());
        }
    }
}
