module com.harmony {

    // JavaFx Dependencies
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.swing;
    requires javafx.web;

    // Exported Source Packages
    exports com.harmony.engine;
    exports com.harmony.engine.controllers;
    exports com.harmony.engine.data;
    exports com.harmony.engine.utils;

    opens com.harmony.engine;
}
