package com.example.Messages;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * A REST API application used to initiate a communication between
 * to users by providing a way to store the messages between them.
 */

@SpringBootApplication
public class MessagesApplication {

	public static void main(String[] args) {
		SpringApplication.run(MessagesApplication.class, args);
	}

}
