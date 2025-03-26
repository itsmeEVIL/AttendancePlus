package com.attendance.plus.attendanceplus;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class SidePanelController {
    @FXML private Label dateLabel;
    @FXML private VBox subjectCard1;
    @FXML private Label courseLabel1, placeLabel1, timeLabel1, subjectLabel1;
    @FXML private Button attendButton1;
    @FXML private HBox subjectStats1;
    @FXML private Label presentLabel1, absentLabel1, ratioLabel1;

    @FXML private VBox subjectCard2;
    @FXML private Label courseLabel2, placeLabel2, timeLabel2, subjectLabel2;
    @FXML private Button attendButton2;
    @FXML private HBox subjectStats2;
    @FXML private Label presentLabel2, absentLabel2, ratioLabel2;

    private DashboardController dashboardController;
    private int presentCount = 0;

    public void setDashboardController(DashboardController controller) {
        this.dashboardController = controller;
    }

    @FXML
    public void initialize() {
        setupCardClickHandlers();
    }

    public void updateSubjectPanel() {
        LocalDate selectedDate = dashboardController.getSelectedDate();
        dateLabel.setText(selectedDate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        DayOfWeek dayOfWeek = selectedDate.getDayOfWeek();

        subjectCard1.setVisible(false);
        subjectCard1.setManaged(false);
        subjectCard2.setVisible(false);
        subjectCard2.setManaged(false);

        if (dayOfWeek == DayOfWeek.SATURDAY || dayOfWeek == DayOfWeek.SUNDAY) {
            // No subjects on weekends
        } else if (dayOfWeek == DayOfWeek.MONDAY) {
            setSubject(subjectCard1, courseLabel1, placeLabel1, timeLabel1, subjectLabel1, "TFB1093", "D6", "10:00-12:00", "Data Communication and Network");
            setSubject(subjectCard2, courseLabel2, placeLabel2, timeLabel2, subjectLabel2, "TEB1053", "C4", "2:00-4:00", "Discrete Mathematics");
        } else if (dayOfWeek == DayOfWeek.TUESDAY) {
            setSubject(subjectCard1, courseLabel1, placeLabel1, timeLabel1, subjectLabel1, "MPU3192", "D3", "8:00-10:00", "Falsafah dan Isu Semasa");
            setSubject(subjectCard2, courseLabel2, placeLabel2, timeLabel2, subjectLabel2, "TEB1043", "D6", "16:00-18:00", "Object-oriented Programming");
        } else if (dayOfWeek == DayOfWeek.WEDNESDAY) {
            setSubject(subjectCard1, courseLabel1, placeLabel1, timeLabel1, subjectLabel1, "TFB1093", "02-01-10", "10:00-12:00", "Data Communication and Network (Lab)");
        } else if (dayOfWeek == DayOfWeek.THURSDAY) {
            setSubject(subjectCard1, courseLabel1, placeLabel1, timeLabel1, subjectLabel1, "TFB1043", "01-02-05", "9:00-11:00", "Object-oriented Programming");
            setSubject(subjectCard2, courseLabel2, placeLabel2, timeLabel2, subjectLabel2, "TEB1053", "01-02-10", "12:00-14:00", "Discrete Mathematics");
        } else if (dayOfWeek == DayOfWeek.FRIDAY) {
            setSubject(subjectCard1, courseLabel1, placeLabel1, timeLabel1, subjectLabel1, "CEB1032", "Online", "15:00-17:00", "Health Safety and Environment");
        }

        attendButton1.setDisable(false);
        attendButton2.setDisable(false);
        presentLabel1.setText(String.valueOf(presentCount));
        presentLabel2.setText(String.valueOf(presentCount));
    }

    private void setSubject(VBox card, Label course, Label place, Label time, Label subject, String courseText, String placeText, String timeText, String subjectText) {
        card.setVisible(true);
        card.setManaged(true);
        course.setText(courseText);
        place.setText(placeText);
        time.setText(timeText);
        subject.setText(subjectText);
    }

    private void setupCardClickHandlers() {
        subjectCard1.setOnMouseClicked(event -> {
            subjectStats1.setVisible(!subjectStats1.isVisible());
            subjectStats1.setManaged(subjectStats1.isVisible());
        });
        subjectCard2.setOnMouseClicked(event -> {
            subjectStats2.setVisible(!subjectStats2.isVisible());
            subjectStats2.setManaged(subjectStats2.isVisible());
        });
    }

    @FXML
    private void attendBtnclick1() {
        attendButton1.setText("Marked");
        attendButton1.setStyle("-fx-background-color: #86FF80;");
        presentCount++;
        presentLabel1.setText(String.valueOf(presentCount));
        attendButton1.setDisable(true);
    }

    @FXML
    private void attendBtnclick2() {
        attendButton2.setText("Marked");
        attendButton2.setStyle("-fx-background-color: #86FF80;");
        presentCount++;
        presentLabel2.setText(String.valueOf(presentCount));
        attendButton2.setDisable(true);
    }
}