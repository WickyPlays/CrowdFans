package me.wickyplays.spigot.crowdfans.tasks;

import me.wickyplays.spigot.crowdfans.Utils;
import org.bukkit.entity.LivingEntity;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.List;

public class EntityJumpTask extends BukkitRunnable {
    private final List<LivingEntity> entitiesToJump;
    private final long duration;
    private long ticksElapsed = 0;

    public EntityJumpTask(List<LivingEntity> entitiesToJump, long duration) {
        this.entitiesToJump = entitiesToJump;
        this.duration = duration;
    }

    @Override
    public void run() {
        if (ticksElapsed >= duration) {
            this.cancel();
            return;
        }

        for (LivingEntity entity : entitiesToJump) {
            if (!entity.isOnGround()) continue;
            double jumpY = Utils.randomDouble(0.1, 0.5);
            entity.setVelocity(entity.getVelocity().add(new Vector(0, jumpY, 0)));
        }

        ticksElapsed += 1;
    }
}