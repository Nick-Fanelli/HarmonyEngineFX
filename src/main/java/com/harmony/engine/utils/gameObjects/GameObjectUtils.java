package com.harmony.engine.utils.gameObjects;

import com.harmony.engine.utils.Status;
import com.harmony.engine.utils.textures.TextureUtils;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;

public class GameObjectUtils {

    public static Stage staticStage;

    public static void createGameObject() {
        try {
            Status.setCurrentStatus(Status.Type.LOADING);
            FXMLLoader loader = new FXMLLoader(TextureUtils.class.getResource("/utils/gameObject.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            GameObjectUtils.staticStage = stage;
            stage.setTitle("Create Game Object");
            stage.setResizable(false);
            stage.initStyle(StageStyle.UNDECORATED);
            stage.setAlwaysOnTop(true);
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
