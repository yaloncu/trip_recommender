package com.example.mybackend.repository;

import com.example.mybackend.model.User;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.stereotype.Repository;

/**
 * UserRepository is a repository for the User entity.
 */
@Repository
public interface UserRepository extends Neo4jRepository<User, Long> {
    User findByEmail(String email);
    User findByEmailAndPassword(String email, String password);
}