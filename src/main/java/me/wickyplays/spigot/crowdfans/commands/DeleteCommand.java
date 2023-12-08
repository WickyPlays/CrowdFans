package me.wickyplays.spigot.crowdfans.commands;

import me.wickyplays.spigot.crowdfans.CrowdFansPlugin;
import me.wickyplays.spigot.crowdfans.CrowdManager;
import me.wickyplays.spigot.crowdfans.MsgUtils;
import me.wickyplays.spigot.crowdfans.obj.Crowd;
import org.bukkit.command.CommandSender;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class DeleteCommand extends ACommand {

    private final CrowdFansPlugin plugin;

    public DeleteCommand() {
        setName("delete");
        setPermission("crowdfans.delete");
        setDescription("Delete crowd");
        this.plugin = CrowdFansPlugin.get();
    }

    @Override
    public void execute(CommandSender sender, List<String> args) {

        if (args.size() != 1) {
            MsgUtils.sendMessage(sender, "&cCorrect usage: /fans delete <crowd_name>");
            return;
        }

        String crowdName = args.get(0);
        Crowd crowd = CrowdManager.get().getCrowdByName(crowdName);
        if (crowd == null) {
            MsgUtils.sendMessage(sender, "&cUnknown crowd. Please check again.");
            return;
        }

        crowd.spawnAudience(0, new ArrayList<>());
        CrowdManager.get().removeCrowd(crowd);
        try {
            CrowdManager.get().saveCrowdToFile();
        } catch (IOException e) {}
        MsgUtils.sendMessage(sender, "&aCrowd &e" + crowdName + " &ahas been deleted!");
    }
}
