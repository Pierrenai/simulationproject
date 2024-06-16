module com.bayle {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;
    requires javafx.base;

    opens com.bayle to javafx.fxml;
    opens com.bayle.controller to javafx.fxml;
    exports com.bayle;
}
