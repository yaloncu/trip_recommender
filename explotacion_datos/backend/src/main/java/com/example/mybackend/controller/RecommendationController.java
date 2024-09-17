package com.example.mybackend.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.example.mybackend.services.RecommendationService;

import java.util.List;

@RestController
public class RecommendationController {

    private final RecommendationService recommendationService;

    public RecommendationController(RecommendationService recommendationService) {
        this.recommendationService = recommendationService;
    }

    @GetMapping("/api/recommendations/{groupId}")
    public ResponseEntity<List<String>> getRecommendations(@PathVariable Long groupId) {
        try {
            recommendationService.createGroupDestinationRecommendations(groupId);
            List<String> recommendations = recommendationService.getRecommendations(groupId);
            return ResponseEntity.ok(recommendations);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
}
