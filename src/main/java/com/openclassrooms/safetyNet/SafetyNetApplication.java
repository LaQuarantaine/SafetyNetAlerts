package com.openclassrooms.safetyNet;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication		// annotation de la classe principale en langage "Spring"
public class SafetyNetApplication {
	
	public static void main(String[] args) {
		// démarre Spring Boot et initialise le contexte Spring
		SpringApplication.run(SafetyNetApplication.class, args);
	}

}
