package me.Lozke.tasks;

import me.Lozke.MobMechanics;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.Entity;
import org.bukkit.scheduler.BukkitRunnable;

public class SlimeZaWarudoTask extends BukkitRunnable {

    private Entity entity;
    private Location location;
    private BlockData blockData;
    private int cooldown;

    private boolean ticking;

    public SlimeZaWarudoTask(Entity entity) {
        this.entity = entity;
        this.cooldown = 30;
        runTaskTimer(MobMechanics.getInstance(), 0, 20);
    }

    @Override
    public void run() {
        if (!ticking && entity.isOnGround()) {
            Location location = entity.getLocation().add(0, -1, 0);
            blockData = entity.getLocation().add(0, -1, 0).getBlock().getBlockData();
            location.getBlock().setType(Material.SLIME_BLOCK);
            ticking = true;
            return;
        }
        if (cooldown <= 0) {
            this.cancel();
            return;
        }
        cooldown--;
    }
}
