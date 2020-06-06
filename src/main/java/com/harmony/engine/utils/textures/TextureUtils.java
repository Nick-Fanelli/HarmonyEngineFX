package com.harmony.engine.utils.textures;

import com.harmony.engine.Harmony;
import com.harmony.engine.data.GlobalData;
import com.harmony.engine.utils.Status;
import com.harmony.engine.utils.gameObjects.GameObject;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;

public class TextureUtils {

    public static Stage staticStage;
    public static GameObject staticGameObject;

    public static void createTexture() {
        TextureUtils.createStage("/utils/texture/createTexture.fxml");
    }

    public static void chooseTextureForGameObject(GameObject gameObject) {
        TextureUtils.staticGameObject = gameObject;
        TextureUtils.createStage("/utils/texture/selectTexture.fxml");
    }

    private static void createStage(String fxmlLocation) {
        if(staticStage != null && staticStage.isShowing()) {
            staticStage.close();
            Status.setCurrentStatus(Status.Type.READY);
        }

        Status.setCurrentStatus(Status.Type.STAND_BY);

        try {
            FXMLLoader loader = new FXMLLoader(TextureUtils.class.getResource(fxmlLocation));
            Stage stage = new Stage();
            TextureUtils.staticStage = stage;
            Parent root = loader.load();

            Scene scene = new Scene(root);

            // Handle Theme
            scene.getStylesheets().add(Harmony.class.getResource(GlobalData.getThemeCSSLocation()).toExternalForm());

            stage.setResizable(false);
            stage.initStyle(StageStyle.UNDECORATED);
            stage.setAlwaysOnTop(true);
            stage.setScene(scene);
            stage.show();
        } catch(IOException e) {
            e.printStackTrace();
        }
    }

    public static void close() {
        Status.setCurrentStatus(Status.Type.READY);
        staticStage.close();
    }

}
