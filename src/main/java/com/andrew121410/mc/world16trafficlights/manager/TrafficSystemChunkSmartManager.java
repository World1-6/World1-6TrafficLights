package com.andrew121410.mc.world16trafficlights.manager;

import com.andrew121410.mc.world16trafficlights.World16TrafficLights;
import com.andrew121410.mc.world16trafficlights.objects.TrafficSystem;
import org.bukkit.Location;

import java.util.Iterator;
import java.util.Map;

public class TrafficSystemChunkSmartManager implements Runnable {

    private Map<String, TrafficSystem> trafficSystemMap;
    private Map<Location, String> chunkToTrafficSystemName;

    private World16TrafficLights plugin;

    public TrafficSystemChunkSmartManager(World16TrafficLights plugin) {
        this.plugin = plugin;
        this.trafficSystemMap = this.plugin.getSetListMap().getTrafficSystemMap();
        this.chunkToTrafficSystemName = this.plugin.getSetListMap().getChunkToTrafficSystemName();
    }

    @Override
    public void run() {
        Iterator<Map.Entry<Location, String>> iterator = this.chunkToTrafficSystemName.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<Location, String> entry = iterator.next();
            Location location = entry.getKey();
            String trafficSystemName = entry.getValue();
            boolean isChunkLoaded = location.getWorld().isChunkLoaded(location.getBlockX(), location.getBlockZ());
            if (isChunkLoaded && !this.trafficSystemMap.containsKey(trafficSystemName)) {
                this.plugin.getTrafficSystemManager().loadTrafficSystem(trafficSystemName);
            } else if (!isChunkLoaded && this.trafficSystemMap.containsKey(trafficSystemName)) {
                TrafficSystem trafficSystem = this.trafficSystemMap.get(trafficSystemName);
                trafficSystem.stop();
                this.plugin.getTrafficSystemManager().saveAndUnloadTrafficSystem(trafficSystemName);
            }
        }
    }
}
