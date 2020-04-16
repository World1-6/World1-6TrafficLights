package com.andrew121410.mc.world16trafficlights;

import com.andrew121410.mc.world16trafficlights.commands.TrafficLightCMD;
import com.andrew121410.mc.world16trafficlights.utils.SetListMap;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.plugin.java.JavaPlugin;

public final class Main extends JavaPlugin {

    static {
        ConfigurationSerialization.registerClass(TrafficSystem.class, "TrafficSystem");
        ConfigurationSerialization.registerClass(TrafficLightSystem.class, "TrafficLightSystem");
        ConfigurationSerialization.registerClass(TrafficLight.class, "TrafficLight");
    }

    private static Main instance;

    private SetListMap setListMap;

    private TrafficSystemManager trafficSystemManager;

    @Override
    public void onEnable() {
        instance = this;
        this.setListMap = new SetListMap();

        this.trafficSystemManager = new TrafficSystemManager(this);
        this.trafficSystemManager.loadAll();
    }

    @Override
    public void onDisable() {
        this.trafficSystemManager.saveAll();
    }

    public void regCommands() {
        new TrafficLightCMD(this);
    }

    public static Main getInstance() {
        return instance;
    }

    public SetListMap getSetListMap() {
        return setListMap;
    }

    public TrafficSystemManager getTrafficSystemManager() {
        return trafficSystemManager;
    }
}
