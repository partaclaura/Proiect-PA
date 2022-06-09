package ClientUI;

import Connection.ClientConnection;
import Connection.ServerCom;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * The class responsible for the user account page.
 * It consists of a method which creates the UI and the functions
 * that will communicate with the classes responsible with the connection
 * to the server.
 */

public class MainPage {
    private HBox fActions;
    private final GridPane gridPane;
    private final ClientConnection con;
    private final Stage stage;
    private final String username;
    private final Label response;

    public MainPage(ClientConnection con, Stage stage, String username, Label response)
    {
        this.con = con;
        this.stage = stage;
        this.username = username;
        this.gridPane = new GridPane();
        this.response = response;
        pageSetup();
    }

    /**
     * This method is used to create the UI of the page through which
     * the user can see its username and friend list, but also through
     * which the user can add a friend and access the messages page.
     */
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

        //THE FRIEND LIST
        VBox friendsContainer = new VBox(5);
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

        //the button to access the Messages page
        Button messages = new Button("Messages");
        gridPane.add(messages, 0, 3);
        messages.setOnAction(t-> new MessagesPage(this.con, this.stage, this.username, showFriends()));

        //setting the scene
        Scene scene = new Scene(gridPane, 500, 500);
        File f = new File("file.css");
        scene.getStylesheets().clear();
        scene.getStylesheets().add("file:///" + f.getAbsolutePath().replace("\\", "/"));
        this.stage.setScene(scene);
    }

    /**
     * This method is used to obtain a user's friend list by sending
     * to class responsible with the server communication the username
     * of the user to obtain a list of labels that will be displayed in
     * the scene.
     * @return The list of labels to be displayed in the container
     *         which will show the user's friend list.
     */
    private List<Label> showFriends()
    {
        List<Label> showFriends = new ArrayList<>();
        List<String> friendsList = new ServerCom(con).getUserFriends(username);
        for(String f : friendsList)
        {
            Label friend = new Label(f);
            friend.setStyle("-fx-text-fill: #efd9ce");
            showFriends.add(friend);
            System.out.println(friend.getText());
        }
        return showFriends;
    }

    /**
     * This method is used to change the UI when the user wants to add
     * a friend by providing a new button and a text field in which the
     * user can write the username of the possible friend.
     * @param button The button used to initiate the add friend action
     *               will be reused as the button that will invoke the
     *               method responsible with the server communication.
     */
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

    /**
     * This method is used to initiate the communication with server
     * by creating an instance of the ServerCom class to access the
     * method responsible with adding friends.
     * It also recreates the current page just in case a new friend
     * was added to the list.
     * @param response The label where the response from the server
     *                 will be displayed.
     * @param friendUsername The username of the friend the user wants
     *                       to add.
     */
    private void reload(Label response, TextField friendUsername)
    {
        response.setText(new ServerCom(con).addFriendRequest(this.username, friendUsername.getText()));
        MainPage mp = new MainPage(this.con, this.stage, this.username, response);
    }

}
