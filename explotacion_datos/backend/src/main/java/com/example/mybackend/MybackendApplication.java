package com.example.mybackend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.neo4j.repository.config.EnableNeo4jRepositories;

/**
 * MybackendApplication is the main class that starts the Spring Boot application.
 */
@SpringBootApplication
@EnableNeo4jRepositories(basePackages = "com.example.mybackend.repository")
public class MybackendApplication {

	/**
	 * The main method that starts the Spring Boot application.
	 *
	 * @param args the command line arguments
	 */
	public static void main(String[] args) {
		SpringApplication.run(MybackendApplication.class, args);
	}

	

}