package com.example.Server.Connection;

import com.example.Server.Commands.FriendCommand;
import com.example.Server.Commands.MessageCommand;
import com.example.Server.Commands.UserCommand;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;


public class ClientHandler extends Thread{
    private boolean terminated;
    final ServerSocket serverSocket;
    final Socket socket;
    final DataInputStream dataInputStream;
    final DataOutputStream dataOutputStream;
    public ClientHandler(ServerSocket serverSocket, Socket socket,
                         DataInputStream dataInputStream, DataOutputStream dataOutputStream)
    {
        this.serverSocket = serverSocket;
        this.socket = socket;
        this.dataInputStream = dataInputStream;
        this.dataOutputStream = dataOutputStream;
        this.terminated = false;
    }

    @Override
    public void run()
    {
        while(!terminated) {
            String request = "";
            //String response;
            while (!terminated) {
                try {
                    if (serverSocket.isClosed())
                        break;
                    System.out.println("Waiting for request");
                    request = dataInputStream.readUTF();
                    System.out.println("Got for request: " + request);
                    String command = "";
                    String parameters = "";
                    if (request.contains(" ")) {
                        int i = 0;
                        while (request.charAt(i) != ' ') {
                            i++;
                        }
                        command = request.substring(0, i);
                        parameters = request.substring(i + 1);
                        System.out.println("Got " + command + " " + parameters);
                    } else command = request;
                    handleCommands(command, parameters);
                } catch (IOException e) {
                    this.terminated = true;
                    e.printStackTrace();
                }
            }
            try {
                this.dataInputStream.close();
                this.dataOutputStream.close();
                this.socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * This function is used to analyze the command from the client and
     * invoke the methods/classes qualified to execute the request and
     * send a response to the client.
     * @param command A string representing the request from the client.
     * @param parameters A string of parameters associated with the given request.
     */
    private void handleCommands(String command, String parameters)
    {
        FriendCommand friendCommand = new FriendCommand();
        MessageCommand messageCommand = new MessageCommand();
        UserCommand userCommand = new UserCommand();
        try {
            String[] p = parameters.split(" ");
            System.out.println(command);
            switch (command) {
                case "login" -> {
                    System.out.println("Client wants to login");
                    userCommand.login(this.dataOutputStream, p);
                }
                case "signup" -> {
                    System.out.println("Client wants to signup");
                    userCommand.signup(this.dataOutputStream, p);
                }
                case "friend" -> {
                    System.out.println("Client " + p[0] + " wants to friend " + p[1]);
                    friendCommand.friend(this.dataOutputStream, p);
                }
                case "getFriends" -> {
                    System.out.println("Client wants to get friends.");
                    friendCommand.getFriends(this.dataOutputStream, p);
                }
                case "send" -> {
                    System.out.println(p.length);
                    messageCommand.send(this.dataOutputStream, p);
                }
                case "getmess" -> {
                    System.out.println("Client wants to get messages");
                    System.out.println("Get messages for " + p[0]);
                    messageCommand.getMessages(this.dataOutputStream, p);
                }
                default -> {
                    System.out.println("Invalid command.");
                    dataOutputStream.writeUTF("Invalid command.");
                    System.out.println("Sending");
                }
            }
            dataOutputStream.flush();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

    }
}

