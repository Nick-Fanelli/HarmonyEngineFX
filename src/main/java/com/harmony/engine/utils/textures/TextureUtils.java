package com.harmony.engine.utils.textures;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class TextureUtils {

    public static Stage staticStage;

    public static void createTexture() {
        try {
            FXMLLoader loader = new FXMLLoader(TextureUtils.class.getResource("/utils/texture.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            TextureUtils.staticStage = stage;
            stage.setTitle("Create Texture");
            stage.setResizable(false);
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
