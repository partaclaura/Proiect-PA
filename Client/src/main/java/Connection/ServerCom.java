package Connection;

import Connection.ClientConnection;

import java.io.IOException;

public class ServerCom {
    private ClientConnection con;
    public ServerCom(ClientConnection con)
    {
        this.con = con;
    }
    public String sendLoginRequest(String username, String password)
    {
        String response = "";
        try {
            this.con.getDataOutputStream().writeUTF("login " + username + " " + password);
            response = this.con.getDataInputStream().readUTF();

        }catch (IOException e)
        {
            e.printStackTrace();
        }
        return response;
    }
}
