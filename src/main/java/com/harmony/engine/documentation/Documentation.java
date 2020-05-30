package com.harmony.engine.documentation;

import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.web.WebView;
import javafx.stage.Stage;

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
        Stage stage = new Stage();
        stage.setTitle("Harmony Documentation - " + location.title);
        stage.setResizable(true);

        stage.setMinWidth(600);
        stage.setMinHeight(600);

        WebView webView = new WebView();

        webView.getEngine().load(location.url);

        AnchorPane anchorPane = new AnchorPane(webView);

        AnchorPane.setTopAnchor(webView, 0.0);
        AnchorPane.setBottomAnchor(webView, 0.0);
        AnchorPane.setLeftAnchor(webView, 0.0);
        AnchorPane.setRightAnchor(webView, 0.0);

        Scene scene = new Scene(anchorPane, 800, 640);

        stage.setScene(scene);
        stage.show();
    }

}
