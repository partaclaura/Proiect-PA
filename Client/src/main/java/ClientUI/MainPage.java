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
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;

public class MainPage {
    private HBox fActions;
    private VBox friendsContainer;
    private GridPane gridPane;
    private ClientConnection con;
    private Stage stage;
    private String username;
    private Label response;

    String buttonStyle = "-fx-font: 20px Verdana;" +
            "-fx-text-fill: #7161ef;" +
            "-fx-border-color: #7161ef;" +
            "-fx-background-color: #11151c";

    public MainPage(ClientConnection con, Stage stage, String username, Label response)
    {
        this.con = con;
        this.stage = stage;
        this.username = username;
        this.gridPane = new GridPane();
        this.response = response;
        pageSetup();
    }

    private void pageSetup()
    {
        //--MAIN CONTAINER-------------------------------------------
        //gridPane.setAlignment(Pos.CENTER);
        gridPane.setHgap(10);
        gridPane.setVgap(10);
        //gridPane.setPadding(new Insets(25, 25, 25, 25));
        gridPane.setMinWidth(500);
        gridPane.setMaxWidth(500);
        gridPane.setMinHeight(500);
        gridPane.setMaxHeight(500);
        gridPane.setStyle("-fx-background-color: #11151c");

        //--TOP CONTAINER--------------------------------------------
        VBox top = new VBox(5);
        top.setSpacing(50);
        top.setMinWidth(500);
        top.setMaxWidth(500);
        top.setStyle("-fx-background-color: #212d40");
        //top.setPadding(new Insets(10, 10, 10, 10));
        Button back = new Button("Back");
        back.setStyle(buttonStyle);
        back.setOnAction(t -> new TitlePage(this.stage, this.con));
        Label title = new Label(this.username);
        title.setPadding(new Insets(10, 10, 10, 10));
        title.setStyle("-fx-font: 30px Verdana;" +
                "-fx-text-fill: linear-gradient(from 0% 0% to 100% 200%, repeat, #7161ef 0%, #dec0f1 50%);" +
                "-fx-background-color: #11151c");
        top.getChildren().add(back);
        top.getChildren().add(title);
        gridPane.add(top, 0, 1);

        //--MIDDLE--------------------------------------------------------
        GridPane friendList = new GridPane();
        fActions = new HBox(5);
        fActions.setMinWidth(250);
        fActions.setSpacing(10);
        fActions.setMaxWidth(350);

        Label friendListLabel = new Label("Friend list");
        friendListLabel.setStyle("-fx-text-fill: #7161ef;" +
                "-fx-font: 20px Verdana;");
        fActions.getChildren().add(friendListLabel);

        Button addFriendButton = new Button("Add friend");
        addFriendButton.setStyle("-fx-text-fill: #7161ef;" +
                "-fx-border-color: #7161ef;" +
                "-fx-background-color: #11151c");
        fActions.getChildren().add(addFriendButton);
        //fActions.getChildren().add(response);

        this.friendsContainer = new VBox(5);
        friendsContainer.setSpacing(10);
        friendsContainer.setMinWidth(350);
        friendsContainer.setStyle("-fx-background-color: #212d40");
        friendsContainer.setPadding(new Insets(10, 10, 10, 10));
        friendList.add(fActions, 0, 1);
        for(Label text : showFriends()) {
            friendsContainer.getChildren().add(text);
            text.setStyle("-fx-text-fill: #efd9ce;" +
                    "-fx-font: 20px Verdana;");
        }
        friendList.add(friendsContainer, 0, 2);
        gridPane.add(friendList, 0, 2);
        addFriendButton.setOnAction(t -> addFriendAction(addFriendButton));

        Button messages = new Button("Messages");
        gridPane.add(messages, 0, 3);
        messages.setStyle(buttonStyle);
        messages.setOnAction(t-> new MessagesPage(this.con, this.stage, this.username, showFriends()));



        Scene scene = new Scene(gridPane, 500, 500);
        this.stage.setScene(scene);
    }

    private List<Label> showFriends()
    {
        List<Label> showfriends = new ArrayList<>();
        List<String> friendsList = new ServerCom(con).getUserFriends(username);
        for(String f : friendsList)
        {
            Label friend = new Label(f);
            friend.setStyle("-fx-text-fill: #efd9ce");
            showfriends.add(friend);
            System.out.println(friend.getText());
        }
        return showfriends;
    }

    private void addFriendAction(Button button)
    {
        response.setStyle("-fx-text-fill: #efd9ce;" +
                "-fx-font: 20px Verdana;");
        TextField friendUsername = new TextField("username");
        friendUsername.setStyle("-fx-background-color: #212d40;" +
                "-fx-text-fill: #efd9ce");
        fActions.getChildren().add(friendUsername);
        button.setText("Add");
        button.setStyle("-fx-text-fill: #7161ef;" +
                "-fx-border-color: #7161ef;" +
                "-fx-background-color: #11151c");

        button.setOnAction(t-> reload(response, friendUsername));
    }

    private void reload(Label response, TextField friendUsername)
    {
        response.setText(new ServerCom(con).addFriendRequest(this.username, friendUsername.getText()));
        MainPage mp = new MainPage(this.con, this.stage, this.username, response);
    }

}
