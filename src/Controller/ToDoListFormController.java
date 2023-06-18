package Controller;

import Database.DBConnection;
import TableModel.ToDoTM;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.*;
import java.util.Objects;

public class ToDoListFormController {
    public AnchorPane root;
    public ListView<ToDoTM> lstTasks;
    public Label lblWelcome;
    public Label lblUserId;
    public TextField txtTask;
    public Button btnDelete;
    public Button btnUpdate;
    public AnchorPane rootAddNew;
    public TextField txtNewTask;
    public Button btnAddNew;

    Connection connection;
    private String userID,taskID;

    public void initialize(){
        connection = DBConnection.getDBConnection().getConnection();

        rootAddNew.setVisible(false);
        setDisable(true);

        userID = LoginFormController.loginID;
        LoginFormController.loginID = null;
        lblUserId.setText(userID);
        try {
            ResultSet resultSet = connection.createStatement().executeQuery("select name from user where user_id = '" + userID + "';");
            resultSet.next();
            lblWelcome.setText("Hi " + resultSet.getString(1).substring(0,1).toUpperCase() + resultSet.getString(1).substring(1) + ", Welcome to ToDo List");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        loadList();

        lstTasks.getSelectionModel().selectedItemProperty().addListener((observableValue, toDoTM, t1) -> {
            ToDoTM task = lstTasks.getSelectionModel().getSelectedItem();
            if (task == null) {
                return;
            }
            txtTask.setText(task.description());
            taskID = task.id();
            setDisable(false);
            rootAddNew.setVisible(false);
        });
    }

    private void setDisable(boolean b) {
        txtTask.setDisable(b);
        btnDelete.setDisable(b);
        btnUpdate.setDisable(b);
    }

    public void loadList() {
        ObservableList<ToDoTM> tasks = lstTasks.getItems();
        tasks.clear();
        try {
            ResultSet resultSet = connection.createStatement().executeQuery("select * from todo where user_id = '" + userID + "';");
            while (resultSet.next()){
                tasks.add(new ToDoTM(resultSet.getString(1),resultSet.getString(2)));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        lstTasks.refresh();
    }

    public void txtTaskOnAction() {
        btnUpdateOnAction();
    }

    public void btnDeleteOnAction() {
        try {
            connection.createStatement().executeUpdate("delete from todo where id = '"+taskID+"';");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        txtTask.setText("");
        lstTasks.getSelectionModel().clearSelection();
        txtTask.clear();
        taskID = null;
        setDisable(true);
        loadList();
    }

    public void btnUpdateOnAction() {
        try {
            connection.createStatement().executeUpdate("update todo set description = '" + txtTask.getText() + "' where id = '" + taskID + "';");
        }catch (SQLException e) {
            throw new RuntimeException(e);
        }
        txtTask.setText("");
        lstTasks.getSelectionModel().clearSelection();
        txtTask.clear();
        taskID = null;
        setDisable(true);
        loadList();
    }

    public void btnAddNewOnAction() {
        rootAddNew.setVisible(true);
        setDisable(true);
        txtTask.clear();
        txtNewTask.clear();
        txtNewTask.requestFocus();
    }

    public void txtNewTaskOnAction() {
        btnAddToListOnAction();
    }

    public void btnAddToListOnAction() {
        String newid;

        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("select id from todo order by id desc limit 1;");
            if(resultSet.next()){
                int id = Integer.parseInt(resultSet.getString(1).substring(1)) +1;
                if(id<10)
                    newid = "T00" + id;
                else if (id<100)
                    newid = "T0" + id;
                else
                    newid = "T" + id;
            }else
                newid = "T001";
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        try {
            PreparedStatement statement = connection.prepareStatement("insert into todo values (?,?,?);");
            statement.setObject(1,newid);
            statement.setObject(2,txtNewTask.getText());
            statement.setObject(3,userID);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        rootAddNew.setVisible(false);
        loadList();
    }

    public void btnLogOutOnClick() throws IOException {
        Stage stage = (Stage) root.getScene().getWindow();
        stage.setScene(new Scene(FXMLLoader.load(Objects.requireNonNull(getClass().getResource("../View/LoginForm.fxml")))));
        stage.setTitle("Login");
        stage.centerOnScreen();
    }
}
