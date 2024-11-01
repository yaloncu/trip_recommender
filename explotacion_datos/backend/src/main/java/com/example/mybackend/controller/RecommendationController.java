package com.example.mybackend.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.mybackend.services.RecommendationService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class RecommendationController {

    private final RecommendationService recommendationService;

    public RecommendationController(RecommendationService recommendationService) {
        this.recommendationService = recommendationService;
    }

    @GetMapping("/recommendations/{groupId}")
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

    @GetMapping("/groups/{groupId}/finalDestination")
    public ResponseEntity<Map<String, String>> getFinalDestination(@PathVariable Long groupId) {
        try {
            String finalDestination = recommendationService.getFinalDestination(groupId);
            if (finalDestination != null) {
                Map<String, String> response = new HashMap<>();
                response.put("destination", finalDestination);
                return ResponseEntity.ok(response);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @PostMapping("/vote")
    public ResponseEntity<String> voteForCity(@RequestBody VoteRequest voteRequest) {
        try {
            recommendationService.voteForCity(voteRequest.getUserId(), voteRequest.getCity(), voteRequest.getGroupId());
            return ResponseEntity.ok("Voto registrado con éxito.");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al registrar el voto." + voteRequest.getUserId()+ voteRequest.getCity()+ voteRequest.getGroupId().toString());
        }
    }

    public static class VoteRequest {
        private Long groupId; 
        private String userId;
        private String city;

        // Getters y Setters
        public String getUserId() { return userId; }
        public void setUserId(String userId) { this.userId = userId; }
        public String getCity() { return city; }
        public void setCity(String city) { this.city = city; }
        public Long getGroupId() { return groupId; }
        public void setGroupId(Long groupId) { this.groupId = groupId; }
    }    
}
