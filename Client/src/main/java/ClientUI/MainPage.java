package ClientUI;

import Connection.ClientConnection;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class MainPage {
    private GridPane gridPane;
    private ClientConnection con;
    private Stage stage;
    private String username;
    public MainPage(Stage stage, String username)
    {
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

        Text title = new Text(this.username);
        title.setStyle("-fx-font: 30px Verdana");
        HBox top = new HBox();
        top.getChildren().add(title);
        top.setStyle("-fx-background-color: red");
        gridPane.add(top, 0, 1);

        GridPane friendList = new GridPane();
        Text friendListLabel = new Text("Friend list");
        Button addFriendButton = new Button("Add friend");
        VBox friendsContainer = new VBox();
        friendsContainer.setStyle("-fx-background-color: yellow");
        friendList.add(friendListLabel, 0, 2);
        friendList.add(addFriendButton, 1, 2);
        gridPane.add(friendList, 0, 2);


        Scene scene = new Scene(gridPane, 500, 500);
        this.stage.setScene(scene);
    }
}
