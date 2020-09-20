package com.harmony.engine.managers;

import com.harmony.engine.context.HierarchyItemContext;
import com.harmony.engine.data.DataUtils;
import com.harmony.engine.data.ProjectData;
import com.harmony.engine.utils.Log;
import javafx.application.Platform;
import javafx.beans.InvalidationListener;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.scene.control.*;
import javafx.scene.effect.BoxBlur;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
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

    private final BoxBlur boxBlur = new BoxBlur(0, 0, 0);

    public FileManager(TreeView<String> fileManager, VBox newFilePane, Button newFileButton, TextField newFileField) {
        this.fileManager = fileManager;
        this.newFilePane = newFilePane;
        this.newFileButton = newFileButton;
        this.newFileField = newFileField;

        initialize();

        FileManager.synchronize = this::synchronizeFileManager;
        synchronize.run();
    }

    private void initialize() {
        fileManager.setEffect(boxBlur);
    }

    private void setBoxBlur(boolean value) {
        boxBlur.setWidth(value ? 5 : 0);
        boxBlur.setHeight(value ? 5 : 0);
        boxBlur.setIterations(value ? 5 : 0);
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

    private File getCurrentDirectory() {
        TreeItem<String> currentItem = fileManager.getSelectionModel().getSelectedItem();
        File currentFile = fileManagerData.get(currentItem);

        if(!currentFile.exists()) return null;

        if(currentFile.isDirectory()) return currentFile;
        else return currentFile.getParentFile();
    }

    private File getCurrentFile() {
        TreeItem<String> currentItem = fileManager.getSelectionModel().getSelectedItem();
        File currentFile = fileManagerData.get(currentItem);

        return currentFile.exists() ? currentFile : null;
    }

    public void create(DataUtils.FileType fileType) {
        newFileField.setText("");
        newFileField.setPromptText(fileType.fileName + " Name");
        this.setBoxBlur(true);
        this.handleNewFile(fileType);
        this.shouldHandleNewFile = true;

        newFilePane.setVisible(true);
    }

    public void delete() {
        File currentFile = getCurrentFile();
        // TODO: Delete This!
    }

    private boolean shouldHandleNewFile = false;

    private void handleNewFile(DataUtils.FileType fileType) {
        Platform.runLater(newFileField::requestFocus);

        newFileField.textProperty().addListener(observable -> {
            if(!shouldHandleNewFile) return;

            switch (fileType) {
                case BLANK: break;
                case DIRECTORY:
                    newFileButton.setDisable(newFileField.getText().contains("\\") || newFileField.getText().contains(".")
                    || newFileField.getText().contains("/") || newFileField.getText().isEmpty());
                    break;
            }
        });

        newFileButton.setOnAction(actionEvent -> {
            if(!shouldHandleNewFile) return;
            createFileType(fileType);
            shouldHandleNewFile = false;
            this.setBoxBlur(false);
            newFilePane.setVisible(false);
        });
    }

    private void createFileType(DataUtils.FileType fileType) {
        File parentDirectory = getCurrentDirectory();
        if(parentDirectory == null) return;

        File childFile = new File(parentDirectory.getPath() + File.separator + newFileField.getText().trim());

        switch (fileType) {
            case BLANK: break;
            case DIRECTORY:
                boolean isSuccess = childFile.mkdirs();
                if(!isSuccess) Log.error("Could not create childFile as directory at " + childFile.getPath());
                break;

        }

        FileManager.synchronize.run();
    }
}
