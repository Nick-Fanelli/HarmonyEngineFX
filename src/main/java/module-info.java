module com.harmony {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.swing;

    opens com.harmony.engine to javafx.fxml;
    exports com.harmony.engine;
}