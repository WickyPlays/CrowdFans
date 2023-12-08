package me.wickyplays.spigot.crowdfans.tasks;

import me.wickyplays.spigot.crowdfans.CrowdManager;
import me.wickyplays.spigot.crowdfans.obj.Crowd;
import org.bukkit.scheduler.BukkitRunnable;

public class AudienceTask extends BukkitRunnable {

    @Override
    public void run() {
        for (Crowd crowd : CrowdManager.get().getCrowds()) {
            crowd.lookpoint();
        }
    }
}