package com.example.mybackend.repository;

import com.example.mybackend.model.Group;
import org.springframework.data.neo4j.repository.Neo4jRepository;

public interface GroupRepository extends Neo4jRepository<Group, Long> {
    Group findByName(String name);
}