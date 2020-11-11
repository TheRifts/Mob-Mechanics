package me.Lozke.tasks;

import me.Lozke.MobMechanics;
import me.Lozke.data.RiftsMob;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.LivingEntity;
import org.bukkit.scheduler.BukkitRunnable;

public class TrackMobTask extends BukkitRunnable {

    private final MobMechanics plugin;
    private final RiftsMob trackedMob;
    private final LivingEntity trackedEntity;

    public TrackMobTask(RiftsMob mob) {
        this.plugin = MobMechanics.getInstance();
        this.trackedMob = mob;
        this.trackedEntity = mob.getEntity();
        runTaskTimer(MobMechanics.getInstance(), 0L, 100L);
    }

    @Override
    public void run() {
        if (!plugin.getMobManager().isTracked(trackedEntity)) {
            cancel();
            return;
        }
        if (trackedEntity == null || trackedEntity.isDead() || !trackedEntity.isValid()) {
            MobMechanics.getInstance().getMobManager().stopTracking(trackedEntity);
            cancel();
            return;
        }

        int leashRange = trackedMob.getSpawner() != null ? trackedMob.getSpawner().getLeashRange() : 8;
        if (trackedEntity.getLocation().distanceSquared(trackedMob.getHome()) > Math.pow(leashRange, 2)) {
            trackedEntity.teleport(trackedMob.getHome());
            trackedEntity.setHealth(trackedEntity.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue());
            MobMechanics.getInstance().getMobManager().updateHealthDisplay(trackedMob);
        }
    }
}
