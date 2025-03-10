package com.cr.pr_summarizer.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import okhttp3.*;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

@Service
public class GitHubService {

    @Value("${github.api.token}")
    private String githubToken;

    private static final String GITHUB_API_URL = "https://api.github.com/repos/";

    public String fetchPRDescription(String owner, String repo, int prNumber) throws IOException {
        OkHttpClient client = new OkHttpClient();
        ObjectMapper objectMapper = new ObjectMapper();

        String url = GITHUB_API_URL + owner + "/" + repo + "/pulls/" + prNumber;
        Request request = new Request.Builder()
                .url(url)
                .addHeader("Authorization", "Bearer " + githubToken)
                .addHeader("Accept", "application/vnd.github.v3+json")
                .build();

        Response response = client.newCall(request).execute();
        JsonNode jsonResponse = objectMapper.readTree(response.body().string());

        return jsonResponse.path("title").asText() + ": " + jsonResponse.path("body").asText();
    }
}

