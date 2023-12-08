package me.wickyplays.spigot.crowdfans.commands;

import com.sk89q.worldedit.IncompleteRegionException;
import com.sk89q.worldedit.LocalSession;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.regions.Region;
import me.wickyplays.spigot.crowdfans.CrowdFansPlugin;
import me.wickyplays.spigot.crowdfans.CrowdManager;
import me.wickyplays.spigot.crowdfans.MsgUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.io.IOException;
import java.util.List;

public class AddCommand extends ACommand {

    private final CrowdFansPlugin plugin;

    public AddCommand() {
        setName("add");
        setPermission("crowdfans.add");
        setDescription("Add an area to your crowd");
        this.plugin = CrowdFansPlugin.get();
    }

    @Override
    public void execute(CommandSender sender, List<String> args) {
        if (!(sender instanceof Player)) {
            MsgUtils.sendMessage(sender, "&cThis command is for player only!");
            return;
        }

        if (args.size() != 1) {
            MsgUtils.sendMessage(sender, "&cCorrect usage: /fans add <crowd_name>");
            return;
        }

        String crowdName = args.get(0);

        Player player = (Player) sender;
        LocalSession session = plugin.getWorldEdit().getSession(player);
        if (session.getSelectionWorld() == null) {
            MsgUtils.sendMessage(sender, "&cTo use this command, player must first set WorldEdit selection.");
            return;
        }
        World world = Bukkit.getWorld(session.getSelectionWorld().getName());
        if (world == null) {
            MsgUtils.sendMessage(player, "&cSomething is wrong with the adding region procedure (possibly of world deletion).");
            return;
        }

        try {
            Region selection = session.getSelection();
            BlockVector3 point1 = selection.getMinimumPoint();
            BlockVector3 point2 = selection.getMaximumPoint();
            Location l1 = new Location(world, point1.getBlockX(), point1.getBlockY(), point1.getBlockZ());
            Location l2 = new Location(world, point2.getBlockX(), point2.getBlockY(), point2.getBlockZ());
            CrowdManager.get().addRegion(crowdName, world, l1, l2);
            try {
                CrowdManager.get().saveCrowdToFile();
            } catch (IOException e) {}
            MsgUtils.sendMessage(player, "&aAdding points successfully to &e" + crowdName + "!");
        } catch (IncompleteRegionException e) {
            MsgUtils.sendMessage(sender, "&cTo use this command, player must first set WorldEdit selection.");
        }

    }
}
