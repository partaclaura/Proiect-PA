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

import java.util.ArrayList;
import java.util.List;

public class MainPage {
    private HBox fActions;
    private VBox friendsContainer;
    private GridPane gridPane;
    private ClientConnection con;
    private Stage stage;
    private String username;
    public MainPage(ClientConnection con, Stage stage, String username)
    {
        this.con = con;
        this.stage = stage;
        this.username = username;
        this.gridPane = new GridPane();
        pageSetup();
    }

    private void pageSetup()
    {
        //gridPane.setAlignment(Pos.CENTER);
        gridPane.setHgap(10);
        gridPane.setVgap(10);
        gridPane.setPadding(new Insets(25, 25, 25, 25));

        Button back = new Button("Back");
        back.setOnAction(t -> new TitlePage(this.stage, this.con));
        Text title = new Text(this.username);
        title.setStyle("-fx-font: 30px Verdana");
        HBox top = new HBox(5);
        top.setMinWidth(400);
        top.setMaxWidth(400);
        top.setPadding(new Insets(10, 10, 10, 10));
        top.getChildren().add(back);
        top.getChildren().add(title);
        //top.setStyle("-fx-background-color: red");
        gridPane.add(top, 0, 1);

        GridPane friendList = new GridPane();
        fActions = new HBox(5);
        fActions.setMinWidth(250);
        fActions.setMaxWidth(250);
        Text friendListLabel = new Text("Friend list");
        friendListLabel.setStyle("-fx-font: 10px Verdana");
        fActions.getChildren().add(friendListLabel);
        Button addFriendButton = new Button("Add friend");
        fActions.getChildren().add(addFriendButton);
        this.friendsContainer = new VBox(5);
        friendsContainer.setStyle("-fx-background-color: yellow");
        friendList.add(fActions, 0, 1);
        for(Text text : showFriends()) {
            friendsContainer.getChildren().add(text);
        }
        friendList.add(friendsContainer, 0, 2);
        gridPane.add(friendList, 0, 2);
        addFriendButton.setOnAction(t -> addFriendAction(addFriendButton));

        Button messages = new Button("Messages");
        gridPane.add(messages, 0, 3);
        messages.setOnAction(t-> new MessagesPage(this.con, this.stage, this.username));



        Scene scene = new Scene(gridPane, 500, 500);
        this.stage.setScene(scene);
    }

    private List<Text> showFriends()
    {
        List<Text> showfriends = new ArrayList<>();
        List<String> friendsList = new ServerCom(con).getUserFriends(username);
        for(String f : friendsList)
        {
            Text friend = new Text(f);
            showfriends.add(friend);
            System.out.println(friend.getText());
        }
        return showfriends;
    }

    private void addFriendAction(Button button)
    {
        Text response = new Text("");
        TextField friendUsername = new TextField("username");
        fActions.getChildren().add(friendUsername);
        fActions.getChildren().add(response);
        button.setText("Add");

        button.setOnAction(t-> reload(response, friendUsername));
    }

    private void reload(Text response, TextField friendUsername)
    {
        response.setText(new ServerCom(con).addFriendRequest(this.username, friendUsername.getText()));
        MainPage mp = new MainPage(this.con, this.stage, this.username);
    }

}
