package com.andrew121410.mc.world16trafficlights.commands;

import com.andrew121410.mc.world16trafficlights.TrafficJunctionBox;
import com.andrew121410.mc.world16trafficlights.TrafficLight;
import com.andrew121410.mc.world16trafficlights.TrafficSystem;
import com.andrew121410.mc.world16trafficlights.World16TrafficLights;
import com.andrew121410.mc.world16trafficlights.enums.TrafficSystemType;
import com.andrew121410.mc.world16trafficlights.tabcomplete.TrafficLightTab;
import com.andrew121410.mc.world16utils.chat.Translate;
import com.andrew121410.mc.world16utils.player.PlayerUtils;
import com.andrew121410.mc.world16utils.utils.Utils;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Map;

public class TrafficLightCMD implements CommandExecutor {

    private final Map<String, TrafficSystem> trafficSystemMap;

    private final World16TrafficLights plugin;

    private final Utils utils;

    public TrafficLightCMD(World16TrafficLights plugin) {
        this.plugin = plugin;
        this.utils = new Utils();

        this.trafficSystemMap = this.plugin.getSetListMap().getTrafficSystemMap();

        this.plugin.getCommand("trafficlight").setExecutor(this);
        this.plugin.getCommand("trafficlight").setTabCompleter(new TrafficLightTab(this.plugin));
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("Only Players Can Use This Command.");
            return true;
        }

        if (!player.hasPermission("world16.trafficlight")) {
            player.sendMessage(Translate.chat("&bYou don't have permission to use this command."));
            return true;
        }

