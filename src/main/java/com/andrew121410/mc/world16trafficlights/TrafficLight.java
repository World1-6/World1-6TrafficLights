package com.andrew121410.mc.world16trafficlights;

import com.andrew121410.mc.world16trafficlights.enums.TrafficLightState;
import org.bukkit.DyeColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Banner;
import org.bukkit.block.banner.Pattern;
import org.bukkit.block.banner.PatternType;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.configuration.serialization.SerializableAs;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SerializableAs("TrafficLight")
public class TrafficLight implements ConfigurationSerializable {

    private final Location location;
    private Boolean isLeft;

    public TrafficLight(Location location) {
        this.location = location;
    }

    public TrafficLight(Location location, boolean isLeft) {
        this.location = location;
        this.isLeft = isLeft;
    }

    public static TrafficLight deserialize(Map<String, Object> map) {
        return new TrafficLight((Location) map.get("Location"), (Boolean) map.get("IsLeft"));
    }

    public boolean doLight(TrafficLightState trafficLightState) {
        switch (trafficLightState) {
            case GREEN:
                return green();
            case YELLOW:
                return yellow();
            case RED:
                return red();
            case TURN:
                if (isLeft) return turn_left();
                else return turn_right();
        }
        return false;
    }

    public boolean isBanner() {
        return location.getBlock().getState() instanceof Banner;
    }

    public boolean green() {
        if (isBanner()) {
            Banner banner = (Banner) location.getBlock().getState();
            banner.setBaseColor(DyeColor.BLACK);
            List<Pattern> patterns = new ArrayList<>();
            patterns.add(new Pattern(DyeColor.LIME, PatternType.STRIPE_BOTTOM));
            patterns.add(new Pattern(DyeColor.BLACK, PatternType.BORDER));
            banner.setPatterns(patterns);
            banner.update();
        } else {
            location.getBlock().setType(Material.LIME_TERRACOTTA);
        }
        return true;
    }

    public boolean yellow() {
        if (isBanner()) {
            Banner banner = (Banner) location.getBlock().getState();
            List<Pattern> patterns = new ArrayList<>();
            patterns.add(new Pattern(DyeColor.YELLOW, PatternType.CIRCLE_MIDDLE));
            banner.setPatterns(patterns);
            banner.update();
        } else {
            location.getBlock().setType(Material.YELLOW_TERRACOTTA);
        }
        return true;
    }

    public boolean red() {
        if (isBanner()) {
            Banner banner = (Banner) location.getBlock().getState();
            banner.setBaseColor(DyeColor.BLACK);
            List<Pattern> patterns = new ArrayList<>();
            patterns.add(new Pattern(DyeColor.RED, PatternType.STRIPE_TOP));
            patterns.add(new Pattern(DyeColor.BLACK, PatternType.BORDER));
            banner.setPatterns(patterns);
            banner.update();
        } else {
            location.getBlock().setType(Material.RED_TERRACOTTA);
        }
        return true;
    }

    //TODO add turn_left
    public boolean turn_left() {
        return yellow();
    }

    //TODO add turn_right
    public boolean turn_right() {
        return yellow();
    }

    public boolean off() {
        if (isBanner()) {
            Banner banner = (Banner) location.getBlock().getState();
            banner.setBaseColor(DyeColor.BLACK);
            for (int i = 0; i < banner.getPatterns().size(); i++) {
                banner.removePattern(i);
            }
            banner.update();
        } else {
            location.getBlock().setType(Material.BLACK_CONCRETE);
        }
        return true;
    }

    public Location getLocation() {
        return location;
    }

    public Boolean isLeft() {
        return isLeft;
    }

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> map = new HashMap<>();
        map.put("Location", this.location);
        map.put("IsLeft", isLeft);
        return map;
    }
}