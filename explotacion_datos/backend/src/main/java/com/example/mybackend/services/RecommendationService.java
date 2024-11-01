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

@Service
public class RecommendationService {

    private final Driver driver;

    public RecommendationService(Driver driver) {
        this.driver = driver;
    }

    public void createGroupDestinationRecommendations(Long groupId) {
        try (Session session = driver.session()) {
            session.executeWrite(tx -> {
                tx.run(
                    "MATCH (g:Group) " +
                    "MATCH (d:Destino) " +
                    "WHERE id(g) = $groupId AND g.tripType IN d.tipo_vacacion " +
                    "AND g.audience IN d.publico_dirigido " +
                    "MERGE (g)-[:RECOMIENDA]->(d) ",
                    Map.of("groupId", groupId)
                );
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

    public void voteForCity(String userId, String city, Long groupId) {
        Objects.requireNonNull(city, "La ciudad no puede ser nula");
        Objects.requireNonNull(userId, "El userId no puede ser nulo");
        Objects.requireNonNull(groupId, "El groupId no puede ser nulo");
    
        try(Session session = driver.session()) {
            session.executeWrite(tx -> {
                tx.run(
                    "MATCH (u:User {email: $userId}), (g:Group), (d:Destino {nombre_destino: $city}) " +
                    "WHERE id(g) = $groupId " + 
                    "CREATE (v:Vote {email: $userId, groupId: $groupId}) " +
                    "MERGE (u)-[:VOTO]->(v) " +
                    "MERGE (g)-[:EN_VOTO]->(v) " +
                    "MERGE (d)-[:VOTADO_EN]->(v)",
                    Map.of("userId", userId, "city", city, "groupId", groupId)
                );
                return null;
            });
        } catch (Neo4jException e) {
            throw new RuntimeException("Error registering vote", e);
        }
    }

    public String getFinalDestination(Long groupId) {
        try (Session session = driver.session()) {
            return session.executeRead(tx -> {
                var result = tx.run(
                    "MATCH (g:Group)-[:EN_VOTO]->(v:Vote)<-[:VOTADO_EN]-(d:Destino) " +
                    "WHERE id(g)=$groupId " +
                    "RETURN d.nombre_destino AS destination, COUNT(v) AS votes " +
                    "ORDER BY votes DESC LIMIT 1", 
                    Map.of("groupId", groupId)
                );
                var record = result.single();
                return record.get("destination").asString();
            });
        } catch (Neo4jException e) {
            throw new RuntimeException("Error al obtener el destino final", e);
        }
    }
    
    
}

