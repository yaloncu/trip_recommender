package com.example.mybackend.repository;

import java.util.List;

import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.mybackend.model.Group;

@Repository
public interface RecommendationRepository extends Neo4jRepository<Group, Long> {

    @Query("MATCH (g:Group) " +
            "MATCH (d:Destino) " +
            "WHERE g.name = $groupName AND g.tripType IN d.tipo_vacacion " +
            "AND g.audience IN d.publico_dirigido " +
            "MERGE (g)-[:RECOMIENDA]->(d) ")
    void createGroupDestinationRecommendations(@Param("groupName") String groupName);

    @Query("MATCH (g:Group)-[:RECOMIENDA]->(d:Destino) " +
            "WHERE id(g) = $groupId " +
            "RETURN d.nombre_destino AS destinationName")
    List<String> getRecommendations(@Param("groupId") Long groupId);

    @Query("MATCH (u:User {email: $userId}), (g:Group), (d:Destino {nombre_destino: $city}) " +
            "WHERE id(g) = $groupId " + 
            "CREATE (v:Vote {email: $userId, groupId: $groupId}) " +
            "MERGE (u)-[:VOTO]->(v) " +
            "MERGE (g)-[:EN_VOTO]->(v) " +
            "MERGE (d)-[:VOTADO_EN]->(v)" +
            "RETURN d.nombre_destino")
    String voteForCity(@Param("userId") String userId, @Param("city") String city, @Param("groupId") Long groupId);
    
    @Query("""
        MATCH (g:Group)-[:EN_VOTO]->(v:Vote)<-[:VOTADO_EN]-(d:Destino)
        WHERE g.name = $groupName
        WITH d, COUNT(v) AS voteCount
        ORDER BY voteCount DESC
        LIMIT 1
        RETURN d.nombre_destino
        """)
    String getFinalDestination(@Param("groupName") String groupName);

}
