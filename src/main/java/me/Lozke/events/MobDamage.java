/*
 * Created by Noah Pritchard on 5/6/2020
 */
package me.Lozke.events;

import me.Lozke.MobMechanics;
import me.Lozke.data.CustomMob;
import me.Lozke.utils.Logger;
import org.bukkit.EntityEffect;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;

public class MobDamage implements Listener {

    private MobMechanics plugin;

    public MobDamage(MobMechanics plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onMobDamage(EntityDamageEvent event) {
        Entity damaged = event.getEntity();
        CustomMob customMob = plugin.getMobManager().getCustomMob(damaged.getUniqueId());

        if (customMob != null) {
            LivingEntity damagedMob = (LivingEntity)damaged;
            
            //Deal Damage
            dealDamage(damagedMob, event.getDamage());
            event.setCancelled(true);

            //Update Health Bar
            customMob.updateHealthDisplay();
        }
    }


    private void dealDamage(LivingEntity damaged, double damage) {
        damaged.setHealth(damaged.getHealth()-damage);
        damaged.playEffect(EntityEffect.HURT);
    }
}
