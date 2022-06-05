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
    private Label pageTitle;
    private GridPane gridPane;
    String buttonStyle = "-fx-font: 20px Verdana;" +
            "-fx-text-fill: #7161ef;" +
            "-fx-border-color: #7161ef;" +
            "-fx-background-color: #11151c";
    String textStyle = "-fx-font: 20px Verdana;" +
            "-fx-text-fill: #efd9ce";

    public FormPage(String type, Stage stage, ClientConnection con) {
        this.con = con;
        this.stage = stage;
        pageTitle = new Label();
        this.gridPane = new GridPane();
        gridPane.setStyle("-fx-background-color: #11151c");
        this.pageTitle.setText(type);
        pageTitle.setStyle("-fx-font: 35px Verdana;" +
                "-fx-text-fill: linear-gradient(from 0% 0% to 100% 200%, repeat, #7161ef 0%, #dec0f1 50%);");
        FormSetup();
    }

    public void FormSetup()
    {
        //setting form grid
        gridPane.setAlignment(Pos.CENTER);
        gridPane.setHgap(10);
        gridPane.setVgap(10);
        gridPane.setPadding(new Insets(25, 25, 25, 25));

        Button back = new Button("BACK");
        back.setStyle(buttonStyle);
        back.setOnAction(t -> new TitlePage(this.stage, this.con));
        //setting labels and fields
        HBox controls = new HBox(5);
        controls.getChildren().add(back);
        controls.getChildren().add(this.pageTitle);
        gridPane.add(controls, 0, 0, 2, 1);

        HBox usernameContainer = new HBox(5);
        Label userName = new Label("username: ");
        gridPane.add(userName, 0, 1);
        userName.setStyle(textStyle);
        usernameContainer.getChildren().add(userName);
        TextField userNameField = new TextField();
        gridPane.add(userNameField, 1, 1);
        userNameField.setStyle("-fx-background-color: #212d40;" +
                "-fx-text-fill: #efd9ce");
        usernameContainer.getChildren().add(userNameField);
        gridPane.add(usernameContainer, 0, 1);

        HBox passwordContainer = new HBox(5);
        Label password = new Label("password: ");
        gridPane.add(password, 0, 2);
        password.setStyle(textStyle);
        PasswordField passwordField = new PasswordField();
        gridPane.add(passwordField, 1, 2);
        passwordField.setStyle("-fx-background-color: #212d40;" +
                "-fx-text-fill: #efd9ce");
        passwordContainer.getChildren().add(password);
        passwordContainer.getChildren().add(passwordField);
        gridPane.add(passwordContainer, 0, 2);


        //button
        Button button = new Button(this.pageTitle.getText());
        button.setStyle(buttonStyle);
        HBox hBox = new HBox(10);
        hBox.setAlignment(Pos.BOTTOM_LEFT);
        hBox.getChildren().add(button);
        gridPane.add(hBox, 0, 3);
        Label response = new Label("Welcome<3");
        response.setStyle(textStyle);
        gridPane.add(response, 0, 4);

        Scene scene = new Scene(gridPane, 500, 500);
        this.stage.setScene(scene);
        button.setOnAction(t -> attempt(response, userNameField.getText(), passwordField.getText()));

    }

    private void attempt(Label text, String username, String password)
    {
        text.setText( submitButton(username, password));
        if(text.getText().contains("successful"))
        {
            Button next = new Button("Continue");
            next.setStyle(buttonStyle);
            gridPane.add(next, 0, 5);
            next.setOnAction(t -> new MainPage(this.con, this.stage, username, new Label("")));
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