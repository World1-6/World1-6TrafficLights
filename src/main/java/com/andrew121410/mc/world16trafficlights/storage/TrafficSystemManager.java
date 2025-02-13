package com.andrew121410.mc.world16trafficlights.storage;

import com.andrew121410.mc.world16trafficlights.TrafficJunctionBox;
import com.andrew121410.mc.world16trafficlights.TrafficLight;
import com.andrew121410.mc.world16trafficlights.TrafficSystem;
import com.andrew121410.mc.world16trafficlights.World16TrafficLights;
import com.andrew121410.mc.world16trafficlights.storage.serializers.TrafficJunctionBoxSerializer;
import com.andrew121410.mc.world16trafficlights.storage.serializers.TrafficLightSerializer;
import com.andrew121410.mc.world16trafficlights.storage.serializers.TrafficSystemSerializer;
import com.andrew121410.mc.world16utils.config.World16ConfigurateManager;
import com.andrew121410.mc.world16utils.dependencies.spongepowered.configurate.CommentedConfigurationNode;
import com.andrew121410.mc.world16utils.dependencies.spongepowered.configurate.serialize.TypeSerializerCollection;
import com.andrew121410.mc.world16utils.dependencies.spongepowered.configurate.yaml.YamlConfigurationLoader;
import lombok.SneakyThrows;
import org.bukkit.Location;

import java.util.Map;

public class TrafficSystemManager {

    private final Map<String, TrafficSystem> trafficSystemMap;
    private final Map<Location, String> chunkToTrafficSystemName;

    private final World16TrafficLights plugin;
    private final YamlConfigurationLoader trafficLightYML;

    public TrafficSystemManager(World16TrafficLights plugin) {
        this.plugin = plugin;

        this.trafficSystemMap = this.plugin.getSetListMap().getTrafficSystemMap();
        this.chunkToTrafficSystemName = this.plugin.getSetListMap().getChunkToTrafficSystemName();

        World16ConfigurateManager world16ConfigurateManager = new World16ConfigurateManager(this.plugin);
        world16ConfigurateManager.registerTypeSerializerCollection(getOurSerializers());
        this.trafficLightYML = world16ConfigurateManager.getYamlConfigurationLoader("trafficlights.yml");
    }

    private TypeSerializerCollection getOurSerializers() {
        TypeSerializerCollection.Builder ourSerializers = TypeSerializerCollection.builder();

        ourSerializers.registerExact(TrafficLight.class, new TrafficLightSerializer());
        ourSerializers.registerExact(TrafficJunctionBox.class, new TrafficJunctionBoxSerializer());
        ourSerializers.registerExact(TrafficSystem.class, new TrafficSystemSerializer());

        return ourSerializers.build();
    }

    @SneakyThrows
    private TrafficSystem load(String key) {
        CommentedConfigurationNode node = this.trafficLightYML.load().node("TrafficSystems");
        return node.node(key).get(TrafficSystem.class);
    }

    @SneakyThrows
    private void save(TrafficSystem trafficSystem) {
        CommentedConfigurationNode node = this.trafficLightYML.load().node("TrafficSystems");
        node.node(trafficSystem.getName()).set(trafficSystem);
        this.trafficLightYML.save(node);
    }

    @SneakyThrows
    public void loadAll() {
        CommentedConfigurationNode node = this.trafficLightYML.load().node("TrafficSystems");

        for (Map.Entry<Object, CommentedConfigurationNode> objectCommentedConfigurationNodeEntry : node.childrenMap().entrySet()) {
            String key = (String) objectCommentedConfigurationNodeEntry.getKey();
            TrafficSystem trafficSystem = objectCommentedConfigurationNodeEntry.getValue().get(TrafficSystem.class);

            if (this.plugin.isChunkSmartManagement())
                this.chunkToTrafficSystemName.put(trafficSystem.getMainChunk(), key);
            else this.trafficSystemMap.put(key, trafficSystem);
        }
    }

    @SneakyThrows
    public void saveAll() {
        CommentedConfigurationNode node = this.trafficLightYML.load();

        for (Map.Entry<String, TrafficSystem> mapEntry : this.trafficSystemMap.entrySet()) {
            String key = mapEntry.getKey();
            TrafficSystem trafficSystem = mapEntry.getValue();
            node.node("TrafficSystems", key).set(trafficSystem);
            this.trafficLightYML.save(node);
        }
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

    @SneakyThrows
    public void deleteTrafficSystem(String key) {
        TrafficSystem trafficSystem = this.trafficSystemMap.get(key);

        // Stop ticking
        trafficSystem.stop();

        // Remove from the chunk smart management map.
        if (trafficSystem.getMainChunk() != null) {
            this.chunkToTrafficSystemName.remove(trafficSystem.getMainChunk());
        }

        // Remove from the elevator controller map.
        this.trafficSystemMap.remove(key);

        // Remove from the config.
        CommentedConfigurationNode node = this.trafficLightYML.load().node("TrafficSystems");
        node.removeChild(key);
        this.trafficLightYML.save(node);
    }
}