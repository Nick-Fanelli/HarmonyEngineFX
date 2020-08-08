/*
 * Copyright © 2020 Harmony Engines.
 * All rights reserved.
 */

package com.harmony.engine.io.editor;

import com.harmony.engine.Harmony;
import com.harmony.engine.data.DataUtils;
import com.harmony.engine.data.GlobalData;
import com.harmony.engine.io.SelectionModel;
import com.harmony.engine.io.context.FileItemContext;
import com.harmony.engine.utils.Status;
import javafx.application.Platform;
import javafx.collections.ListChangeListener;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class CodeEditor implements Runnable {

    public static final ImageView FOLDER_ICON = new ImageView(
            new Image(CodeEditor.class.getResource("/images/icons/folder-icon.png").toExternalForm(), 18, 18, true, true)
    );

    public static final ImageView PYTHON_ICON = new ImageView(
            new Image(CodeEditor.class.getResource("/images/icons/file/python-icon.png").toExternalForm(), 18, 18, true, true)
    );

    public static final ImageView TEXT_ICON = new ImageView(
            new Image(CodeEditor.class.getResource("/images/icons/file/text-icon.png").toExternalForm(), 18, 18, true, true)
    );

    private static Thread codeEditorThread = null;
    public static Runnable syncRunnable;
    private static Runnable inputRunnable;

    public static TreeView<String> codeFileList;
    private static WebEngine webEngine;
    private static AnchorPane noFilePane;
    private static AnchorPane contentPane;
    private static TabPane codeTabBar;

    private static TreeItem<String> root = new TreeItem<>("Scripts");
    private static final HashMap<TreeItem<String>, File> scripts = new HashMap<>();
    private static final SelectionModel<File> selectedFiles = new SelectionModel<>();
    private static File openFile;

    public CodeEditor(TreeView<String> codeFileList, WebView codeView, AnchorPane noFilePane, AnchorPane contentPane, TabPane codeTabBar) {
        CodeEditor.codeFileList = codeFileList;
        CodeEditor.webEngine = codeView.getEngine();
        CodeEditor.noFilePane = noFilePane;
        CodeEditor.contentPane = contentPane;
        CodeEditor.codeTabBar = codeTabBar;

        if(codeEditorThread != null) return;

        codeEditorThread = new Thread(this, "Harmony:CodeEditor");
        codeEditorThread.start();
    }

    @Override
    public void run() {
        syncRunnable = CodeEditor::synchronizeFileList;
        inputRunnable = this::handleInput;

        initializeFileList();
        initializeTabPane();
        Platform.runLater(CodeEditor::initializeCodeView);
    }

    private void initializeFileList() {
        syncRunnable.run();
        inputRunnable.run();
    }

    private void initializeTabPane() {
        codeTabBar.setTabClosingPolicy(TabPane.TabClosingPolicy.ALL_TABS);
    }

    private static void synchronizeFileList() {
        File scriptsDirectory = Harmony.getScriptsLocation();

        if(!scriptsDirectory.exists() || !scriptsDirectory.isDirectory()) {
            System.err.println("Could not find a valid scripts directory!");
            return;
        }

        loadAllChildren(root, scriptsDirectory);

        root.setGraphic(CodeEditor.FOLDER_ICON);

        root.setExpanded(true);
        codeFileList.setRoot(root);
        codeFileList.setContextMenu(new FileItemContext());
    }

    private static void loadAllChildren(TreeItem<String> parent, File file) {
        if(!file.isDirectory()) return;

        File[] children = file.listFiles();
        if(children == null) return;

        for(File child : children) {
            if(child.getName().startsWith(".")) continue;
            TreeItem<String> childItem = new TreeItem<>(child.getName());
            parent.getChildren().add(childItem);
            scripts.put(childItem, child);
            if(child.isDirectory()) {
                childItem.setGraphic(CodeEditor.FOLDER_ICON);
                loadAllChildren(childItem, child);
            } else {
                try {
                    if (child.getName().split("\\.")[1].equals("py"))
                        childItem.setGraphic(CodeEditor.PYTHON_ICON);
                    else
                        childItem.setGraphic(CodeEditor.TEXT_ICON);
                } catch (Exception ignored) {}
            }
        }
    }

    private static void initializeCodeView() {
        webEngine.load(CodeEditor.class.getResource(
                GlobalData.getTheme() == GlobalData.Theme.LIGHT ? "/editor/lightEditor.html" : "/editor/darkEditor.html").toExternalForm());
    }

    private void handleInput() {
        codeFileList.getSelectionModel().selectedItemProperty().addListener((observableValue, stringTreeItem, t1) -> {
            selectScript(codeFileList.getSelectionModel().getSelectedItem());
        });

        codeTabBar.getTabs().addListener((ListChangeListener<Tab>) change -> {
            change.next();
            if(change.wasRemoved())
                for(Tab tab : change.getRemoved()) {
                    selectedFiles.removeFromSelection(tabFileHashMap.get(tab));
                    tabFileHashMap.remove(tab);
                }
        });

        codeTabBar.getSelectionModel().selectedItemProperty().addListener(observable -> {
            selectScript(tabFileHashMap.get(codeTabBar.getSelectionModel().getSelectedItem()));
        });

        selectedFiles.handler.addEventListener(() -> {
            if(selectedFiles.model.size() == 0) {
                contentPane.setVisible(false);
                noFilePane.setVisible(true);
            } else {
                if(!contentPane.isVisible()) {
                    contentPane.setVisible(true);
                    noFilePane.setVisible(false);
                }
            }
        });
    }

    private final static HashMap<Tab, File> tabFileHashMap = new HashMap<>();

    private static void addTab(File file) {
        Tab tab = new Tab(file.getName());
        tabFileHashMap.put(tab, file);
        codeTabBar.getTabs().add(tab);
        codeTabBar.getSelectionModel().select(tab);
    }

    private static void selectScript(TreeItem<String> key) {
        selectScript(scripts.get(key));
    }

    private static void selectScript(File file) {
        saveSelectedScript();
        if(file == null || file.isDirectory()) return;
        if(!selectedFiles.contains(file)) selectedFiles.addToSelection(file);
        openFile = file;
        loadScript(file, DataUtils.FileType.fromFile(file));
    }

    public static void saveSelectedScript() {
        if(openFile != null) {
            Document document = webEngine.getDocument();

            webEngine.executeScript("var code = ace.edit(\"editor\").getValue();" +
                    "document.getElementById(\"bufferObject\").innerHTML = code");

            NodeList nList = document.getElementsByTagName("buffer");

            if(nList.getLength() > 0) {
                Node node = nList.item(0);

                try {
                    BufferedWriter writer = new BufferedWriter(new FileWriter(openFile));
                    writer.write(node.getTextContent());
                    writer.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void loadScript(File file, DataUtils.FileType type) {
        String data = DataUtils.readFileForAce(file).replaceAll("\"", "\\\"");

        if(!tabFileHashMap.containsValue(file)) addTab(file);
        else {
            for(Map.Entry<Tab, File> entry : tabFileHashMap.entrySet()) {
                if(entry.getValue() == file) {
                    codeTabBar.getSelectionModel().select(entry.getKey());
                    break;
                }
            }
        }

        webEngine.executeScript(
                "var docSession = new ace.createEditSession(\"" +
                        data + "\", \"ace/mode/" + type.aceKey + "\");\n" +
                    "editor.setSession(docSession);"
        );
        Status.setUtilityText(file.getName());
    }
}
