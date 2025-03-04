package com.andrew121410.mc.world16trafficlights.storage.serializers;

import com.andrew121410.mc.world16trafficlights.TrafficJunctionBox;
import com.andrew121410.mc.world16trafficlights.TrafficSystem;
import com.andrew121410.mc.world16trafficlights.World16TrafficLights;
import com.andrew121410.mc.world16utils.config.serializers.SerializerUtils;
import com.andrew121410.mc.world16utils.dependencies.spongepowered.configurate.ConfigurationNode;
import com.andrew121410.mc.world16utils.dependencies.spongepowered.configurate.serialize.SerializationException;
import com.andrew121410.mc.world16utils.dependencies.spongepowered.configurate.serialize.TypeSerializer;
import io.leangen.geantyref.TypeToken;
import org.bukkit.Location;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.lang.reflect.Type;
import java.util.Map;

public class TrafficSystemSerializer implements TypeSerializer<TrafficSystem> {
    @Override
    public TrafficSystem deserialize(Type type, ConfigurationNode node) throws SerializationException {
        if (node.raw() == null) {
            return null;
        }

        String name = SerializerUtils.nonVirtualNode(node, "Name").getString();
        Location mainChunk = SerializerUtils.nonVirtualNode(node, "MainChunk").get(Location.class);
        TypeToken<Map<Integer, TrafficJunctionBox>> junctionBoxMapTypeToken = new TypeToken<>() {
        };
        Map<Integer, TrafficJunctionBox> trafficJunctionBoxMap = SerializerUtils.nonVirtualNode(node, "TrafficJunctionBoxMap").get(junctionBoxMapTypeToken);

        if (name == null || mainChunk == null || trafficJunctionBoxMap == null) {
            throw new SerializationException("Failed to deserialize TrafficSystem.");
        }

        return new TrafficSystem(World16TrafficLights.getInstance(), name, mainChunk, trafficJunctionBoxMap);
    }

    @Override
    public void serialize(Type type, @Nullable TrafficSystem obj, ConfigurationNode node) throws SerializationException {
        if (obj == null) {
            node.raw(null);
            return;
        }

        node.node("Name").set(obj.getName());
        node.node("MainChunk").set(obj.getMainChunk());

        TypeToken<Map<Integer, TrafficJunctionBox>> elevatorMapTypeToken = new TypeToken<>() {
        };
        node.node("TrafficJunctionBoxMap").set(elevatorMapTypeToken, obj.getTrafficJunctionBoxMap());
    }
}
