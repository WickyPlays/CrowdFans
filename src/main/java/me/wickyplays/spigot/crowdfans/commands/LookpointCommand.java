package me.wickyplays.spigot.crowdfans.commands;

import me.wickyplays.spigot.crowdfans.CrowdManager;
import me.wickyplays.spigot.crowdfans.MsgUtils;
import me.wickyplays.spigot.crowdfans.obj.Crowd;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.io.IOException;
import java.util.List;

public class LookpointCommand extends ACommand {

    public LookpointCommand() {
        setName("lookpoint");
        setDescription("Makes all audience look at the same location set.");
        setPermission("crowdfans.lookpoint");
    }

    @Override
    public void execute(CommandSender sender, List<String> args) {
        if (!(sender instanceof Player)) {
            MsgUtils.sendMessage(sender, "&cThis command is for player only!");
            return;
        }

        Player player = (Player) sender;
        if (args.size() != 1) {
            MsgUtils.sendMessage(sender, "&cCorrect command: /fans lookpoint <crowd_name>");
            return;
        }

        String crowdName = args.get(0);
        Crowd crowd = CrowdManager.get().getCrowdByName(crowdName);
        if (crowd == null) {
            MsgUtils.sendMessage(sender, "&cUnknown crowd. Please check again.");
            return;
        }

        if (!crowd.getWorld().getName().equals(player.getWorld().getName())) {
            MsgUtils.sendMessage(sender, "&cError: This command only works if you are at the world where the crowd is at.");
            return;
        }

        crowd.setLookpointLoc(player.getLocation());
        crowd.lookpoint();
        try {
            CrowdManager.get().saveCrowdToFile();
        } catch (IOException e) {}
        MsgUtils.sendMessage(sender, "&aSuccess! The audience will now look toward the location you have set.");
    }
}
