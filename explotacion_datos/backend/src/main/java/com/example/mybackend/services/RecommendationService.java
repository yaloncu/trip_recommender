package com.example.mybackend.services;

import org.neo4j.driver.Driver;
import org.neo4j.driver.Result;
import org.neo4j.driver.Session;
import org.neo4j.driver.exceptions.Neo4jException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * RecommendationService is a service that provides recommendation-related operations using Neo4j.
 * It allows creating recommendations between groups and destinations, retrieving recommendations,
 * voting for a city, and getting the final destination for a group.
 */

public interface RecommendationService {

    /**
     * Create recommendations between a group and destinations based on the group's trip type and audience.
     * @param groupId The ID of the group.
     */
    void createGroupDestinationRecommendations(String groupName);

    /**
     * Retrieve recommendations for a group.
     * @param groupId The ID of the group.
     * @return A list of destination names recommended for the group.
     */
    List<String> getRecommendations(String groupName);

    /**
     * Vote for a city in a group.
     * @param userId The email of the user voting.
     * @param city The name of the city being voted for.
     * @param groupId The ID of the group.
     */
    String voteForCity(String userId, String city, Long groupId);
    /**
     * Get the final destination for a group.
     * @param groupId The ID of the group.
     * @return The name of the final destination for the group.
     */
    String getFinalDestination(Long groupId);    
}

