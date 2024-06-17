module com.bayle {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;
    requires javafx.base;

    opens com.bayle to javafx.fxml;
    exports com.bayle;
}
