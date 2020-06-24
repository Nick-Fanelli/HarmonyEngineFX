package com.harmony.engine.io.editor;

import javafx.application.Platform;
import javafx.scene.control.TreeView;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

public class CodeEditor implements Runnable {

    private static Thread codeEditorThread = null;

    private static TreeView<String> codeFileList;
    private static WebView codeView;

    public CodeEditor(TreeView<String> codeFileList, WebView codeView) {
        CodeEditor.codeFileList = codeFileList;
        CodeEditor.codeView = codeView;

        if(codeEditorThread != null) return;

        codeEditorThread = new Thread(this, "Harmony:CodeEditor");
        codeEditorThread.start();
    }

    @Override
    public void run() {
        initializeFileList();
        Platform.runLater(this::initializeCodeView);
    }

    private void initializeFileList() { }

    private void initializeCodeView() {
        WebEngine webEngine = codeView.getEngine();
        webEngine.load(CodeEditor.class.getResource("/editor/editor.html").toExternalForm());
    }
}
