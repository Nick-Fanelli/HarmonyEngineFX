/*
 * Copyright © 2020 Harmony Engines.
 * All rights reserved.
 */

package com.harmony.engine.io;

import com.harmony.engine.Harmony;
import com.harmony.engine.Launcher;
import com.harmony.engine.data.DataUtils;
import com.harmony.engine.data.GlobalData;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class MenuManager {

    public static final KeyCombination.Modifier controlModifier = DataUtils.OperatingSystem.getCurrentOS() == DataUtils.OperatingSystem.MACINTOSH
            ? KeyCombination.META_DOWN : KeyCombination.CONTROL_DOWN;

    private final MenuBar menuBar;
    private final AnchorPane contentPane;

    public MenuManager(MenuBar menuBar, AnchorPane contentPane) {
        this.menuBar = menuBar;
        this.contentPane = contentPane;
    }

    public void initialize() {
        this.setMenuBarPosition();
        this.setMenuBarChildren();

        Runnable handleInput = this::handleMenuBarChildren;
        handleInput.run();
    }

    private void setMenuBarPosition() {
        if(DataUtils.OperatingSystem.getCurrentOS() == DataUtils.OperatingSystem.MACINTOSH) {
//        if(false) {
            menuBar.setUseSystemMenuBar(true);
        } else {
            AnchorPane.setTopAnchor(contentPane, menuBar.getPrefHeight());
        }
    }

    private MenuItem save;
    private MenuItem closeProject;

    private MenuItem exportJar;
    private MenuItem exportWeb;
    private MenuItem exportAndroid;

    private MenuItem globalPreferences;

    private void setMenuBarChildren() {
        // Create All Menus
        Menu file = new Menu("File");
        Menu edit = new Menu("Edit");
        Menu view = new Menu("View");
        Menu navigate = new Menu("Navigate");
        Menu help = new Menu("Help");

        // File Menu Items
        save = new MenuItem("Save");
        save.setAccelerator(new KeyCodeCombination(KeyCode.S, controlModifier));

        closeProject = new MenuItem("Close Project");

        globalPreferences = new MenuItem("Global Preferences");
        globalPreferences.setAccelerator(new KeyCodeCombination(KeyCode.COMMA, controlModifier));

        Menu export = new Menu("Export");
        exportJar = new MenuItem("To Jar File");
        exportWeb = new MenuItem("To Web Formats");
        exportAndroid = new MenuItem("To Android APK");
        export.getItems().addAll(exportJar, exportWeb, new SeparatorMenuItem(), exportAndroid);

        file.getItems().addAll(save, export, closeProject, new SeparatorMenuItem(), globalPreferences);

        // Add All Menus
        menuBar.getMenus().addAll(file, edit, view, navigate, help);
    }

    private void handleMenuBarChildren() {
        save.setOnAction(actionEvent -> Harmony.save());

        closeProject.setOnAction(actionEvent -> {
            Harmony.save();
            Harmony.staticStage.close();
            try {
                new Launcher().start(new Stage());
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        globalPreferences.setOnAction(actionEvent -> GlobalData.launchGlobalPreferences());
    }

}
