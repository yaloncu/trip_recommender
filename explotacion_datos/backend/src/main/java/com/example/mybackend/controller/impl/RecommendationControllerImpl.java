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
import com.example.mybackend.model.VoteRequest;

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
    public List<String> getRecommendations(@PathVariable Long groupId) {
        try {
            return recommendationService.getRecommendations(groupId);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
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
            String result = recommendationService.voteForCity(
                voteRequest.getUserId(), 
                voteRequest.getCity(), 
                voteRequest.getGroupId()
            );
            return ResponseEntity.ok(result);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Parámetros inválidos: " + e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error interno al registrar el voto.");
        }
    }
}
