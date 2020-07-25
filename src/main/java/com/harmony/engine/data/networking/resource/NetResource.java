/*
 * Copyright © 2020 Harmony Engines.
 * All rights reserved.
 */

package com.harmony.engine.data.networking.resource;

import com.harmony.engine.data.networking.NetUtils;

import java.io.File;

public abstract class NetResource {

    public final String URL_LOCATION;
    public final String[] FILE_LOCATIONS;

    public NetResource(String urlLocation, String[] fileLocations) {
        this.URL_LOCATION = urlLocation;
        this.FILE_LOCATIONS = fileLocations;
    }

    public void downloadTo(File directory) {
        NetUtils.downloadAllFiles(URL_LOCATION, FILE_LOCATIONS, directory);
    }

}
