package com.attendance.plus.attendanceplus;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class LoginController {

    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    @FXML private Button loginButton;

    /**
     * Handles the login button action, redirecting to the dashboard if credentials are valid.
     */
    @FXML
    private void handleLogin() {
        // Simple placeholder validation (replace with real logic later)
        String username = usernameField.getText();
        String password = passwordField.getText();

        if ("admin".equals(username) && "password".equals(password)) {
            try {
                // Load Dashboard.fxml
                FXMLLoader loader = new FXMLLoader(getClass().getResource("fxml/Dashboard.fxml"));
                Parent dashboardRoot = loader.load();

                // Get the current stage and switch scene
                Stage stage = (Stage) loginButton.getScene().getWindow();
                Scene dashboardScene = new Scene(dashboardRoot, 980, 620);
                stage.setScene(dashboardScene);
                stage.setTitle("Attendance+ - Dashboard");
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            // For now, just print an error (could add a UI alert later)
            System.out.println("Invalid credentials");
        }
    }
}