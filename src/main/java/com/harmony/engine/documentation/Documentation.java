package com.harmony.engine.documentation;

import com.harmony.engine.EngineController;
import javafx.scene.control.Tab;

import java.awt.*;
import java.net.URI;

public class Documentation {

    public static final String documentationBranch = "version-1.0";

    public enum Location {
        PROJECT_TAB(String.format("https://github.com/HarmonyEngines/HarmonyDocumentation/blob/%s/ProjectTab.md#project-tab", documentationBranch)),
        TEXTURES_TAB(String.format("https://github.com/HarmonyEngines/HarmonyDocumentation/blob/%s/TexturesTab.md#textures-tab", documentationBranch)),
        GAME_OBJECTS_TAB(String.format("https://github.com/HarmonyEngines/HarmonyDocumentation/blob/%s/GameObjectsTab.md#game-objects-tab", documentationBranch));

        public String url;

        Location (String url) {
            this.url = url;
        }
    }

    public static void showDocumentation(EngineController engineController, Tab tab) {
        Location location = Documentation.getDocumentationLocation(engineController, tab);
        assert location != null;

        URI uri = URI.create(location.url);

        try {
            Desktop.getDesktop().browse(uri);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Location getDocumentationLocation(EngineController engineController, Tab tab) {
        if (tab == engineController.projectTab)
            return Location.PROJECT_TAB;
        else if (tab == engineController.texturesTab)
            return Location.TEXTURES_TAB;
        else if (tab == engineController.gameObjectsTab)
            return Location.GAME_OBJECTS_TAB;

        System.err.println("Documentation not handled for tab \"" + tab + "\"");
        return null;
    }
}
