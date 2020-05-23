module com.harmony {

    // JavaFx Dependencies
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.swing;

    opens com.harmony.engine to javafx.fxml;

    exports com.harmony.engine;
    exports com.harmony.engine.data;
}