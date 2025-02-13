package com.andrew121410.mc.world16trafficlights.storage.serializers;

import com.andrew121410.mc.world16trafficlights.TrafficLight;
import com.andrew121410.mc.world16utils.config.serializers.SerializerUtils;
import com.andrew121410.mc.world16utils.dependencies.spongepowered.configurate.ConfigurationNode;
import com.andrew121410.mc.world16utils.dependencies.spongepowered.configurate.serialize.SerializationException;
import com.andrew121410.mc.world16utils.dependencies.spongepowered.configurate.serialize.TypeSerializer;
import org.bukkit.Location;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.lang.reflect.Type;

public class TrafficLightSerializer implements TypeSerializer<TrafficLight> {
    @Override
    public TrafficLight deserialize(Type type, ConfigurationNode node) throws SerializationException {
        if (node.raw() == null) {
            return null;
        }

        Location location = SerializerUtils.nonVirtualNode(node, "Location").get(Location.class);
        Boolean isLeft = SerializerUtils.nonVirtualNode(node, "IsLeft").get(Boolean.class);

        return new TrafficLight(location, isLeft);
    }

    @Override
    public void serialize(Type type, @Nullable TrafficLight obj, ConfigurationNode node) throws SerializationException {
        if (obj == null) {
            node.raw(null);
            return;
        }

        node.node("Location").set(obj.getLocation());
        node.node("IsLeft").set(obj.isLeft());
    }
}
