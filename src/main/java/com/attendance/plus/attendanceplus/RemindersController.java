package com.attendance.plus.attendanceplus;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

public class RemindersController {
    @FXML
    public void sayHello(ActionEvent e){
        Node button = (Node) e.getSource();
        Pane vbox = (Pane) button.getParent().getParent();

        vbox.getChildren().removeAll(vbox.getChildren());
    }
}
