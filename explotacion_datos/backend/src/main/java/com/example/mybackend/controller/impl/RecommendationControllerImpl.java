package com.example.mybackend.controller.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.mybackend.controller.RecommendationController;
import com.example.mybackend.services.RecommendationService;

@RestController
@RequestMapping("/api")
public class RecommendationControllerImpl implements RecommendationController {
    private final RecommendationService recommendationService;

    public RecommendationControllerImpl(RecommendationService recommendationService) {
        this.recommendationService = recommendationService;
    }

    /**
     * Get recommendations for a specific group.
     *
     * @param groupId the ID of the group
     * @return a list of recommendations for the group
     */
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

    /**
     * Get the final destination for a group.
     *
     * @param groupId the ID of the group
     * @return the final destination for the group
     */
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

    /**
     * Vote for a city in a group.
     *
     * @param voteRequest the vote request
     * @return a response entity with the result of the vote
     */
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

    /**
     * VoteRequest is a class that represents a vote request.
     */
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
