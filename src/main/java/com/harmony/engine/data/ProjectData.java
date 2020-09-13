package com.harmony.engine.data;

import com.harmony.engine.utils.Log;

import java.io.File;
import java.io.IOException;

public class ProjectData {

    private static File directory;
    private static File projectFile;
    private static File resourceDirectory;

    public static File getDirectory() { return directory; }
    public static File getProjectFile() { return projectFile; }
    public static File getResourceDirectory() { return resourceDirectory; }

    public static void setDirectory(File directory) {
        if(!directory.exists()) {
            boolean isSuccess = directory.mkdirs();
            if(!isSuccess) Log.error("Could not create directory at: " + directory.getPath());
        }

        ProjectData.directory = directory;

        File[] files = new File[] {
                projectFile = new File(directory.getPath() + File.separator + "project.xml")
        };

        File[] directories = new File[]{
                resourceDirectory = new File(directory.getPath() + File.separator + "Resources")
        };

        for(File file : files) {
            if(!file.exists()) {
                try {
                    boolean isSuccess = file.createNewFile();
                    if(!isSuccess) Log.error("Could not create project file at: " + file.getPath());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        for(File dir : directories) {
            if(!dir.exists()) {
                boolean isSuccess = dir.mkdirs();
                if(!isSuccess) Log.error("Could not create project directory at: " + dir.getPath());
            }
        }
    }

}
