package Databases;

import java.sql.*;

public class UserDAO {
    public void create(int id, String username, String password)
    {
        Connection connection = DatabaseConnection.getConnection();
        try(PreparedStatement pstmt = connection.prepareStatement(
                "INSERT INTO users (id, username, password) VALUES (?, ?, ?)"
        )){
            pstmt.setInt(1, id);
            pstmt.setString(2, username);
            pstmt.setString(3, password);
            pstmt.executeUpdate();
        } catch (SQLException e)
        {
            e.printStackTrace();
        }
    }

    public int getIdByUsername(String username)
    {
        Connection connection = DatabaseConnection.getConnection();
        int id = 0;
        try(Statement stmt = connection.createStatement();
            ResultSet resultSet = stmt.executeQuery(
                    "SELECT id FROM users WHERE username='" + username + "'"
            )){
            while (resultSet.next())
            {
                id = Integer.parseInt(resultSet.getString(1));
            }
        }catch (SQLException e)
        {
            e.printStackTrace();
        }
        return id;
    }
}
