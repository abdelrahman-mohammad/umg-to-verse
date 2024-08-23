package io.versetools.umgtoverse.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class KeepAliveService {

    private final RestTemplate restTemplate;
    private static final String API_URL = "https://umg-to-verse.onrender.com/keep-alive";

    @Autowired
    public KeepAliveService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Scheduled(fixedRate = 600000) // 600,000 milliseconds = 10 minutes
    public void keepAlive() {
        try {
            String response = restTemplate.getForObject(API_URL, String.class);
            System.out.println("Keep-alive request sent. Response: " + response);
        } catch (Exception e) {
            System.err.println("Error sending keep-alive request: " + e.getMessage());
        }
    }
}