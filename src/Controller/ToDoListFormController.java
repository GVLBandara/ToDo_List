package Controller;

import Database.DBConnection;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;

import static Controller.LoginFormController.loginID;

public class ToDoListFormController {
    public AnchorPane root;
    public ListView lstTasks;
    public Label lblWelcome;
    public Label lblUserId;
    public TextField txtTask;
    public Button btnDelete;
    public Button btnUpdate;
    public AnchorPane rootAddNew;
    public TextField txtNewTask;

    Connection connection;
    private String userID;

    public void initialize(){
        connection = DBConnection.getDBConnection().getConnection();
        userID = loginID;
        loginID = null;
        lblUserId.setText(userID);
        try {
            ResultSet resultSet = connection.createStatement().executeQuery("select name from user where user_id = '" + userID + "';");
            resultSet.next();
            lblWelcome.setText("Hi " + resultSet.getString(1).substring(0,1).toUpperCase() + resultSet.getString(1).substring(1) + ", Welcome to ToDo List");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void btnDeleteOnAction() {
    }

    public void btnUpdateOnAction() {
    }

    public void btnAddNewOnAction() {
    }

    public void txtNewTaskOnAction() {
    }

    public void btnAddToListOnAction() {
    }

    public void btnLogOutOnAction() throws IOException {
        Stage stage = (Stage) root.getScene().getWindow();
        stage.setScene(new Scene(FXMLLoader.load(Objects.requireNonNull(getClass().getResource("../View/LoginForm.fxml")))));
        stage.setTitle("Login");
        stage.centerOnScreen();
    }
}
