package com.example.mybackend.repository;

import java.util.List;

import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.data.repository.query.Param;

import com.example.mybackend.model.ActivityGroup;
import com.example.mybackend.model.SimpleUserDTO;
import com.example.mybackend.model.User;

public interface ActivityGroupRepository extends Neo4jRepository<ActivityGroup, Long> {
    List<ActivityGroup> findAll();
    @Query("MATCH (u:User {email: $email})<-[:INSCRITO]-(a:ActivityGroup) RETURN a")
    List<ActivityGroup> findByInscritosEmail(String email);

    @Query("MATCH (u:User)<-[:INSCRITO]-(a:ActivityGroup) WHERE id(a) = $activityGroupId " +
       "RETURN u.name AS name, u.email AS email, u.aboutMe AS aboutMe, u.gender AS gender, u.age AS age")
    List<SimpleUserDTO> findParticipantsByActivityId(@Param("activityGroupId") Long activityGroupId);

}
