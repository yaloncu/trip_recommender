package com.example.mybackend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.neo4j.repository.config.EnableNeo4jRepositories;

@SpringBootApplication
@EnableNeo4jRepositories(basePackages = "com.example.mybackend.repository")
public class MybackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(MybackendApplication.class, args);
	}

}