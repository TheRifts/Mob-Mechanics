package me.Lozke.tasks;

import me.Lozke.MobMechanics;
import me.Lozke.data.Abilities.AbilityTriggerReason;
import me.Lozke.data.RiftsMob;
import org.bukkit.scheduler.BukkitRunnable;

public class EntityAbilityTimer extends BukkitRunnable {

    private MobMechanics plugin;
    private RiftsMob mob;

    public EntityAbilityTimer(RiftsMob mob) {
        this.mob = mob;
        plugin = MobMechanics.getInstance();
        runTaskTimer(plugin, 60L, 10L);
    }

    @Override
    public void run() {
        if (mob == null || mob.getEntity() == null || !mob.getEntity().isValid()) {
            cancel();
            return;
        }
        plugin.getAbilityManager().cast(mob, AbilityTriggerReason.INTERVAL);
    }
}
