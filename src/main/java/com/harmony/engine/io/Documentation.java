/*
 * Copyright © 2020 Harmony Engines.
 * All rights reserved.
 */

package com.harmony.engine.io;

import com.harmony.engine.EngineController;
import com.harmony.engine.Launcher;
import javafx.scene.control.Tab;

import java.awt.*;
import java.net.URI;

public class Documentation {

    public static final String GITHUB_LOCATION = "https://github.com/HarmonyEngines/HarmonyDocumentation";

    public enum Location {
        PROJECT_TAB(Location.getLocation("ProjectTab.md#project-tab")),
        TEXTURES_TAB(Location.getLocation("TexturesTab.md#textures-tab")),
        GAME_OBJECTS_TAB(Location.getLocation("GameObjectsTab.md#game-objects-tab")),
        EDITOR_TAB(Location.getLocation("EditorTab.md#editor-tab")),
        GLOBAL_PREFERENCES(Location.getLocation("GlobalPreferences.md#global-preferences"));

        public String url;

        Location (String url) {
            this.url = url;
        }

        public static String getLocation(String subLocation) {
            return String.format("%s/blob/%s/%s", GITHUB_LOCATION, Launcher.GITHUB_VERSION_STRING, subLocation);
        }
    }

    public static void showDocumentation(Location location) {
        URI uri = URI.create(location.url);

        try {
            Desktop.getDesktop().browse(uri);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void showDocumentation(EngineController engineController, Tab tab) {
        Location location = Documentation.getDocumentationLocation(engineController, tab);
        if(location == null) return;
        showDocumentation(location);
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
