package org.example.logic;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;
import java.util.HashMap;
import java.util.Map;

public class Inventory {

    private float wheatAmount;

    public Inventory(){
        this.wheatAmount = 0;
    }

    public void setWheatAmount(float wheatAmount){
        this.wheatAmount=wheatAmount;
        postInventory();
    }

    private void postInventory(){
        try {
            Map<String, Object> inventoryData = new HashMap<>();
            inventoryData.put("wheat",10);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<Map<String, Object>> request = new HttpEntity<>(inventoryData,headers);

            String apiUrl = "localhost:8000/inventory";
            RestTemplate restTemplate = new RestTemplate();
            String response = restTemplate.postForObject(apiUrl,request,String.class);

            System.out.println("API RESPONSE" + response);

        }catch(Exception e){
            e.printStackTrace();
            System.err.println("Failed to send inventory update: "+e.getMessage());
        }
    }

    public static void main(String[] args) {
        Inventory updater = new Inventory();

        // Simulate inventory updates
        updater.setWheatAmount(150);
    }

}
