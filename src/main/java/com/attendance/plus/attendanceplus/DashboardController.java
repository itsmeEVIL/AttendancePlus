package com.attendance.plus.attendanceplus;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.beans.binding.Bindings;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.TextStyle;
import java.util.Locale;

public class DashboardController {

    @FXML
    private HBox topBar;

    @FXML
    private HBox mainBody;

    @FXML
    private VBox mainPanel;

    @FXML
    private GridPane calendar;

    @FXML
    private VBox sidePanel;

    @FXML
    private Label monthLabel, monLabel, tueLabel, wedLabel, thuLabel, friLabel, satLabel, sunLabel;

    @FXML
    private Button prevMonthButton, resetMonthButton, nextMonthButton;

    private Button selectedButton;
    private YearMonth currentYearMonth;

    private static final String DEFAULT_BG_COLOR = "-fx-pref-width: 55px; -fx-background-color: rgb(37, 37, 37); -fx-border-width: 0; -fx-font-size: 20px; -fx-cursor: hand; -fx-background-radius: 6px;";
    private static final String HIGHLIGHTED_BG_COLOR = "-fx-background-color: rgb(21, 133, 246); -fx-pref-width: 55px; -fx-border-width: 0; -fx-font-size: 20px; -fx-cursor: hand; -fx-background-radius: 6px;";

    @FXML
    public void initialize() {
        // Bind heights and widths when the scene is available
        topBar.sceneProperty().addListener((obs, oldScene, newScene) -> {
            if (newScene != null) {
                // Top and bottom HBox heights (15% and 85%)
                topBar.prefHeightProperty().bind(Bindings.multiply(newScene.heightProperty(), 0.15));
                mainBody.prefHeightProperty().bind(Bindings.multiply(newScene.heightProperty(), 0.85));

                // MainPanel and SidePanel widths (65% and 35%)
                mainPanel.prefWidthProperty().bind(mainBody.widthProperty().multiply(0.65));
                sidePanel.prefWidthProperty().bind(mainBody.widthProperty().multiply(0.35));

                // Bind calendar height to mainPanel height minus the HBox containing monthLabel and buttons
                calendar.prefHeightProperty().bind(mainPanel.heightProperty()
                        .subtract(monthLabel.getParent().prefHeight(-1))
                        .subtract(10)); // Extra buffer for padding

                // Bind header label sizes
                styleAndBindLabel(monLabel);
                styleAndBindLabel(tueLabel);
                styleAndBindLabel(wedLabel);
                styleAndBindLabel(thuLabel);
                styleAndBindLabel(friLabel);
                styleAndBindLabel(satLabel);
                styleAndBindLabel(sunLabel);
            }
        });

        // Initialize with March 2025
        currentYearMonth = YearMonth.of(2025, 3);
        updateMonthLabel();
        populateCalendar();
    }

    private void styleAndBindLabel(Label label) {
        label.prefWidthProperty().bind(calendar.widthProperty().divide(7));
        label.prefHeightProperty().bind(calendar.heightProperty().divide(8)); // 8 rows: header, gap, 6 dates
        label.setStyle("-fx-background-color: rgb(37, 37, 37); -fx-border-width: 0; -fx-text-fill: #B5B5B5; -fx-font: 18px 'Inter'; -fx-background-radius: 6px");
    }

    private void updateMonthLabel() {
        String monthName = currentYearMonth.getMonth().getDisplayName(TextStyle.FULL, Locale.ENGLISH).toUpperCase();
        monthLabel.setText(monthName + " " + currentYearMonth.getYear());
    }

    private void updateResetButtonStyle() {
        boolean isCurrentMonth = currentYearMonth.equals(YearMonth.now());
        resetMonthButton.setStyle(isCurrentMonth ? HIGHLIGHTED_BG_COLOR : DEFAULT_BG_COLOR);
    }

    private void populateCalendar() {
        // Clear existing buttons
        calendar.getChildren().removeIf(node -> GridPane.getRowIndex(node) >= 2);

        LocalDate firstOfMonth = currentYearMonth.atDay(1);
        int daysInMonth = currentYearMonth.lengthOfMonth();
        int startDay = firstOfMonth.getDayOfWeek().getValue() - 1; // Monday = 0

        // Get current date for highlighting
        LocalDate today = LocalDate.now();
        boolean isCurrentMonth = today.getYear() == currentYearMonth.getYear() && today.getMonthValue() == currentYearMonth.getMonthValue();
        int currentDay = isCurrentMonth ? today.getDayOfMonth() : -1;

        selectedButton = null; // Reset selection
        int day = 1;
        for (int row = 2; row <= 7 && day <= daysInMonth; row++) {
            for (int col = 0; col < 7 && day <= daysInMonth; col++) {
                if (row == 2 && col < startDay) {
                    continue;
                }
                Button dayButton = new Button(String.valueOf(day));
                dayButton.prefWidthProperty().bind(calendar.widthProperty().divide(7));
                dayButton.prefHeightProperty().bind(calendar.heightProperty().divide(8));
                dayButton.setStyle("-fx-background-color: rgb(37, 37, 37); -fx-border-width: 0; -fx-text-fill: #fff; -fx-font: 18px 'Inter'; -fx-cursor: hand; -fx-background-radius: 6px;");

                // Highlight current day initially if in current month
                if (day == currentDay && selectedButton == null) {
                    dayButton.setStyle("-fx-background-color: rgb(21, 133, 246); -fx-border-width: 0; -fx-text-fill: #fff; -fx-font: 18px 'Inter'; -fx-cursor: hand; -fx-background-radius: 6px");
                    selectedButton = dayButton;
                }

                // Add click handler to change highlight
                dayButton.setOnAction(event -> {
                    if (selectedButton != null && selectedButton != dayButton) {
                        selectedButton.setStyle("-fx-background-color: rgb(37, 37, 37); -fx-border-width: 0; -fx-text-fill: #fff; -fx-font: 18px 'Inter'; -fx-cursor: hand; -fx-background-radius: 6px");
                    }
                    dayButton.setStyle("-fx-background-color: rgb(21, 133, 246); -fx-border-width: 0; -fx-text-fill: #fff; -fx-font: 18px 'Inter'; -fx-cursor: hand; -fx-background-radius: 6px");
                    selectedButton = dayButton;
                });

                calendar.add(dayButton, col, row);
                day++;
            }
        }

        // Update reset button style after populating
        updateResetButtonStyle();
    }

    @FXML
    private void handlePrevMonth() {
        currentYearMonth = currentYearMonth.minusMonths(1);
        updateMonthLabel();
        populateCalendar();
    }

    @FXML
    private void handleResetMonth() {
        currentYearMonth = YearMonth.now();
        updateMonthLabel();
        populateCalendar();
    }

    @FXML
    private void handleNextMonth() {
        currentYearMonth = currentYearMonth.plusMonths(1);
        updateMonthLabel();
        populateCalendar();
    }
}