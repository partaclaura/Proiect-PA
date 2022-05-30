package ClientUI;

import Connection.ClientConnection;
import Connection.ServerCom;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class MessagesPage {
    ClientConnection con;
    Stage stage;
    String username;
    public MessagesPage(ClientConnection con, Stage stage, String username)
    {
        this.con = con;
        this.stage = stage;
        this.username = username;
        setup();
    }

    private void setup()
    {
        HBox sendContainer = new HBox(5);

        Text from_text = new Text("From: " + this.username);
        Text to_text = new Text("To: ");
        TextField to_field = new TextField();
        Text mess_text = new Text("Message");
        TextField mess_field = new TextField();

        GridPane gridPane = new GridPane();
        gridPane.add(from_text, 0, 0);
        sendContainer.getChildren().add(to_text);
        sendContainer.getChildren().add(to_field);
        gridPane.add(sendContainer, 0, 1);
        gridPane.add(mess_text, 0, 2);
        gridPane.add(mess_field, 0, 3, 1, 3);

        gridPane.setAlignment(Pos.CENTER);
        gridPane.setHgap(10);
        gridPane.setVgap(10);
        gridPane.setPadding(new Insets(25, 25, 25, 25));

        Button send = new Button("SEND");
        gridPane.add(send, 1, 4);
        send.setOnAction(t-> sendEvent(gridPane, to_field.getText(), mess_field.getText()));

        Button get = new Button("SHOW");
        gridPane.add(get, 1, 5);
        get.setOnAction(t->getMessage(gridPane));

        Scene scene = new Scene(gridPane, 500, 500);
        this.stage.setScene(scene);

    }

    private void sendEvent(GridPane gridPane, String friend, String message)
    {
        Text resp = new Text();
        resp.setText(new ServerCom(this.con).sendMessage(this.username, friend, message));
        gridPane.add(resp, 2, 4);
    }

    private void getMessage(GridPane gridPane)
    {
        Text messages = new Text();
        messages.setText(new ServerCom(this.con).getMessages(this.username));
        gridPane.add(messages, 0, 5, 1, 6);
    }

}
