package me.Lozke.tasks;

import me.Lozke.MobMechanics;
import me.Lozke.data.RiftsMob;
import org.bukkit.entity.LivingEntity;
import org.bukkit.scheduler.BukkitRunnable;

public class TrackMobTask extends BukkitRunnable {

    private final RiftsMob trackedMob;
    private final LivingEntity trackedEntity;

    public TrackMobTask(RiftsMob mob) {
        this.trackedMob = mob;
        this.trackedEntity = mob.getEntity();
        runTaskTimerAsynchronously(MobMechanics.getInstance(), 0L, 100L);
    }

    @Override
    public void run() {
        if (trackedEntity == null || trackedEntity.isDead() || !trackedEntity.isValid()) {
            MobMechanics.getInstance().getMobManager().stopTracking(trackedEntity);
            cancel();
        }
    }
}
