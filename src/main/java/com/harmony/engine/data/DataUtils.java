package com.harmony.engine.data;

import com.harmony.engine.utils.Log;
import javafx.scene.input.KeyCombination;

import java.io.File;
import java.util.regex.Pattern;

public class DataUtils {

    // XML Data Utils
    public static final String IDENTIFIER_STRING = "`^~";

    public static String stringArrayToString(String[] array) {
        StringBuilder builder = new StringBuilder();

        for(String string : array) {
            if(string.contains(IDENTIFIER_STRING)) {
                Log.error("The string array to be turned into a string must not contain the character sequence " + IDENTIFIER_STRING + " Please make sure it's removed!");
                return null;
            }

            builder.append(string).append(IDENTIFIER_STRING);
        }

        return builder.toString();
    }

    public static String[] stringToStringArray(String string) {
        return string.split(Pattern.quote(IDENTIFIER_STRING));
    }

    // File Utils
    public enum FileType {
        DIRECTORY("", "Directory"),
        BLANK(".~\\", "File"),
        JS("js", "Javascript File"),
        TXT("txt", "Text File");

        public final String fileExtension;
        public final String fileName;

        FileType(String fileExtension, String fileName) {
            this.fileExtension = fileExtension;
            this.fileName = fileName;
        }

        public static FileType identifyFile(File file) {
            String[] fileSplit = file.getName().split("\\.");
            String fileName = fileSplit[fileSplit.length - 1].toLowerCase();

            for(FileType type : FileType.values()) {
                if(fileName.equals(type.fileExtension)) return type;
            }

            return BLANK;
        }
    }

    // OS Utils
    public enum OperatingSystem {
        MACINTOSH(KeyCombination.META_DOWN),
        WINDOWS(KeyCombination.CONTROL_DOWN),
        UNKNOWN(null);

        public final KeyCombination.Modifier controlModifier;

        OperatingSystem(KeyCombination.Modifier controlModifier) {
            this.controlModifier = controlModifier;
        }

        public static OperatingSystem getCurrentOS() {
            String osName = System.getProperty("os.name").toLowerCase();

            if(osName.startsWith("win")) return WINDOWS;
            if(osName.startsWith("mac")) return MACINTOSH;

            return UNKNOWN;
        }
    }

}
