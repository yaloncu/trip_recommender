package com.example.mybackend.services.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.neo4j.driver.Driver;
import org.neo4j.driver.Session;
import org.neo4j.driver.exceptions.Neo4jException;
import org.springframework.stereotype.Service;

import com.example.mybackend.repository.RecommendationRepository;
import com.example.mybackend.services.RecommendationService;

@Service
public class RecommendationServiceImpl implements RecommendationService {
    private final Driver driver;
    private final RecommendationRepository recommendationRepository;

    public RecommendationServiceImpl(Driver driver) {
        this.driver = driver;
        this.recommendationRepository = null;
    }

    /**
     * Create recommendations between a group and destinations based on the group's trip type and audience.
     * @param groupId The ID of the group.
     */
    public void createGroupDestinationRecommendations(Long groupId) {
        recommendationRepository.createGroupDestinationRecommendations(groupId);
    }

    /**
     * Retrieve recommendations for a group.
     * @param groupId The ID of the group.
     * @return A list of destination names recommended for the group.
     */
    public List<String> getRecommendations(Long groupId) {
        return recommendationRepository.getRecommendations(groupId);
    }

    /**
     * Vote for a city in a group.
     * @param userId The email of the user voting.
     * @param city The name of the city being voted for.
     * @param groupId The ID of the group.
     */
    public String voteForCity(String userId, String city, Long groupId) {
        return recommendationRepository.voteForCity(userId, city, groupId);
    }

    /**
     * Get the final destination for a group.
     * @param groupId The ID of the group.
     * @return The name of the final destination for the group.
     */
    public String getFinalDestination(Long groupId) {
        return recommendationRepository.getFinalDestination(groupId);
    }
    
}
