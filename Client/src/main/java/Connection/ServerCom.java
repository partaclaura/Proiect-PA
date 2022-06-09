package Connection;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ServerCom {
    private final ClientConnection con;
    public ServerCom(ClientConnection con)
    {
        this.con = con;
    }
    public String sendLoginRequest(String username, String password)
    {
        String response = "";
        try {
            this.con.getDataOutputStream().writeUTF("login " + username + " " + password);
            System.out.println("Sending");
            response = this.con.getDataInputStream().readUTF();
            System.out.println("Got " + response);

        }catch (IOException e)
        {
            e.printStackTrace();
        }
        return response;
    }

    public String sendSignupRequest(String username, String password)
    {
        String response = "";
        try {
            this.con.getDataOutputStream().writeUTF("signup " + username + " " + password);
            System.out.println("Sending");
            response = this.con.getDataInputStream().readUTF();
            System.out.println("Got " + response);

        }catch (IOException e)
        {
            e.printStackTrace();
        }
        return response;
    }

    public List<String> getUserFriends(String username)
    {
        List<String> friends = new ArrayList<>();
        try{
            String response = "";
            this.con.getDataOutputStream().writeUTF("getFriends " + username);
            System.out.println("Sending");
            response = this.con.getDataInputStream().readUTF();
            System.out.println("Got " + response);
            String[] f = response.split(",");
            friends.addAll(Arrays.asList(f));
        }catch (IOException e)
        {
            e.printStackTrace();
        }
        return friends;
    }

    public String addFriendRequest(String friend1, String friend2)
    {
        String response = "";
        try {
            this.con.getDataOutputStream().writeUTF("friend " + friend1 + " " + friend2);
            System.out.println("Sending");
            this.con.getDataOutputStream().flush();
            response = this.con.getDataInputStream().readUTF();
            System.out.println("Got " + response);
        }catch (IOException e)
        {
            e.printStackTrace();
        }
        return response;
    }

    public String sendMessage(String from, String to, String message)
    {
        String toSend = from + " " + to + " " + message;
        String response = "";
        try {
            this.con.getDataOutputStream().writeUTF("send " + toSend);
            System.out.println("Sending " + toSend);
            this.con.getDataOutputStream().flush();
            response = this.con.getDataInputStream().readUTF();
            System.out.println("Got " + response);
        }catch (IOException e)
        {
            e.printStackTrace();
        }
        return response;
    }

    public String getMessages(String username)
    {
        String response = "";
        try {
            this.con.getDataOutputStream().writeUTF("getmess "+username);
            System.out.println("Sending " + username);
            this.con.getDataOutputStream().flush();
            response = this.con.getDataInputStream().readUTF();
            System.out.println("Got " + response);
        }catch (IOException e)
        {
            e.printStackTrace();
        }
        return response;
    }
}
