/*
 * Created by Noah Pritchard on 5/6/2020
 */
package me.Lozke.listeners;

import me.Lozke.MobMechanics;
import me.Lozke.PlayerMechanics;
import me.Lozke.data.*;
import me.Lozke.managers.ItemFactory;
import me.Lozke.utils.NumGenerator;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
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

    @EventHandler
    public void onMobDeath(EntityDeathEvent event) {
        LivingEntity dyingEntity = event.getEntity();
        if (plugin.getMobManager().isTracked(dyingEntity)) {
            RiftsMob riftsMob = plugin.getMobManager().asCalamityMob(dyingEntity);
            Tier tier = riftsMob.getTier();
            Rarity rarity = riftsMob.getRarity();

            //Spawner update
            MobSpawner spawner = riftsMob.getSpawner();
            if (spawner != null) spawner.setSpawnedMobsAmount(spawner.getSpawnedMobsAmount() - 1);

            //Drop check and production
            event.setDroppedExp(0);
            event.getDrops().clear();
            RiftsPlayer riftsPlayer = getPlayerKiller(event);
            if (riftsPlayer != null && riftsPlayer.reportMobKillAndCheckForDrop(tier, rarity)) {
                ItemStack drop;
                int value = NumGenerator.roll(5);
                if (value == 5) drop = ItemFactory.newWeapon(tier, rarity, riftsMob.getWeaponType());
                else drop = ItemFactory.newArmour(tier, rarity, ArmourType.values()[value]);
                event.getDrops().add(drop);
            }

            //Mob manager update
            MobMechanics.getInstance().getMobManager().stopTracking(event.getEntity());
        }
    }

    private RiftsPlayer getPlayerKiller(EntityDeathEvent event) {
        EntityDamageEvent edEvent = event.getEntity().getLastDamageCause();
        if (edEvent.getCause() == DamageCause.ENTITY_ATTACK) {
            EntityDamageByEntityEvent edbeEvent = (EntityDamageByEntityEvent) edEvent;
            Entity damager = edbeEvent.getDamager();
            if (damager instanceof Player) {
                return PlayerMechanics.getInstance().getPlayerManager().getAsRiftsPlayer(damager.getUniqueId());
            }
        }
        return null;
    }
}
