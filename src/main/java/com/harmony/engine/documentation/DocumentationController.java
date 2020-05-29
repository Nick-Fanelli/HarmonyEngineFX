package com.harmony.engine.documentation;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import org.commonmark.node.Node;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.HtmlRenderer;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URISyntaxException;

import static javafx.concurrent.Worker.State;


public class DocumentationController {

    public static final String markdownCSS;

    static {
        StringBuilder builder = new StringBuilder();

        try {
            BufferedReader reader = new BufferedReader(new FileReader(
                    new File(DocumentationController.class.getResource("/documentation/markdown/markdown.css").toURI())
            ));

            String line;

            while((line = reader.readLine()) != null) {
                builder.append(line).append("\n");
            }
        } catch(IOException | URISyntaxException e) {
            e.printStackTrace();
        }

        markdownCSS = builder.toString();
    }

    public WebView htmlViewer;

    @FXML
    public void initialize() {
        WebEngine webEngine = htmlViewer.getEngine();

        webEngine.getLoadWorker().stateProperty().addListener((ov, oldState, newState) -> {
            if (newState == State.SUCCEEDED) {
                Documentation.staticStage.setTitle(webEngine.getTitle());
            }
        });

        StringBuilder fileContents = new StringBuilder();

        try {
            BufferedReader reader = new BufferedReader(new FileReader(new File(
                    DocumentationController.class.getResource(Documentation.MARKDOWN_DIRECTORY +
                            Documentation.location.markdownLocation).toURI()
            )));

            String line;

            while((line = reader.readLine()) != null) {
                fileContents.append(line).append("\n");
            }
        } catch(IOException | URISyntaxException e) {
            e.printStackTrace();
        }

        Parser parser = Parser.builder().build();
        Node document = parser.parse(fileContents.toString());

        HtmlRenderer renderer = HtmlRenderer.builder().build();
        String html = renderer.render(document);
        html = "<html><head><title>" + Documentation.location.titleName + "</title><style>" + markdownCSS +
                "</style></head><body>" + html + "</body></html>";


        webEngine.loadContent(html);
    }

}
