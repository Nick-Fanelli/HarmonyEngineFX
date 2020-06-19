module com.harmony {

    // JavaFx Dependencies
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.swing;

    // Open to JavaFX
    opens com.harmony.engine to javafx.fxml;

    // Exported Source Packages
    exports com.harmony.engine;

    exports com.harmony.engine.io;
    exports com.harmony.engine.io.hierarchy;

    exports com.harmony.engine.data;
    exports com.harmony.engine.math;
    exports com.harmony.engine.utils;
    exports com.harmony.engine.documentation;

    exports com.harmony.engine.utils.textures;
    exports com.harmony.engine.utils.gameObjects;
}