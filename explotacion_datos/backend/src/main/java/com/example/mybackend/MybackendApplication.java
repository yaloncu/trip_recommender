package com.example.mybackend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.example.mybackend.config.Neo4jConnection;
import com.example.mybackend.services.UserService;

@SpringBootApplication(scanBasePackages = "com.example.mybackend")
public class MybackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(MybackendApplication.class, args);
	}

	 @Bean
    public Neo4jConnection neo4jConnection() {
        return new Neo4jConnection("bolt://localhost:7687", "neo4j", "your_password");
    }

    @Bean
    public UserService userService(Neo4jConnection neo4jConnection) {
        return new UserService(neo4jConnection);
    }

}