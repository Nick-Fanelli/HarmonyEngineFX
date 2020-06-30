package com.harmony.engine.io.tabs;

import com.harmony.engine.EngineController;
import com.harmony.engine.Harmony;
import com.harmony.engine.data.GlobalData;
import com.harmony.engine.data.ProjectData;
import com.harmony.engine.utils.textures.Texture;
import com.harmony.engine.utils.textures.TextureUtils;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.FileChooser;

import java.io.File;
import java.util.Optional;

public class TexturesTab {

    public static final int IMAGE_SIZE = 75;

    public static Runnable synchronize;
    private static Runnable input;

    private static GridPane texturesArray;
    private static AnchorPane interactables;
    private static Button newTexturesButton;

    private static HBox hBoxSelected = null;
    private static Texture selectedTexture = null;

    private static TextField name;
    private static TextField location;
    private static Button locationButton;
    private static Button deleteButton;

    public TexturesTab(GridPane texturesArray, Button newTexturesButton, AnchorPane interactables, TextField name,
                       TextField location, Button locationButton, Button deleteButton) {
        TexturesTab.texturesArray = texturesArray;
        TexturesTab.newTexturesButton = newTexturesButton;
        TexturesTab.interactables = interactables;
        TexturesTab.name = name;
        TexturesTab.location = location;
        TexturesTab.locationButton = locationButton;
        TexturesTab.deleteButton = deleteButton;

        synchronize = this::synchronize;
        input = this::handleInput;

        initialize();
    }

    private synchronized void synchronize() {
        int x = 0;
        int y = 0;

        texturesArray.getChildren().clear();

        for(Texture texture : ProjectData.textures) {
            Image image = EngineController.loadTexturesImage(texture.path, IMAGE_SIZE, IMAGE_SIZE);
            if(image == null) continue;

            ImageView imageView = new ImageView(image);
            Label label = new Label(texture.name);
            label.getStyleClass().add("default-label");
            label.setWrapText(true);
            HBox.setMargin(label, new Insets(0, 0, 0, 10));

            HBox hBox = new HBox();
            hBox.getStyleClass().add("item-array");
            hBox.setAlignment(Pos.CENTER_LEFT);
            hBox.setPadding(new Insets(0, 0, 0, 20));

            hBox.getChildren().add(imageView);
            hBox.getChildren().add(label);

            hBox.setOnMouseClicked(mouseEvent -> select(hBox, texture));

            if(x > 2) {
                x = 0;
                y++;
            }

            texturesArray.add(hBox, x, y);

            x++;
        }
    }

    private void initialize() {
        interactables.setVisible(false);

        texturesArray.setHgap(20);
        texturesArray.setVgap(20);
        texturesArray.setPadding(new Insets(10));

        input.run();

        synchronize();
    }

    private void handleInput() {
        newTexturesButton.setOnMouseClicked(mouseEvent -> TextureUtils.createTexture());

        name.textProperty().addListener(inputMethodEvent -> {
            if(!name.getText().isEmpty()) {
                selectedTexture.name = name.getText();
                ((Label) (hBoxSelected.getChildren().get(1))).setText(name.getText());
            }
        });

        location.textProperty().addListener(inputMethodEvent -> {
            if(location.getText().isEmpty()) return;

            Image image = EngineController.loadTexturesImage(location.getText(), IMAGE_SIZE, IMAGE_SIZE);

            if(image != null) {
                location.setStyle("-fx-text-fill: -fx-default-text-fill;");
                selectedTexture.path = location.getText();
                ((ImageView) hBoxSelected.getChildren().get(0)).setImage(image);
            } else {
                location.setStyle("-fx-text-fill: red;");
            }
        });

        locationButton.setOnMouseClicked(mouseEvent -> {
            if(selectedTexture == null) return;

            FileChooser chooser = new FileChooser();
            chooser.setInitialDirectory(new File(Harmony.directory.getPath() + File.separator + "Resources" + File.separator + "Textures"));
            chooser.setTitle("Choose Texture");
            File selectedFile = chooser.showOpenDialog(Harmony.staticStage);

            if (selectedFile == null) return;

            location.setText(Harmony.getResourceString(selectedFile.getPath()));
        });

        deleteButton.setOnMouseClicked(mouseEvent -> {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirm Deletion");
            alert.setHeaderText("Delete Texture");
            alert.setContentText("Deleting this texture is final and can not be reversed easily.");

            DialogPane dialogPane = alert.getDialogPane();
            dialogPane.getStylesheets().add(getClass().getResource("/css/harmony.css").toExternalForm());
            dialogPane.getStylesheets().add(Harmony.class.getResource(GlobalData.getThemeCSSLocation()).toExternalForm());

            Optional<ButtonType> result = alert.showAndWait();
            if(result.isPresent() && result.get() == ButtonType.OK) {
                ProjectData.textures.remove(selectedTexture);
                selectedTexture = null;
                hBoxSelected = null;
                synchronize.run();
            }

        });

    }

    private void select(HBox hBox, Texture texture) {
        if(hBoxSelected != null) hBoxSelected.getStyleClass().remove(1);
        (hBoxSelected = hBox).getStyleClass().add(1, "selected");
        selectedTexture = texture;

        updateInteractables();
    }

    private void updateInteractables() {
        if(selectedTexture == null) {
            interactables.setVisible(false);
            return;
        }

        interactables.setVisible(true);

        name.setText(selectedTexture.name);
        location.setText(selectedTexture.path);
    }

}
