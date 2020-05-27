package com.harmony.engine.utils.textures;

import com.harmony.engine.EngineController;
import com.harmony.engine.Harmony;
import com.harmony.engine.data.ProjectData;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.FileChooser;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class TextureFXMLController {

    public Button locationButton;
    public TextField locationField;
    public TextField nameField;
    public ImageView textureImage;
    public Button chooseButton;
    public Button cancelButton;

    @FXML
    public void initialize() {
        locationButton.setOnMouseClicked(mouseEvent -> {
            FileChooser chooser = new FileChooser();
            chooser.setTitle("Choose Texture");
            chooser.setInitialDirectory(new File(Harmony.directory.getPath() + "/Resources"));
            File selectedFile = chooser.showOpenDialog(TextureUtils.staticStage);

            if(selectedFile != null) {
                locationField.setText(selectedFile.getPath());
                try {
                    textureImage.setImage(new Image(new BufferedInputStream(new FileInputStream(selectedFile))));
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
        });

        cancelButton.setOnMouseClicked(mouseEvent -> TextureUtils.staticStage.close());

        chooseButton.setOnMouseClicked(mouseEvent -> {
            if(!(locationField.getText().isEmpty() || nameField.getText().isEmpty())) {
                ProjectData.textures.add(new Texture(locationField.getText().trim().replaceAll(Harmony.directory.getPath(), ""), nameField.getText().trim(),
                        ProjectData.textures.size()));
                EngineController.synchronizeTextures();
                TextureUtils.staticStage.close();
            }
        });

    }
}
