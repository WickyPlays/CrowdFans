package me.wickyplays.spigot.crowdfans.commands;

import me.wickyplays.spigot.crowdfans.CrowdManager;
import me.wickyplays.spigot.crowdfans.MsgUtils;
import me.wickyplays.spigot.crowdfans.obj.Crowd;
import org.bukkit.command.CommandSender;

import java.util.List;
import java.util.stream.Collectors;

public class ListCommand extends ACommand {

    public ListCommand() {
        setName("list");
        setDescription("List all crowds available");
        setPermission("crowdfans.list");
    }

    @Override
    public void execute(CommandSender sender, List<String> args) {
        List<Crowd> crowdList = CrowdManager.get().getCrowds();
        MsgUtils.sendMessage(sender, "");
        MsgUtils.sendMessage(sender, "&aThere are " + crowdList.size() + " crowds added:");
        MsgUtils.sendMessage(sender,
                crowdList.stream().map(p -> "&e" + p.getName() + " &7(" + p.getBlocks().size() + ")").collect(Collectors.joining("&7, ")));
        MsgUtils.sendMessage(sender, "");
    }
}
