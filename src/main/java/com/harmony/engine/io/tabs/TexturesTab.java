package com.harmony.engine.io.tabs;

import com.harmony.engine.EngineController;
import com.harmony.engine.data.ProjectData;
import com.harmony.engine.utils.textures.Texture;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;

public class TexturesTab {

    public static Runnable synchronize;

    private static GridPane texturesArray;
    private static ScrollPane scrollPane;

    public TexturesTab(GridPane texturesArray, ScrollPane scrollPane) {
        TexturesTab.texturesArray = texturesArray;
        TexturesTab.scrollPane = scrollPane;

        synchronize = this::synchronize;

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
            label.setStyle("-fx-font-size: 20px;");

            HBox hBox = new HBox();
            hBox.getStyleClass().add("item-array");
            hBox.setAlignment(Pos.CENTER_LEFT);
            hBox.setPadding(new Insets(0, 0, 0, 30));

            hBox.getChildren().add(imageView);
            hBox.getChildren().add(label);

            HBox.setMargin(label, new Insets(0, 0, 0, 100));

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

        synchronize();
    }

}
