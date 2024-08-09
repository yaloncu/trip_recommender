package com.example.mybackend.repository;

import com.example.mybackend.model.User;
import org.springframework.data.neo4j.repository.Neo4jRepository;

public interface UserRepository extends Neo4jRepository<User, Long> {
    User findByEmail(String email);
}