package com.attendance.plus.attendanceplus;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class AttendancePlus extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(AttendancePlus.class.getResource("Dashboard.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 980, 620);
        stage.setTitle("Attendance+");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}