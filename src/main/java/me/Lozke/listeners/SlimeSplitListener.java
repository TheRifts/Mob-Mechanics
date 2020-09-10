package me.Lozke.listeners;

import me.Lozke.MobMechanics;
import me.Lozke.data.RiftsMob;
import me.Lozke.managers.MobManager;
import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.SlimeSplitEvent;

public class SlimeSplitListener implements Listener {

    MobManager mobManager;

    public SlimeSplitListener(MobManager mobManager) {
        this.mobManager = mobManager;
    }

    @EventHandler
    public void onSplit(SlimeSplitEvent event) {
        event.setCancelled(true);
        RiftsMob mob = MobMechanics.getInstance().getMobManager().asCalamityMob(event.getEntity());

        if (mob == null || !mob.isSplittable() || event.getEntity().getSize() - 1 < mob.getMinSize()) {
            return;
        }

        mob.setSize(mob.getSize() - 1);
        mob.formatName();

        Location location = event.getEntity().getLocation();
        for (int i = 0; i < mob.getSplitSpawnCount(); i++) {
            RiftsMob clone = mob.clone();
            clone.spawnEntity(location);
            mobManager.trackEntity(clone);
        }
    }

}
