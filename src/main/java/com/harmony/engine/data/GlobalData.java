package com.harmony.engine.data;

import java.io.*;
import java.net.URISyntaxException;

public class GlobalData implements Serializable {

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
            FileOutputStream file = new FileOutputStream(new File(GlobalData.class.getResource("/users/globalPreferences.dat").toURI()));
            ObjectOutputStream out = new ObjectOutputStream(file);

            out.writeObject(dataContext);

            out.close();
            file.close();
        } catch (IOException | URISyntaxException e) {
            e.printStackTrace();
        }
    }

    public GlobalData load() {
        try {
            FileInputStream file = new FileInputStream(new File(GlobalData.class.getResource("/users/globalPreferences.dat").toURI()));
            ObjectInputStream out = new ObjectInputStream(file);

            GlobalData.dataContext = (GlobalData) out.readObject();

            out.close();
            file.close();

            return GlobalData.dataContext;
        } catch (Exception e) {
            System.out.println("Harmony -> Creating the globalPreferences.dat workspace...");
        }

        return GlobalData.defaultData;
    }

}
