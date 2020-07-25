/*
 * Copyright © 2020 Harmony Engines.
 * All rights reserved.
 */

package com.harmony.engine.data.networking.resource;

import com.harmony.engine.data.networking.NetUtils;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public abstract class NetResource {

    public final String URL_LOCATION;
    public final HashMap<String, String> FILE_LOCATIONS;

    public NetResource(String urlLocation, HashMap<String, String> fileLocations) {
        this.URL_LOCATION = urlLocation;
        this.FILE_LOCATIONS = fileLocations;
    }

    public void downloadTo(File directory) {
        for(Map.Entry<String, String> entry : FILE_LOCATIONS.entrySet()) {
            NetUtils.downloadFile(URL_LOCATION + "/" + entry.getKey(), directory + File.separator + entry.getValue() + File.separator + entry.getKey());
        }
    }

}
