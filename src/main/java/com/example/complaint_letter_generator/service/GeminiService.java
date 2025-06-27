package com.example.complaint_letter_generator.service;

import org.springframework.stereotype.Service;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.beans.factory.annotation.Value;
import java.util.*;


@Service
public class GeminiService {

    @Value("${gemini.api.key}")
    private String apiKey;

    public String generateComplaintLetter(String problemDescription, String tone) {
        String prompt = "Write a " + tone + " complaint or request letter to customer care regarding this issue:\n\n" + problemDescription;

        String url = "https://generativelanguage.googleapis.com/v1/models/gemini-1.5-flash:generateContent?key=" + apiKey;

        RestTemplate restTemplate = new RestTemplate();

        Map<String, Object> part = Map.of("text", prompt);
        Map<String, Object> content = Map.of("parts", List.of(part));
        Map<String, Object> body = Map.of("contents", List.of(content));

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(body, headers);

        ResponseEntity<Map> response = restTemplate.postForEntity(url, entity, Map.class);

        try {
            List candidates = (List) response.getBody().get("candidates");
            Map candidate = (Map) candidates.get(0);
            Map contentOut = (Map) candidate.get("content");
            List parts = (List) contentOut.get("parts");
            Map firstPart = (Map) parts.get(0);
            return (String) firstPart.get("text");
        } catch (Exception e) {
            return "Error generating letter: " + e.getMessage();
        }
    }
}
