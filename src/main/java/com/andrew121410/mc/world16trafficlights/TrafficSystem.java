package com.andrew121410.mc.world16trafficlights;


import com.andrew121410.mc.world16trafficlights.enums.TrafficLightState;
import org.bukkit.Location;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.Map;

public class TrafficSystem {

    private final World16TrafficLights plugin;

    //Saving
    private final String name;
    private final Location mainChunk;
    private final Map<Integer, TrafficJunctionBox> trafficJunctionBoxMap;

    private boolean isTicking;
    private int currentTick;
    private int currentJunctionBox;
    private boolean stop;

    public TrafficSystem(World16TrafficLights plugin, String name, Location mainChunk, Map<Integer, TrafficJunctionBox> trafficJunctionBoxMap) {
        this.plugin = plugin;
        this.name = name.toLowerCase();
        this.mainChunk = mainChunk;
        this.trafficJunctionBoxMap = trafficJunctionBoxMap;

        this.isTicking = false;
        this.currentTick = 0;
        this.currentJunctionBox = 0;
        this.stop = false;
    }

    public TrafficSystem(World16TrafficLights plugin, String name, Location mainChunk) {
        this(plugin, name, mainChunk, new HashMap<>());
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

                if (trafficJunctionBox == null) {
                    World16TrafficLights.getInstance().getLogger().warning("TrafficJunctionBox is null for TrafficSystem: " + name);
                    return;
                }

                // Get the left-turning lights.
                Map<Integer, TrafficLight> leftTurningLights = new HashMap<>();
                for (Map.Entry<Integer, TrafficLight> entry : trafficJunctionBox.getTrafficLightMap().entrySet()) {
                    if (entry.getValue().isLeft()) {
                        leftTurningLights.put(entry.getKey(), entry.getValue());
                    }
                }

                if (currentTick <= 10) {
                    if (currentTick == 0) {
                        // Turn all non-left-turn lights to green.
                        trafficJunctionBox.getTrafficLightMap().values().stream()
                                .filter(light -> !light.isLeft())
                                .forEach(light -> light.doLight(TrafficLightState.GREEN));

                        // Start blinking yellow for left-turn lights
                        leftTurningLights.values().forEach(TrafficLight::turn_left);

                        // Set all other junction boxes to red.
                        trafficJunctionBoxMap.entrySet().stream()
                                .filter(entry -> entry.getKey() != currentJunctionBox)
                                .forEach(entry -> entry.getValue().doLight(TrafficLightState.RED));
                    } else {
                        // Continue blinking yellow for left-turn lights
                        if (currentTick % 2 == 0) {
                            leftTurningLights.values().forEach(TrafficLight::off);
                        } else {
                            leftTurningLights.values().forEach(TrafficLight::turn_left);
                        }
                    }
                } else if (currentTick <= 15) {
                    // Set all lights to solid yellow
                    trafficJunctionBox.getTrafficLightMap().values().forEach(TrafficLight::yellow);
                } else if (currentTick <= 17) {
                    // Set all lights to red
                    trafficJunctionBox.getTrafficLightMap().values().forEach(TrafficLight::red);
                } else {
                    // Switch to the next junction box should be either 0 or 1
                    currentJunctionBox = currentJunctionBox == 0 ? 1 : 0;
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