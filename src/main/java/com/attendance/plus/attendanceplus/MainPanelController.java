package com.attendance.plus.attendanceplus;

import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.TextStyle;
import java.util.Locale;

abstract  class CalendarUI{
    abstract void updateCalendar();
    abstract void updateMonthLabel();
    abstract void clearCalendar();
    abstract void updateResetButtonStyle();
    abstract void handlePrevMonth();
    abstract void handleNextMonth();
    abstract void handleResetMonth();
    abstract void bindHeaderLabels();
    abstract void bindLabel(Label label);
    abstract Button createDayButton(int day);
}

public class MainPanelController extends CalendarUI {
    @FXML private Label monthLabel;
    @FXML private Button prevMonthButton;
    @FXML private Button resetMonthButton;
    @FXML private Button nextMonthButton;
    @FXML private GridPane calendar;
    @FXML private Label monLabel, tueLabel, wedLabel, thuLabel, friLabel, satLabel, sunLabel;

    private DashboardController dashboardController;
    private YearMonth currentYearMonth;

    private static final int CALENDAR_ROWS = 8;
    private static final int CALENDAR_COLS = 7;
    private static final int FIRST_DATE_ROW = 2;

    public void setDashboardController(DashboardController controller) {
        this.dashboardController = controller;
    }

    @FXML
    public void initialize() {
        currentYearMonth = YearMonth.of(2025, 3); // Default starting month
        updateMonthLabel(); // Safe to call, doesn’t depend on dashboardController
        bindHeaderLabels();
        // Do NOT call updateCalendar() here
        VBox.setVgrow(calendar, javafx.scene.layout.Priority.ALWAYS);
    }


    public void bindHeaderLabels() {
        Label[] headers = {monLabel, tueLabel, wedLabel, thuLabel, friLabel, satLabel, sunLabel};
        for (Label header : headers) {
            bindLabel(header);
        }
    }

    /**
     * Applies size bindings to a label.
     */
    public void bindLabel(Label label) {
        label.prefWidthProperty().bind(calendar.widthProperty().divide(CALENDAR_COLS));
        label.prefHeightProperty().bind(calendar.heightProperty().divide(CALENDAR_ROWS));
    }

    public void updateMonthLabel() {
        String monthName = currentYearMonth.getMonth().getDisplayName(TextStyle.FULL, Locale.ENGLISH).toUpperCase();
        monthLabel.setText(monthName + " " + currentYearMonth.getYear());
    }

    public void updateCalendar() {
        clearCalendar();
        LocalDate firstOfMonth = currentYearMonth.atDay(1);
        int daysInMonth = currentYearMonth.lengthOfMonth();
        int startDay = firstOfMonth.getDayOfWeek().getValue() - 1; // Monday = 0
        int day = 1;
        LocalDate selectedDate = dashboardController.getSelectedDate();

        for (int row = FIRST_DATE_ROW; row < CALENDAR_ROWS && day <= daysInMonth; row++) {
            for (int col = 0; col < CALENDAR_COLS && day <= daysInMonth; col++) {
                if (row == FIRST_DATE_ROW && col < startDay) continue;
                Button dayButton = createDayButton(day);
                calendar.add(dayButton, col, row);
                if (selectedDate != null && selectedDate.getDayOfMonth() == day &&
                        selectedDate.getMonth() == currentYearMonth.getMonth() &&
                        selectedDate.getYear() == currentYearMonth.getYear()) {
                    dayButton.getStyleClass().add("calendarDaySelected");
                }
                day++;
            }
        }
        updateResetButtonStyle();
    }

    public Button createDayButton(int day) {
        Button dayButton = new Button(String.valueOf(day));
        dayButton.prefWidthProperty().bind(calendar.widthProperty().divide(CALENDAR_COLS));
        dayButton.prefHeightProperty().bind(calendar.heightProperty().divide(CALENDAR_ROWS));
        dayButton.getStyleClass().add("calendarDayDefault");

        dayButton.setOnAction(event -> {
            LocalDate newDate = LocalDate.of(currentYearMonth.getYear(), currentYearMonth.getMonthValue(), day);
            dashboardController.setSelectedDate(newDate);
            updateCalendar(); // Re-render to update highlighting
        });
        return dayButton;
    }

    public void clearCalendar() {
        calendar.getChildren().removeIf(node -> GridPane.getRowIndex(node) >= FIRST_DATE_ROW);
    }

    public void updateResetButtonStyle() {
        resetMonthButton.getStyleClass().removeAll("nav-button-default", "nav-button-highlighted");
        boolean isCurrentMonthAndToday = currentYearMonth.equals(YearMonth.now()) &&
                dashboardController.getSelectedDate().equals(LocalDate.now());
        resetMonthButton.getStyleClass().add(isCurrentMonthAndToday ? "nav-button-highlighted" : "nav-button-default");
    }

    @FXML
    public void handlePrevMonth() {
        currentYearMonth = currentYearMonth.minusMonths(1);
        updateMonthLabel();
        updateCalendar();
    }

    @FXML
    public void handleNextMonth() {
        currentYearMonth = currentYearMonth.plusMonths(1);
        updateMonthLabel();
        updateCalendar();
    }

    @FXML
    public void handleResetMonth() {
        currentYearMonth = YearMonth.now();
        dashboardController.setSelectedDate(LocalDate.now());
        updateMonthLabel();
        updateCalendar();
    }
}