package Controller;

import Database.DBConnection;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;

public class LoginFormController {
    public AnchorPane root;
    public TextField txtName;
    public PasswordField txtPassword;
    public Button btnLogin;

    public static String loginID;

    public void txtNameOnAction() {
        txtPassword.requestFocus();
    }

    public void txtPasswordOnAction() throws SQLException, IOException {
        logIn();
    }

    public void btnLoginONAction() throws SQLException, IOException {
        logIn();
    }

    public void lblCreateNewAccountOnClicks() throws IOException {
        Stage stage = (Stage) root.getScene().getWindow();
        stage.setScene(new Scene(FXMLLoader.load(Objects.requireNonNull(getClass().getResource("../View/RegisterForm.fxml")))));
        stage.setTitle("Create New Account");
        stage.centerOnScreen();
    }

    public void logIn() throws SQLException, IOException {
        Connection connection = DBConnection.getDBConnection().getConnection();
        PreparedStatement statement = connection.prepareStatement("select id from user where name = ? and password = ?");
        statement.setObject(1,txtName.getText());
        statement.setObject(2,txtPassword.getText());
        ResultSet resultSet = statement.executeQuery();
        if(resultSet.next()){
            loginID = resultSet.getString(1);
            Stage stage = (Stage) root.getScene().getWindow();
            stage.setScene(new Scene(FXMLLoader.load(Objects.requireNonNull(this.getClass().getResource("../View/ToDoListForm.fxml")))));
            stage.setTitle("ToDo List");
            stage.centerOnScreen();
        }else {
            txtName.setStyle("-fx-background-color:#fab1a0");
            txtPassword.setStyle("-fx-background-color:#fab1a0");
            new Alert(Alert.AlertType.WARNING,"Incorrect user name or password!").showAndWait();
            txtName.requestFocus();
        }
    }

    public void txtNameKeyTyped() {
        txtName.setStyle("-fx-background-color:#ffffff");
        txtPassword.setStyle("-fx-background-color:#ffffff");
    }
}
