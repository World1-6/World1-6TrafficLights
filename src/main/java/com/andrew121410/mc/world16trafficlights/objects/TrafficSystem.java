package com.andrew121410.mc.world16trafficlights.objects;


import com.andrew121410.mc.world16trafficlights.World16TrafficLights;
import org.bukkit.Location;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.configuration.serialization.SerializableAs;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

@SerializableAs("TrafficSystem")
public class TrafficSystem implements ConfigurationSerializable {

    private World16TrafficLights plugin;

    //Saving
    private String name;
    private TrafficSystemType trafficSystemType;
    private Location mainChunk;
    private Map<Integer, TrafficLightSystem> trafficLightSystemMap;

    private boolean isTicking;
    private int currentTick;
    private int currentTrafficLightSystem;
    private boolean stop;

    public TrafficSystem(World16TrafficLights plugin, String name, Location mainChunk, TrafficSystemType trafficSystemType, Map<Integer, TrafficLightSystem> trafficLightSystemMap) {
        this.plugin = plugin;
        this.name = name.toLowerCase();
        this.mainChunk = mainChunk;
        this.trafficSystemType = trafficSystemType;
        this.trafficLightSystemMap = trafficLightSystemMap;

        this.isTicking = false;
        this.currentTick = 0;
        this.currentTrafficLightSystem = 0;
        this.stop = false;
    }

    public TrafficSystem(World16TrafficLights plugin, String name, Location mainChunk, TrafficSystemType trafficSystemType) {
        this(plugin, name, mainChunk, trafficSystemType, new HashMap<>());
    }

    public void tick() {
        if (isTicking) return;
        isTicking = true;

        new BukkitRunnable() {
            @Override
            public void run() {
                if (stop) {
                    this.cancel();
                    stop = false;
                    isTicking = false;
                    return;
                }

                TrafficLightSystem trafficLightSystem = trafficLightSystemMap.get(currentTrafficLightSystem);
                Stream<Map.Entry<Integer, TrafficLightSystem>> turningJunctions = trafficLightSystemMap.entrySet().stream().filter((k) -> k.getValue().isTurningJunction());

                if (trafficLightSystem == null) {
                    return;
                }

                if (currentTick <= 10) {
                    //ONLY RUN ONE TIME.
                    if (currentTick == 0) {
                        //GREEN
                        trafficLightSystem.doLight(TrafficLightState.GREEN);
                        trafficLightSystemMap.entrySet().stream().filter(key -> key.getKey() != currentTrafficLightSystem).forEach((k -> k.getValue().doLight(TrafficLightState.RED)));
                    }
                } else if (currentTick <= 15) {
                    //YELLOW
                    //ONLY RUN ONE TIME
                    if (currentTick == 11) {
                        trafficLightSystem.doLight(TrafficLightState.YELLOW);
                    } else if (trafficSystemType.hasTurningLane() && currentTick == 15) {
                        trafficLightSystem.doLight(TrafficLightState.RED);
                    }
                } else if (trafficSystemType.hasTurningLane() && currentTick <= 26) {
                    if (currentTick == 16) {
                        turningJunctions.forEach((k) -> k.getValue().doLight(TrafficLightState.TURN));
                    } else if (currentTick == 17) {
                        turningJunctions.forEach((k) -> k.getValue().getTrafficLightMap().forEach((k1, v1) -> v1.off()));
                    } else if (currentTick == 18) {
                        turningJunctions.forEach((k) -> k.getValue().doLight(TrafficLightState.TURN));
                    } else if (currentTick == 19) {
                        turningJunctions.forEach((k) -> k.getValue().getTrafficLightMap().forEach((k1, v1) -> v1.off()));
                    } else if (currentTick == 20) {
                        turningJunctions.forEach((k) -> k.getValue().doLight(TrafficLightState.TURN));
                    } else if (currentTick == 21) {
                        turningJunctions.forEach((k) -> k.getValue().getTrafficLightMap().forEach((k1, v1) -> v1.off()));
                    } else if (currentTick == 22) {
                        turningJunctions.forEach((k) -> k.getValue().doLight(TrafficLightState.TURN));
                    } else if (currentTick == 23) {
                        turningJunctions.forEach((k) -> k.getValue().getTrafficLightMap().forEach((k1, v1) -> v1.off()));
                    } else if (currentTick == 24) {
                        turningJunctions.forEach((k) -> k.getValue().doLight(TrafficLightState.TURN));
                    } else if (currentTick == 25) {
                        turningJunctions.forEach((k) -> k.getValue().getTrafficLightMap().forEach((k1, v1) -> v1.yellow()));
                    } else {
                        turningJunctions.forEach((k) -> k.getValue().getTrafficLightMap().forEach((k1, v1) -> v1.yellow()));
                    }

                } else {
                    //RED
                    trafficLightSystem.doLight(TrafficLightState.RED);

                    if (currentTrafficLightSystem == 0) currentTrafficLightSystem = 1;
                    else if (currentTrafficLightSystem == 1) currentTrafficLightSystem = 0;

                    currentTick = 0;
                    return;
                }
                currentTick++;
            }
        }.runTaskTimer(this.plugin, 20L, 20L);
    }

    public World16TrafficLights getPlugin() {
        return plugin;
    }

    public String getName() {
        return name;
    }

    public TrafficSystemType getTrafficSystemType() {
        return trafficSystemType;
    }

    public Location getMainChunk() {
        return mainChunk;
    }

    public Map<Integer, TrafficLightSystem> getTrafficLightSystemMap() {
        return trafficLightSystemMap;
    }

    public boolean isTicking() {
        return isTicking;
    }

    public int getCurrentTick() {
        return currentTick;
    }

    public int getCurrentTrafficLightSystem() {
        return currentTrafficLightSystem;
    }

    public void stop() {
        this.stop = true;
    }

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> map = new HashMap<>();
        map.put("Name", this.name);
        map.put("MainChunk", this.mainChunk);
        map.put("TrafficSystemType", this.trafficSystemType.name());
        map.put("TrafficLightSystemMap", this.trafficLightSystemMap);
        return map;
    }

    public static TrafficSystem deserialize(Map<String, Object> map) {
        return new TrafficSystem(World16TrafficLights.getInstance(), (String) map.get("Name"), (Location) map.get("MainChunk"), TrafficSystemType.valueOf((String) map.get("TrafficSystemType")), (Map<Integer, TrafficLightSystem>) map.get("TrafficLightSystemMap"));
    }
}