package com.harmony.engine.data;

import com.harmony.engine.Launcher;

import java.io.*;

public class GlobalData implements Serializable {

    public static final String GLOBAL_PREFERENCES_FILENAME = "globalPreferences.dat";
    public static final String GLOBAL_DATA_LOCATION =  System.getProperty("user.home") + File.separator
            + ".harmony" + File.separator + Launcher.GITHUB_VERSION_STRING.replaceAll("version-", "") + File.separator;

    public static final String GLOBAL_PREFERENCES_LOCATION = GLOBAL_DATA_LOCATION + File.separator + GLOBAL_PREFERENCES_FILENAME;

    private static final GlobalData defaultData = new GlobalData();

    // Defaults For Global Data
    static {
        defaultData.theme = Theme.LIGHT;
    }

    public static GlobalData dataContext = new GlobalData().load();

    // Values

    public Theme theme;
    public enum Theme { LIGHT, DARK }

    public void save() {
        try {
            FileOutputStream file = new FileOutputStream(GLOBAL_PREFERENCES_LOCATION);
            ObjectOutputStream out = new ObjectOutputStream(file);

            out.writeObject(dataContext);

            out.close();
            file.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public GlobalData load() {
        try {
            FileInputStream file = new FileInputStream(GLOBAL_PREFERENCES_LOCATION);
            ObjectInputStream out = new ObjectInputStream(file);

            GlobalData.dataContext = (GlobalData) out.readObject();

            out.close();
            file.close();

            return GlobalData.dataContext;
        } catch (Exception e) {
            System.out.println("Harmony -> Creating the globalPreferences.dat workspace...");
        }

        System.out.println(new File(GLOBAL_DATA_LOCATION).mkdirs() ? "Harmony -> Location Successfully Set-Up"
                : "Harmony -> Error Setting Up Location");

        return GlobalData.defaultData;
    }

}
