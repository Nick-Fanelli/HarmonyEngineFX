package com.harmony.engine.documentation;

import com.harmony.engine.EngineController;
import com.harmony.engine.Launcher;
import javafx.scene.control.Tab;

import java.awt.*;
import java.net.URI;

public class Documentation {

    public static final String GITHUB_LOCATION = "https://github.com/HarmonyEngines/HarmonyDocumentation";

    public enum Location {
        PROJECT_TAB(String.format("%s/blob/%s/ProjectTab.md#project-tab", GITHUB_LOCATION, Launcher.GITHUB_VERSION_STRING)),
        TEXTURES_TAB(String.format("%s/blob/%s/TexturesTab.md#textures-tab", GITHUB_LOCATION, Launcher.GITHUB_VERSION_STRING)),
        GAME_OBJECTS_TAB(String.format("%s/blob/%s/GameObjectsTab.md#game-objects-tab", GITHUB_LOCATION, Launcher.GITHUB_VERSION_STRING)),
        EDITOR_TAB(String.format("%s/blob/%s/EditorTab.md#editor-tab", GITHUB_LOCATION, Launcher.GITHUB_VERSION_STRING));

        public String url;

        Location (String url) {
            this.url = url;
        }
    }

    public static void showDocumentation(EngineController engineController, Tab tab) {
        Location location = Documentation.getDocumentationLocation(engineController, tab);
        if(location == null) return;

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
        else if(tab == engineController.editorTab)
        	return Location.EDITOR_TAB;

        System.err.println("Documentation not handled for tab \"" + tab.getText() + "\"");
        return null;
    }
}
