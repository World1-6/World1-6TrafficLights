package com.andrew121410.mc.world16trafficlights;

import com.andrew121410.mc.world16trafficlights.commands.TrafficLightCMD;
import com.andrew121410.mc.world16trafficlights.manager.TrafficSystemChunkSmartManager;
import com.andrew121410.mc.world16trafficlights.manager.TrafficSystemManager;
import com.andrew121410.mc.world16trafficlights.utils.SetListMap;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.plugin.java.JavaPlugin;

public final class World16TrafficLights extends JavaPlugin {

    static {
        ConfigurationSerialization.registerClass(TrafficLight.class, "TrafficLight");
        ConfigurationSerialization.registerClass(TrafficLightSystem.class, "TrafficLightSystem");
        ConfigurationSerialization.registerClass(TrafficSystem.class, "TrafficSystem");
    }

    private static World16TrafficLights instance;

    private SetListMap setListMap;

    private TrafficSystemManager trafficSystemManager;

    private boolean chunkSmartManagement = false;

    @Override
    public void onEnable() {
        instance = this;
        this.setListMap = new SetListMap();
        regDefaultConfig();

        //Config
        this.chunkSmartManagement = this.getConfig().getBoolean("ChunkSmartManagement");

        this.trafficSystemManager = new TrafficSystemManager(this);
        this.trafficSystemManager.loadAll();

        if (chunkSmartManagement) {
            this.getServer().getScheduler().scheduleSyncRepeatingTask(this, new TrafficSystemChunkSmartManager(this), 200L, 200L);
        }

        regCommands();
    }

    @Override
    public void onDisable() {
        this.trafficSystemManager.saveAll();
    }

    public void regCommands() {
        new TrafficLightCMD(this);
    }

    public void regDefaultConfig() {
        this.getConfig().options().copyDefaults(true);
        this.saveConfig();
        this.reloadConfig();
    }

    public static World16TrafficLights getInstance() {
        return instance;
    }

    public SetListMap getSetListMap() {
        return setListMap;
    }

    public TrafficSystemManager getTrafficSystemManager() {
        return trafficSystemManager;
    }

    public boolean isChunkSmartManagement() {
        return chunkSmartManagement;
    }
}
