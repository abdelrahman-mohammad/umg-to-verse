package io.versetools.umgtoverse.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class KeepAliveService {

    private final RestTemplate restTemplate;

    @Value("${API_URL}")
    private String apiUrl;

    @Value("${API_KEY}")
    private String apiKey;

    private int count = 0;

    @Autowired
    public KeepAliveService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Scheduled(fixedRate = 600000) // 600,000 milliseconds = 10 minutes
    public void keepAlive() {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.set("X-API-KEY", apiKey);  // Add the API key as a header

            HttpEntity<String> entity = new HttpEntity<>(headers);

            ResponseEntity<String> response = restTemplate.exchange(
                    apiUrl,
                    HttpMethod.GET,
                    entity,
                    String.class
            );

            System.out.println("Keep-alive request sent. Response: " + response.getBody() + " #" + count++);
        } catch (Exception e) {
            System.err.println("Error sending keep-alive request: " + e.getMessage());
        }
    }
}