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
import java.net.URI;
import java.util.ArrayList;

public class Bridge {

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

        Bridge.index = writeSourceFiles(buildDirectory);
        copyTextures(buildDirectory);

        System.out.println("Harmony [Build] -> Project Built");
    }

    public static void runProject() {
        try { Bridge.buildProject(); } catch (Exception e) { e.printStackTrace(); }

        if(!index.exists()) {
            try { Bridge.buildProject(); } catch (Exception e) { e.printStackTrace(); }
            return;
        }

        System.out.println("Harmony [Build] -> Running Project...");

        URI indexURI = index.toURI();

        try {
            Desktop.getDesktop().browse(indexURI);
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("Harmony [Build] -> Project Running");
    }

    public enum ExportType {
        WEB, JAR, MAC, WINDOWS, ANDROID, IOS
    }

    public static void exportProject(ExportType type) {

    }

    private static File writeSourceFiles(File location) throws Exception {
        // Create "Client" index.html File
//        System.out.println("Harmony [Build] -> Creating File index.html");
        File index = new File(location.getPath() + File.separator + "index.html");
        if(!index.exists()) {
            try {
                boolean isSuccess = index.createNewFile();
                if(!isSuccess) return null; // TODO: Throw Error
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
//        System.out.println("Harmony [Build] -> File index.html Created\n");

        // Copy index.html Data
//        System.out.println("Harmony [Build] -> Copying index.html Data");
        File indexSourceFile = new File(Bridge.class.getResource("/HarmonyJS/index.html").toURI());
        DataUtils.copyFile(indexSourceFile, index);
//        System.out.println("Harmony [Build] -> Copied index.html Data\n");

        // JS Directory
//        System.out.println("Harmony [Build] -> Creating js Directory");
        File jsDirectory = new File(location.getPath() + File.separator + "engine");
        if(!jsDirectory.exists() || !jsDirectory.isDirectory()) {
            boolean isSuccess = jsDirectory.mkdirs();
            if(!isSuccess) return null; // TODO: Throw Error
        }
//        System.out.println("Harmony [Build] -> Directory js Created\n");

        // Copy JS Files
        File jsSourceDirectory = new File(Bridge.class.getResource("/HarmonyJS/engine").toURI());
        File[] engineJSFiles = jsSourceDirectory.listFiles();
        if(engineJSFiles == null) return null; // TODO: Throw Error

        for(File sourceFile : engineJSFiles) {
//            System.out.printf("Harmony [Build] -> Creating %s File\n", sourceFile.getName());
            File file = new File(jsDirectory.getPath() + File.separator + sourceFile.getName());
//            System.out.printf("Harmony [Build] -> Created %s File\n", sourceFile.getName());

//            System.out.printf("Harmony [Build] -> Copying %s File\n", sourceFile.getName());
            DataUtils.copyFile(sourceFile, file);
//            System.out.printf("Harmony [Build] -> Copied %s File\n\n", sourceFile.getName());
        }

        // Copy CSS File
        File cssSourceFile = new File(Bridge.class.getResource("/HarmonyJS/css/main.css").toURI());
        File cssDirectory = new File(location.getPath() + File.separator + "css");
        if(!cssDirectory.exists()) {
            boolean isSuccess = cssDirectory.mkdir();
            if(!isSuccess) return null; // TODO: Throw Error
        }

        File cssFile = new File(cssDirectory.getPath() + File.separator + "main.css");
        DataUtils.copyFile(cssSourceFile, cssFile);

        // Copy Client Entry Point
        File clientDirectory = new File(location.getPath() + File.separator + "client");
        if(!clientDirectory.exists()) {
            boolean isSuccess = clientDirectory.mkdir();
            if(!isSuccess) return null; // TODO: Throw Error
        }

        File sourceClientEntryPoint = new File(Bridge.class.getResource("/HarmonyJS/client/client_entry_point.js").toURI());
        File clientEntryPoint = new File(clientDirectory.getPath() + File.separator + sourceClientEntryPoint.getName());
        DataUtils.copyFile(sourceClientEntryPoint, clientEntryPoint);

        // Create Client Project
        File clientProject = new File(clientDirectory + File.separator + "client_project.js");
        if(!clientProject.exists()) {
            boolean isSuccess = clientProject.createNewFile();
            if(!isSuccess) return null; // TODO: Throw Error
        }

        String projectFileData = DataUtils.readFile(ProjectData.getProjectFile(Harmony.directory));
        String clientProjectData = String.format("const projectXML = `%s`;", projectFileData);
        DataUtils.writeFile(clientProject, clientProjectData);

        return index;
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
