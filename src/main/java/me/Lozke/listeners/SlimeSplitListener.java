package me.Lozke.listeners;

import me.Lozke.MobMechanics;
import me.Lozke.data.*;
import me.Lozke.managers.BaseEntityManager;
import me.Lozke.managers.MobManager;
import me.Lozke.utils.NamespacedKeyWrapper;
import org.bukkit.Location;
import org.bukkit.entity.Slime;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.SlimeSplitEvent;

public class SlimeSplitListener implements Listener {

    private MobManager mobManager;
    private BaseEntityManager baseEntityManager;

    public SlimeSplitListener(MobMechanics plugin) {
        this.mobManager = plugin.getMobManager();
        this.baseEntityManager = plugin.getBaseEntityManager();
    }

    @EventHandler (priority = EventPriority.LOWEST)
    public void onSplit(SlimeSplitEvent event) {
        event.setCancelled(true);
        Slime entity = event.getEntity();

        NamespacedKeyWrapper wrapper = new NamespacedKeyWrapper(entity.getPersistentDataContainer());
        Tier tier = Tier.valueOf(wrapper.getString(ARNamespacedKey.TIER));
        Rarity rarity = Rarity.valueOf(wrapper.getString(ARNamespacedKey.RARITY));
        BaseEntity baseEntity = baseEntityManager.getBaseEntity(wrapper.getString(ARNamespacedKey.MOB_ID));

        if (baseEntity == null || !baseEntity.isSplittable() || event.getEntity().getSize() - 1 < baseEntity.getMinSize()) {
            return;
        }

        int newSize = entity.getSize() - 1;

        Location location = event.getEntity().getLocation();
        for (int i = 0; i < baseEntity.getSplitSpawnCount(); i++) {
            RiftsMob newMob = baseEntityManager.spawnBaseEntity(baseEntity, location, tier, rarity);
            ((Slime) newMob.getEntity()).setSize(newSize);
            mobManager.trackEntity(newMob);
        }
    }

}
