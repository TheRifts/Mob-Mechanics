package me.Lozke.managers;

import me.Lozke.MobMechanics;
import me.Lozke.data.*;
import me.Lozke.menus.MobSelector.MobSelectorMenu;
import me.Lozke.tasks.TrackMobTask;
import me.Lozke.utils.Text;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

import java.util.*;

public class MobManager {

    private final MobMechanics plugin;
    private final Map<LivingEntity, RiftsMob> trackedEntities = new WeakHashMap<>();

    public MobManager(MobMechanics plugin) {
        this.plugin = plugin;
    }

    public void openEditor(Player player) {
        new MobSelectorMenu().openMenu(player);
    }

    public void trackEntity(RiftsMob mob) {
        trackedEntities.put(mob.getEntity(), mob);
        new TrackMobTask(mob);
    }

    public void stopTracking(LivingEntity entity) {
        stopTracking(asRiftsMob(entity));
    }
    public void stopTracking(RiftsMob mob) {
        if (mob == null) {
            return;
        }
        MobSpawner spawner = mob.getSpawner();
        if (spawner != null) {
            spawner.incrementSpawnedMobsAmountBy(-1);
        }
        LivingEntity entity = mob.getEntity();
        if (entity != null && (entity.isValid() || !entity.isDead())) {
            entity.remove();
        }
        trackedEntities.remove(entity);
    }
    public void stopTrackingAllMobs() {
        Iterator<RiftsMob> iterator = trackedEntities.values().iterator();
        while (iterator.hasNext()) {
            RiftsMob mob = iterator.next();
            iterator.remove();
            stopTracking(mob);
        }
    }

    public boolean isTracked(LivingEntity entity) {
        return trackedEntities.containsKey(entity);
    }

    public RiftsMob asRiftsMob(LivingEntity entity) {
        return trackedEntities.get(entity);
    }

    public void updateHealthDisplay(LivingEntity entity) {
        updateHealthDisplay(entity, entity.getHealth());
    }
    public void updateHealthDisplay(LivingEntity entity, double hp) {
        RiftsMob mob = trackedEntities.get(entity);
        if (mob == null) {
            return;
        }
        updateHealthDisplay(mob, hp);
    }
    public void updateHealthDisplay(RiftsMob mob) {
        updateHealthDisplay(mob, mob.getEntity().getHealth());
    }
    public void updateHealthDisplay(RiftsMob mob, double hp) {
        LivingEntity entity = mob.getEntity();
        hp = Math.max(hp, 0);
        double maxHP = entity.getAttribute(Attribute.GENERIC_MAX_HEALTH).getBaseValue();
        double healthPercentage = hp/maxHP;
        String colorCode;
        if (healthPercentage >= 0.75) {
            colorCode = "&a";
        }
        else if (healthPercentage >= 0.25) {
            colorCode = "&e";
        }
        else {
            colorCode = "&c";
        }
        String name = (String) entity.getPersistentDataContainer().get(MobNamespacedKey.CUSTOM_NAME.getNamespacedKey(), MobNamespacedKey.CUSTOM_NAME.getDataType());
        entity.setCustomName(Text.colorize(mob.getTier().getColorCode() + "[" + mob.getRarity().getSymbol() + "] " + name + " " + colorCode + (int) Math.ceil(hp) + "&c❤"));
    }
}
