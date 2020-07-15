/*
 * Copyright © 2020 Harmony Engines.
 * All rights reserved.
 */

package com.harmony.engine.bridge;

import com.harmony.engine.data.ProjectData;
import com.harmony.engine.io.editor.state.State;

import java.util.ArrayList;

public class Bridge {

    public static String name;
    public static String version;

    public static ArrayList<State> states;

    public static void update() {
        Bridge.name = ProjectData.projectName;
        Bridge.version = ProjectData.versionID;

        Bridge.states = new ArrayList<>(ProjectData.states);
    }

    public static void run() {
        
    }

}
