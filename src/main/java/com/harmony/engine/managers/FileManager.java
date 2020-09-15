package com.harmony.engine.managers;

import com.harmony.engine.context.HierarchyItemContext;
import com.harmony.engine.data.DataUtils;
import com.harmony.engine.data.ProjectData;
import javafx.scene.control.*;
import javafx.scene.effect.BoxBlur;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

public class FileManager {

    public static final Image FOLDER_IMAGE = new Image(FileManager.class.getResourceAsStream("/images/icons/file-types/folder.png"), 15, 15, true, true);
    public static final Image BLANK_FILE = new Image(FileManager.class.getResourceAsStream("/images/icons/file-types/blank-file.png"), 15, 15, true, true);
    public static final Image JS_FILE = new Image(FileManager.class.getResourceAsStream("/images/icons/file-types/js-file.png"), 15, 15, true, true);
    public static final Image TXT_FILE = new Image(FileManager.class.getResourceAsStream("/images/icons/file-types/txt-file.png"), 15, 15, true, true);

    public static Runnable synchronize;

    private final HashMap<TreeItem<String>, File> fileManagerData = new HashMap<>();
    private final TreeView<String> fileManager;
    private TreeItem<String> root;

    private final VBox newFilePane;
    private final Button newFileButton;
    private final TextField newFileField;
    private final AnchorPane blurPane;

    public FileManager(TreeView<String> fileManager, VBox newFilePane, Button newFileButton, TextField newFileField, AnchorPane blurPane) {
        this.fileManager = fileManager;
        this.newFilePane = newFilePane;
        this.newFileButton = newFileButton;
        this.newFileField = newFileField;
        this.blurPane = blurPane;

        init();

        FileManager.synchronize = this::synchronizeFileManager;
        synchronize.run();
    }

    private void init() {
        blurPane.setEffect(new BoxBlur(10, 10, 3));
        blurPane.setVisible(true);
    }

    private void synchronizeFileManager() {
        this.root = new TreeItem<>("Resources");
        fileManager.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        fileManager.setEditable(true);
        fileManager.setFocusTraversable(false);
        fileManager.setRoot(root);
        root.setExpanded(true);

        fileManager.setContextMenu(new HierarchyItemContext(this));

        loadDirectory(ProjectData.getResourceDirectory(), root);
    }

    private synchronized void loadDirectory(File directory, TreeItem<String> parent) {
        File[] children = directory.listFiles();
        if(children == null) return;
        ArrayList<TreeItem<String>> directories = new ArrayList<>();

        for(File child : children) {
            if(child.isHidden()) continue;
            TreeItem<String> item = new TreeItem<>(child.getName());
            parent.getChildren().add(item);
            fileManagerData.put(item, child);
            if(child.isDirectory()) directories.add(item);
            else {
                DataUtils.FileType type = DataUtils.FileType.identifyFile(child);
                if(type != null) item.setGraphic(new ImageView(getImageFromType(type)));
            }
        }

        for(TreeItem<String> child : directories) {
            child.setGraphic(new ImageView(FOLDER_IMAGE));
            loadDirectory(fileManagerData.get(child), child);
        }

    }

    private Image getImageFromType(DataUtils.FileType fileType) {
        switch (fileType) {
            case TXT: return TXT_FILE;
            case JS:  return JS_FILE;
        }

        return BLANK_FILE;
    }

    public void create(DataUtils.FileType fileType) {
        newFileField.setText("");
        newFileField.setPromptText(fileType.fileName + " Name");
        newFilePane.setVisible(true);
        blurPane.setVisible(true);
    }

}
