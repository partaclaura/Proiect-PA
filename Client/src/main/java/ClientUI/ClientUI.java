package ClientUI;

import Connection.ClientConnection;
import javafx.application.Application;
import javafx.stage.Stage;

/**
 * The UI of the project which gives a user the possibility to:
 * - create an account by picking a username and a password
 * - add friends if they also have an account
 * - send and receive messages from friends through the help of a REST API
 */
public class ClientUI extends Application {

    public static void main(String[] args) {
        launch(args);

    }

    @Override
    public void start(Stage primaryStage)
    {
        ClientConnection clientConnection = new ClientConnection("localhost", 6666);
        new TitlePage(primaryStage, clientConnection);

    }
}
