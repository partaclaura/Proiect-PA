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

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The class responsible for the user's messages page.
 * It consists of a method which creates the UI and the functions
 * that will communicate with the classes responsible with the connection
 * to the server.
 */
public class MessagesPage {
    ClientConnection con;
    Stage stage;
    String username;
    List<Label> friends;
    ScrollPane scrollPane;
    String friendToSend = "";

    String buttonStyle = "-fx-background-color: #212d40;" +
            "-fx-text-fill: #7161ef;" +
            "-fx-border-color: #212d40;" +
            "-fx-font: 13px Verdana;";
    String textStyle = "-fx-font: 20px Verdana;" +
            "-fx-text-fill: #efd9ce";

    public MessagesPage(ClientConnection con, Stage stage, String username, List<Label> friends)
    {
        this.con = con;
        this.stage = stage;
        this.username = username;
        this.friends = friends;
        setup();
    }

    /**
     * This method is used to create the UI of the page which displays a list of
     * the user's friends. When the user picks a friend from the list, the messages
     * between the two will be displayed.
     * The user is also provided with a reload button, a text field to send a new
     * message to the selected friend and a send button for this action.
     */
    private void setup()
    {
        //the main container and stage
        GridPane gridPane = new GridPane();
        Scene scene = new Scene(gridPane, 500, 500);
        File f = new File("file.css");
        scene.getStylesheets().clear();
        scene.getStylesheets().add("file:///" + f.getAbsolutePath().replace("\\", "/"));
        gridPane.setStyle("-fx-background-color: #11151c;");
        gridPane.setHgap(10);
        gridPane.setVgap(10);
        gridPane.setPadding(new Insets(25, 25, 25, 25));

        //the top container
        HBox top = new HBox();
        Button back = new Button("BACK");
        back.setOnAction(t-> new MainPage(this.con, this.stage, this.username, new Label()));
        top.getChildren().add(back);
        ScrollPane friendsList = new ScrollPane(); //the friend list
        //adding the friends to the container
        friendsList.setMaxWidth(300);
        HBox list = new HBox(5);
        for(Label label : friends)
        {
            Button friend = new Button(label.getText());
            friend.setOnAction(t->showMessages(label.getText()));
            friend.setStyle("-fx-font: 15px Verdana;" +
                    "-fx-text-fill: #7161ef;" +
                    "-fx-border-color: #11151c;" +
                    "-fx-background-color: #11151c");
            list.getChildren().add(friend);
        }
        friendsList.setContent(list);
        top.getChildren().add(friendsList);
        gridPane.add(top, 0, 1);

        StackPane stackPane = new StackPane();
        stackPane.setMaxWidth(400);
        stackPane.setMaxHeight(300);
        stackPane.setMinWidth(400);
        stackPane.setMinHeight(300);
        scrollPane = new ScrollPane();
        stackPane.getChildren().add(scrollPane);
        scrollPane.setStyle("-fx-background-color: #212d40;");
        //scrollPane.setPrefSize(300, 300);
        gridPane.add(stackPane, 0, 2);

        HBox sendMessageContainer = new HBox(5);
        sendMessageContainer.setStyle("-fx-background-color: #11151c");
        sendMessageContainer.setPadding(new Insets(25, 25, 25, 25));

        //the bottom container -> the reload/send button and message text field
        Button refresh = new Button("RELOAD");
        sendMessageContainer.getChildren().add(refresh);
        refresh.setOnAction(t->showMessages(this.friendToSend));
        refresh.setStyle(buttonStyle);
        TextField textField = new TextField();
        textField.setStyle("-fx-background-color: #212d40;" +
                "-fx-text-fill: #efd9ce;");
        sendMessageContainer.getChildren().add(textField);

        Button send = new Button("SEND");
        send.setOnAction(t->sendEvent(gridPane, this.friendToSend, textField.getText()));
        send.setStyle(buttonStyle);
        sendMessageContainer.getChildren().add(send);

        gridPane.add(sendMessageContainer, 0, 3);

        this.stage.setScene(scene);

    }

    /**
     * The method responsible with the action of sending a message to a
     * selected friend.
     * @param gridPane The main container.
     * @param friend The friend currently selected.
     * @param message The message to be sent, extracted from the text field.
     */
    private void sendEvent(GridPane gridPane, String friend, String message)
    {
        Text resp = new Text("");
        resp.setStyle(textStyle);
        resp.setText(new ServerCom(this.con).sendMessage(this.username, friend, message));
        showMessages(this.friendToSend);
    }

    /**
     * This function is used to get the friend list from the server.
     * @return The response from the server.
     */
    private String getMessage()
    {
        return (new ServerCom(this.con).getMessages(this.username));
    }

    /**
     * This method is used to create the part of the page that displays the
     * messages between the user and the selected friend.
     * @param friend The friend selected by the user.
     */
    private void showMessages(String friend)
    {
        this.friendToSend = friend;
        String[] messages = getMessage().split(";");
        VBox vBox = new VBox(5);
        vBox.setMinHeight(300);
        vBox.setMinWidth(400);
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
