package com.cr.pr_summarizer.controller;

import com.cr.pr_summarizer.service.AIService;
import com.cr.pr_summarizer.service.GitHubService;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;

@RestController
@RequestMapping("/api/pr")
public class PRController {

    @Autowired
    private GitHubService gitHubService;

    @Autowired
    private AIService aiService;

    @GetMapping("/summary")
    public String getPRSummary(@RequestParam String owner, @RequestParam String repo, @RequestParam int prNumber) {
        try {
            String prDescription = gitHubService.fetchPRDescription(owner, repo, prNumber);
            return aiService.getSummary(prDescription);
        } catch (IOException e) {
            return "Error: " + e.getMessage();
        }
    }
}

