/*
 * Copyright © 2020 Harmony Engines.
 * All rights reserved.
 */

package com.harmony.engine.utils.textures;

import com.harmony.core.io.Texture;
import com.harmony.engine.Harmony;
import com.harmony.engine.data.ProjectData;
import com.harmony.engine.io.tabs.TexturesTab;
import com.harmony.engine.utils.Status;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
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
            chooser.setInitialDirectory(new File(Harmony.directory.getPath() + File.separator + "Resources" + File.separator + "Textures"));
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

        cancelButton.setOnMouseClicked(mouseEvent -> {
            Status.setCurrentStatus(Status.Type.READY);
            TextureUtils.staticStage.close();
        });

        chooseButton.setOnMouseClicked(mouseEvent -> {
            if(!(locationField.getText().isEmpty() || nameField.getText().isEmpty())) {
                ProjectData.textures.add(new Texture(Harmony.getResourceString(locationField.getText()), nameField.getText().trim(),
                        ProjectData.textures.size()));
                TexturesTab.synchronize.run();
                Status.setCurrentStatus(Status.Type.READY);
                TextureUtils.staticStage.close();
            }
        });

    }
}
