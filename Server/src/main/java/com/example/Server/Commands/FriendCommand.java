package com.example.Server.Commands;

import com.example.Server.Databases.DatabaseConnection;
import com.example.Server.Databases.FriendDAO;
import com.example.Server.Databases.UserDAO;

import java.io.DataOutputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class FriendCommand {

    /**
     * This method is used to execute an add friend request.
     * @param dataOutputStream To send a response to the client.
     * @param p The parameters received from the client.
     */
    public void friend(DataOutputStream dataOutputStream, String[] p)
    {
        try {
            var user = new UserDAO();
            var friendship = new FriendDAO();
            if (p.length == 2) {
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
                } else dataOutputStream.writeUTF("User does not exist/ already friends");
            } else {
                dataOutputStream.writeUTF("Not enough data.");
                System.out.println("Sending");
            }
        }
        catch (IOException ioException)
        {
            System.out.println("Error adding friend.");
        }
    }

    /**
     * This method is used to execute a get friends request.
     * It sends a csv string to the client of the users implicated
     * in a friendship with the user given as a parameter.
     * @param dataOutputStream To send a response to the client.
     * @param p The parameters received from the client.
     */
    public void getFriends(DataOutputStream dataOutputStream, String[] p)
    {
        var user = new UserDAO();
        var friendship = new FriendDAO();
        try {
            if (p.length == 1) {
                List<Integer> ids = friendship.getAllFriends(user.getIdByUsername(p[0]));
                List<String> usernames = new ArrayList<>();
                for (Integer fid : ids)
                    usernames.add(user.getUsernameById(fid));
                String sendFriends = "";
                for (String friends : usernames)
                    sendFriends = sendFriends.concat(friends + ",");
                dataOutputStream.writeUTF(sendFriends);
            } else dataOutputStream.writeUTF("error");
        }
        catch (IOException ioException)
        {
            System.out.println("Error getting friends.");
        }
    }
}
