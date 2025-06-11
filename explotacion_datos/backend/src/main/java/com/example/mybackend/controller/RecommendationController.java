package com.example.mybackend.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.mybackend.controller.impl.RecommendationControllerImpl.VoteRequest;
import com.example.mybackend.services.RecommendationService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * RecommendationController is a REST controller that handles HTTP requests related to recommendations.
 * It provides endpoints to get recommendations for groups.
 */
@RequestMapping("/api")
public interface RecommendationController {

    /**
     * Get recommendations for a specific group.
     *
     * @param groupId the ID of the group
     * @return a list of recommendations for the group
     */
    @GetMapping("/recommendations/{groupId}")
    List<String> getRecommendations(@PathVariable String groupName);

    /**
     * Get the final destination for a group.
     *
     * @param groupId the ID of the group
     * @return the final destination for the group
     */
    @GetMapping("/groups/{groupId}/finalDestination")
    String getFinalDestination(@PathVariable Long groupId);

    /**
     * Vote for a city in a group.
     *
     * @param voteRequest the vote request
     * @return a response entity with the result of the vote
     */
    @PostMapping("/vote")
    String voteForCity(@RequestBody VoteRequest voteRequest);  
}
