package org.example.logic;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Timer;
import java.util.TimerTask;

public class WebConnector {
    public static void main(String[]args){
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run(){
                sendButtonPress();
            }
        }, 0,5000);
    }


    public static void sendButtonPress() {
        try {
            URL url = new URL("http://localhost:8000/api/button-pressed");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setDoOutput(true);

            String jsonInputString = "{\"buttonPressed\": true}";

            try (OutputStream os = conn.getOutputStream()) {
                byte[] input = jsonInputString.getBytes("utf-8");
                os.write(input, 0, input.length);
            }

            int code = conn.getResponseCode();
            System.out.println("Response code: " + code);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}