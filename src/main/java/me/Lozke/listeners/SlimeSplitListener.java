package me.Lozke.listeners;

import me.Lozke.MobMechanics;
import me.Lozke.data.BaseEntity;
import me.Lozke.data.RiftsMob;
import me.Lozke.managers.BaseEntityManager;
import me.Lozke.managers.MobManager;
import org.bukkit.Location;
import org.bukkit.entity.Slime;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.SlimeSplitEvent;

public class SlimeSplitListener implements Listener {

    private MobManager mobManager;
    private BaseEntityManager baseEntityManager;

    public SlimeSplitListener(MobMechanics plugin) {
        this.mobManager = plugin.getMobManager();
        this.baseEntityManager = plugin.getBaseEntityManager();
    }

    @EventHandler
    public void onSplit(SlimeSplitEvent event) {
        event.setCancelled(true);
        Slime entity = event.getEntity();
        RiftsMob mob = mobManager.asRiftsMob(entity);
        BaseEntity baseEntity = baseEntityManager.getBaseEntity(mob.getBaseEntityID());

        if (baseEntity == null || !baseEntity.isSplittable() || event.getEntity().getSize() - 1 < baseEntity.getMinSize()) {
            return;
        }

        entity.setSize(entity.getSize() - 1);
        mob.formatName();

        Location location = event.getEntity().getLocation();
        for (int i = 0; i < baseEntity.getSplitSpawnCount(); i++) {
            RiftsMob newMob = baseEntityManager.spawnBaseEntity(baseEntity, entity.getLocation(), mob.getTier(), mob.getRarity());
            mobManager.trackEntity(newMob);
        }
    }

}
