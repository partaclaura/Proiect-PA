package com.example.Server.Commands;

import com.example.Server.Connection.APIConnection;

import java.io.DataOutputStream;
import java.io.IOException;

public class MessageCommand {

    /**
     * This method is used to invoke the method from the class
     * which communicates with the API responsible for adding a
     * message in the database.
     * @param dataOutputStream To send a response to the client.
     * @param p The parameters received from the client.
     */
    public void send(DataOutputStream dataOutputStream, String[] p)
    {
        try {
            if (p.length > 2) {
                String toSend = p[0] + "," + p[1] + "," + p[2];
                APIConnection.PostMessage(toSend);
                dataOutputStream.writeUTF("Message sent");
            } else dataOutputStream.writeUTF("Can't send message.");
        }
        catch (IOException ioException)
        {
            ioException.printStackTrace();
        }
    }

    /**
     * This method is used to get the messages of the user send as
     * a parameter by the client by invoking the corresponding method
     * from the class which communicates with the API.
     * @param dataOutputStream To send a response to the client.
     * @param p The parameters received from the client.
     */
    public void getMessages(DataOutputStream dataOutputStream, String[] p)
    {
        try {
            if (p.length == 1) {
                dataOutputStream.writeUTF(APIConnection.GetMessage(p[0]));
            } else dataOutputStream.writeUTF("Error");
        }
        catch (IOException ioException)
        {
            ioException.printStackTrace();
        }
    }
}
