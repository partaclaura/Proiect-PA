package com.example.Server.Connection;

import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * The application that will open a server which will take
 * requests from a client and send responses to it after
 * executing the commands.
 */
@SpringBootApplication
public class ServerApplication {

	public static void main(String[] args) {

		ServerConnection serverConnection = new ServerConnection(6666);
	}

}
