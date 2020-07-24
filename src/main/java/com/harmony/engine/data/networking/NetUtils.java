/*
 * Copyright © 2020 Harmony Engines.
 * All rights reserved.
 */

package com.harmony.engine.data.networking;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

public class NetUtils {

    public static final String WEBSITE_URL = "https://harmonyengines.com";
    public static final String RESOURCES_LOCATION = "https://harmonyengines.com/resources";
    public static final String DEMO_RESOURCES_LOCATION = "https://harmonyengines.com/resources/demo";

    public static void downloadFile(URL url, File location) {
        if(location.exists()) {
            System.err.println("Harmony Error -> Location Requested Exists\n" +
                    "\tLocation of " + location.getPath() + " already exits.\n" +
                    "\tAborting File Download...");
            return;
        }

        try {
            BufferedInputStream in = new BufferedInputStream(url.openStream());
            FileOutputStream out = new FileOutputStream(location);

            byte[] dataBuffer = new byte[1024];
            int currentByte;

            while((currentByte = in.read(dataBuffer, 0, 1024)) != -1) {
                out.write(dataBuffer, 0, currentByte);
            }

            out.close();
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void downloadFile(String url, File location) {
        try {
            downloadFile(new URL(url), location);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    public static void downloadFile(URL url, String location) { downloadFile(url, new File(location)); }

    public static void downloadFile(String url, String location) {
        try {
            downloadFile(new URL(url), new File(location));
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }


}
