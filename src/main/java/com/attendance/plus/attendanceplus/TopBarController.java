package com.attendance.plus.attendanceplus;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

public class TopBarController {
    @FXML
    private Button remindersButton;

    @FXML
    public void initialize() {
        // Placeholder for reminders button logic, as functionality is not specified
        remindersButton.setOnAction(event -> {
            try {
                // Load Dashboard.fxml
                FXMLLoader reminders = new FXMLLoader(getClass().getResource("fxml/Reminders.fxml"));
                Parent reminderRoot = reminders.load();

                // Get the current stage and switch scene
                Stage stage = new Stage();
                Scene reminderScene = new Scene(reminderRoot, 365, 620);
                stage.setScene(reminderScene);
                stage.setTitle("Attendance+ - Reminder");

                stage.initModality(Modality.APPLICATION_MODAL);

                stage.showAndWait();
            } catch (IOException e) {
                System.out.println(e);
            }
        });
    }
}