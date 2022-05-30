package com.example.Server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerConnection {
    public ServerConnection(int port) {
        try {
            ServerSocket serverSocket = new ServerSocket(port);
            System.out.println("Server open.");
            while (true) {
                Socket socket = null;
                try {
                    socket = serverSocket.accept();
                    System.out.println("Client connected.");
                    DataInputStream dataInputStream = new DataInputStream(socket.getInputStream());
                    DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());
                    Thread thread = new ClientHandler(serverSocket, socket, dataInputStream, dataOutputStream);
                    thread.start();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
