/*
 * Created by Noah Pritchard on 5/6/2020
 */
package me.Lozke.listeners;

import me.Lozke.MobMechanics;
import me.Lozke.data.MobSpawner;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;

public class MobDeath implements Listener {

    private MobMechanics plugin;

    public MobDeath(MobMechanics plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onMobDeath(EntityDeathEvent event) {
        if (plugin.getMobManager().isTracked(event.getEntity())) {
            MobSpawner spawner = plugin.getMobManager().asCalamityMob(event.getEntity()).getSpawner();
            if (spawner == null) return;

            spawner.setSpawnedMobsAmount(spawner.getSpawnedMobsAmount() - 1);
            MobMechanics.getInstance().getMobManager().stopTracking(event.getEntity());
        }

        /*
        UUID uuid = event.getEntity().getUniqueId();
        CustomMob customMob = plugin.getMobManager().getCustomMob(uuid);
        ItemStack drop;
        if (customMob != null) {
            drop = customMob.getDrop();
            if (drop != null) {
                event.getDrops().add(drop);
            }
        }
        plugin.getMobManager().killMob(event.getEntity().getUniqueId());
         */
    }
}
