package com.harmony.engine.data;

import com.harmony.engine.utils.Log;

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

}
