package com.andrew121410.mc.world16trafficlights.utils;

import com.andrew121410.mc.world16trafficlights.TrafficSystem;

import java.util.HashMap;
import java.util.Map;

public class SetListMap {

    private Map<String, TrafficSystem> trafficSystemMap;

    public SetListMap() {
        this.trafficSystemMap = new HashMap<>();
    }

    public Map<String, TrafficSystem> getTrafficSystemMap() {
        return trafficSystemMap;
    }
}
