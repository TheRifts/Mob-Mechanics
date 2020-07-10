/*
 * Created by Noah Pritchard on 5/6/2020
 */
package me.Lozke.listeners;

import me.Lozke.MobMechanics;
import me.Lozke.data.CustomMob;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;

public class MobDeath implements Listener {

    private MobMechanics plugin;

    public MobDeath(MobMechanics plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onMobDeath(EntityDeathEvent event) {
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
