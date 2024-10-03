package com.example.mybackend.services;

import org.neo4j.driver.Driver;
import org.neo4j.driver.Result;
import org.neo4j.driver.Session;
import org.neo4j.driver.exceptions.Neo4jException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class RecommendationService {

    private final Driver driver;

    public RecommendationService(Driver driver) {
        this.driver = driver;
    }

    public void createGroupDestinationRecommendations(Long groupId) {
        try (Session session = driver.session()) {
            String mostFrequentTravelType = session.executeRead(tx -> {
                Result result = tx.run(
                    "MATCH (g:Group)<-[r:PERTENECE_A]-(u:User) " +
                    "WHERE id(g) = $groupId " +
                    "RETURN g.tripType",
                    Map.of("groupId", groupId)
                );
    
                if (!result.hasNext()) {
                    throw new RuntimeException("No travel type found for group with ID " + groupId);
                }
    
                return result.next().get("travelType").asString();
            });


              session.executeWrite(tx -> {
            Result destinationsResult = tx.run(
                "MATCH (g:Group)<-[:PERTENECE_A]-(u:User) " +
                "MATCH (d:Destino) " +
                "WHERE id(g) = $groupId AND $travelType IN d.tipo_vacacion " +
                "AND g.audience IN d.publico_dirigido " +
                "MERGE (g)-[:RECOMIENDA]->(d) " +
                "RETURN d",
                Map.of("groupId", groupId, "travelType", mostFrequentTravelType)
            );

            if (!destinationsResult.hasNext()) {
                throw new RuntimeException("No destinations found for group with ID " + groupId + " and travel type " + mostFrequentTravelType);
            }

            return null;
        });
    } catch (Neo4jException e) {
        throw new RuntimeException("Error creating recommendations between group and destinations", e);
    }
}

    public List<String> getRecommendations(Long groupId) {
        try (Session session = driver.session()) {
            return session.executeRead(tx -> {
                var result = tx.run(
                    "MATCH (g:Group)<-[:RECOMIENDA]->(d:Destino) " +
                    "WHERE id(g) = $groupId " +
                    "RETURN d.nombre_destino AS destinationName",
                    Map.of("groupId", groupId)
                );
                List<String> destinations = new ArrayList<>();
                while (result.hasNext()) {
                    destinations.add(result.next().get("destinationName").asString());
                }
                return destinations;
            });
        } catch (Neo4jException e) {
            throw new RuntimeException("Error retrieving recommendations", e);
        }
    }
}