        if (args.length == 0) {
            player.sendMessage(Translate.chat("&6/trafficlight create &r- Shows help to create."));
            player.sendMessage(Translate.chat("&6/trafficlight delete &r- Shows help to delete."));
            return true;
        } else if (args[0].equalsIgnoreCase("create")) {
            if (args.length == 1) {
                player.sendMessage(Translate.chat("&6/trafficlight create system <Name> <Type>"));
                player.sendMessage(Translate.chat("&6/trafficlight create junction <Name> <Int> <isTurningJunction"));
                player.sendMessage(Translate.chat("&6/trafficlight create light <Name> <Junction> <Int> <O isLeft"));
                return true;
            } else if (args[1].equalsIgnoreCase("system")) {
                if (args.length != 4) {
                    player.sendMessage(Translate.chat("&6/trafficlight create system <Name> <Type>"));
                    return true;
                }

                String name = args[2].toLowerCase();
                String rawType = args[3];

                TrafficSystemType trafficSystemType;
                try {
                    trafficSystemType = TrafficSystemType.valueOf(rawType);
                } catch (Exception e) {
                    player.sendMessage(Translate.chat("Not a valid TrafficSystemType"));
                    return true;
                }

                if (this.trafficSystemMap.containsKey(name)) {
                    player.sendMessage(Translate.chat("Looks like that traffic light system already exists with that name."));
                    return true;
                }

                Location location = player.getLocation().clone();
                Location chunkLoc = new Location(location.getWorld(), location.getChunk().getX(), 0, location.getChunk().getZ());
                this.trafficSystemMap.put(name, new TrafficSystem(this.plugin, name, chunkLoc, trafficSystemType));
                player.sendMessage(Translate.chat(name + " traffic system has been added."));
                return true;
            } else if (args[1].equalsIgnoreCase("junction")) {
                if (args.length != 5) {
                    player.sendMessage(Translate.chat("&6/trafficlight create junction <Name> <Int> <isTurningJunction"));
                    return true;
                }

                String name = args[2].toLowerCase();
                int key = Utils.asIntegerOrElse(args[3], 0);
                boolean isTurningJunction = Utils.asBooleanOrElse(args[4], false);

                TrafficSystem trafficSystem = this.trafficSystemMap.get(name);
                if (trafficSystem == null) {
                    player.sendMessage(Translate.chat("Looks like that isn't a valid traffic system"));
                    return true;
                }

                trafficSystem.getTrafficJunctionBoxMap().putIfAbsent(key, new TrafficJunctionBox(plugin, isTurningJunction));
                player.sendMessage(Translate.chat("Junction box has been added to: " + name));
                return true;
            } else if (args[1].equalsIgnoreCase("light")) {
                if (args.length != 6) {
                    player.sendMessage(Translate.chat("&6/trafficlight create light <Name> <Junction> <Int> <IsLeft>"));
                    return true;
                }

                Block block = PlayerUtils.getBlockPlayerIsLookingAt(player);
                String name = args[2].toLowerCase();
                int junctionName = Utils.asIntegerOrElse(args[3], 0);
                int number = Utils.asIntegerOrElse(args[4], 0);
                Boolean isLeft = Utils.asBooleanOrElse(args[5], false);

                if (this.trafficSystemMap.get(name) == null) {
                    player.sendMessage(Translate.chat("Looks like that isn't a valid traffic system"));
                    return true;
                }

                if (this.trafficSystemMap.get(name).getTrafficJunctionBoxMap().get(junctionName) == null) {
                    player.sendMessage(Translate.chat("Looks like that isn't a valid junction"));
                    return true;
                }

                this.trafficSystemMap.get(name).getTrafficJunctionBoxMap().get(junctionName).getTrafficLightMap().put(number, new TrafficLight(block.getLocation(), isLeft));
                player.sendMessage(Translate.chat("The traffic light has been added to junction: " + junctionName + " to traffic system: " + name));
                return true;
            }
        } else if (args[0].equalsIgnoreCase("delete")) {
            if (args.length == 1) {
                player.sendMessage(Translate.chat("&6/trafficlight delete system <Name>"));
                player.sendMessage(Translate.chat("&6/trafficlight delete junction <Name> <Junction>"));
                player.sendMessage(Translate.chat("&6/trafficlight delete light <Name> <Junction> <INT>"));
            } else if (args[1].equalsIgnoreCase("system") && args.length == 3) {
                String name = args[2].toLowerCase();

                if (this.trafficSystemMap.get(name) == null) {
                    player.sendMessage(Translate.chat("Looks like that isn't a valid system."));
                    return true;
                }

                this.plugin.getTrafficSystemManager().deleteTrafficSystem(name);
                player.sendMessage(Translate.chat("Traffic system has been deleted."));
                return true;
            } else if (args[1].equalsIgnoreCase("junction") && args.length == 4) {
                String name = args[2].toLowerCase();
                String junctionKey = args[3];

                TrafficSystem trafficSystem = this.trafficSystemMap.get(name);
                if (trafficSystem == null) {
                    player.sendMessage(Translate.chat("Looks like that isn't a valid system."));
                    return true;
                }

                if (trafficSystem.getTrafficJunctionBoxMap().get(Integer.valueOf(junctionKey)) == null) {
                    player.sendMessage(Translate.chat("Looks like that isn't a vaild junction."));
                    return true;
                }

                trafficSystem.getTrafficJunctionBoxMap().remove(Integer.valueOf(junctionKey));
                player.sendMessage(Translate.chat("The junction has been deleted for: " + name));
            } else if (args[1].equalsIgnoreCase("light") && args.length == 5) {
                String name = args[2].toLowerCase();
                String junctionKey = args[3];
                String lightKey = args[4];

                TrafficSystem trafficSystem = this.trafficSystemMap.get(name);
                if (trafficSystem == null) {
                    player.sendMessage(Translate.chat("Looks like that isn't a valid system."));
                    return true;
                }

                if (trafficSystem.getTrafficJunctionBoxMap().get(Integer.valueOf(junctionKey)) == null) {
                    player.sendMessage(Translate.chat("Looks like that isn't a vaild junction."));
                    return true;
                }

                if (trafficSystem.getTrafficJunctionBoxMap().get(Integer.valueOf(junctionKey)).getTrafficLightMap().get(Integer.valueOf(lightKey)) == null) {
                    player.sendMessage(Translate.chat("Looks like that isn't a vaild light"));
                    return true;
                }

                trafficSystem.getTrafficJunctionBoxMap().get(Integer.valueOf(junctionKey)).getTrafficLightMap().remove(Integer.valueOf(lightKey));
                player.sendMessage(Translate.chat("The light has been deleted."));
            }
        } else if (args[0].equalsIgnoreCase("tick")) {
            if (args.length != 2) {
                player.sendMessage(Translate.chat("&6/trafficlight tick <Name>"));
                return true;
            }

            String name = args[1].toLowerCase();

            TrafficSystem trafficSystem = this.trafficSystemMap.get(name);
            if (trafficSystem == null) {
                player.sendMessage(Translate.chat("Looks like that isn't a valid traffic system"));
                return true;
            }

            trafficSystem.tick();
            player.sendMessage(Translate.chat(name + " has started ticking"));
            return true;
        }
        return true;
    }
}