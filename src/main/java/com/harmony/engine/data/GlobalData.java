package com.harmony.engine.data;

import com.harmony.engine.Harmony;
import com.harmony.engine.Launcher;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

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
    public enum Theme {
        LIGHT("Default Light"), DARK("Default Dark");

        private String viewableString;
        Theme(String viewableString) { this.viewableString = viewableString; }

        @Override public String toString() { return viewableString; }
    }

    public GlobalData copy() {
        GlobalData globalData = new GlobalData();

        globalData.theme = dataContext.theme;

        return globalData;
    }

    @Override
    public String toString() {
        return "GlobalData = [\n" +
                    "\tTheme: \"" + theme.viewableString + "\"\n" +
                "]";

    }

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

    public static Stage staticStage;

    public static void launchGlobalPreferences() {
        try {
            FXMLLoader loader = new FXMLLoader(GlobalData.class.getResource("/utils/globalPreferences.fxml"));

            Stage stage = new Stage();
            GlobalData.staticStage = stage;
            Parent root = loader.load();

            Scene scene = new Scene(root);

            // Handle Theme
            scene.getStylesheets().add(Harmony.class.getResource("/cssThemes/"
                    + GlobalData.dataContext.theme.name().toLowerCase() + "Theme.css").toExternalForm());

            stage.setResizable(false);
            stage.initStyle(StageStyle.UNDECORATED);
            stage.setAlwaysOnTop(true);
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
