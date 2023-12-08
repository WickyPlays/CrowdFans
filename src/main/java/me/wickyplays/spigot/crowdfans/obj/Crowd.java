package me.wickyplays.spigot.crowdfans.obj;

import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.regions.CuboidRegion;
import me.wickyplays.spigot.crowdfans.CrowdFansPlugin;
import me.wickyplays.spigot.crowdfans.Utils;
import me.wickyplays.spigot.crowdfans.tasks.EntityJumpTask;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.attribute.Attribute;
import org.bukkit.block.Block;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Vector;

import java.util.*;

public class Crowd {

    private final String name;
    private final World world;
    private List<CrowdPoint> points;
    private List<LivingEntity> entities;
    private Location lookpointLoc = null;
    private BukkitTask task;

    public Crowd(String name, World world) {
        this.name = name;
        this.world = world;
        points = new ArrayList<>();
        entities = new ArrayList<>();
    }

    public void addPoint(Location l1, Location l2) {
        points.add(new CrowdPoint(l1, l2));
    }

    public Set<Block> getBlocks() {
        Set<Block> locs = new HashSet<>();
        for (CrowdPoint point : points) {
            Location point1 = point.getPoint1();
            Location point2 = point.getPoint2();
            int x1 = point1.getBlockX();
            int y1 = point1.getBlockY();
            int z1 = point1.getBlockZ();
            int x2 = point2.getBlockX();
            int y2 = point2.getBlockY();
            int z2 = point2.getBlockZ();
            CuboidRegion targetRG = new CuboidRegion(BlockVector3.at(x1, y1, z1), BlockVector3.at(x2, y2, z2));
            for (BlockVector3 block : targetRG) {
                locs.add(new Location(world, block.getBlockX(), block.getBlockY(), block.getBlockZ()).getBlock());
            }
        }

        return locs;
    }

    public String getName() {
        return name;
    }

    public World getWorld() {
        return world;
    }

    public List<CrowdPoint> getPoints() {
        return points;
    }

    public void spawnAudience(int amount, List<Block> selBlocks) {
        for (LivingEntity entity : entities) {
            entity.remove();
        }

        entities.clear();
        if (amount == 0) return;

        Collections.shuffle(selBlocks);

        for (int i = 0; i < amount; i++) {
            Block block = selBlocks.get(i);
            while (block.getType() != Material.AIR) {
                block = block.getLocation().add(0, 1, 0).getBlock();
            }

            switch (Utils.randomInt(1, 4)) {
                case 1:
                    spawnMob(block, EntityType.VILLAGER);
                    break;
                case 2:
                    spawnMob(block, EntityType.IRON_GOLEM);
                    break;
                default:
                    spawnMob(block, EntityType.PIGLIN);
                    break;
            }

        }
        lookpoint();
    }

    public void spawnMob(Block block, EntityType type) {
        LivingEntity entity = (LivingEntity) block.getWorld().spawnEntity(block.getLocation(), type);
        entity.setSilent(true);
        entity.setInvulnerable(true);
        entity.setGravity(true);
        entity.getAttribute(Attribute.GENERIC_KNOCKBACK_RESISTANCE).setBaseValue(99);
        entity.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE).setBaseValue(0);
        entity.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).setBaseValue(0);
        entities.add(entity);
    }

    public void setLookpointLoc(Location loc) {
        this.lookpointLoc = loc;
    }

    public void lookpoint() {
        if (lookpointLoc == null) return;
        for (LivingEntity entity : entities) {
            Location entityLocation = entity.getLocation();

            Vector direction = lookpointLoc.toVector().subtract(entityLocation.toVector()).normalize();

            double yaw = Math.toDegrees(Math.atan2(-direction.getX(), direction.getZ()));
            double pitch = Math.toDegrees(Math.asin(direction.getY()));

            entity.setRotation((float) yaw, (float) (pitch * -1));
        }
    }

    public Location getLookpoint() {
        return lookpointLoc;
    }

    public void jump(int second) {
        if (entities.isEmpty()) return;
        if (task != null && !task.isCancelled()) {
            task.cancel();
            task = null;
        }

        task = new EntityJumpTask(entities, second * 20L).runTaskTimer(CrowdFansPlugin.get(), 0L, 1L);
    }

}
