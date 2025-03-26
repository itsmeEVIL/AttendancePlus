package com.attendance.plus.attendanceplus;

import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class TopBarController {
    @FXML
    private Button remindersButton;

    @FXML
    public void initialize() {
        // Placeholder for reminders button logic, as functionality is not specified
        remindersButton.setOnAction(event -> System.out.println("Reminders button clicked"));
    }
}