package me.Lozke.managers;

import me.Lozke.MobMechanics;
import me.Lozke.data.*;
import me.Lozke.menus.MobSelector.MobSelectorMenu;
import me.Lozke.utils.Text;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

import java.util.*;

public class MobManager {

    private MobMechanics plugin;

    private HashMap<LivingEntity, RiftsMob> trackedEntities = new HashMap<>();

    public MobManager(MobMechanics plugin) {
        this.plugin = plugin;
    }

    public void openEditor(Player player) {
        new MobSelectorMenu().openMenu(player);
    }

    public void trackEntity(RiftsMob mob) {
        trackedEntities.put(mob.getEntity(), mob);
    }

    public void stopTracking(LivingEntity entity) {
        trackedEntities.remove(entity);
    }
    public void stopTracking(RiftsMob mob) {
        trackedEntities.remove(mob.getEntity());
    }

    public boolean isTracked(LivingEntity entity) {
        return trackedEntities.containsKey(entity);
    }

    public RiftsMob asRiftsMob(LivingEntity entity) {
        return trackedEntities.get(entity);
    }

    public void updateHealthDisplay(LivingEntity entity) {
        RiftsMob mob = trackedEntities.get(entity);
        if (mob == null) {
            return;
        }
        updateHealthDisplay(mob);
    }
    public void updateHealthDisplay(RiftsMob mob) {
        LivingEntity entity = mob.getEntity();
        double hp = entity.getHealth();
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
