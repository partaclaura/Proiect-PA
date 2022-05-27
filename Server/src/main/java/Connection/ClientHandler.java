package Connection;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ClientHandler extends Thread{
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
    }

    @Override
    public void run()
    {
        String request = "";
        String response;
        while (true)
        {
            try{
                if(serverSocket.isClosed())
                    break;
                request = dataInputStream.readUTF();
                String command = "";
                String parameters = "";
                if(request.contains(" ")) {
                    int i = 0;
                    while (request.charAt(i) != ' ') {
                        i++;
                    }
                    command = request.substring(0, i);
                    parameters = request.substring(i + 1);
                    System.out.println("Got " + command + " " + parameters);
                }
                else command = request;
                switch(command)
                {
                    case "login":
                        System.out.println("Client wants to login");
                        dataOutputStream.writeUTF("Login request received.");
                        break;
                    default:
                        System.out.println("Invalid command.");
                        dataOutputStream.writeUTF("Invalid command.");
                        break;
                }
            } catch (IOException e)
            {
                e.printStackTrace();
            }
        }
        try{
            this.dataInputStream.close();
            this.dataOutputStream.close();
            this.socket.close();
        }catch (IOException e)
        {
            e.printStackTrace();
        }
    }
}

