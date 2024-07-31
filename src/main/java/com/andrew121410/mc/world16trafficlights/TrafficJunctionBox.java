package com.andrew121410.mc.world16trafficlights;

import com.andrew121410.mc.world16trafficlights.enums.TrafficLightState;

import java.util.HashMap;
import java.util.Map;

public class TrafficJunctionBox {

    private final World16TrafficLights plugin;

    private final Map<Integer, TrafficLight> trafficLightMap;
    private TrafficLightState lightState;

    public TrafficJunctionBox(World16TrafficLights plugin, Map<Integer, TrafficLight> trafficLightMap) {
        this.plugin = plugin;
        this.trafficLightMap = trafficLightMap;
    }

    public TrafficJunctionBox(World16TrafficLights plugin) {
        this(plugin, new HashMap<>());
    }

    public void doLight(TrafficLightState trafficLightState) {
        this.lightState = trafficLightState;
        trafficLightMap.entrySet().removeIf(entry -> !entry.getValue().doLight(trafficLightState));
    }

    public Map<Integer, TrafficLight> getTrafficLightMap() {
        return trafficLightMap;
    }

    public World16TrafficLights getPlugin() {
        return plugin;
    }

    public TrafficLightState getLightState() {
        return lightState;
    }
}