package com.attendance.plus.attendanceplus;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class LoginController {
    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    @FXML private Button loginButton;
    @FXML private Button signUpButton;

    private void checkFieldsValue() throws LoginException{
        if (usernameField.getText().isEmpty() || passwordField.getText().isEmpty()){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Attendance");
            alert.setHeaderText(null);
            alert.setContentText("Some field can not be empty!");
            alert.showAndWait();

            throw (new LoginException("Email and Password Field Empty"));
        }
    }

    @FXML
    private void setSignUpButton() throws LoginException{
        String usernameInput = usernameField.getText();
        String passwordInput = passwordField.getText();

        try {
            checkFieldsValue(); //check if username or password field empty

            File myObj = new File("C:\\Users\\Amir Shahrani\\Desktop\\UserData.txt");
            Scanner myReader = new Scanner(myObj);
            FileWriter myWriter = new FileWriter(myObj, true);

            //Check if username already taken
            while (myReader.hasNextLine()) {
                String[] data = myReader.nextLine().split("\t");

                if (usernameInput.equals(data[0])) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Attendance");
                    alert.setHeaderText(null);
                    alert.setContentText("Email already registered!");
                    alert.showAndWait();

                    myReader.close();
                    myWriter.close();
                   throw (new LoginException("Email already registered"));
                }
            }

            //if username not taken
            myWriter.write(String.format("%s\t%s\n", usernameInput, passwordInput));

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Attendance");
            alert.setHeaderText(null);
            alert.setContentText("Successfully created your account");
            alert.showAndWait();
            myReader.close();
            myWriter.close();
        } catch (IOException e) {
            System.out.println("An error occurred: " + e);
        } catch (LoginException e){
            System.out.println(e);
        }
    }

    private void loadDashboard() throws LoginException{
        try {
            // Load Dashboard.fxml
            FXMLLoader dashboard = new FXMLLoader(getClass().getResource("fxml/Dashboard.fxml"));
            Parent dashboardRoot = dashboard.load();

            // Get the current stage and switch scene
            Stage stage = (Stage) loginButton.getScene().getWindow();
            Scene dashboardScene = new Scene(dashboardRoot, 980, 620);
            stage.setScene(dashboardScene);
            stage.setTitle("Attendance+ - Dashboard");
        } catch (IOException e) {
            throw (new LoginException("Dashboard failed to load"));
        }
    }

    @FXML
    private void setLoginButton() throws LoginException{
        String usernameInput = usernameField.getText();
        String passwordInput = passwordField.getText();

        //only check if both field not empty
        try {
            checkFieldsValue(); //check if username or password field empty

            File myObj = new File("C:\\Users\\Amir Shahrani\\Desktop\\UserData.txt");
            Scanner myReader = new Scanner(myObj);

            while (myReader.hasNextLine()) {
                String[] data = myReader.nextLine().split("\t");

                if (usernameInput.equals(data[0])){
                    if (passwordInput.equals(data[1])){
                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setTitle("Attendance");
                        alert.setHeaderText(null);
                        alert.setContentText("Login successful");
                        alert.showAndWait();

                        loadDashboard();
                        return;
                    }
                    else{
                        Alert alert = new Alert(Alert.AlertType.WARNING);
                        alert.setTitle("Attendance");
                        alert.setHeaderText(null);
                        alert.setContentText("Incorrect Password");
                        alert.showAndWait();

                        throw (new LoginException("Invalid Password"));
                    }
                }
            }

            //If username not found in .txt file
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Attendance");
            alert.setHeaderText(null);
            alert.setContentText("Invalid username");
            alert.showAndWait();

            myReader.close();

            throw (new LoginException("Invalid Email"));
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred: " + e);
        } catch (LoginException e){
            System.out.println(e);
        }
    }
}