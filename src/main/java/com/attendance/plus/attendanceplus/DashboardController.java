package com.attendance.plus.attendanceplus;

import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.TextStyle;
import java.util.Locale;

/**
 * Controller for the dashboard UI, managing a calendar display with month navigation.
 */
public class DashboardController {

    // UI Components
    @FXML private HBox topBar;
    @FXML private HBox mainBody;
    @FXML private VBox mainPanel;
    @FXML private GridPane calendar;
    @FXML private VBox sidePanel;
    @FXML private Label monthLabel;
    @FXML private Label monLabel, tueLabel, wedLabel, thuLabel, friLabel, satLabel, sunLabel;
    @FXML private Button prevMonthButton, resetMonthButton, nextMonthButton;

    // State
    private LocalDate selectedDate; // Tracks the selected date persistently
    private YearMonth currentYearMonth;

    // Constants
    private static final String DEFAULT_DAY_STYLE = "-fx-background-color: rgb(37, 37, 37); -fx-border-width: 0; -fx-text-fill: #fff; -fx-font: 18px 'Inter'; -fx-cursor: hand; -fx-background-radius: 6px;";
    private static final String HIGHLIGHTED_DAY_STYLE = "-fx-background-color: rgb(21, 133, 246); -fx-border-width: 0; -fx-text-fill: #fff; -fx-font: 18px 'Inter'; -fx-cursor: hand; -fx-background-radius: 6px;";
    private static final String HEADER_STYLE = "-fx-background-color: rgb(37, 37, 37); -fx-border-width: 0; -fx-text-fill: #B5B5B5; -fx-font: 18px 'Inter'; -fx-background-radius: 6px;";
    private static final String DEFAULT_NAV_STYLE = "-fx-pref-width: 55px; -fx-background-color: rgb(37, 37, 37); -fx-border-width: 0; -fx-font-size: 20px; -fx-cursor: hand; -fx-background-radius: 6px;";
    private static final String HIGHLIGHTED_NAV_STYLE = "-fx-pref-width: 55px; -fx-background-color: rgb(21, 133, 246); -fx-border-width: 0; -fx-font-size: 20px; -fx-cursor: hand; -fx-background-radius: 6px;";
    private static final double TOP_BAR_HEIGHT_RATIO = 0.15;
    private static final double MAIN_PANEL_WIDTH_RATIO = 0.65;
    private static final int CALENDAR_ROWS = 8;
    private static final int CALENDAR_COLS = 7;
    private static final int FIRST_DATE_ROW = 2;

    /**
     * Initializes the controller, setting up bindings and initial state.
     */
    @FXML
    public void initialize() {
        setupBindings();
        currentYearMonth = YearMonth.of(2025, 3); // Initial month
        selectedDate = LocalDate.now(); // Default to today
        updateMonthLabel();
        populateCalendar();
        logSelectedDate();
    }

    /**
     * Sets up size bindings for UI components.
     */
    private void setupBindings() {
        topBar.sceneProperty().addListener((obs, oldScene, newScene) -> {
            if (newScene != null) {
                bindHeights(newScene);
                bindWidths();
                bindCalendarHeight();
                bindHeaderLabels();
            }
        });
    }

    private void bindHeights(javafx.scene.Scene scene) {
        topBar.prefHeightProperty().bind(Bindings.multiply(scene.heightProperty(), TOP_BAR_HEIGHT_RATIO));
        mainBody.prefHeightProperty().bind(Bindings.multiply(scene.heightProperty(), 1 - TOP_BAR_HEIGHT_RATIO));
    }

    private void bindWidths() {
        mainPanel.prefWidthProperty().bind(mainBody.widthProperty().multiply(MAIN_PANEL_WIDTH_RATIO));
        sidePanel.prefWidthProperty().bind(mainBody.widthProperty().multiply(1 - MAIN_PANEL_WIDTH_RATIO));
    }

    private void bindCalendarHeight() {
        calendar.prefHeightProperty().bind(mainPanel.heightProperty()
                .subtract(monthLabel.getParent().prefHeight(-1))
                .subtract(10)); // Buffer for padding
    }

    private void bindHeaderLabels() {
        Label[] headers = {monLabel, tueLabel, wedLabel, thuLabel, friLabel, satLabel, sunLabel};
        for (Label header : headers) {
            styleAndBindLabel(header);
        }
    }

    /**
     * Applies size bindings and style to a label.
     */
    private void styleAndBindLabel(Label label) {
        label.prefWidthProperty().bind(calendar.widthProperty().divide(CALENDAR_COLS));
        label.prefHeightProperty().bind(calendar.heightProperty().divide(CALENDAR_ROWS));
        label.setStyle(HEADER_STYLE);
    }

