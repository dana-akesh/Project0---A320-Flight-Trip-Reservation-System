module com.example.project0 {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.project0 to javafx.fxml;
    exports com.example.project0;
}