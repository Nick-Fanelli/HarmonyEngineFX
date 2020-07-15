/*
 * Copyright © 2020 Harmony Engines.
 * All rights reserved.
 */

package com.harmony.engine.bridge;

<<<<<<< HEAD
<<<<<<< HEAD
import com.harmony.core.HarmonyProject;
import com.harmony.engine.data.ProjectData;
import com.harmony.engine.io.editor.state.State;
import com.harmony.engine.utils.gameObjects.GameObject;
import javafx.embed.swing.SwingFXUtils;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class Bridge {

    public static String name;
    public static String version;

    public static ArrayList<State> states;

    private static HarmonyProject project;

    public static void update() {
        Bridge.name = ProjectData.projectName;
        Bridge.version = ProjectData.versionID;

        Bridge.states = new ArrayList<>(ProjectData.states);

        if(project != null) { project.stop(); }

        // TODO: Update
        project = new HarmonyProject(false, name, version, new Dimension(1280, 720), false);
    }

    public static void run() {
        if(project == null) update();

        ArrayList<com.harmony.core.State> states = new ArrayList<>();

        for(State state : ProjectData.states) states.add(translateState(state));

        project.start(states.toArray(new com.harmony.core.State[0]));
    }

    // Utils
    public static com.harmony.core.State translateState(State state) {
        ArrayList<com.harmony.core.GameObject> gameObjects = new ArrayList<>();

        for(GameObject gameObject : state.gameObjects) {
            gameObjects.add(translateGameObject(gameObject));
        }

        return new com.harmony.core.State(state.name, gameObjects);
    }

    public static com.harmony.core.GameObject translateGameObject(GameObject gameObject) {
        BufferedImage image = new BufferedImage((int) gameObject.texture.getImage().getWidth(), (int) gameObject.texture.getImage().getHeight(), BufferedImage.TYPE_INT_ARGB);
        image = SwingFXUtils.fromFXImage(gameObject.texture.getImage(), image);
        com.harmony.core.Vector2f vector2f = new com.harmony.core.Vector2f(gameObject.position.x, gameObject.position.y);

        return new com.harmony.core.GameObject(gameObject.name, image, vector2f);
    }
=======
=======
import com.harmony.engine.data.ProjectData;
import com.harmony.engine.io.editor.state.State;

import java.util.ArrayList;

>>>>>>> Set-up instance for everyting
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

>>>>>>> Refactored to bridge
}
