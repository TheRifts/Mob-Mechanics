/*
 * Created by Noah Pritchard on 5/6/2020
 */
package me.Lozke.listeners;

import me.Lozke.MobMechanics;
import me.Lozke.managers.MobManager;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;

public class MobDamagedListener implements Listener {

    private MobManager mobManager;

    public MobDamagedListener(MobMechanics plugin) {
        this.mobManager = plugin.getMobManager();
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onDamage(EntityDamageEvent event) {
        Entity victim = event.getEntity();
        if (victim instanceof LivingEntity && mobManager.isTracked(victim)) {
            mobManager.updateHealthDisplay(((LivingEntity) victim));
        }
    }
}
