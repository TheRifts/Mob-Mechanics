package me.Lozke.data;

import me.Lozke.utils.Text;
import org.bukkit.Location;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;

public class Mob {

    private EntityType entityType;
    private String name;
    private String rarity;
    private Tier tier;

    public Mob(EntityType entityType, String name, String rarity, Tier tier) {
        this.entityType = entityType;
        this.name = name;
        this.rarity = rarity;
        this.tier = tier;
    }

    public void spawnMob(Location location) {
        LivingEntity entity = (LivingEntity) location.getWorld().spawnEntity(location, entityType);
        entity.setCustomName(Text.colorize(name));
        entity.setCustomNameVisible(true);
        entity.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(20000);
        entity.setHealth(20000);
    }
}
