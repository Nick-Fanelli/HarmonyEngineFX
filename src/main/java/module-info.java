module com.harmony {

    // JavaFx Dependencies
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.swing;
    requires javafx.web;

    // Exported Source Packages
    exports com.harmony.engine;

    exports com.harmony.engine.io;
    exports com.harmony.engine.io.editor;
    exports com.harmony.engine.io.context;
    exports com.harmony.engine.io.tabs;
    exports com.harmony.engine.io.editor.state;

    exports com.harmony.engine.data;
    exports com.harmony.engine.math;
    exports com.harmony.engine.utils;
    exports com.harmony.engine.bridge;
    exports com.harmony.engine.documentation;
    exports com.harmony.engine.data.networking;
    exports com.harmony.engine.data.networking.resource;

    exports com.harmony.engine.utils.textures;
    exports com.harmony.engine.utils.gameObjects;

    opens com.harmony.engine;
}
