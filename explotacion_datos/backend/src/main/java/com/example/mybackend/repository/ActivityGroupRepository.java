package com.example.mybackend.repository;

import java.util.List;

import org.springframework.data.neo4j.repository.Neo4jRepository;

import com.example.mybackend.model.ActivityGroup;

public interface ActivityGroupRepository extends Neo4jRepository<ActivityGroup, Long> {
    List<ActivityGroup> findAll();
}
