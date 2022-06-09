package Connection;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

/**
 * The class responsible with the action of creating the TCP connection
 * between the client and the server.
 */
public class ClientConnection {
    public Socket socket;
    DataInputStream dataInputStream;
    DataOutputStream dataOutputStream;
    public ClientConnection(String address, int port)
    {
        try{
            this.socket = new Socket(address, port);
            System.out.println("Connected to server.");
            this.dataInputStream = new DataInputStream(socket.getInputStream());
            this.dataOutputStream = new DataOutputStream(socket.getOutputStream());
        }catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    public DataInputStream getDataInputStream() {
        return dataInputStream;
    }

    public DataOutputStream getDataOutputStream() {
        return dataOutputStream;
    }
}
