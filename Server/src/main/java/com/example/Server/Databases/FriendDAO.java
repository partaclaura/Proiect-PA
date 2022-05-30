package com.example.Server.Databases;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class FriendDAO {
    public void create(int idFriend1, int idFriend2)
    {
        Connection connection = DatabaseConnection.getConnection();
        try(PreparedStatement pstmt = connection.prepareStatement(
                "INSERT INTO friendships (id_friend1, id_friend2) VALUES (?, ?)"
        )){
            pstmt.setInt(1, idFriend1);
            pstmt.setInt(2, idFriend2);
            pstmt.executeUpdate();
        } catch (SQLException e)
        {
            e.printStackTrace();
        }
    }
    public boolean exists(int idFriend1, int idFriend2)
    {
        Connection connection = DatabaseConnection.getConnection();
        int id = 0;
        try(Statement stmt = connection.createStatement();
            ResultSet resultSet = stmt.executeQuery(
                    "SELECT id_friend2 FROM friendships WHERE id_friend1=" + idFriend1
            )){
            while (resultSet.next())
            {
                id = Integer.parseInt(resultSet.getString(1));
                if(id == idFriend2)
                    return true;
            }
        }catch (SQLException e)
        {
            e.printStackTrace();
        }
        return false;
    }

    public List<Integer> getAllFriends(int id)
    {
        List<Integer> friendsIds = new ArrayList<>();
        Connection connection = DatabaseConnection.getConnection();
        int idFriend = 0;
        try(Statement stmt = connection.createStatement();
            ResultSet resultSet = stmt.executeQuery(
                    "SELECT id_friend2 FROM friendships WHERE id_friend1=" + id
            )){
            while (resultSet.next())
            {
                idFriend = Integer.parseInt(resultSet.getString(1));
                friendsIds.add(idFriend);
            }
        }catch (SQLException e)
        {
            e.printStackTrace();
        }
        return friendsIds;
    }
}
