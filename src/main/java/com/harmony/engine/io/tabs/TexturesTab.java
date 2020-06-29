package com.harmony.engine.io.tabs;

import com.harmony.engine.EngineController;
import com.harmony.engine.data.ProjectData;
import com.harmony.engine.utils.textures.Texture;
import com.harmony.engine.utils.textures.TextureUtils;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.text.TextAlignment;

public class TexturesTab {

    public static Runnable synchronize;
    private static Runnable input;

    private static GridPane texturesArray;
    private static ScrollPane scrollPane;
    private static Button newTexturesButton;

    public TexturesTab(GridPane texturesArray, ScrollPane scrollPane, Button newTexturesButton) {
        TexturesTab.texturesArray = texturesArray;
        TexturesTab.scrollPane = scrollPane;
        TexturesTab.newTexturesButton = newTexturesButton;

        synchronize = this::synchronize;
        input = this::handleInput;

        initialize();
    }

    private synchronized void synchronize() {
        int x = 0;
        int y = 0;

        for(Texture texture : ProjectData.textures) {
            Image image = EngineController.loadTexturesImage(texture.path, 75, 75);
            if(image == null) continue;

            ImageView imageView = new ImageView(image);
            Label label = new Label(texture.name);
            label.getStyleClass().add("default-label");
            label.setWrapText(true);
            HBox.setMargin(label, new Insets(0, 0, 0, 10));

            HBox hBox = new HBox();
            hBox.getStyleClass().add("item-array");
            hBox.setAlignment(Pos.CENTER_LEFT);
            hBox.setPadding(new Insets(0, 0, 0, 30));

            hBox.getChildren().add(imageView);
            hBox.getChildren().add(label);

            if(x > 2) {
                x = 0;
                y++;
            }

            texturesArray.add(hBox, x, y);

            x++;
        }
    }

    private void initialize() {
        texturesArray.setHgap(20);
        texturesArray.setVgap(20);
        texturesArray.setPadding(new Insets(10));

        input.run();

        synchronize();
    }

    private void handleInput() {
        newTexturesButton.setOnMouseClicked(mouseEvent -> TextureUtils.createTexture());
    }

}
