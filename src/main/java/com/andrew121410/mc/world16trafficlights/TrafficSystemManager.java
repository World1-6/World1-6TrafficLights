package com.andrew121410.mc.world16trafficlights;

import com.andrew121410.mc.world16utils.config.CustomYmlManager;
import org.bukkit.configuration.ConfigurationSection;

import java.util.Map;

public class TrafficSystemManager {

    private Map<String, TrafficSystem> trafficSystemMap;

    private Main plugin;
    private CustomYmlManager trafficLightYML;

    public TrafficSystemManager(Main plugin) {
        this.plugin = plugin;

        this.trafficSystemMap = this.plugin.getSetListMap().getTrafficSystemMap();

        this.trafficLightYML = new CustomYmlManager(this.plugin, false);
        this.trafficLightYML.setup("trafficlights.yml");
        this.trafficLightYML.saveConfig();
        this.trafficLightYML.reloadConfig();
    }

    public void loadAll() {
        ConfigurationSection mCS = trafficLightYML.getConfig().getConfigurationSection("TrafficSystems");
        if (mCS == null) {
            trafficLightYML.getConfig().createSection("TrafficSystems");
            trafficLightYML.saveConfig();
            return; //DON'T LOAD
        }

        for (String key : mCS.getKeys(false)) {
            ConfigurationSection trafficSystem = mCS.getConfigurationSection(key);
            ConfigurationSection trafficLightSystems = trafficSystem.getConfigurationSection("TrafficLightSystems");

            TrafficSystem trafficSystemObject = (TrafficSystem) trafficSystem.get("TrafficSystem");

            for (String trafficLightSystemsKey : trafficLightSystems.getKeys(false)) {
                ConfigurationSection trafficLightSystem = trafficLightSystems.getConfigurationSection(trafficLightSystemsKey);
                ConfigurationSection lights = trafficLightSystem.getConfigurationSection("Lights");

                TrafficLightSystem trafficLightSystemObject = (TrafficLightSystem) trafficLightSystem.get("TrafficLightSystem");

                for (String lightsKey : lights.getKeys(false)) {
                    TrafficLight trafficLight = (TrafficLight) lights.get(lightsKey);
                    trafficLightSystemObject.getTrafficLightMap().put(Integer.valueOf(lightsKey), trafficLight);
                }
                trafficSystemObject.getTrafficLightSystemMap().put(Integer.valueOf(trafficLightSystemsKey), trafficLightSystemObject);
            }
            trafficSystemObject.tick();
            this.trafficSystemMap.put(key, trafficSystemObject);
        }
    }

    public void saveAll() {
        ConfigurationSection mCS = trafficLightYML.getConfig().getConfigurationSection("TrafficSystems");
        if (mCS == null) {
            mCS = trafficLightYML.getConfig().createSection("TrafficSystems");
            trafficLightYML.saveConfig();
        }

        for (Map.Entry<String, TrafficSystem> entry : trafficSystemMap.entrySet()) {
            String k = entry.getKey().toLowerCase();
            TrafficSystem v = entry.getValue();

            ConfigurationSection trafficSystem = mCS.getConfigurationSection(k);
            if (trafficSystem == null) {
                trafficSystem = mCS.createSection(k);
                this.trafficLightYML.saveConfig();
            }

            trafficSystem.set("TrafficSystem", v);

            ConfigurationSection trafficLightSystems = trafficSystem.getConfigurationSection("TrafficLightSystems");
            if (trafficLightSystems == null) {
                trafficLightSystems = trafficSystem.createSection("TrafficLightSystems");
                this.trafficLightYML.saveConfig();
            }
            for (Map.Entry<Integer, TrafficLightSystem> e : v.getTrafficLightSystemMap().entrySet()) {
                Integer key = e.getKey();
                TrafficLightSystem value = e.getValue();

                ConfigurationSection trafficLightSystem = trafficLightSystems.getConfigurationSection(String.valueOf(key));
                if (trafficLightSystem == null) {
                    trafficLightSystem = trafficLightSystems.createSection(String.valueOf(key));
                    this.trafficLightYML.saveConfig();
                }
                trafficLightSystem.set("TrafficLightSystem", value);

                ConfigurationSection lights = trafficLightSystem.getConfigurationSection("Lights");
                if (lights == null) {
                    lights = trafficLightSystem.createSection("Lights");
                    this.trafficLightYML.saveConfig();
                }
                for (Map.Entry<Integer, TrafficLight> mapEntry : value.getTrafficLightMap().entrySet()) {
                    Integer integer = mapEntry.getKey();
                    TrafficLight trafficLight = mapEntry.getValue();
                    lights.set(String.valueOf(integer), trafficLight);
                }
            }
            this.trafficLightYML.saveConfig();
        }
    }

    public void deleteSystem(String key) {
        this.trafficSystemMap.get(key.toLowerCase()).stop();
        this.trafficSystemMap.remove(key.toLowerCase());

        ConfigurationSection mCS = trafficLightYML.getConfig().getConfigurationSection("TrafficSystems");
        if (mCS == null) return;

        mCS.set(key.toLowerCase(), null);
        this.trafficLightYML.saveConfig();
    }

    public void deleteJunction(String key, String junctionKey) {
        this.trafficSystemMap.get(key.toLowerCase()).getTrafficLightSystemMap().remove(Integer.valueOf(junctionKey));

        ConfigurationSection mCS = trafficLightYML.getConfig().getConfigurationSection("TrafficSystems");
        if (mCS == null) return;

        ConfigurationSection trafficLightSystems = mCS.getConfigurationSection(key.toLowerCase() + ".TrafficLightSystems");
        if (trafficLightSystems == null) return;

        trafficLightSystems.set(junctionKey, null);
        this.trafficLightYML.saveConfig();
    }

    public void deleteLight(String key, String junctionKey, String lightKey) {
        this.trafficSystemMap.get(key.toLowerCase()).getTrafficLightSystemMap().get(Integer.valueOf(junctionKey)).getTrafficLightMap().remove(Integer.valueOf(lightKey));

        ConfigurationSection mCS = trafficLightYML.getConfig().getConfigurationSection("TrafficSystems");
        if (mCS == null) return;

        ConfigurationSection trafficLights = mCS.getConfigurationSection(key.toLowerCase() + ".TrafficLightSystems." + junctionKey);
        if (trafficLights == null) return;

        trafficLights.set(lightKey, null);
        this.trafficLightYML.saveConfig();
    }
}