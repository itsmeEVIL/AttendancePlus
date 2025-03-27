package com.attendance.plus.attendanceplus;

import javafx.fxml.FXML;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.time.LocalDate;

public class DashboardController{
    @FXML private HBox topBar;
    @FXML private HBox mainBody;
    @FXML private VBox mainPanel;
    @FXML private VBox sidePanel;

    @FXML private TopBarController topBarController;
    @FXML private MainPanelController mainPanelController;
    @FXML private SidePanelController sidePanelController;

    private LocalDate selectedDate;

    private static final double TOP_BAR_HEIGHT_RATIO = 0.15;
    private static final double MAIN_PANEL_WIDTH_RATIO = 0.60;

    @FXML
    public void initialize() {
        selectedDate = LocalDate.now();
        mainPanelController.setDashboardController(this);
        sidePanelController.setDashboardController(this);
        mainPanelController.updateCalendar(); // Safe to call here, after setting the reference
        sidePanelController.updateSubjectPanel();
        setupBindings();
    }

    private void setupBindings() {
        topBar.sceneProperty().addListener((obs, oldScene, newScene) -> {
            if (newScene != null) {
                topBar.prefHeightProperty().bind(newScene.heightProperty().multiply(TOP_BAR_HEIGHT_RATIO));
                mainBody.prefHeightProperty().bind(newScene.heightProperty().multiply(1 - TOP_BAR_HEIGHT_RATIO));
                mainPanel.prefWidthProperty().bind(mainBody.widthProperty().multiply(MAIN_PANEL_WIDTH_RATIO));
                sidePanel.prefWidthProperty().bind(mainBody.widthProperty().multiply(1 - MAIN_PANEL_WIDTH_RATIO));
            }
        });
    }

    public LocalDate getSelectedDate() {
        return selectedDate;
    }

    public void setSelectedDate(LocalDate date) {
        this.selectedDate = date;
        sidePanelController.updateSubjectPanel();
        mainPanelController.updateCalendar();
        System.out.println("Selected Date: " + selectedDate);
    }
}