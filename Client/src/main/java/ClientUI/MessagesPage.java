package ClientUI;

import Connection.ClientConnection;
import Connection.ServerCom;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MessagesPage {
    ClientConnection con;
    Stage stage;
    String username;
    List<Label> friends;
    ScrollPane scrollPane;
    String friendToSend = "";

    String buttonStyle = "-fx-font: 20px Verdana;" +
            "-fx-text-fill: #7161ef;" +
            "-fx-border-color: #7161ef;" +
            "-fx-background-color: #11151c";
    String textStyle = "-fx-font: 20px Verdana;" +
            "-fx-text-fill: #efd9ce";
    String fieldStyle = "-fx-background-color: #212d40;" +
            "-fx-text-fill: #efd9ce";

    public MessagesPage(ClientConnection con, Stage stage, String username, List<Label> friends)
    {
        this.con = con;
        this.stage = stage;
        this.username = username;
        this.friends = friends;
        setup();
    }

    private void setup()
    {
        GridPane gridPane = new GridPane();
        gridPane.setStyle("-fx-background-color: #11151c;");
        gridPane.setHgap(10);
        gridPane.setVgap(10);
        gridPane.setPadding(new Insets(25, 25, 25, 25));

        HBox top = new HBox();
        Button back = new Button("BACK");
        back.setOnAction(t-> new MainPage(this.con, this.stage, this.username));
        back.setStyle(buttonStyle);
        top.getChildren().add(back);
        for(Label label : friends)
        {
            Button friend = new Button(label.getText());
            friend.setOnAction(t->showMessages(label.getText()));
            friend.setStyle("-fx-font: 20px Verdana;" +
                    "-fx-text-fill: #7161ef;" +
                    "-fx-border-color: #11151c;" +
                    "-fx-background-color: #11151c");
            top.getChildren().add(friend);
        }
        gridPane.add(top, 0, 1);

        StackPane stackPane = new StackPane();
        stackPane.setMinWidth(300);
        stackPane.setMinHeight(300);
        scrollPane = new ScrollPane();
        stackPane.getChildren().add(scrollPane);
        scrollPane.setStyle("-fx-background-color: #212d40;");
        //scrollPane.setPrefSize(300, 300);
        gridPane.add(stackPane, 0, 2);

        HBox sendMessageContainer = new HBox(5);
        sendMessageContainer.setStyle("-fx-background-color: #11151c");
        sendMessageContainer.setPadding(new Insets(25, 25, 25, 25));

        Button refresh = new Button("RELOAD");
        sendMessageContainer.getChildren().add(refresh);
        refresh.setOnAction(t->showMessages(this.friendToSend));
        refresh.setStyle("-fx-background-color: #212d40;" +
                "-fx-text-fill: #7161ef;");
        TextField textField = new TextField();
        TextInputControl
        textField.setStyle("-fx-background-color: #212d40;" +
                "-fx-text-fill: #efd9ce;");
        sendMessageContainer.getChildren().add(textField);

        Button send = new Button("SEND");
        send.setOnAction(t->sendEvent(gridPane, this.friendToSend, textField.getText()));
        send.setStyle("-fx-background-color: #212d40;" +
                "-fx-text-fill: #7161ef;");
        sendMessageContainer.getChildren().add(send);

        gridPane.add(sendMessageContainer, 0, 3);


        Scene scene = new Scene(gridPane, 500, 500);
        this.stage.setScene(scene);

    }

    private void sendEvent(GridPane gridPane, String friend, String message)
    {
        Text resp = new Text("");
        resp.setStyle(textStyle);
        resp.setText(new ServerCom(this.con).sendMessage(this.username, friend, message));
        showMessages(this.friendToSend);
    }

    private String getMessage()
    {
        return (new ServerCom(this.con).getMessages(this.username));
    }

    private void showMessages(String friend)
    {
        this.friendToSend = friend;
        String[] messages = getMessage().split(";");
        VBox vBox = new VBox(5);
        vBox.setMinHeight(300);
        vBox.setMinWidth(300);
        vBox.setPadding(new Insets(25, 25, 25, 25));
        vBox.setStyle("-fx-background-color: #212d40;");
        vBox.setFillWidth(true);
        scrollPane.setContent(vBox);
        for(int i = 1;  i < messages.length - 2; i+= 3) {
            VBox vBox1 = new VBox();
            if(messages[i].equals(friend) || messages[i + 1].equals(friend))
            {
                Label name = new Label(messages[i]);
                name.setStyle("-fx-text-fill: #efd9ce;");
                Label message = new Label(messages[i+2]);
                message.setPadding(new Insets(10, 10, 10, 10));
                message.setStyle("-fx-text-fill: #efd9ce;" +
                        "-fx-font: 20px Verdana;" +
                        "-fx-border-width: 2px;" +
                        "-fx-border-radius: 10px;" +
                        "-fx-border-color: #efd9ce");
                vBox1.getChildren().add(name);
                vBox1.getChildren().add(message);
                if(messages[i].equals(username))
                    vBox1.setAlignment(Pos.TOP_RIGHT);
                else vBox1.setAlignment(Pos.TOP_LEFT);
            }
            vBox.getChildren().add(vBox1);
        }

    }

}
