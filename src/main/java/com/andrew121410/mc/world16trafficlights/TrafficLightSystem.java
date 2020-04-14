package com.andrew121410.mc.world16trafficlights;

import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.configuration.serialization.SerializableAs;

import java.util.HashMap;
import java.util.Map;

@SerializableAs("TrafficLightSystem")
public class TrafficLightSystem implements ConfigurationSerializable {

    private Main plugin;

    private Map<Integer, TrafficLight> trafficLightMap;
    private boolean isTurningJunction;
    private TrafficLightState lightState;

    public TrafficLightSystem(Main plugin, boolean isTurningJunction) {
        this.plugin = plugin;
        this.isTurningJunction = isTurningJunction;
        this.trafficLightMap = new HashMap<>();
    }

    public TrafficLightSystem(Main plugin) {
        this(plugin, false);
    }

    public void doLight(TrafficLightState trafficLightState) {
        this.lightState = trafficLightState;
        trafficLightMap.entrySet().removeIf(entry -> !entry.getValue().doLight(trafficLightState));
    }

    public Map<Integer, TrafficLight> getTrafficLightMap() {
        return trafficLightMap;
    }

    public Main getPlugin() {
        return plugin;
    }

    public TrafficLightState getLightState() {
        return lightState;
    }

    public boolean isTurningJunction() {
        return isTurningJunction;
    }

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> map = new HashMap<>();
        map.put("isTurningJunction", isTurningJunction);
        return map;
    }

    public static TrafficLightSystem deserialize(Map<String, Object> map) {
        return new TrafficLightSystem(Main.getInstance(), (boolean) map.get("isTurningJunction"));
    }
}