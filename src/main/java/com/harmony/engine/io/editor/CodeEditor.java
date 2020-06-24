package com.harmony.engine.io.editor;

import com.harmony.engine.EngineController;
import com.harmony.engine.Harmony;
import javafx.application.Platform;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

import java.io.File;

public class CodeEditor implements Runnable {

    public static final ImageView FOLDER_ICON = new ImageView(
            new Image(CodeEditor.class.getResource("/images/icons/folder-icon.png").toExternalForm(), 18, 18, true, true)
    );

    public static final ImageView PYTHON_ICON = new ImageView(
            new Image(CodeEditor.class.getResource("/images/icons/python-icon.png").toExternalForm(), 18, 18, true, true)
    );

    private static Thread codeEditorThread = null;
    private static Runnable syncRunnable;

    private static TreeView<String> codeFileList;
    private static WebView codeView;

    private static TreeItem<String> root = new TreeItem<>("Scripts");
    private static File[] scripts;

    public CodeEditor(TreeView<String> codeFileList, WebView codeView) {
        CodeEditor.codeFileList = codeFileList;
        CodeEditor.codeView = codeView;

        if(codeEditorThread != null) return;

        codeEditorThread = new Thread(this, "Harmony:CodeEditor");
        codeEditorThread.start();
    }

    @Override
    public void run() {
        syncRunnable = CodeEditor::synchronizeFileList;

        initializeFileList();
        Platform.runLater(this::initializeCodeView);
    }

    private void initializeFileList() {
        syncRunnable.run();
    }

    public static void synchronizeFileList() {
        File scriptsDirectory = Harmony.getScriptsLocation();

        if(!scriptsDirectory.exists() || !scriptsDirectory.isDirectory()) {
            // TODO: Throw an error
            System.err.println("Could not find a valid scripts directory!");
            return;
        }

        loadAllChildren(root, scriptsDirectory);


        root.setGraphic(CodeEditor.FOLDER_ICON);

        root.setExpanded(true);
        codeFileList.setRoot(root);
        codeFileList.setEditable(true);
    }

    private static void loadAllChildren(TreeItem<String> parent, File file) {
        if(!file.isDirectory()) return;

        File[] children = file.listFiles();
        if(children == null) return;

        for(File child : children) {
            if(child.getName().startsWith(".")) continue;
            TreeItem<String> childItem = new TreeItem<>(child.getName());
            parent.getChildren().add(childItem);
            if(child.isDirectory()) {
                childItem.setGraphic(CodeEditor.FOLDER_ICON);
                loadAllChildren(childItem, child);
            } else {
                try {
                    if (child.getName().split("\\.")[1].equals("py"))
                        childItem.setGraphic(CodeEditor.PYTHON_ICON);
                } catch (Exception ignored) {}
            }
        }
    }

    private void initializeCodeView() {
        WebEngine webEngine = codeView.getEngine();
        webEngine.load(CodeEditor.class.getResource("/editor/editor.html").toExternalForm());
    }
}
