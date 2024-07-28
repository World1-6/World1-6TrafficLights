package com.andrew121410.mc.world16trafficlights.storage.serializers;

import com.andrew121410.mc.world16trafficlights.TrafficJunctionBox;
import com.andrew121410.mc.world16trafficlights.TrafficLight;
import com.andrew121410.mc.world16trafficlights.World16TrafficLights;
import com.andrew121410.mc.world16utils.config.serializers.SerializerUtils;
import com.andrew121410.mc.world16utils.utils.spongepowered.configurate.ConfigurationNode;
import com.andrew121410.mc.world16utils.utils.spongepowered.configurate.serialize.SerializationException;
import com.andrew121410.mc.world16utils.utils.spongepowered.configurate.serialize.TypeSerializer;
import io.leangen.geantyref.TypeToken;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.lang.reflect.Type;
import java.util.Map;

public class TrafficJunctionBoxSerializer implements TypeSerializer<TrafficJunctionBox> {
    @Override
    public TrafficJunctionBox deserialize(Type type, ConfigurationNode node) throws SerializationException {
        if (node.raw() == null) {
            return null;
        }

        Boolean isTurningJunction = SerializerUtils.nonVirtualNode(node, "IsTurningJunction").get(Boolean.class);
        TypeToken<Map<Integer, TrafficLight>> trafficLightMapTypeToken = new TypeToken<>() {
        };
        Map<Integer, TrafficLight> trafficLightMap = SerializerUtils.nonVirtualNode(node, "TrafficLightMap").get(trafficLightMapTypeToken);

        return new TrafficJunctionBox(World16TrafficLights.getInstance(), isTurningJunction, trafficLightMap);
    }

    @Override
    public void serialize(Type type, @Nullable TrafficJunctionBox obj, ConfigurationNode node) throws SerializationException {
        if (obj == null) {
            node.raw(null);
            return;
        }

        node.node("IsTurningJunction").set(obj.isTurningJunction());
        TypeToken<Map<Integer, TrafficLight>> trafficLightMapTypeToken = new TypeToken<>() {
        };
        node.node("TrafficLightMap").set(trafficLightMapTypeToken, obj.getTrafficLightMap());
    }
}
