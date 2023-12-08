package me.wickyplays.spigot.crowdfans;

import me.wickyplays.spigot.crowdfans.commands.*;
import me.wickyplays.spigot.crowdfans.obj.Crowd;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class CrowdManager implements CommandExecutor {

    private static CrowdManager instance;
    private final List<Crowd> crowds = new ArrayList<>();
    private File configFile;
    private final CrowdFansPlugin plugin = CrowdFansPlugin.get();
    private YamlConfiguration crowdsConfig;
    private final Set<ACommand> cmds = new HashSet<>();

    public static CrowdManager get() {
        if (instance == null) {
            instance = new CrowdManager();
        }

        return instance;
    }

    public void addRegion(String name, World world, Location l1, Location l2) {
        Crowd crowd = getCrowdByName(name);
        if (crowd == null) {
            crowd = new Crowd(name, world);
            crowds.add(crowd);
        }

        crowd.addPoint(l1, l2);
    }

    public void removeCrowd(String name) {
        Crowd crowd = crowds.stream().filter(p -> p.getName().equals(name)).findFirst().orElse(null);
        removeCrowd(crowd);
    }

    public void removeCrowd(Crowd crowd) {
        if (crowd == null) {
            return;
        }
        crowds.remove(crowd);
    }

    public Crowd getCrowdByName(String crowdName) {
        return crowds.stream().filter(p -> p.getName().equals(crowdName)).findFirst().orElse(null);
    }

    public List<Crowd> getCrowds() {
        return crowds;
    }

    public void loadCrowdFromFile() {

        configFile = new File(plugin.getDataFolder(), "crowds.yml");
        if (!configFile.exists()) {
            plugin.saveResource("crowds.yml", false);
        }
        crowdsConfig = YamlConfiguration.loadConfiguration(configFile);

        ConfigurationSection cs = crowdsConfig.getConfigurationSection("crowds");
        if (cs == null) {
            return;
        }

        Set<String> crowdList = cs.getKeys(false);
        for (String crowdName : crowdList) {
            String worldName = crowdsConfig.getString("crowds." + crowdName + ".world");
            if (worldName == null) continue;
            World world = Bukkit.getWorld(worldName);
            if (world == null) {
                continue;
            }

            Crowd crowd = new Crowd(crowdName, world);
            for (String locStr : crowdsConfig.getStringList("crowds." + crowdName + ".points")) {
                String[] locSplit = locStr.split(",");
                if (locSplit.length != 6) continue;
                try {
                    int x1 = Integer.parseInt(locSplit[0]);
                    int y1 = Integer.parseInt(locSplit[1]);
                    int z1 = Integer.parseInt(locSplit[2]);
                    int x2 = Integer.parseInt(locSplit[3]);
                    int y2 = Integer.parseInt(locSplit[4]);
                    int z2 = Integer.parseInt(locSplit[5]);

                    crowd.addPoint(new Location(world, x1, y1, z1), new Location(world, x2, y2, z2));
                } catch (NumberFormatException nfe) {}
            }

            String lookPoint = crowdsConfig.getString("crowds." + crowdName + ".lookpoint");
            if (lookPoint != null) {
                String[] lpSplit = lookPoint.split(",");
                if (lpSplit.length == 3) {
                    try {
                        crowd.setLookpointLoc(new Location(
                                world,
                                Double.parseDouble(lpSplit[0]),
                                Double.parseDouble(lpSplit[1]),
                                Double.parseDouble(lpSplit[2])));
                        crowd.lookpoint();
                    } catch (Exception ignored) {}
                }
            }

            crowds.add(crowd);
        }
    }

    public void saveCrowdToFile() throws IOException {
        crowdsConfig.set("crowds", new ArrayList<>());
        for (Crowd crowd : crowds) {
            String name = crowd.getName();
            World world = crowd.getWorld();
            if (world == null) {
                continue;
            }
            String worldName = world.getName();
            List<String> points = crowd.getPoints().stream()
                    .map(p ->
                            p.getPoint1().getBlockX() + "," +
                                    p.getPoint1().getBlockY() + "," +
                                    p.getPoint1().getBlockZ() + "," +
                                    p.getPoint2().getBlockX() + "," +
                                    p.getPoint2().getBlockY() + "," +
                                    p.getPoint2().getBlockZ())
                    .toList();

            crowdsConfig.set("crowds." + name + ".world", worldName);
            crowdsConfig.set("crowds." + name + ".points", points);

            Location lookpoint = crowd.getLookpoint();
            if (lookpoint != null) {
                crowdsConfig.set("crowds." + name + ".lookpoint", lookpoint.getBlockX() + "," + lookpoint.getBlockY() + "," + lookpoint.getBlockZ());
            }

        }

        crowdsConfig.save(configFile);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {

        if (args.length == 0) {
            MsgUtils.sendMessage(sender, "&7---------------------------");
            for (ACommand cmd : cmds) {
                MsgUtils.sendMessage(sender, "&e/fans " + cmd.getName() + " &f" + cmd.getDescription());
            }
            MsgUtils.sendMessage(sender, "&7---------------------------");
            return true;
        }

        ACommand ac = cmds.stream().filter(p -> p.getName().equals(args[0])).findFirst().orElse(null);
        if (ac == null) {
            MsgUtils.sendMessage(sender, "&cUnknown command. Please use command '/fans' for help");
            return true;
        }

        if (ac.getPermission() != null) {
            if (!sender.hasPermission(ac.getPermission()) && !sender.hasPermission("*") && !sender.isOp()) {
                MsgUtils.sendMessage(sender, "&cYou don't have permission to use this command!");
                return true;
            }
        }

        ac.execute(sender, Arrays.stream(args).skip(1).collect(Collectors.toList()));
        return true;
    }

    public void loadCommands() {
        cmds.add(new AddCommand());
        cmds.add(new AudienceCommand());
        cmds.add(new LookpointCommand());
        cmds.add(new ListCommand());
        cmds.add(new DeleteCommand());
        cmds.add(new JumpCommand());
    }
}
