package com.harmony.engine.documentation;

import java.awt.*;
import java.net.URI;

public class Documentation {

    public static final String documentationBranch = "version-1.0";

    public enum Location {
        PROJECT_TAB(String.format("https://github.com/HarmonyEngines/HarmonyDocumentation/blob/%s/projectTab.md#project-tab", documentationBranch), "Project Tab"),
        TEXTURES_TAB(String.format("https://github.com/HarmonyEngines/HarmonyDocumentation/blob/%s/texturesTab.md#textures-tab", documentationBranch), "Textures Tab");

        public String url;
        public String title;

        Location (String url, String title) {
            this.url = url;
            this.title = title;
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

}
