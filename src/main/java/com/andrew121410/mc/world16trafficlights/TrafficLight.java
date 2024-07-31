package com.andrew121410.mc.world16trafficlights;

import com.andrew121410.mc.world16trafficlights.enums.TrafficLightState;
import org.bukkit.DyeColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Banner;
import org.bukkit.block.banner.Pattern;
import org.bukkit.block.banner.PatternType;

import java.util.ArrayList;
import java.util.List;

// https://mcutils.com/banner-creator
public class TrafficLight {

    private final Location location;
    private Boolean isLeft;

    public TrafficLight(Location location) {
        this.location = location;
    }

    public TrafficLight(Location location, boolean isLeft) {
        this.location = location;
        this.isLeft = isLeft;
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
            patterns.add(new Pattern(DyeColor.YELLOW, PatternType.CIRCLE));
            patterns.add(new Pattern(DyeColor.BLACK, PatternType.BORDER));
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

    public boolean turn_left() {
        if (isBanner()) {
            Banner banner = (Banner) location.getBlock().getState();
            banner.setBaseColor(DyeColor.BLACK);
            List<Pattern> patterns = new ArrayList<>();

            patterns.add(new Pattern(DyeColor.BLACK, PatternType.BASE)); // This is still needed
            patterns.add(new Pattern(DyeColor.YELLOW, PatternType.RHOMBUS));
            patterns.add(new Pattern(DyeColor.YELLOW, PatternType.STRIPE_RIGHT));
            patterns.add(new Pattern(DyeColor.BLACK, PatternType.SQUARE_TOP_RIGHT));
            patterns.add(new Pattern(DyeColor.BLACK, PatternType.SQUARE_BOTTOM_RIGHT));
            patterns.add(new Pattern(DyeColor.BLACK, PatternType.BORDER));

            banner.setPatterns(patterns);
            banner.update();
        } else {
            yellow(); // Use yellow as default for non-banner blocks
        }

        return true;
    }

    //TODO add turn_right
    public boolean turn_right() {
        return yellow();
    }

    public boolean off() {
        if (isBanner()) {
            Banner banner = (Banner) location.getBlock().getState();
            banner.setBaseColor(DyeColor.BLACK);

            // Make a blank banner
            List<Pattern> patterns = new ArrayList<>();
            patterns.add(new Pattern(DyeColor.BLACK, PatternType.BASE));
            banner.setPatterns(patterns);
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
}