package me.wickyplays.spigot.crowdfans.commands;

import me.wickyplays.spigot.crowdfans.obj.Crowd;
import me.wickyplays.spigot.crowdfans.CrowdManager;
import me.wickyplays.spigot.crowdfans.MsgUtils;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class AudienceCommand extends ACommand {

    public AudienceCommand() {
        setName("audience");
        setDescription("Spawn audiences to the crowd");
        setPermission("crowdfans.audience");
    }

    @Override
    public void execute(CommandSender sender, List<String> args) {
        if (!(sender instanceof Player)) {
            MsgUtils.sendMessage(sender, "&cThis command is for player only!");
            return;
        }

        Player player = (Player) sender;
        if (args.size() < 2) {
            MsgUtils.sendMessage(sender, "&cCorrect command: /fans audience <crowd_name> <amount> (<block>)");
            return;
        }

        String crowdName = args.get(0);
        Crowd crowd = CrowdManager.get().getCrowdByName(crowdName);
        if (crowd == null) {
            MsgUtils.sendMessage(sender, "&cUnknown crowd. Please check again.");
            return;
        }

        List<Block> blocks = new ArrayList<>(crowd.getBlocks().stream().toList());
        if (args.size() >= 3) {
            Material mat = Material.getMaterial(args.get(2).toUpperCase());
            if (mat == null) {
                MsgUtils.sendMessage(sender, "&cYou attempted to use material as filter, but this material is invalid!");
                return;
            }

            blocks = blocks.stream().filter(p -> p.getType().equals(mat)).collect(Collectors.toList());
        }
        String input = args.get(1);
        int audience;

        try {
            if (input.endsWith("%")) {
                int percentage = Integer.parseInt(input.replace("%", ""));
                double finalAudience = ((double) percentage / 100) * blocks.size();
                audience = (int) finalAudience;
            } else {
                audience = Integer.parseInt(input);
            }
        } catch (NumberFormatException nfe) {
            MsgUtils.sendMessage(player, "&cAmount should be a number or a percentage!");
            return;
        }

        if (audience > blocks.size()) {
            MsgUtils.sendMessage(player, "&cWarning! The audience amount should not be greater than total area of blocks!");
            return;
        }

        crowd.spawnAudience(audience, blocks);
        MsgUtils.sendMessage(sender, "&e" + audience + " &aaudience(s) spawned!");
    }
}
