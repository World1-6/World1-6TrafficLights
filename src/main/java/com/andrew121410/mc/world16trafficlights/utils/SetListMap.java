package com.andrew121410.mc.world16trafficlights.utils;

import com.andrew121410.mc.world16trafficlights.objects.TrafficSystem;
import org.bukkit.Location;

import java.util.HashMap;
import java.util.Map;

public class SetListMap {

    private Map<String, TrafficSystem> trafficSystemMap;
    private Map<Location, String> chunkToTrafficSystemName;

    public SetListMap() {
        this.trafficSystemMap = new HashMap<>();
        this.chunkToTrafficSystemName = new HashMap<>();
    }

    public Map<String, TrafficSystem> getTrafficSystemMap() {
        return trafficSystemMap;
    }

    public Map<Location, String> getChunkToTrafficSystemName() {
        return chunkToTrafficSystemName;
    }
}
