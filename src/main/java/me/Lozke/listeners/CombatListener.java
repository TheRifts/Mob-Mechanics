/*
 * Created by Noah Pritchard on 5/6/2020
 */
package me.Lozke.listeners;

import me.Lozke.MobMechanics;
import me.Lozke.managers.ItemWrapper;
import me.Lozke.managers.MobManager;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByBlockEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.inventory.ItemStack;

public class CombatListener implements Listener {

    private MobManager mobManager;

    public CombatListener(MobMechanics plugin) {
        this.mobManager = plugin.getMobManager();
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onDamage(EntityDamageEvent event) {
        Entity victim = event.getEntity();

        if (event instanceof EntityDamageByEntityEvent) {
            Entity attacker = ((EntityDamageByEntityEvent) event).getDamager();
            event.setDamage(getDamage(attacker));
        }
        if (event instanceof EntityDamageByBlockEvent) {
            Block block = ((EntityDamageByBlockEvent) event).getDamager();
            event.setDamage(1D);
        }

        if (victim instanceof LivingEntity && mobManager.isTracked(victim)) {
            mobManager.updateHealthDisplay(((LivingEntity) victim));
        }
    }

    private double getDamage(Entity attacker) {
        if (attacker instanceof LivingEntity) {
            ItemStack weapon = ((LivingEntity) attacker).getEquipment().getItemInMainHand();
            return new ItemWrapper(weapon).getDamage();
        }
        return 1D;
    }
}
