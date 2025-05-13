package com.example.content_moderator.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ContentModerationService {

    @Value("${openai.api.key}")
    private String apiKey;

    public boolean isContentFlagged(String inputText) {
        String url = "https://api.openai.com/v1/moderations";

        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(apiKey);
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String, String> body = new HashMap<>();
        body.put("input", inputText);

        HttpEntity<Map<String, String>> request = new HttpEntity<>(body, headers);

        try {
            ResponseEntity<Map> response = restTemplate.postForEntity(url, request, Map.class);
            Map<String, Object> result = response.getBody();

            if (result != null && result.containsKey("results")) {
                List<Map<String, Object>> results = (List<Map<String, Object>>) result.get("results");
                if (!results.isEmpty()) {
                    Map<String, Object> firstResult = results.get(0);
                    Boolean flagged = (Boolean) firstResult.get("flagged");
                    return flagged != null && flagged;
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("Error while moderating content: " + e.getMessage());
        }

        return false;
    }
}

