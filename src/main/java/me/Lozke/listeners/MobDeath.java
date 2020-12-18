/*
 * Created by Noah Pritchard on 5/6/2020
 */
package me.Lozke.listeners;

import me.Lozke.MobMechanics;
import me.Lozke.PlayerMechanics;
import me.Lozke.data.*;
import me.Lozke.utils.ItemFactory;
import me.Lozke.utils.NumGenerator;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;

public class MobDeath implements Listener {

    private MobMechanics plugin;

    public MobDeath(MobMechanics plugin) {
        this.plugin = plugin;
    }

    @EventHandler (priority = EventPriority.MONITOR)
    public void onMobDeath(EntityDeathEvent event) {
        LivingEntity dyingEntity = event.getEntity();
        if (plugin.getMobManager().isTracked(dyingEntity)) {
            RiftsMob riftsMob = plugin.getMobManager().asRiftsMob(dyingEntity);
            Tier tier = riftsMob.getTier();
            Rarity rarity = riftsMob.getRarity();

            //Drop check and production
            event.setDroppedExp(0);
            event.getDrops().clear();
            RiftsPlayer riftsPlayer = getPlayerKiller(event);
            if (riftsPlayer != null && riftsPlayer.reportMobKillAndCheckForDrop(tier, rarity)) {
                ItemStack drop;
                int value = NumGenerator.index(5);
                if (value == 4) drop = ItemFactory.newWeapon(tier, rarity, riftsMob.getWeaponType());
                else drop = ItemFactory.newArmour(tier, rarity, ArmourType.values()[value]);
                event.getDrops().add(drop);
            }

            if (NumGenerator.fraction() <= 0.15) {
                ItemStack drop;
                int value = NumGenerator.index(5);
                if (value >= 4) drop = ItemFactory.newPouch(tier);
                else drop = ItemFactory.newScrap(tier, value);
                event.getDrops().add(drop);
            }

            //Mob manager update
            plugin.getMobManager().stopTracking(riftsMob);
        }
    }

    private RiftsPlayer getPlayerKiller(EntityDeathEvent event) {
        EntityDamageEvent edEvent = event.getEntity().getLastDamageCause();
        if (edEvent != null && edEvent.getCause() == DamageCause.ENTITY_ATTACK) {
            EntityDamageByEntityEvent edbeEvent = (EntityDamageByEntityEvent) edEvent;
            Entity damager = edbeEvent.getDamager();
            if (damager instanceof Player) {
                return PlayerMechanics.getInstance().getPlayerManager().getAsRiftsPlayer(damager.getUniqueId());
            }
        }
        return null;
    }
}
