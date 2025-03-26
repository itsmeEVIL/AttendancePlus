package com.attendance.plus.attendanceplus;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

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

    // Map to store attendance stats (cumulative present/absent counts)
    private Map<String, AttendanceData> attendanceData = new HashMap<>();
    // Map to track specific dates when attendance was marked for each subject
    private Map<String, Set<LocalDate>> markedDates = new HashMap<>();

    // Inner class to hold attendance stats
    private static class AttendanceData {
        int presentCount;
        int absentCount;

        AttendanceData(int present, int absent) {
            this.presentCount = present;
            this.absentCount = absent;
        }

        double getRatio() {
            int total = presentCount + absentCount;
            return total == 0 ? 0.0 : (presentCount * 100.0) / total;
        }
    }

    public void setDashboardController(DashboardController controller) {
        this.dashboardController = controller;
    }

    @FXML
    public void initialize() {
        setupCardClickHandlers();
        initializeDummyData();
    }

    // Initialize dummy data for week 10 of 12-week semester
    private void initializeDummyData() {
        attendanceData.put("TFB1093-Data Communication and Network", new AttendanceData(8, 2)); // 10 sessions
        attendanceData.put("TEB1053-Discrete Mathematics", new AttendanceData(9, 1)); // 10 sessions
        attendanceData.put("MPU3192-Falsafah dan Isu Semasa", new AttendanceData(7, 3)); // 10 sessions
        attendanceData.put("TEB1043-Object-oriented Programming", new AttendanceData(15, 3)); // 18 sessions (twice weekly)
        attendanceData.put("CEB1032-Health Safety and Environment", new AttendanceData(8, 2)); // 10 sessions
        attendanceData.put("TFB1093-Data Communication and Network (Lab)", new AttendanceData(7, 3)); // 10 sessions
        attendanceData.put("TFB1043-Object-oriented Programming", new AttendanceData(16, 2)); // 18 sessions (duplicate handled by name)

        // Initialize markedDates with dummy past dates (e.g., assuming some days in March 2025 were marked)
        LocalDate startOfSemester = LocalDate.of(2025, 1, 6); // Arbitrary start
        for (String subject : attendanceData.keySet()) {
            Set<LocalDate> dates = new HashSet<>();
            AttendanceData data = attendanceData.get(subject);
            int presentDays = data.presentCount;
            LocalDate date = startOfSemester;
            int dayCount = 0;
            while (dayCount < presentDays) {
                if (isSubjectDay(subject, date.getDayOfWeek())) {
                    dates.add(date);
                    dayCount++;
                }
                date = date.plusDays(1);
            }
            markedDates.put(subject, dates);
        }
    }

    // Helper to determine if a subject occurs on a given day (simplified logic)
    private boolean isSubjectDay(String subject, DayOfWeek day) {
        return switch (day) {
            case MONDAY -> subject.contains("TFB1093") || subject.contains("TEB1053");
            case TUESDAY -> subject.contains("MPU3192") || subject.contains("TEB1043");
            case WEDNESDAY -> subject.contains("TFB1093");
            case THURSDAY -> subject.contains("TFB1043") || subject.contains("TEB1053");
            case FRIDAY -> subject.contains("CEB1032");
            default -> false;
        };
    }

    public void updateSubjectPanel() {
        LocalDate selectedDate = dashboardController.getSelectedDate();
        dateLabel.setText(selectedDate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        DayOfWeek dayOfWeek = selectedDate.getDayOfWeek();

        // Reset visibility
        subjectCard1.setVisible(false);
        subjectCard1.setManaged(false);
        subjectCard2.setVisible(false);
        subjectCard2.setManaged(false);

        // Determine date context relative to today
        LocalDate today = LocalDate.now();
        boolean isToday = selectedDate.equals(today);
        boolean isPast = selectedDate.isBefore(today);

        // Update subjects based on day of week
        if (dayOfWeek == DayOfWeek.SATURDAY || dayOfWeek == DayOfWeek.SUNDAY) {
            // No subjects on weekends
        } else if (dayOfWeek == DayOfWeek.MONDAY) {
            updateSubject(subjectCard1, courseLabel1, placeLabel1, timeLabel1, subjectLabel1, attendButton1, presentLabel1, absentLabel1, ratioLabel1,
                    "TFB1093", "D6", "10:00-12:00", "Data Communication and Network", selectedDate, isToday, isPast);
            updateSubject(subjectCard2, courseLabel2, placeLabel2, timeLabel2, subjectLabel2, attendButton2, presentLabel2, absentLabel2, ratioLabel2,
                    "TEB1053", "C4", "2:00-4:00", "Discrete Mathematics", selectedDate, isToday, isPast);
        } else if (dayOfWeek == DayOfWeek.TUESDAY) {
            updateSubject(subjectCard1, courseLabel1, placeLabel1, timeLabel1, subjectLabel1, attendButton1, presentLabel1, absentLabel1, ratioLabel1,
                    "MPU3192", "D3", "8:00-10:00", "Falsafah dan Isu Semasa", selectedDate, isToday, isPast);
            updateSubject(subjectCard2, courseLabel2, placeLabel2, timeLabel2, subjectLabel2, attendButton2, presentLabel2, absentLabel2, ratioLabel2,
                    "TEB1043", "D6", "16:00-18:00", "Object-oriented Programming", selectedDate, isToday, isPast);
        } else if (dayOfWeek == DayOfWeek.WEDNESDAY) {
            updateSubject(subjectCard1, courseLabel1, placeLabel1, timeLabel1, subjectLabel1, attendButton1, presentLabel1, absentLabel1, ratioLabel1,
                    "TFB1093", "02-01-10", "10:00-12:00", "Data Communication and Network (Lab)", selectedDate, isToday, isPast);
        } else if (dayOfWeek == DayOfWeek.THURSDAY) {
            updateSubject(subjectCard1, courseLabel1, placeLabel1, timeLabel1, subjectLabel1, attendButton1, presentLabel1, absentLabel1, ratioLabel1,
                    "TFB1043", "01-02-05", "9:00-11:00", "Object-oriented Programming", selectedDate, isToday, isPast);
            updateSubject(subjectCard2, courseLabel2, placeLabel2, timeLabel2, subjectLabel2, attendButton2, presentLabel2, absentLabel2, ratioLabel2,
                    "TEB1053", "01-02-10", "12:00-14:00", "Discrete Mathematics", selectedDate, isToday, isPast);
        } else if (dayOfWeek == DayOfWeek.FRIDAY) {
            updateSubject(subjectCard1, courseLabel1, placeLabel1, timeLabel1, subjectLabel1, attendButton1, presentLabel1, absentLabel1, ratioLabel1,
                    "CEB1032", "Online", "15:00-17:00", "Health Safety and Environment", selectedDate, isToday, isPast);
        }
    }

    private void updateSubject(VBox card, Label course, Label place, Label time, Label subject, Button attendButton,
                               Label presentLabel, Label absentLabel, Label ratioLabel,
                               String courseText, String placeText, String timeText, String subjectText,
                               LocalDate selectedDate, boolean isToday, boolean isPast) {
        card.setVisible(true);
        card.setManaged(true);
        course.setText(courseText);
        place.setText(placeText);
        time.setText(timeText);
        subject.setText(subjectText);

        String subjectKey = courseText + "-" + subjectText;
        AttendanceData data = attendanceData.getOrDefault(subjectKey, new AttendanceData(0, 0));
        Set<LocalDate> datesMarked = markedDates.getOrDefault(subjectKey, new HashSet<>());

        presentLabel.setText(String.valueOf(data.presentCount));
        absentLabel.setText(String.valueOf(data.absentCount));
        double ratio = data.getRatio();
        ratioLabel.setText(String.format("%.2f", ratio));
        ratioLabel.setTextFill(ratio < 90.0 ? Color.RED : Color.web("#64EE64"));

        // Button state logic
        if (isPast) {
            if (datesMarked.contains(selectedDate)) {
                attendButton.setText("Marked");
                attendButton.setStyle("-fx-background-color: #86FF80; -fx-text-fill: black; -fx-opacity: 0.6; -fx-cursor: none;"); // Green, faded for past
            } else {
                attendButton.setText("Missed");
                attendButton.setStyle("-fx-background-color: #FF0000; -fx-text-fill: white; -fx-opacity: 0.6; -fx-cursor: none;"); // Red for missed
            }
        } else if (isToday) {
            if (datesMarked.contains(selectedDate)) {
                attendButton.setText("Marked");
                attendButton.setStyle("-fx-background-color: #86FF80; -fx-text-fill: black; -fx-opacity: 0.6; -fx-cursor: none;"); // Green, faded to indicate final
            } else {
                attendButton.setText("Attend");
                attendButton.setStyle(""); // Active style for clickable
            }
        } else { // Future
            attendButton.setText("Not Yet");
            attendButton.setStyle("-fx-background-color: #FFFF99; -fx-text-fill: black; -fx-opacity: 0.6; -fx-cursor: none;"); // Yellow for future
        }
        // Keep button enabled in all cases per your requirement
        attendButton.setDisable(false);
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
        markAttendance("TFB1093-" + subjectLabel1.getText(), attendButton1, presentLabel1, absentLabel1, ratioLabel1);
    }

    @FXML
    private void attendBtnclick2() {
        markAttendance(courseLabel2.getText() + "-" + subjectLabel2.getText(), attendButton2, presentLabel2, absentLabel2, ratioLabel2);
    }

    private void markAttendance(String subjectKey, Button attendButton, Label presentLabel, Label absentLabel, Label ratioLabel) {
        LocalDate selectedDate = dashboardController.getSelectedDate();
        LocalDate today = LocalDate.now();

        // Only allow marking if the selected date is today and the button is "Attend"
        if (!selectedDate.equals(today) || attendButton.getText().equals("Marked")) {
            return; // Do nothing if not today or already marked
        }

        AttendanceData data = attendanceData.getOrDefault(subjectKey, new AttendanceData(0, 0));
        Set<LocalDate> datesMarked = markedDates.getOrDefault(subjectKey, new HashSet<>());

        // Mark attendance only if not already marked
        if (!datesMarked.contains(selectedDate)) {
            data.presentCount++;
            datesMarked.add(selectedDate);
            attendanceData.put(subjectKey, data);
            markedDates.put(subjectKey, datesMarked);

            attendButton.setText("Marked");
            attendButton.setStyle("-fx-background-color: #86FF80; -fx-text-fill: black; -fx-opacity: 0.6; -fx-cursor: none;"); // Faded to indicate final

            presentLabel.setText(String.valueOf(data.presentCount));
            absentLabel.setText(String.valueOf(data.absentCount));
            double ratio = data.getRatio();
            ratioLabel.setText(String.format("%.2f", ratio));
            ratioLabel.setTextFill(ratio < 90.0 ? Color.RED : Color.web("#64EE64"));
        }
    }
}