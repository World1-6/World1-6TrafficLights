package com.andrew121410.mc.world16trafficlights;


import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

public class TrafficSystem implements ConfigurationSerializable {

    private Main plugin;
    private int currentTick;
    private int currentTrafficLightSystem;
    private boolean isTicking;
    private boolean stop;

    //SAVE
    private TrafficSystemType trafficSystemType;

    //SAVING SOLD SEPAERED
    private Map<Integer, TrafficLightSystem> trafficLightSystemMap;

    public TrafficSystem(Main plugin, TrafficSystemType trafficSystemType) {
        this.plugin = plugin;
        this.trafficSystemType = trafficSystemType;

        this.trafficLightSystemMap = new HashMap<>();
        this.isTicking = false;
        this.currentTick = 0;
        this.stop = false;
    }

    public void tick() {
        if (isTicking) return;
        isTicking = true;

        new BukkitRunnable() {
            @Override
            public void run() {
                if (stop) this.cancel();
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
        }.runTaskTimer(this.plugin, 20, 20);
    }

    public Main getPlugin() {
        return plugin;
    }

    public void setPlugin(Main plugin) {
        this.plugin = plugin;
    }

    public TrafficSystemType getTrafficSystemType() {
        return trafficSystemType;
    }

    public void setTrafficSystemType(TrafficSystemType trafficSystemType) {
        this.trafficSystemType = trafficSystemType;
    }

    public Map<Integer, TrafficLightSystem> getTrafficLightSystemMap() {
        return trafficLightSystemMap;
    }

    public boolean isTicking() {
        return isTicking;
    }

    public void stop() {
        this.stop = true;
    }

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> map = new HashMap<>();
        map.put("trafficSystemType", trafficSystemType.toString());
        return map;
    }

    public static TrafficSystem deserialize(Map<String, Object> map) {
        return new TrafficSystem(Main.getInstance(), TrafficSystemType.valueOf((String) map.get("trafficSystemType")));
    }
}