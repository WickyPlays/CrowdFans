package me.wickyplays.spigot.crowdfans.commands;

import me.wickyplays.spigot.crowdfans.CrowdManager;
import me.wickyplays.spigot.crowdfans.MsgUtils;
import me.wickyplays.spigot.crowdfans.obj.Crowd;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class JumpCommand extends ACommand {

    public JumpCommand() {
        setName("jump");
        setDescription("Cause the crowd to jump in specific second");
        setPermission("crowdfans.jump");
    }

    @Override
    public void execute(CommandSender sender, List<String> args) {
        if (!(sender instanceof Player)) {
            MsgUtils.sendMessage(sender, "&cThis command is for player only!");
            return;
        }

        Player player = (Player) sender;
        if (args.size() != 2) {
            MsgUtils.sendMessage(sender, "&cCorrect command: /fans jump <crowd_name> <second>");
            return;
        }

        String crowdName = args.get(0);
        Crowd crowd = CrowdManager.get().getCrowdByName(crowdName);
        if (crowd == null) {
            MsgUtils.sendMessage(sender, "&cUnknown crowd. Please check again.");
            return;
        }

        int second;
        try {
            second = Integer.parseInt(args.get(1));
        } catch (NumberFormatException nfe) {
            MsgUtils.sendMessage(player, "&cThe second specified should be a number!");
            return;
        }

        crowd.jump(second);
        MsgUtils.sendMessage(sender, "&aSuccess! The crowd &e" + crowdName + "&a will be jumping in &e" + second + "s&a!");
    }
}
