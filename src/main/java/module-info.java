module com.attendance.plus.attendanceplus {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.attendance.plus.attendanceplus to javafx.fxml;
    exports com.attendance.plus.attendanceplus;
}