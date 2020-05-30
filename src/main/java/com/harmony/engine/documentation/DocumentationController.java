package com.harmony.engine.documentation;

import javafx.fxml.FXML;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;


public class DocumentationController {

    public WebView htmlViewer;

    @FXML
    public void initialize() {
        WebEngine webEngine = htmlViewer.getEngine();

        webEngine.load(Documentation.location.url);
    }

}
