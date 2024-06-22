module com.bayle {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;
    requires javafx.base;

    opens com.bayle to javafx.fxml;

    exports com.bayle;
    exports com.bayle.affichage;
    exports com.bayle.service;
    exports com.bayle.model;
    exports com.bayle.util;
}
