package org.example.logic;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

public class Inventory {
        public static void main(String[] args) {
            // Create RestTemplate instance
            RestTemplate restTemplate = new RestTemplate();
    
            // URL of the API
            String url = "http://localhost:8000/api/update-inventory";
    
            // Create headers
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
    
            // Create JSON body
            String jsonBody = "{\"name\": \"wheat\", \"quantity\": \"400\", \"id\": \4\}";
    
            // Create HttpEntity
            HttpEntity<String> request = new HttpEntity<>(jsonBody, headers);
    
            // Send PUT request
            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.PUT, request, String.class);
    
            // Print the response
            System.out.println("Response: " + response.getBody());
        }
    }