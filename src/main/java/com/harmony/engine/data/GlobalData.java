package com.harmony.engine.data;

import com.harmony.engine.Launcher;
import com.harmony.engine.utils.Log;

import java.io.File;
import java.io.IOException;

public class GlobalData {

    public static final String DATA_LOCATION = System.getProperty("user.home") + File.separator + ".harmony"
            + File.separator + String.format("v%s.%s.%s", Launcher.VERSION_ID[0], Launcher.VERSION_ID[1], Launcher.VERSION_ID[2]);
    public static final String CACHE_DATA_LOCATION = DATA_LOCATION + File.separator + "cache.xml";
    public static final String GLOBAL_PREF_LOCATION = DATA_LOCATION + File.separator + "globalPreferences.xml";

    static {
        File[] directories = new File[] {
                new File(DATA_LOCATION)
        };

        for(File directory: directories) {
            if(!directory.exists()) {
                Log.debug("Creating directory: " + directory.getPath());
                boolean isSuccess = directory.mkdirs();
                if(!isSuccess) Log.error("Could not create directory at: " + directory.getPath());
            }
        }

        File[] files = new File[] {
                new File(CACHE_DATA_LOCATION),
                new File(GLOBAL_PREF_LOCATION)
        };

        for(File file : files) {
            if(!file.exists()) {
                try {
                    Log.debug("Creating file: " + file.getPath());
                    boolean isSuccess = file.createNewFile();
                    if(!isSuccess) Log.error("Could not create file at: " + file.getPath());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
