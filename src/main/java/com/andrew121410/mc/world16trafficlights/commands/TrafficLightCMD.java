package com.andrew121410.mc.world16trafficlights.commands;

import com.andrew121410.mc.world16trafficlights.*;
import com.andrew121410.mc.world16trafficlights.tabcomplete.TrafficLightTab;
import com.andrew121410.mc.world16utils.Utils;
import com.andrew121410.mc.world16utils.chat.Translate;
import com.andrew121410.mc.world16utils.player.PlayerUtils;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Map;

public class TrafficLightCMD implements CommandExecutor {

    private Map<String, TrafficSystem> trafficSystemMap;

    private Main plugin;

    private Utils utils;

    public TrafficLightCMD(Main plugin) {
        this.plugin = plugin;
        this.utils = new Utils();

        this.trafficSystemMap = this.plugin.getSetListMap().getTrafficSystemMap();

        this.plugin.getCommand("trafficlight").setExecutor(this);
        this.plugin.getCommand("trafficlight").setTabCompleter(new TrafficLightTab(this.plugin));
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Only Players Can Use This Command.");
            return true;
        }
        Player p = (Player) sender;

        if (!p.hasPermission("world16.trafficlight")) {
            p.sendMessage(Translate.chat("&bYou don't have permission to use this command."));
            return true;
        }

        if (args.length == 0) {
            p.sendMessage(Translate.chat("&6/trafficlight create [SHOWS HELP TO CREATE"));
            p.sendMessage(Translate.chat("&6/trafficlight delete [SHOWS HELP TO DELETE"));
            p.sendMessage(Translate.chat("&6/trafficlight tick <Name>"));
            return true;
        } else if (args[0].equalsIgnoreCase("create")) {
            if (args.length == 1) {
                p.sendMessage(Translate.chat("&6/trafficlight create system <Name> <Type>"));
                p.sendMessage(Translate.chat("&6/trafficlight create junction <Name> <Int> <isTurningJunction"));
                p.sendMessage(Translate.chat("&6/trafficlight create light <Name> <Junction> <Int> <O isLeft"));
                return true;
            } else if (args[1].equalsIgnoreCase("system")) {
                String name = args[2].toLowerCase();
                String rawType = args[3];

                TrafficSystemType trafficSystemType;
                try {
                    trafficSystemType = TrafficSystemType.valueOf(rawType);
                } catch (Exception e) {
                    p.sendMessage(Translate.chat("Not a valid TrafficSystemType"));
                    return true;
                }

                if (this.trafficSystemMap.get(name) != null) {
                    p.sendMessage(Translate.chat("Looks like that traffic light system already exists with that name."));
                    return true;
                }

                this.trafficSystemMap.put(name, new TrafficSystem(plugin, trafficSystemType));
                p.sendMessage(Translate.chat(name + " traffic system has been added."));
                return true;
            } else if (args[1].equalsIgnoreCase("junction")) {
                String name = args[2].toLowerCase();
                int key = utils.asIntOrDefault(args[3], 0);
                boolean isturningJunction = utils.asBooleanOrDefault(args[4], false);

                if (this.trafficSystemMap.get(name) == null) {
                    p.sendMessage(Translate.chat("Looks like that isn't a valid traffic system"));
                    return true;
                }

                this.trafficSystemMap.get(name).getTrafficLightSystemMap().putIfAbsent(key, new TrafficLightSystem(plugin, isturningJunction));
                p.sendMessage(Translate.chat("Junction box has been added to: " + name));
                return true;
            } else if (args[1].equalsIgnoreCase("light")) {
                Block block = PlayerUtils.getBlockPlayerIsLookingAt(p);
                String name = args[2].toLowerCase();
                int junctionName = utils.asIntOrDefault(args[3], 0);
                int number = utils.asIntOrDefault(args[4], 0);
                Boolean isLeft = Boolean.valueOf(args[5]);

                if (this.trafficSystemMap.get(name) == null) {
                    p.sendMessage(Translate.chat("Looks like that isn't a valid traffic system"));
                    return true;
                }

                if (this.trafficSystemMap.get(name).getTrafficLightSystemMap().get(junctionName) == null) {
                    p.sendMessage(Translate.chat("Looks like that isn't a valid junction"));
                    return true;
                }

                this.trafficSystemMap.get(name).getTrafficLightSystemMap().get(junctionName).getTrafficLightMap().put(number, new TrafficLight(block.getLocation(), isLeft));
                p.sendMessage(Translate.chat("The traffic light has been added to junction: " + junctionName + " to traffic system: " + name));
                return true;
            }
        } else if (args[0].equalsIgnoreCase("delete")) {
            if (args.length == 1) {
                p.sendMessage(Translate.chat("&6/trafficlight delete system <Name>"));
                p.sendMessage(Translate.chat("&6/trafficlight delete junction <Name> <Junction>"));
                p.sendMessage(Translate.chat("&6/trafficlight delete light <Name> <Junction> <INT>"));
            } else if (args[1].equalsIgnoreCase("system") && args.length == 3) {
                String name = args[2].toLowerCase();

                if (this.trafficSystemMap.get(name) == null) {
                    p.sendMessage(Translate.chat("Looks like that isn't a valid system."));
                    return true;
                }

                this.plugin.getTrafficSystemManager().deleteSystem(name);
                p.sendMessage(Translate.chat("Traffic system has been deleted."));
                return true;
            } else if (args[1].equalsIgnoreCase("junction") && args.length == 4) {
                String name = args[2].toLowerCase();
                String junctionKey = args[3];

                if (this.trafficSystemMap.get(name) == null) {
                    p.sendMessage(Translate.chat("Looks like that isn't a valid system."));
                    return true;
                }

                if (this.trafficSystemMap.get(name).getTrafficLightSystemMap().get(Integer.valueOf(junctionKey)) == null) {
                    p.sendMessage(Translate.chat("Looks like that isn't a vaild junction."));
                    return true;
                }

                this.plugin.getTrafficSystemManager().deleteJunction(name, junctionKey);
                p.sendMessage(Translate.chat("The junction has been deleted for: " + name));
            } else if (args[1].equalsIgnoreCase("light") && args.length == 5) {
                String name = args[2].toLowerCase();
                String junctionKey = args[3];
                String lightKey = args[4];

                if (this.trafficSystemMap.get(name) == null) {
                    p.sendMessage(Translate.chat("Looks like that isn't a valid system."));
                    return true;
                }

                if (this.trafficSystemMap.get(name).getTrafficLightSystemMap().get(Integer.valueOf(junctionKey)) == null) {
                    p.sendMessage(Translate.chat("Looks like that isn't a vaild junction."));
                    return true;
                }

                if (this.trafficSystemMap.get(name).getTrafficLightSystemMap().get(Integer.valueOf(junctionKey)).getTrafficLightMap().get(Integer.valueOf(lightKey)) == null) {
                    p.sendMessage(Translate.chat("Looks like that isn't a vaild light"));
                    return true;
                }

                this.plugin.getTrafficSystemManager().deleteLight(name, junctionKey, lightKey);
                p.sendMessage(Translate.chat("The light has been deleted."));
            }
        } else if (args[0].equalsIgnoreCase("tick")) {
            String name = args[1].toLowerCase();

            if (this.trafficSystemMap.get(name) == null) {
                p.sendMessage(Translate.chat("Looks like that isn't a valid traffic system"));
                return true;
            }

            this.trafficSystemMap.get(name).tick();
            p.sendMessage(Translate.chat(name + " has started ticking"));
            return true;
        }
        return true;
    }
}