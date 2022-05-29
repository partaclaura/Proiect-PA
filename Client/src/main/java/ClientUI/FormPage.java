package ClientUI;

import Connection.ClientConnection;
import Connection.ServerCom;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class FormPage{
    private ClientConnection con;
    private Stage stage;
    private Text pageTitle;
    private GridPane gridPane;

    public FormPage(String type, Stage stage, ClientConnection con) {
        this.con = con;
        this.stage = stage;
        pageTitle = new Text();
        this.gridPane = new GridPane();
        this.pageTitle.setText(type);
        FormSetup();
    }

    public Scene FormSetup()
    {
        //setting form grid
        gridPane.setAlignment(Pos.CENTER);
        gridPane.setHgap(10);
        gridPane.setVgap(10);
        gridPane.setPadding(new Insets(25, 25, 25, 25));

        Button back = new Button("Back");
        back.setOnAction(t -> new TitlePage(this.stage, this.con));
        //setting labels and fields
        HBox controls = new HBox(5);
        controls.getChildren().add(back);
        controls.getChildren().add(this.pageTitle);
        gridPane.add(controls, 0, 0, 2, 1);

        Label userName = new Label("username: ");
        gridPane.add(userName, 0, 1);

        TextField userNameField = new TextField();
        gridPane.add(userNameField, 1, 1);

        Label password = new Label("password: ");
        gridPane.add(password, 0, 2);

        PasswordField passwordField = new PasswordField();
        gridPane.add(passwordField, 1, 2);

        //button
        Button button = new Button(this.pageTitle.getText());
        HBox hBox = new HBox(10);
        hBox.setAlignment(Pos.BOTTOM_RIGHT);
        hBox.getChildren().add(button);
        gridPane.add(hBox, 1, 4);
        Text response = new Text();
        gridPane.add(response, 1, 6);

        Scene scene = new Scene(gridPane, 500, 500);
        this.stage.setScene(scene);
        button.setOnAction(t -> attempt(response, userNameField.getText(), passwordField.getText()));

        return scene;

    }

    private void attempt(Text text, String username, String password)
    {
        text.setText( submitButton(username, password));
        if(text.getText().contains("successful"))
        {
            Button next = new Button("Continue");
            gridPane.add(next, 1, 8);
            next.setOnAction(t -> new MainPage(this.con, this.stage, username));
        }

    }

    private String submitButton(String username, String password)
    {
        if(this.pageTitle.getText().equals("Login"))
            return new ServerCom(this.con).sendLoginRequest(username, password);
        if(this.pageTitle.getText().equals("Signup"))
            return new ServerCom(this.con).sendSignupRequest(username, password);
        return "";
    }


}
