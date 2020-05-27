module com.harmony {

    // For Intellij
    requires kotlin.stdlib;

    // JavaFx Dependencies
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.swing;

    opens com.harmony.engine to javafx.fxml;

    exports com.harmony.engine;
    exports com.harmony.engine.data;
    exports com.harmony.engine.utils.textures;
    exports com.harmony.engine.utils.gameObjects;
}