package com.enam.supershop.init;


import com.enam.supershop.utils.DBConnection;
import com.enam.supershop.utils.UserDefMethods;
import javafx.animation.PauseTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.sql.*;
import java.util.regex.Pattern;

import static com.enam.supershop.utils.UserDefMethods.*;


public class SignUpController {
    @FXML private VBox leftPane;
    @FXML private Label usernameLabel;
    @FXML private TextField usernameField;
    @FXML private Label usernameStatus;
    @FXML private Label emailLabel;
    @FXML private TextField emailField;
    @FXML private Label emailStatus;
    @FXML private Label passwordLabel;
    @FXML private PasswordField passwordField;
    @FXML private Label passwordStatus;
    @FXML private Hyperlink signInLink;
    @FXML private Button signUpButton;
    @FXML private Label signUpStatus;


    public void initialize() {
        emailField.requestFocus();
        UserDefMethods.checkFieldStatus(usernameField, usernameStatus, "Username");
        UserDefMethods.checkFieldStatus(emailField, emailStatus, "Email");
        UserDefMethods.checkFieldStatus(passwordField, passwordStatus, "Password");
    }

    public void goToSignIn() {
        signInLink.setOnMouseClicked(event -> {
            try {
                Parent signInRoot = FXMLLoader.load(getClass().getResource("signIn.fxml"));
                Stage stage = (Stage) signInLink.getScene().getWindow();
                stage.setScene(new Scene(signInRoot));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    public void handleSignUp() {
        String username = usernameField.getText();
        String email = emailField.getText();
        String password = passwordField.getText();

        String emailRegex = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
        String usernameRegex = "[a-zA-Z0-9_]+";

        if (username.isEmpty()) {
            makeLabelExistent(usernameStatus, "Username cannot be empty!");
            return;
        } else {
            if (!username.matches(usernameRegex)) {
                makeLabelExistent(usernameStatus, "Username format is not valid!");
                return;
            }
        }
        if (email.isEmpty()) {
            makeLabelExistent(emailStatus, "Email cannot be empty!");
            return;
        } else {
            if(!Pattern.matches(emailRegex, email)) {
                makeLabelExistent(emailStatus, "Email is not valid!");
                return;
            }
        }
        if (password.isEmpty()) {
            makeLabelExistent(passwordStatus, "Password cannot be empty!");
            return;
        } else if (password.length() < 8) {
            makeLabelExistent(passwordStatus, "Password must be at least 8 characters!");
            return;
        }
        String checkSQL = "SELECT * FROM customers WHERE username = ? OR email = ?";
        String insertSQL = "INSERT INTO customers (username, email, password) VALUES (?, ?, ?)";
        Connection conn = DBConnection.databaseConn;
        try {
            PreparedStatement checkStmt = conn.prepareStatement(checkSQL);
            checkStmt.setString(1, username);
            checkStmt.setString(2, email);
            ResultSet rs = checkStmt.executeQuery();

            if (rs.next()) { // A matching record is found
                signUpStatus.setTextFill(Color.RED);
                signUpStatus.setText("Sign-up failed.");
                if (rs.getString("username").equals(username)) {
                    makeLabelExistent(usernameStatus, "Username is taken!");
                } else if (rs.getString("email").equals(email)) {
                    makeLabelExistent(emailStatus, "Email already exists!");
                }
            } else {
                PreparedStatement stmt = conn.prepareStatement(insertSQL);
                stmt.setString(1, username);
                stmt.setString(2, email);
                stmt.setString(3, password);
                int rows = stmt.executeUpdate();
                if (rows > 0) {
                    signUpStatus.setTextFill(Color.GREEN);
                    signUpStatus.setText("Sign-up successful!");
                    signUpStatus.setVisible(true);
                    createUserTable(username);
                } else {
                    signUpStatus.setTextFill(Color.RED);
                    signUpStatus.setText("Sign-up failed.");
                    signUpStatus.setVisible(true);
                }
                PauseTransition delay = new PauseTransition(Duration.seconds(3));
                delay.setOnFinished(event -> signUpStatus.setVisible(false));
                delay.play();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void createUserTable(String username) {
        // Validate username to avoid SQL injection
        if (username == null || !username.matches("[a-zA-Z0-9_]+")) {
            throw new IllegalArgumentException("Invalid username");
        }

        Connection conn = DBConnection.databaseConn;
        String query = "create table " + username +
                "(\n" +
                "    serialNo  int auto_increment\n" +
                "        primary key,\n" +
                "    product   varchar(255)                       not null,\n" +
                "    productID int                                not null,\n" +
                "    quantity  int                                not null,\n" +
                "    price     float                              not null,\n" +
                "    date      datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP\n" +
                ");";
        try (Statement statement = conn.createStatement()) {
            statement.executeUpdate(query);
        } catch (SQLException e) {
            throw new RuntimeException("Error creating table for user: " + username, e);
        }
    }



}