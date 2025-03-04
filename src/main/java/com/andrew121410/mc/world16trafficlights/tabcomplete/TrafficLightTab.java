package com.andrew121410.mc.world16trafficlights.tabcomplete;

import com.andrew121410.mc.world16trafficlights.TrafficSystem;
import com.andrew121410.mc.world16trafficlights.World16TrafficLights;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class TrafficLightTab implements TabCompleter {

    private final Map<String, TrafficSystem> trafficSystemMap;
    private final List<String> tabCompleteMap;

    private final World16TrafficLights plugin;

    public TrafficLightTab(World16TrafficLights plugin) {
        this.plugin = plugin;

        this.trafficSystemMap = this.plugin.getSetListMap().getTrafficSystemMap();

        this.tabCompleteMap = new ArrayList<>();

        tabCompleteMap.add("create");
        tabCompleteMap.add("delete");
        tabCompleteMap.add("tick");
    }

    public static List<String> getContainsString(String args, List<String> oldArrayList) {
        List<String> list = new ArrayList<>();

        for (String mat : oldArrayList) {
            if (mat.contains(args.toLowerCase())) {
                list.add(mat);
            }
        }

        return list;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String ailis, String[] args) {
        if (!(sender instanceof Player p)) {
            return null;
        }

        if (!cmd.getName().equalsIgnoreCase("trafficlight") || !p.hasPermission("world16.trafficlight")) {
            return null;
        }

        List<String> trafficSystemsList = new ArrayList<>(this.trafficSystemMap.keySet());

        if (args.length == 1) {
            return getContainsString(args[0], tabCompleteMap);
        } else if (args[0].equalsIgnoreCase("create")) {
            if (args.length == 2) {
                return getContainsString(args[1], Arrays.asList("system", "junction", "light"));
            } else if (args.length == 3 && !args[1].equalsIgnoreCase("system")) {
                return getContainsString(args[2], trafficSystemsList);
            } else if (args.length == 4) {
                List<String> junctionNames = new ArrayList<>();
                trafficSystemMap.get(args[2]).getTrafficJunctionBoxMap().forEach((k, v) -> junctionNames.add(String.valueOf(k)));
                return getContainsString(args[3], junctionNames);
            } else if (args.length == 5 && !args[1].equalsIgnoreCase("light") && !args[1].equalsIgnoreCase("junction")) {
                return getContainsString(args[4], Arrays.asList("true", "false", "null"));
            } else if (args.length == 6 && args[1].equalsIgnoreCase("light")) {
                return getContainsString(args[5], Arrays.asList("true", "false", "null"));
            }
        } else if (args[0].equalsIgnoreCase("delete")) {
            if (args.length == 2) {
                return getContainsString(args[1], Arrays.asList("system", "junction", "light"));
            } else if (args.length == 3) {
                return getContainsString(args[2], trafficSystemsList);
            } else if (args.length == 4) {
                List<String> junctionNames = new ArrayList<>();
                trafficSystemMap.get(args[2]).getTrafficJunctionBoxMap().forEach((k, v) -> junctionNames.add(String.valueOf(k)));
                return getContainsString(args[3], junctionNames);
            } else if (args.length == 5) {
                List<String> lights = new ArrayList<>();
                trafficSystemMap.get(args[2]).getTrafficJunctionBoxMap().get(Integer.valueOf(args[3])).getTrafficLightMap().forEach((k, v) -> lights.add(String.valueOf(k)));
                return getContainsString(args[4], lights);
            }
        } else if (args[0].equalsIgnoreCase("tick")) {
            if (args.length == 2) {
                return getContainsString(args[1], trafficSystemsList);
            }
        }
        return null;
    }
}
