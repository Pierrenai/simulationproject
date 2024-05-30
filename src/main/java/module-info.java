module com.bayle {
    requires javafx.controls;
    requires javafx.fxml;

    opens com.bayle to javafx.fxml;
    opens com.bayle.controller to javafx.fxml;
    exports com.bayle;
}
