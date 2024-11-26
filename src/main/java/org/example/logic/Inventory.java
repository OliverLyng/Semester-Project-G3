package org.example.logic;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;

public class Inventory {
    public static void main(String[] args) {
        // Create RestTemplate instance
        RestTemplate restTemplate = new RestTemplate();

        // URL of the API
        String url = "http://localhost:8000/api/inventory";

        // Create headers
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        // Create JSON body
        String jsonBody = "{\"name\": \"wheat\", \"quantity\": \"300\"}";

        // Create HttpEntity
        HttpEntity<String> request = new HttpEntity<>(jsonBody, headers);

        // Send POST request
        String response = restTemplate.postForObject(url, request, String.class);

        // Print the response
        System.out.println("Response: " + response);
    }
}
