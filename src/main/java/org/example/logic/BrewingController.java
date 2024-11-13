package org.example.logic;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.ResponseEntity;

@RestController
@RequestMapping("/api")
public class BrewingController {

    @PostMapping("/start")
    public ResponseEntity<String> startBrewing() {
        // Add your backend logic to start the brewing process here.
        System.out.println("Brewing process has been started!");
        
        // Return a success message
        return ResponseEntity.ok("Brewing process started successfully!");
    }
}