package me.wickyplays.spigot.crowdfans;

import com.sk89q.worldedit.bukkit.WorldEditPlugin;
import me.wickyplays.spigot.crowdfans.obj.Crowd;
import me.wickyplays.spigot.crowdfans.tasks.AudienceTask;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandExecutor;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.ArrayList;

public class CrowdFansPlugin extends JavaPlugin implements CommandExecutor {

    private static CrowdFansPlugin plugin;
    private WorldEditPlugin worldEdit;

    @Override
    public void onEnable() {
        worldEdit = (WorldEditPlugin) getServer().getPluginManager().getPlugin("WorldEdit");
        if (worldEdit == null) {
            getLogger().warning("WorldEdit not found. Disabling your plugin.");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        plugin = this;

        CrowdManager.get().loadCrowdFromFile();
        CrowdManager.get().loadCommands();

        // Register the /fans command
        getCommand("fans").setExecutor(CrowdManager.get());

        new AudienceTask().runTaskTimer(plugin, 0L, 1L);
        MsgUtils.sendConsole("&7[&a&lCrowdFans&7] &eCrowdFans enabled successfully!");
    }

    @Override
    public void onDisable() {
        for (Crowd crowd : CrowdManager.get().getCrowds()) {
            crowd.spawnAudience(0, new ArrayList<>());
        }
    }

    public WorldEditPlugin getWorldEdit() {
        return worldEdit;
    }

    public static CrowdFansPlugin get() {
        return plugin;
    }
}
