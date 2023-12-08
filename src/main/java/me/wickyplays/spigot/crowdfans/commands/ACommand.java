package me.wickyplays.spigot.crowdfans.commands;

import org.bukkit.command.CommandSender;

import java.util.List;

public abstract class ACommand {

    private String name;
    private String description;
    private String permission = null;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPermission() {
        return permission;
    }

    public void setPermission(String permission) {
        this.permission = permission;
    }

    public abstract void execute(CommandSender sender, List<String> args);
}
