package com.enam.supershop.init;

import com.enam.supershop.currentUser.Info;
import com.enam.supershop.utils.DBConnection;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static com.enam.supershop.utils.UserDefMethods.*;

public class SignInController {
    @FXML private HBox container;
    @FXML private Label usernameLabel;
    @FXML private TextField usernameField;
    @FXML private Label usernameStatus;
    @FXML private Label passwordLabel;
    @FXML private PasswordField passwordField;
    @FXML private Label passwordStatus;
    @FXML private Hyperlink signUpLink;
    @FXML private Button signInButton;
    @FXML private Label signInStatus;


    public void initialize() {
        // Initialize scene and stage after FXML injection
        checkFieldStatus(usernameField, usernameStatus, "Username");
        checkFieldStatus(passwordField, passwordStatus, "Password");
    }

    public void goToSignUp() {
        signUpLink.setOnMouseClicked(event -> {
            try {
                Parent signUpRoot = FXMLLoader.load(getClass().getResource("signUp.fxml"));
                Stage stage = (Stage) signUpLink.getScene().getWindow();
                stage.setScene(new Scene(signUpRoot));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    public void handleSignIn() {
        String username = usernameField.getText();
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
        if (password.isEmpty()) {
            makeLabelExistent(passwordStatus, "Password cannot be empty!");
            return;
        }
        String checkSQL = "SELECT * FROM customers WHERE username = ? AND password = ?";
        String checkEmployeeSQL = "SELECT * FROM employees WHERE username = ? AND password = ?";
        Connection conn = DBConnection.databaseConn;
        try {
            PreparedStatement checkStmt = conn.prepareStatement(checkSQL);
            PreparedStatement checkEmployeeStmt = conn.prepareStatement(checkEmployeeSQL);
            checkStmt.setString(1, username);
            checkStmt.setString(2, password);
            checkEmployeeStmt.setString(1, username);
            checkEmployeeStmt.setString(2, password);
            ResultSet rs = checkStmt.executeQuery();

            if (rs.next()) { // A matching record is found
                System.out.println("Initialising sign in...");
                Info.setInfo(username, rs.getInt("id"), rs.getString("email"));
                Parent root = FXMLLoader.load(getClass().getResource("/com/enam/supershop/onlineStore/homepage/view.fxml"));
                Stage stage = (Stage) signInButton.getScene().getWindow();
                stage.close();
                Stage newStage = new Stage();
                newStage.setScene(new Scene(root));
                newStage.show();
            } else {
                ResultSet rs2 = checkEmployeeStmt.executeQuery();
                if (rs2.next()) {
                    Parent root = FXMLLoader.load(getClass().getResource("/com/enam/supershop/inventory/homepage.fxml"));
                    Stage stage = (Stage) signInButton.getScene().getWindow();
                    stage.close();
                    Stage newStage = new Stage();
                    newStage.setScene(new Scene(root));
                    newStage.show();
                } else {
                    makeLabelExistent(usernameStatus, "Incorrect credentials!");
                    makeLabelExistent(passwordStatus, "Incorrect credentials!");
                    return;
                }
            }
        } catch (SQLException | IOException e) {
            throw new RuntimeException(e);
        }
    }
}