    /**
     * Updates the month label with the current year and month.
     */
    private void updateMonthLabel() {
        String monthName = currentYearMonth.getMonth().getDisplayName(TextStyle.FULL, Locale.ENGLISH).toUpperCase();
        monthLabel.setText(monthName + " " + currentYearMonth.getYear());
    }

    /**
     * Updates the reset button's style based on whether the current month is displayed.
     */
    private void updateResetButtonStyle() {
        resetMonthButton.setStyle(currentYearMonth.equals(YearMonth.now()) ? HIGHLIGHTED_NAV_STYLE : DEFAULT_NAV_STYLE);
    }

    /**
     * Populates the calendar with days for the current month.
     */
    private void populateCalendar() {
        clearCalendar();
        LocalDate today = LocalDate.now();
        boolean isCurrentMonth = isCurrentMonth(today);
        int currentDay = isCurrentMonth ? today.getDayOfMonth() : -1;

        LocalDate firstOfMonth = currentYearMonth.atDay(1);
        int daysInMonth = currentYearMonth.lengthOfMonth();
        int startDay = firstOfMonth.getDayOfWeek().getValue() - 1; // Monday = 0

        int day = 1;
        for (int row = FIRST_DATE_ROW; row < CALENDAR_ROWS && day <= daysInMonth; row++) {
            for (int col = 0; col < CALENDAR_COLS && day <= daysInMonth; col++) {
                if (row == FIRST_DATE_ROW && col < startDay) {
                    continue;
                }
                Button dayButton = createDayButton(day, currentDay);
                calendar.add(dayButton, col, row);
                // Highlight if this matches the selected date
                if (selectedDate != null && selectedDate.getYear() == currentYearMonth.getYear() &&
                        selectedDate.getMonthValue() == currentYearMonth.getMonthValue() &&
                        selectedDate.getDayOfMonth() == day) {
                    dayButton.setStyle(HIGHLIGHTED_DAY_STYLE);
                }
                day++;
            }
        }
        updateResetButtonStyle();
    }

    private boolean isCurrentMonth(LocalDate today) {
        return today.getYear() == currentYearMonth.getYear() && today.getMonthValue() == currentYearMonth.getMonthValue();
    }

    /**
     * Creates a day button with appropriate styling and click behavior.
     */
    private Button createDayButton(int day, int currentDay) {
        Button dayButton = new Button(String.valueOf(day));
        dayButton.prefWidthProperty().bind(calendar.widthProperty().divide(CALENDAR_COLS));
        dayButton.prefHeightProperty().bind(calendar.heightProperty().divide(CALENDAR_ROWS));
        dayButton.setStyle(DEFAULT_DAY_STYLE);

        // Initial highlight for today if no selection yet
        if (day == currentDay && selectedDate == null) {
            selectedDate = LocalDate.of(currentYearMonth.getYear(), currentYearMonth.getMonthValue(), day);
            dayButton.setStyle(HIGHLIGHTED_DAY_STYLE);
        }

        dayButton.setOnAction(event -> handleDaySelection(day, dayButton));
        return dayButton;
    }

    /**
     * Handles day button selection, updating the selected date and highlight.
     */
    private void handleDaySelection(int day, Button dayButton) {
        // Update selected date
        selectedDate = LocalDate.of(currentYearMonth.getYear(), currentYearMonth.getMonthValue(), day);

        // Update UI: unhighlight previous selection, highlight new selection
        calendar.getChildren().forEach(node -> {
            if (node instanceof Button && GridPane.getRowIndex(node) >= FIRST_DATE_ROW) {
                node.setStyle(DEFAULT_DAY_STYLE);
            }
        });
        dayButton.setStyle(HIGHLIGHTED_DAY_STYLE);

        logSelectedDate();
    }

    /**
     * Logs the currently selected date to the console.
     */
    private void logSelectedDate() {
        if (selectedDate != null) {
            System.out.println("Selected Date: " + selectedDate.toString()); // Outputs YYYY-MM-DD
        } else {
            System.out.println("No date selected");
        }
    }

    /**
     * Clears all day buttons from the calendar.
     */
    private void clearCalendar() {
        calendar.getChildren().removeIf(node -> GridPane.getRowIndex(node) >= FIRST_DATE_ROW);
    }

    // Navigation Handlers
    @FXML
    private void handlePrevMonth() {
        updateMonth(-1);
    }

    @FXML
    private void handleResetMonth() {
        currentYearMonth = YearMonth.now();
        selectedDate = LocalDate.now(); // Reset selected date to today
        updateMonthLabel();
        populateCalendar();
        logSelectedDate(); // Log the new selected date
    }

    @FXML
    private void handleNextMonth() {
        updateMonth(1);
    }

    /**
     * Updates the month by the given delta and refreshes the UI.
     */
    private void updateMonth(int delta) {
        currentYearMonth = currentYearMonth.plusMonths(delta);
        updateMonthLabel();
        populateCalendar();
    }
}