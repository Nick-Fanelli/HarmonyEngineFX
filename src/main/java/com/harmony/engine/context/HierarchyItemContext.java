package com.harmony.engine.context;

import com.harmony.engine.data.DataUtils;
import com.harmony.engine.managers.FileManager;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;

public class HierarchyItemContext extends ContextMenu {

    private final FileManager fileManager;

    private final Menu newItems;
    private final MenuItem newDirectory;
    private final MenuItem newJSFile;
    private final MenuItem newFile;
    private final MenuItem newTextFile;

    private final MenuItem delete;

    private final MenuItem synchronize;

    public HierarchyItemContext(FileManager fileManager) {
        this.fileManager = fileManager;

        // Create Objects
        newItems = new Menu("New");

        newDirectory = new MenuItem("Directory");
        newDirectory.setGraphic(new ImageView(FileManager.FOLDER_IMAGE));

        newJSFile = new MenuItem("Javascript File");
        newJSFile.setGraphic(new ImageView(FileManager.JS_FILE));

        newFile = new MenuItem("File");
        newFile.setGraphic(new ImageView(FileManager.BLANK_FILE));

        newTextFile = new MenuItem("Text File");
        newTextFile.setGraphic(new ImageView(FileManager.TXT_FILE));

        newItems.getItems().addAll(newDirectory, new SeparatorMenuItem(), newJSFile, newFile, newTextFile);

        delete = new MenuItem("Delete");
        delete.setAccelerator(new KeyCodeCombination(KeyCode.BACK_SPACE, DataUtils.OperatingSystem.getCurrentOS().controlModifier));

        synchronize = new MenuItem("Synchronize");

        // Handle Accelerators
        // Add Items
        super.getItems().addAll(newItems, new SeparatorMenuItem(), synchronize, new SeparatorMenuItem(), delete);

        handleInput();
    }

    public void handleInput() {
        synchronize.setOnAction(actionEvent -> FileManager.synchronize.run());

        newDirectory.setOnAction(actionEvent -> fileManager.create(DataUtils.FileType.DIRECTORY));

        delete.setOnAction(actionEvent -> fileManager.delete());
    }

}
