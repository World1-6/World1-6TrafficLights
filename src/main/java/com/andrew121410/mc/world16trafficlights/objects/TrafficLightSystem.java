package com.andrew121410.mc.world16trafficlights.objects;

import com.andrew121410.mc.world16trafficlights.World16TrafficLights;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.configuration.serialization.SerializableAs;

import java.util.HashMap;
import java.util.Map;

@SerializableAs("TrafficLightSystem")
public class TrafficLightSystem implements ConfigurationSerializable {

    private World16TrafficLights plugin;

    private boolean isTurningJunction;
    private Map<Integer, TrafficLight> trafficLightMap;
    private TrafficLightState lightState;

    public TrafficLightSystem(World16TrafficLights plugin, boolean isTurningJunction, Map<Integer, TrafficLight> trafficLightMap) {
        this.plugin = plugin;
        this.isTurningJunction = isTurningJunction;
        this.trafficLightMap = trafficLightMap;
    }

    public TrafficLightSystem(World16TrafficLights plugin, boolean isTurningJunction) {
        this(plugin, isTurningJunction, new HashMap<>());
    }

    public TrafficLightSystem(World16TrafficLights plugin) {
        this(plugin, false);
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

    public boolean isTurningJunction() {
        return isTurningJunction;
    }

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> map = new HashMap<>();
        map.put("IsTurningJunction", this.isTurningJunction);
        map.put("TrafficLightMap", this.trafficLightMap);
        return map;
    }

    public static TrafficLightSystem deserialize(Map<String, Object> map) {
        return new TrafficLightSystem(World16TrafficLights.getInstance(), (boolean) map.get("IsTurningJunction"), (Map<Integer, TrafficLight>) map.get("TrafficLightMap"));
    }
}