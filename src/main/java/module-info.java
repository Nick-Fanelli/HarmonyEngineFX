module com.harmony {

    // JavaFx Dependencies
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.swing;
    requires javafx.web;

    opens com.harmony.engine to javafx.web;

    exports com.harmony.engine;
    exports com.harmony.engine.data;
    exports com.harmony.engine.utils;
    exports com.harmony.engine.documentation;
    exports com.harmony.engine.utils.textures;
    exports com.harmony.engine.utils.gameObjects;
}