package com.harmony.engine.utils.textures;

import com.harmony.engine.utils.Status;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;

public class TextureUtils {

    public static Stage staticStage;

    public static void createTexture() {
        try {
            Status.setCurrentStatus(Status.Type.LOADING);
            FXMLLoader loader = new FXMLLoader(TextureUtils.class.getResource("/utils/texture.fxml"));
            Stage stage = new Stage();
            TextureUtils.staticStage = stage;
            Parent root = loader.load();
            stage.setTitle("Create Texture");
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
