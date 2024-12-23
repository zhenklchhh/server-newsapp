package com.logic.server_newsapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Main application class for the Server News App, using Spring Boot.
 */
@SuppressWarnings({"checkstyle:FileTabCharacter"})
@SpringBootApplication
public class ServerNewsappApplication {

	/**
	 * Main method to run the application.
	 *
	 * @param args command line arguments
	 */
	public static void main(final String[] args) {
		SpringApplication.run(ServerNewsappApplication.class, args);
	}
}
