package com.andrew121410.mc.world16trafficlights;


import com.andrew121410.mc.world16trafficlights.enums.TrafficLightState;
import com.andrew121410.mc.world16trafficlights.enums.TrafficSystemType;
import org.bukkit.Location;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

public class TrafficSystem {

    private final World16TrafficLights plugin;

    //Saving
    private final String name;
    private final TrafficSystemType trafficSystemType;
    private final Location mainChunk;
    private final Map<Integer, TrafficJunctionBox> trafficJunctionBoxMap;

    private boolean isTicking;
    private int currentTick;
    private int currentJunctionBox;
    private boolean stop;

    public TrafficSystem(World16TrafficLights plugin, String name, Location mainChunk, TrafficSystemType trafficSystemType, Map<Integer, TrafficJunctionBox> trafficJunctionBoxMap) {
        this.plugin = plugin;
        this.name = name.toLowerCase();
        this.mainChunk = mainChunk;
        this.trafficSystemType = trafficSystemType;
        this.trafficJunctionBoxMap = trafficJunctionBoxMap;

        this.isTicking = false;
        this.currentTick = 0;
        this.currentJunctionBox = 0;
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

                TrafficJunctionBox trafficJunctionBox = trafficJunctionBoxMap.get(currentJunctionBox);
                Stream<Map.Entry<Integer, TrafficJunctionBox>> turningJunctions = trafficJunctionBoxMap.entrySet().stream().filter((k) -> k.getValue().isTurningJunction());

                if (trafficJunctionBox == null) {
                    return;
                }

                if (currentTick <= 10) {
                    //ONLY RUN ONE TIME.
                    if (currentTick == 0) {
                        //GREEN
                        trafficJunctionBox.doLight(TrafficLightState.GREEN);
                        trafficJunctionBoxMap.entrySet().stream().filter(key -> key.getKey() != currentJunctionBox).forEach((k -> k.getValue().doLight(TrafficLightState.RED)));
                    }
                } else if (currentTick <= 15) {
                    //YELLOW
                    //ONLY RUN ONE TIME
                    if (currentTick == 11) {
                        trafficJunctionBox.doLight(TrafficLightState.YELLOW);
                    } else if (trafficSystemType.hasTurningLane() && currentTick == 15) {
                        trafficJunctionBox.doLight(TrafficLightState.RED);
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
                    trafficJunctionBox.doLight(TrafficLightState.RED);

                    if (currentJunctionBox == 0) currentJunctionBox = 1;
                    else if (currentJunctionBox == 1) currentJunctionBox = 0;

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

    public Map<Integer, TrafficJunctionBox> getTrafficJunctionBoxMap() {
        return trafficJunctionBoxMap;
    }

    public boolean isTicking() {
        return isTicking;
    }

    public int getCurrentTick() {
        return currentTick;
    }

    public int getCurrentJunctionBox() {
        return currentJunctionBox;
    }

    public void stop() {
        this.stop = true;
    }
}