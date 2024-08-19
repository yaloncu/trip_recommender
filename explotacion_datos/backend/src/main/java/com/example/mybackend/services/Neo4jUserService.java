package com.example.mybackend.services;

import org.neo4j.driver.Driver;
import org.neo4j.driver.Session;
import org.neo4j.driver.Transaction;
import org.neo4j.driver.TransactionWork;
import org.neo4j.driver.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.example.mybackend.model.User;
import com.example.mybackend.repository.UserRepository;

@Service
public class Neo4jUserService {

    @Autowired
    private UserRepository userRepository;

    @Transactional
    public void createUser(String name, String email, String password) {
        System.out.println("Estoy en la clase Neo4jService");
        User user = new User();
        user.setName(name);
        user.setEmail(email);
        user.setPassword(password);
        userRepository.save(user);
    }
}
