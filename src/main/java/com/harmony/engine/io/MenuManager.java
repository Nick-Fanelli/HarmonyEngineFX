/*
 * Copyright © 2020 Harmony Engines.
 * All rights reserved.
 */

package com.harmony.engine.io;

import com.harmony.engine.data.DataUtils;
import javafx.scene.layout.AnchorPane;
import javafx.scene.control.MenuBar;

public class MenuManager {

    private MenuBar menuBar;
    private AnchorPane contentPane;

    public MenuManager(MenuBar menuBar, AnchorPane contentPane) {
        this.menuBar = menuBar;
        this.contentPane = contentPane;
    }

    public void initialize() {
        this.setMenuBarPosition();
        this.setMenuBarChildren();
    }

    private void setMenuBarPosition() {
        if(DataUtils.OperatingSystem.getCurrentOS() == DataUtils.OperatingSystem.MAC) {
            menuBar.setUseSystemMenuBar(true);
        } else {
            AnchorPane.setTopAnchor(contentPane, menuBar.getPrefHeight());
        }
    }

    private void setMenuBarChildren() {

    }

}
