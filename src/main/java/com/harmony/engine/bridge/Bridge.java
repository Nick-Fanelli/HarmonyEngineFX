/*
 * Copyright © 2020 Harmony Engines.
 * All rights reserved.
 */

package com.harmony.engine.bridge;

import com.harmony.engine.Harmony;
import com.harmony.engine.data.DataUtils;
import com.harmony.engine.data.ProjectData;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URL;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.regex.Pattern;

public class Bridge {

    public static final String[] ENGINE_FILES = new String[] {
            "graphics.js", "harmony.js", "physics.js", "states.js", "utils.js"
    };

    private static File index = null;

    public static void cleanProject() {
        File buildDirectory = new File(Harmony.directory.getPath() + File.separator + ".build");
        if(buildDirectory.exists()) DataUtils.cleanDirectory(buildDirectory);
    }

    public static void buildProject() throws Exception {
        System.out.println("Harmony [Build] -> Building Project...");
        Harmony.save();

        Bridge.cleanProject();
        File buildDirectory = new File(Harmony.directory.getPath() + File.separator + ".build");

        if(!buildDirectory.exists()) {
            boolean success = buildDirectory.mkdirs();
            if(!success) return; // TODO: Throw some kind of error!
        }

        writeSourceFiles(buildDirectory);
        copyTextures(buildDirectory);

        System.out.println("Harmony [Build] -> Project Built");
    }

    public static void runProject() {
        try { Bridge.buildProject(); } catch (Exception e) { e.printStackTrace(); }

        System.out.println("Harmony [Build] -> Running Project...");

        try{
            String[] commands = new String[0];

            if(DataUtils.OperatingSystem.getCurrentOS() == DataUtils.OperatingSystem.MACINTOSH) {
                commands = new String[] { "open", index.getAbsolutePath() };
            } else if(DataUtils.OperatingSystem.getCurrentOS() == DataUtils.OperatingSystem.WINDOWS) {
                commands = new String[] { "start", index.getAbsolutePath() };
            }

            Runtime.getRuntime().exec(commands);
        } catch(Exception e) {
            e.printStackTrace();
        }

        System.out.println("Harmony [Build] -> Project Running");
    }

    public enum ExportType {
        WEB, JAR, MAC, WINDOWS, ANDROID, IOS
    }

    public static void exportProject(ExportType type) {

    }

    private static void writeSourceFiles(File buildDirectory) {
        ArrayList<File> buildFiles = new ArrayList<>();
        ArrayList<File> buildDirectories = new ArrayList<>();

        // Create Directories
        File clientDirectory = new File(buildDirectory.getPath() + File.separator + "client");
        File cssDirectory = new File(buildDirectory.getPath() + File.separator + "css");
        File engineDirectory = new File(buildDirectory.getPath() + File.separator + "engine");

        buildDirectories.add(clientDirectory);
        buildDirectories.add(cssDirectory);
        buildDirectories.add(engineDirectory);

        // Create Files
        File index = new File(buildDirectory.getPath() + File.separator + "index.html");
        File mainCSS = new File(cssDirectory.getPath() + File.separator + "main.css");
        File clientEntryPoint = new File(clientDirectory.getPath() + File.separator + "client_entry_point.js");
        File clientProject = new File(clientDirectory.getPath() + File.separator + "client_project.js");

        for(String location : ENGINE_FILES) {
            buildFiles.add(new File(engineDirectory.getPath() + File.separator + location));
        }

        buildFiles.add(index);
        buildFiles.add(mainCSS);
        buildFiles.add(clientEntryPoint);

        // Create Directories
        for(File directory : buildDirectories) {
            if(!directory.exists()) {
                boolean isSuccess = directory.mkdirs();
                if(!isSuccess) return;
            }
        }

        // Create Files
        for(File file : buildFiles) {
            if(!file.exists()) {
                boolean isSuccess = false;
                try {
                    isSuccess = file.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if(!isSuccess) return;
            }

            String currentLocation = file.getPath().replaceAll(Pattern.quote(buildDirectory.getPath()), "");
            currentLocation = "/HarmonyJS/" + currentLocation.replaceAll(Pattern.quote("\\"), "/");

            InputStream sourceInputStream = Bridge.class.getResourceAsStream(currentLocation.replaceAll("//+", "/"));

            DataUtils.copyFile(sourceInputStream, file);
        }

        // Client Project
        String projectFileData = DataUtils.readFile(ProjectData.getProjectFile(Harmony.directory));
        DataUtils.writeFile(clientProject, String.format("const projectXML = `%s`;", projectFileData));

        Bridge.index = index;
    }

    private static void copyTextures(File location) {
        File texturesLocation = new File(location.getPath() + File.separator + "textures");
        if(!texturesLocation.exists()) {
            boolean isSuccess = texturesLocation.mkdirs();
            if(!isSuccess) return; // TODO: Throw Error
        }

        copyDirectory(Harmony.getTexturesLocation(), texturesLocation);
    }

    private static void copyDirectory(File sourceDirectory, File targetLocation) {
        File[] children = sourceDirectory.listFiles();
        if(children == null) return;
        ArrayList<File> directories = new ArrayList<>();

        for(File child : children) {
            if(child.isHidden()) continue;

            if(child.isDirectory()) {
                directories.add(child);
                continue;
            }

            File targetFile = new File(targetLocation.getPath() + File.separator + child.getName());
            DataUtils.copyFile(child, targetFile);
        }

        for(File directory : directories) {
            File targetDir = new File(targetLocation + File.separator + directory.getName());

            if(!targetDir.exists()) {
                boolean isSuccess = targetDir.mkdirs();
                if(!isSuccess) return; // TODO: Throw Error
            }

            copyDirectory(directory, targetDir);
        }
    }
}
