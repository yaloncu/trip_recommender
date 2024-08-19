package com.example.mybackend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.example.mybackend")
public class MybackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(MybackendApplication.class, args);
	}

}