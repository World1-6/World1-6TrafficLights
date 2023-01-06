package com.andrew121410.mc.world16trafficlights;

import com.andrew121410.mc.world16utils.utils.ccutils.utils.HashBasedUpdater;

public class Updater extends HashBasedUpdater {

    private static final String JAR_URL = "https://github.com/World1-6/World1-6TrafficLights/releases/download/latest/World1-6TrafficLights.jar";
    private static final String HASH_URL = "https://github.com/World1-6/World1-6TrafficLights/releases/download/latest/hash.txt";

    public Updater(World16TrafficLights plugin) {
        super(plugin.getClass(), JAR_URL, HASH_URL);
    }
}
