package com.andrew121410.mc.world16trafficlights.utils;

import com.andrew121410.mc.world16trafficlights.TrafficSystem;
import org.bukkit.Location;

import java.util.HashMap;
import java.util.Map;

public class MemoryHolder {

    private final Map<String, TrafficSystem> trafficSystemMap;
    private final Map<Location, String> chunkToTrafficSystemName;

    public MemoryHolder() {
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
