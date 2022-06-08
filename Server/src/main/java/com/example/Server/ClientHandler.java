package com.example.Server;

import com.example.Server.Databases.DatabaseConnection;
import com.example.Server.Databases.FriendDAO;
import com.example.Server.Databases.UserDAO;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

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

    private void handleCommands(String command, String parameters)
    {
        try {
            var user = new UserDAO();
            var friendship = new FriendDAO();
            String[] p = parameters.split(" ");
            System.out.println(command);
            switch (command) {
                case "login":
                    System.out.println("Client wants to login");
                    if (p.length == 2 && user.findUser(p[0], p[1]))//searching for user in the db
                    {
                        dataOutputStream.writeUTF("Login successful.");
                        System.out.println("Sending");

                    }
                    else {
                        dataOutputStream.writeUTF("Wrong username or password.");
                        System.out.println("Sending");
                    }
                    break;
                case "signup":
                    System.out.println("Client wants to signup");
                    if(p.length == 0 || user.getIdByUsername(p[0]) != 0)
                    {
                        dataOutputStream.writeUTF("Username already exists/Not enough data.");
                        System.out.println("Sending");
                    }
                    else {
                        user.create(1, p[0], p[1]);
                        try {
                            DatabaseConnection.getConnection().commit();
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                        if (user.findUser(p[0], p[1]))
                        {
                            dataOutputStream.writeUTF("Signup successful.");
                            System.out.println("Sending");
                        }
                        else {
                            dataOutputStream.writeUTF("Signup error.");
                            System.out.println("Sending");
                        }
                    }
                    break;
                case "friend":
                    System.out.println("Client " + p[0] + " wants to friend " + p[1]);
                    if(p.length == 2) {
                        if (user.getIdByUsername(p[1]) != 0 &&
                                !friendship.exists(user.getIdByUsername(p[0]), user.getIdByUsername(p[1]))) {
                            System.out.println(p[1] + " exists.");
                            friendship.create(user.getIdByUsername(p[0]), user.getIdByUsername(p[1]));
                            try {
                                DatabaseConnection.getConnection().commit();
                            } catch (SQLException e) {
                                e.printStackTrace();
                            }
                            dataOutputStream.writeUTF("Friendship successful");
                            System.out.println("Sending");
                        }
                        else dataOutputStream.writeUTF("User does not exist/ already friends");
                    }
                    else {
                        dataOutputStream.writeUTF("Not enough data.");
                        System.out.println("Sending");
                    }
                    break;
                case "getFriends":
                    System.out.println("Client wants to get friends.");
                    if(p.length == 1) {
                        List<Integer> ids = friendship.getAllFriends(user.getIdByUsername(p[0]));
                        List<String> usernames = new ArrayList<>();
                        for (Integer fid : ids)
                            usernames.add(user.getUsernameById(fid));
                        String sendFriends = "";
                        for (String friends : usernames)
                            sendFriends = sendFriends.concat(friends + ",");
                        dataOutputStream.writeUTF(sendFriends);
                    }
                    else dataOutputStream.writeUTF("error");
                    break;
                case "send":
                    System.out.println(p.length);
                    if(p.length > 2) {
                        String tosend = p[0] + "," + p[1] + "," + p[2];
                        APIConnection.PostMessage(tosend);
                        dataOutputStream.writeUTF("Message sent");
                    }
                    else dataOutputStream.writeUTF("Can't send message.");
                    break;
                case "getmess":
                    System.out.println("Client wants to get messages");
                    System.out.println("Get messages for " + p[0]);
                    if(p.length == 1) {
                        dataOutputStream.writeUTF(APIConnection.GetMessage(p[0]));
                    }
                    else dataOutputStream.writeUTF("Error");
                    break;
                default:
                    System.out.println("Invalid command.");
                    dataOutputStream.writeUTF("Invalid command.");
                    System.out.println("Sending");
                    break;
            }
            dataOutputStream.flush();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

    }
}

