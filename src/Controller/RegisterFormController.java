package Controller;

import Database.DBConnection;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.*;
import java.util.Objects;
import java.util.regex.Pattern;

public class RegisterFormController {
    public AnchorPane root;
    public Connection connection;
    public Label lblUid;
    public TextField txtName;
    public TextField txtEmail;
    public PasswordField txtPassword;
    public PasswordField txtConfirmPassword;
    public Button btnRegister;

    String newid;

    public void initialize() throws SQLException {
        connection = DBConnection.getDBConnection().getConnection();

        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery("select id from user order by id desc limit 1;");
        if(resultSet.next()){
            int id = Integer.parseInt(resultSet.getString(1).substring(1)) +1;
            if(id<10)
                newid = "U00" + id;
            else if (id<100)
                newid = "U0" + id;
            else
                newid = "U" + id;
        }else
            newid = "U001";
        lblUid.setText(newid);
    }

    private void Validator(){
        //Name
        String name = txtName.getText();
        if(name.equals(""))
            throw new RuntimeException("Name cannot be empty!");
        String regex = "^[A-Za-z]\\w{3,29}$";
        Pattern pattern = Pattern.compile(regex);
        if(!pattern.matcher(name).matches())
            throw new RuntimeException("Invalid name!");

        //Email
        String email = txtEmail.getText();
        if(email.equals(""))
            throw new RuntimeException("Email address cannot be empty!");
        regex = "^[\\w!#$%&'*+/=?`{|}~^-]+(?:\\.[\\w!#$%&'*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}";
        pattern = Pattern.compile(regex);
        if(!pattern.matcher(email).matches())
            throw new RuntimeException("Invalid email address!");

        //Password
        String password = txtPassword.getText();
        if(password.equals(""))
            throw new RuntimeException("Password cannot be empty!");
        regex = "^(?=.*[0-9])"
                + "(?=.*[a-z])(?=.*[A-Z])"
                + "(?=.*[@#$%^&+=])"
                + "(?=\\S+$).{5,20}$";
        pattern = Pattern.compile(regex);
        if(!pattern.matcher(password).matches())
            throw new RuntimeException("Invalid password");
    }

    public void register() throws SQLException {
        try {
            Validator();
        } catch (Exception e) {
            new Alert(Alert.AlertType.WARNING,e.getLocalizedMessage()).showAndWait();
            return;
        }
        PreparedStatement statement = connection.prepareStatement("insert into user values(? , ? , ? , ?);");
        statement.setObject(1,newid);
        statement.setObject(2,txtName.getText());
        statement.setObject(3,txtEmail.getText());
        statement.setObject(4,txtPassword.getText());

        if(txtPassword.getText().equals(txtConfirmPassword.getText())){
            try {
                int i = statement.executeUpdate();
                if (i == 1){
                    new Alert(Alert.AlertType.CONFIRMATION,"Registration successful!").showAndWait();
                    Stage stage = (Stage) root.getScene().getWindow();
                    stage.setScene(new Scene(FXMLLoader.load(Objects.requireNonNull(this.getClass().getResource("../View/LoginForm.fxml")))));
                    stage.setTitle("LogIn");
                    stage.centerOnScreen();
                }
            } catch (SQLException | IOException e) {
                new Alert(Alert.AlertType.ERROR,e.getLocalizedMessage()).showAndWait();
            }
            txtPassword.setStyle("-fx-background-color:#fab1a0");
        }else {
            txtConfirmPassword.setStyle("-fx-background-color:#fab1a0");
            txtPassword.setStyle("-fx-background-color:#fab1a0");
        }
    }

    public void btnBackOnAction() throws IOException {
        Stage stage = (Stage) root.getScene().getWindow();
        stage.setScene(new Scene(FXMLLoader.load(Objects.requireNonNull(this.getClass().getResource("../View/LoginForm.fxml")))));
        stage.setTitle("Login");
        stage.centerOnScreen();
    }

    public void txtNameOnAction() {
        txtEmail.requestFocus();
    }

    public void txtEmailOnAction() {
        txtPassword.requestFocus();
    }

    public void txtPasswordOnAction() {
        txtConfirmPassword.requestFocus();
    }

    public void txtConfirmPasswordOnAction() throws SQLException {
        register();
    }

    public void btnRegisterOnAction() throws SQLException {
        register();
    }

    public void txtPasswordOnTyped() {
        txtConfirmPassword.setStyle("-fx-background-color:#ffffff");
        txtPassword.setStyle("-fx-background-color:#ffffff");
    }

    public void txtConfirmPasswordOnTyped() {
        txtConfirmPassword.setStyle("-fx-background-color:#ffffff");
        txtPassword.setStyle("-fx-background-color:#ffffff");
    }
}
