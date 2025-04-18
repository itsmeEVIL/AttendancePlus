package com.attendance.plus.attendanceplus;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.text.Font;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.io.IOException;

public class AttendancePlus extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        // Load custom fonts from the resources/fonts directory
        Font.loadFont(getClass().getResourceAsStream("fonts/Inter-Bold.ttf"), 14);
        Font.loadFont(getClass().getResourceAsStream("fonts/Inter-ExtraBold.ttf"), 14);
        Font.loadFont(getClass().getResourceAsStream("fonts/Inter-SemiBold.ttf"), 14);

        // Load the FXML file
        FXMLLoader fxmlLoader = new FXMLLoader(AttendancePlus.class.getResource("fxml/Login.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 365, 620);
        stage.setTitle("Attendance+ - Login");
        stage.setScene(scene);
        // Center the stage manually
        Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
        stage.setX((screenBounds.getWidth() - 365) / 2);
        stage.setY((screenBounds.getHeight() - 620) / 2);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}