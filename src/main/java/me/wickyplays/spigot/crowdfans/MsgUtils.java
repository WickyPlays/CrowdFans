package me.wickyplays.spigot.crowdfans;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class MsgUtils {

    private static final String VOLLEYBALL_TAG = "";

    public static String colorize(String msg) {
        return ChatColor.translateAlternateColorCodes('&', msg);
    }

    public static void log(String msg) {
        Bukkit.getConsoleSender().sendMessage(colorize(VOLLEYBALL_TAG + msg));
    }

    public static void broadcastAll(String msg) {
        broadcastAll(msg, true);
    }

    public static void broadcastAll(String msg, boolean colorized) {
        if (colorized) {
            msg = colorize(msg);
        }

        Bukkit.broadcastMessage(msg);
    }

    public static void sendMessage(Player player, String msg) {
        player.sendMessage(colorize(msg));
    }

    public static void sendMessage(CommandSender sender, String msg) {
        sender.sendMessage(colorize(msg));
    }

    public static void sendConsole(String msg) {
        Bukkit.getConsoleSender().sendMessage(colorize(msg));
    }
}
