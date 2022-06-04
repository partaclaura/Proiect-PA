package ClientUI;

import Connection.ClientConnection;
import Connection.ServerCom;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class MessagesPage {
    ClientConnection con;
    Stage stage;
    String username;

    String buttonStyle = "-fx-font: 20px Verdana;" +
            "-fx-text-fill: #7161ef;" +
            "-fx-border-color: #7161ef;" +
            "-fx-background-color: #11151c";
    String textStyle = "-fx-font: 20px Verdana;" +
            "-fx-text-fill: #efd9ce";
    String fieldStyle = "-fx-background-color: #212d40;" +
            "-fx-text-fill: #efd9ce";

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
        Button back = new Button("Back");
        back.setStyle(buttonStyle);
        back.setOnAction(t -> new MainPage(this.con, this.stage, this.username));

        Label from_text = new Label("From: " + this.username);
        from_text.setStyle(textStyle);
        Label to_text = new Label("To: ");
        to_text.setStyle(textStyle);
        TextField to_field = new TextField();
        to_field.setStyle(fieldStyle);
        Label mess_text = new Label("Message");
        mess_text.setStyle(textStyle);
        TextField mess_field = new TextField();
        mess_field.setStyle(fieldStyle);

        GridPane gridPane = new GridPane();
        gridPane.setStyle("-fx-background-color: #11151c;");
        gridPane.add(back, 0, 0);
        gridPane.add(from_text, 0, 1);
        sendContainer.getChildren().add(to_text);
        sendContainer.getChildren().add(to_field);
        gridPane.add(sendContainer, 0, 2);
        gridPane.add(mess_text, 0, 3);
        gridPane.add(mess_field, 0, 4);

        gridPane.setAlignment(Pos.CENTER);
        gridPane.setHgap(10);
        gridPane.setVgap(10);
        gridPane.setPadding(new Insets(25, 25, 25, 25));


        Button send = new Button("SEND");
        send.setStyle(buttonStyle);
        gridPane.add(send, 0, 5);
        send.setOnAction(t-> sendEvent(gridPane, to_field.getText(), mess_field.getText()));

        Button get = new Button("SHOW");
        get.setStyle(buttonStyle);
        gridPane.add(get, 1, 5);

        get.setOnAction(t->getMessage(gridPane));

        Scene scene = new Scene(gridPane, 500, 500);
        this.stage.setScene(scene);

    }

    private void sendEvent(GridPane gridPane, String friend, String message)
    {
        Text resp = new Text("");
        resp.setStyle(textStyle);
        gridPane.add(resp, 2, 5);
        resp.setText(new ServerCom(this.con).sendMessage(this.username, friend, message));
        gridPane.add(resp, 2, 4);
    }

    private void getMessage(GridPane gridPane)
    {
        Text messages = new Text();
        messages.setStyle(textStyle);
        gridPane.add(messages, 0, 6);
        messages.setText(new ServerCom(this.con).getMessages(this.username));
    }

}
