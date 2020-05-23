module com.harmony {

    // For Intellij Kotlin Dependencies
    requires kotlin.stdlib;

    // JavaFx Dependencies
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.swing;

    opens com.harmony.engine to javafx.fxml;

    exports com.harmony.engine;
    exports com.harmony.engine.data;
}