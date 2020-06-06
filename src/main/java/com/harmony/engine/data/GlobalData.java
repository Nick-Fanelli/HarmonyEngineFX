package com.harmony.engine.data;

import com.harmony.engine.Harmony;
import com.harmony.engine.Launcher;
import com.harmony.engine.utils.Status;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.*;
import java.util.HashMap;

public class GlobalData implements Serializable {

    public static final String GLOBAL_PREFERENCES_FILENAME = "globalPreferences.dat";
    public static final String GLOBAL_DATA_LOCATION =  System.getProperty("user.home") + File.separator
            + ".harmony" + File.separator + Launcher.GITHUB_VERSION_STRING.replaceAll("version-", "") + File.separator;

    public static final String GLOBAL_PREFERENCES_LOCATION = GLOBAL_DATA_LOCATION + File.separator + GLOBAL_PREFERENCES_FILENAME;
    public static HashMap<String, String> dataContext = new HashMap<>();

    public static void setDefaults() {
        GlobalData.setTheme(Theme.LIGHT);
    }

    public static final String THEME_LOCATION = "theme";
    public enum Theme {
        LIGHT("Default Light"),
        DARK("Default Dark"),
        BEACH("Beach Pallet"),
        PASTEL_BLUE("Pastel Blue"),
        PASTEL_PINK("Pastel Pink");

        private final String name;
        Theme(String name) { this.name = name; }

        @Override public String toString() { return name; }
    }
    public static void setTheme(Theme theme) { dataContext.put(THEME_LOCATION, theme.name()); }
    public static Theme getTheme() { return Theme.valueOf(dataContext.get(THEME_LOCATION)); }

    public static void save() {
        try {
            FileOutputStream file = new FileOutputStream(new File(GLOBAL_PREFERENCES_LOCATION));
            ObjectOutputStream out = new ObjectOutputStream(file);

            out.writeObject(dataContext);

            out.close();
            file.close();
        } catch(IOException e) {
            e.printStackTrace();
        }
    }

    public static HashMap<String, String> load() {
        try {
            FileInputStream file = new FileInputStream(new File(GLOBAL_PREFERENCES_LOCATION));
            ObjectInputStream in = new ObjectInputStream(file);

            dataContext = (HashMap<String, String>) in.readObject();

            in.close();
            file.close();

            return dataContext;
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Harmony -> Setting up globalPreferences.dat");
        }

        try {
            new File(GLOBAL_DATA_LOCATION).mkdirs();
            System.out.println("Harmony -> Created the global data location");
            new File(GLOBAL_PREFERENCES_LOCATION).createNewFile();
            System.out.println("Harmony -> Created the global data preferences file");
        } catch (Exception e) {
            System.err.println("Harmony -> Could not create the location or the preferences file");
            e.printStackTrace();
        }

        GlobalData.setDefaults();
        return dataContext;
    }

    // Utils
    public static String getThemeCSSLocation() { return "/cssThemes/" + getTheme().name().toLowerCase() + "Theme.css"; }

    public static Stage staticStage;

    public static void launchGlobalPreferences() {
        try {
            Status.setCurrentStatus(Status.Type.STAND_BY);
            FXMLLoader loader = new FXMLLoader(GlobalData.class.getResource("/utils/globalPreferences.fxml"));
            Parent root = loader.load();

            Scene scene = new Scene(root);

            // Handle Theme
            scene.getStylesheets().add(Harmony.class.getResource(GlobalData.getThemeCSSLocation()).toExternalForm());

            Stage stage = new Stage();
            GlobalData.staticStage = stage;
            stage.setTitle("Global Preferences");
            stage.setResizable(false);
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
