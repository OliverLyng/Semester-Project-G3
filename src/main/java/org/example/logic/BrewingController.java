package org.example.logic;

import org.example.data.OPCUAServerConnection;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.ResponseEntity;


@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://127.0.0.1:8000")  // Adjust as needed for CORS


public class BrewingController {

    @PostMapping("/start")
    public ResponseEntity<String> startBrewing() throws Exception {
        Operations.main(null);
        System.out.println("Brewing process has been started!");
        return ResponseEntity.ok("Brewing process started successfully!");
    }

    @PostMapping("/pause")
    public ResponseEntity<String> pauseBrewing() {
        System.out.println("Brewing process has been paused!");
        return ResponseEntity.ok("Brewing process paused successfully!");
    }

    @PostMapping("/stop")
    public ResponseEntity<String> stopBrewing() {
        System.out.println("Brewing process has been stopped!");
        return ResponseEntity.ok("Brewing process stopped successfully!");
    }
}
