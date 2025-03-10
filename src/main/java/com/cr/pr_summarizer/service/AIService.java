package com.cr.pr_summarizer.service;

import okhttp3.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class AIService {

    private static final String HF_API_URL = "https://api-inference.huggingface.co/models/mistralai/Mistral-7B-Instruct-v0.2";

    @Value("${huggingface.api.key}")
    private String hfApiKey;

    private final OkHttpClient client = new OkHttpClient();

    public String getSummary(String prDescription) {
        // Increase token limit for longer summaries
        String jsonPayload = "{"
                + "\"inputs\": \"" + escapeJson(prDescription) + "\","
                + "\"parameters\": {"
                + "\"max_new_tokens\": 4096,"  // Increased from 2000 to 4096
                + "\"temperature\": 0.5,"     // Adjusted for balanced creativity
                + "\"top_p\": 0.9"           // Helps in generating coherent summaries
                + "}"
                + "}";

        // Build the API Request
        Request request = new Request.Builder()
                .url(HF_API_URL)
                .addHeader("Authorization", "Bearer " + hfApiKey)
                .addHeader("Content-Type", "application/json")
                .post(RequestBody.create(jsonPayload, MediaType.parse("application/json")))
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                return "Error: API request failed with status " + response.code();
            }
            return response.body().string();  // Ensure full response is captured
        } catch (IOException e) {
            return "Error: " + e.getMessage();
        }
    }

    // Helper function to escape JSON
    private String escapeJson(String input) {
        return input.replace("\"", "\\\"");
    }
}

