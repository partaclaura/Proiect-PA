package ClientUI;

import Connection.ClientConnection;
import javafx.application.Application;
import javafx.stage.Stage;

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
