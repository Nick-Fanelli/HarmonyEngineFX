package com.harmony.engine.documentation;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Documentation {

    public static Stage staticStage;
    public static Location location;

    public enum Location {
        PROJECT_TAB("https://github.com/HarmonyEngines/HarmonyDocumentation/blob/master/projectTab.md#project-tab", "Project Tab");

        public String url;
        public String titleName;

        Location (String url, String titleName) {
            this.url = url;
            this.titleName = titleName;
        }
    }

    public static void showDocumentation(Location location) {
        if(staticStage != null) staticStage.close();

        Documentation.location = location;

        try {
            FXMLLoader loader = new FXMLLoader(Documentation.class.getResource("/utils/documentation.fxml"));
            Stage stage = new Stage();
            Documentation.staticStage = stage;
            Parent root = loader.load();
            stage.setResizable(true);
            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
