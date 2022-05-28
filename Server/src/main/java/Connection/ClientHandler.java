package Connection;

import Databases.DatabaseConnection;
import Databases.UserDAO;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.SQLException;

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
            String response;
            while (true && !terminated) {
                try {
                    if (serverSocket.isClosed())
                        break;
                    request = dataInputStream.readUTF();
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
            String[] p = parameters.split(" ");
            switch (command) {
                case "login":
                    System.out.println("Client wants to login");
                    if (user.findUser(p[0], p[1]))//searching for user in the db
                        dataOutputStream.writeUTF("Login successful.");
                    else dataOutputStream.writeUTF("Wrong username or password.");
                    break;
                case "signup":
                    System.out.println("Client wants to signup");
                    if(user.getIdByUsername(p[0]) != 0)
                        dataOutputStream.writeUTF("Username already exists.");
                    else {
                        user.create(1, p[0], p[1]);
                        try {
                            DatabaseConnection.getConnection().commit();
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                        if (user.findUser(p[0], p[1]))
                            dataOutputStream.writeUTF("Signup successful.");
                        else dataOutputStream.writeUTF("Signup error.");
                    }

                default:
                    System.out.println("Invalid command.");
                    dataOutputStream.writeUTF("Invalid command.");
                    break;
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

    }
}

