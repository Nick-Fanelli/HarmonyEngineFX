/*
 * Copyright © 2020 Harmony Engines.
 * All rights reserved.
 */

package com.harmony.engine.bridge;

import com.harmony.engine.Harmony;

import java.io.File;

public class Bridge {

    public static void createProject() {
        File exportDirectory = new File(Harmony.directory.getPath() + File.separator + ".build");

        if(!exportDirectory.exists()) {
            boolean success = exportDirectory.mkdirs();
            if(!success) return; // TODO: Throw some kind of error!
        }


    }

    public static void runProject() {
        Bridge.createProject();
    }

    public enum ExportType {
        WEB, JAR, MAC, WINDOWS, ANDROID, IOS
    }

    public static void exportProject(ExportType type) {

    }

}
