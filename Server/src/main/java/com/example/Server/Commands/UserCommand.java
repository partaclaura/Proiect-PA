package com.example.Server.Commands;

import com.example.Server.Databases.DatabaseConnection;
import com.example.Server.Databases.UserDAO;

import java.io.DataOutputStream;
import java.io.IOException;
import java.sql.SQLException;

public class UserCommand {
    /**
     * This method is used to invoke the method from the class
     * responsible with the connection to the 'users' table from the
     * database to verify if the provided parameters exist.
     * @param dataOutputStream To send a response to the client.
     * @param p The parameters received from the client.
     */
    public void login(DataOutputStream dataOutputStream, String[] p)
    {
        var user = new UserDAO();
        try {
            if (p.length == 2 && user.findUser(p[0], p[1]))//searching for user in the db
            {
                dataOutputStream.writeUTF("Login successful.");
                System.out.println("Sending");

            } else {
                dataOutputStream.writeUTF("Wrong username or password.");
                System.out.println("Sending");
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    /**
     * This method is used to invoke the method from the class
     * responsible with the connection to the 'users' table from the
     * database to verify if the provided parameters can be used to
     * create a new user.
     * @param dataOutputStream To send a response to the client.
     * @param p The parameters received from the client.
     */
    public void signup(DataOutputStream dataOutputStream, String[] p)
    {
        var user = new UserDAO();
        try {
            if (p.length == 0 || user.getIdByUsername(p[0]) != 0) {
                dataOutputStream.writeUTF("Username already exists/Not enough data.");
                System.out.println("Sending");
            } else {
                user.create(1, p[0], p[1]);
                try {
                    DatabaseConnection.getConnection().commit();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                if (user.findUser(p[0], p[1])) {
                    dataOutputStream.writeUTF("Signup successful.");
                    System.out.println("Sending");
                } else {
                    dataOutputStream.writeUTF("Signup error.");
                    System.out.println("Sending");
                }
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }
}
