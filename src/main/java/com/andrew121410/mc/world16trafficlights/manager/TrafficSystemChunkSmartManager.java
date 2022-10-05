package com.andrew121410.mc.world16trafficlights.manager;

import com.andrew121410.mc.world16trafficlights.TrafficSystem;
import com.andrew121410.mc.world16trafficlights.World16TrafficLights;
import org.bukkit.Location;

import java.util.Map;

public class TrafficSystemChunkSmartManager implements Runnable {

    private final Map<String, TrafficSystem> trafficSystemMap;
    private final Map<Location, String> chunkToTrafficSystemName;

    private final World16TrafficLights plugin;

    public TrafficSystemChunkSmartManager(World16TrafficLights plugin) {
        this.plugin = plugin;
        this.trafficSystemMap = this.plugin.getSetListMap().getTrafficSystemMap();
        this.chunkToTrafficSystemName = this.plugin.getSetListMap().getChunkToTrafficSystemName();
    }

    @Override
    public void run() {
        for (Map.Entry<Location, String> entry : this.chunkToTrafficSystemName.entrySet()) {
            Location location = entry.getKey();
            String trafficSystemName = entry.getValue();
            boolean isChunkLoaded = location.getWorld().isChunkLoaded(location.getBlockX(), location.getBlockZ());
            if (isChunkLoaded && !this.trafficSystemMap.containsKey(trafficSystemName)) {
                this.plugin.getTrafficSystemManager().loadTrafficSystem(trafficSystemName);
            } else if (!isChunkLoaded && this.trafficSystemMap.containsKey(trafficSystemName)) {
                this.plugin.getTrafficSystemManager().saveAndUnloadTrafficSystem(trafficSystemName);
            }
        }
    }
}
