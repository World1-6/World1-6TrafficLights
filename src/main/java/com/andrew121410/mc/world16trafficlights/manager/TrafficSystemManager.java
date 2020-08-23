package com.andrew121410.mc.world16trafficlights.manager;

import com.andrew121410.mc.world16trafficlights.World16TrafficLights;
import com.andrew121410.mc.world16trafficlights.objects.TrafficSystem;
import com.andrew121410.mc.world16utils.config.CustomYmlManager;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;

import java.util.Map;

public class TrafficSystemManager {

    private Map<String, TrafficSystem> trafficSystemMap;
    private Map<Location, String> chunkToTrafficSystemName;

    private World16TrafficLights plugin;
    private CustomYmlManager trafficLightYML;

    public TrafficSystemManager(World16TrafficLights plugin) {
        this.plugin = plugin;

        this.trafficSystemMap = this.plugin.getSetListMap().getTrafficSystemMap();
        this.chunkToTrafficSystemName = this.plugin.getSetListMap().getChunkToTrafficSystemName();

        this.trafficLightYML = new CustomYmlManager(this.plugin, false);
        this.trafficLightYML.setup("trafficlights.yml");
        this.trafficLightYML.saveConfig();
        this.trafficLightYML.reloadConfig();

        ConfigurationSection cs = this.trafficLightYML.getConfig().getConfigurationSection("TrafficLights");
        if (cs == null) {
            this.trafficLightYML.getConfig().createSection("TrafficLights");
        }
    }

    private TrafficSystem load(String key) {
        ConfigurationSection cs = this.trafficLightYML.getConfig().getConfigurationSection("TrafficLights");
        return (TrafficSystem) cs.get(key);
    }

    private void save(TrafficSystem trafficSystem) {
        ConfigurationSection cs = this.trafficLightYML.getConfig().getConfigurationSection("TrafficLights");
        cs.set(trafficSystem.getName(), trafficSystem);
        this.trafficLightYML.saveConfig();
    }

    public void loadAll() {
        ConfigurationSection trafficLightsSection = this.trafficLightYML.getConfig().getConfigurationSection("TrafficLights");
        for (String key : trafficLightsSection.getKeys(false)) {
            TrafficSystem trafficSystem = load(key);
            if (this.plugin.isChunkSmartManagement())
                this.chunkToTrafficSystemName.put(trafficSystem.getMainChunk(), key);
            else this.trafficSystemMap.put(key, trafficSystem);
        }
    }

    public void saveAll() {
        this.trafficSystemMap.values().forEach(this::save);
    }

    public void saveAndUnloadTrafficSystem(String key) {
        TrafficSystem trafficSystem = this.trafficSystemMap.get(key);
        if (trafficSystem == null) {
            throw new NullPointerException("TrafficSystem is null saveAndUnloadTrafficSystem");
        }
        trafficSystem.stop();
        save(trafficSystem);
        this.trafficSystemMap.remove(key);
    }

    public void loadTrafficSystem(String key) {
        TrafficSystem trafficSystem = load(key);
        trafficSystem.tick();
        this.trafficSystemMap.putIfAbsent(trafficSystem.getName(), trafficSystem);
    }

    public void deleteTrafficSystem(String key) {
        ConfigurationSection trafficLightsSection = this.trafficLightYML.getConfig().getConfigurationSection("TrafficLights");
        trafficLightsSection.set(key, null);
        this.trafficLightYML.saveConfig();
        TrafficSystem trafficSystem = this.trafficSystemMap.get(key);
        trafficSystem.stop();
        if (trafficSystem.getMainChunk() != null) {
            this.chunkToTrafficSystemName.remove(trafficSystem.getMainChunk());
        }
        this.trafficSystemMap.remove(key);
    }
}