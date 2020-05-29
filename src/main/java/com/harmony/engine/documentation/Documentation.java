package com.harmony.engine.documentation;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class Documentation {

    public static final String MARKDOWN_DIRECTORY = "/documentation/markdown/";

    public static Stage staticStage;
    public static Location location;

    public enum Location {
        PROJECT_TAB("projectTab.md", "Project Tab");

        public String markdownLocation;
        public String titleName;

        Location (String markdownLocation, String titleName) {
            this.markdownLocation = markdownLocation;
            this.titleName = titleName;
        }
    }

    public static void showDocumentation(Location location) {
        if(staticStage != null) staticStage.close();

        Documentation.location = location;

        try {
            FXMLLoader loader = new FXMLLoader(Documentation.class.getResource("/documentation/documentation.fxml"));
            Stage stage = new Stage();
            Documentation.staticStage = stage;
            Parent root = loader.load();
            stage.setTitle("Harmony Documentation");
            stage.setResizable(false);
            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
