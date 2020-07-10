package me.Lozke.data;

import me.Lozke.data.items.NamespacedKeys;
import me.Lozke.handlers.ItemHandler;
import me.Lozke.utils.NumGenerator;
import me.Lozke.utils.Text;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.attribute.Attributable;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.UUID;

public class CustomMob {
    public static final int baseHP = 50;

    private static final int numberOfHealthIndicators = 40;
    private static final char healthIndicatorCharacter = '‖'; //╏
    private static final String healthBarPrefix = "§F§L⟦§A§L";
    private static final String healthBarColorHealthy = "§A§L";
    private static final String healthBarColorHurt = "§E§L";
    private static final String healthBarColorDoomed = "§C§L";
    private static final String healthBarColorSwitch = "§7§L";
    private static final String healthBarSuffix = "§F§L⟧";
    private static final int lengthOfHealthBarString = numberOfHealthIndicators + healthBarPrefix.length() + healthBarColorHealthy.length() + healthBarColorSwitch.length() + healthBarSuffix.length();

    private final UUID uuid;
    private final MobSpawner spawner;

    private EntityType entityType;
    private String name;
    private Tier tier;
    private Rarity rarity;
    private ItemStack[] gear;
    private ItemStack weapon;

    public CustomMob(EntityType entityType, String name, Tier tier, Rarity rarity, MobSpawner spawner, Location location) {
        this.entityType = entityType;
        this.name = name;
        this.tier = tier;
        this.rarity = rarity;
        this.gear = ItemHandler.newSet(tier, rarity);
        this.weapon = ItemHandler.newWeapon(tier, rarity, "Sword");

        this.spawner = spawner;
        this.uuid = spawnMob(location);

        handleStats();
    }

    private UUID spawnMob(Location location) {
        LivingEntity entity = (LivingEntity)location.getWorld().spawnEntity(location, entityType);
        entity.setCustomName(Text.colorize(name));
        entity.setCustomNameVisible(true);
        entity.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(50);
        entity.getEquipment().setArmorContents(gear);
        entity.getEquipment().setItemInMainHand(weapon);
        return entity.getUniqueId();
    }

    private void handleStats() {
        for (ItemStack item: gear) {
            PersistentDataContainer dataContainer = item.getItemMeta().getPersistentDataContainer();
            if (dataContainer.has(NamespacedKeys.realItem, PersistentDataType.STRING)) {
                if (dataContainer.has(NamespacedKeys.healthPoints, PersistentDataType.INTEGER)) {
                    AttributeInstance maxHealth = getAttributable().getAttribute(Attribute.GENERIC_MAX_HEALTH);
                    int itemHP = dataContainer.get(NamespacedKeys.healthPoints, PersistentDataType.INTEGER);
                    //TODO move health management under domain of spawner/mob manager
                    maxHealth.setBaseValue((int)maxHealth.getValue() + itemHP);
                }
            }
        }
        getLivingEntity().setHealth(getAttributable().getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue());
    }

    public UUID getUniqueId() {
        return uuid;
    }

    public MobSpawner getSpawner() {
        return spawner;
    }

    public EntityType getType() {
        return entityType;
    }

    public String getName() {
        return name;
    }

    public Tier getTier() {
        return tier;
    }

    public Rarity getRarity() {
        return rarity;
    }

    public Entity getEntity() {
        return Bukkit.getEntity(uuid);
    }

    public LivingEntity getLivingEntity() {
        return (LivingEntity)getEntity();
    }

    public Attributable getAttributable() {
        return (Attributable)getEntity();
    }

    public boolean isDropping() {
        return NumGenerator.roll(5) == 5;
    }

    public ItemStack getDrop() {
        if (isDropping()) {
            if (NumGenerator.roll(2) == 2) {
                return weapon;
            }
            return gear[NumGenerator.index(gear.length)];
        }
        else return null;
    }

    public void updateHealthDisplay() {
        double health = getLivingEntity().getHealth();
        double maxHealth = getAttributable().getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue();
        int healthIndicators = (int)(Math.ceil(
                numberOfHealthIndicators*Math.ceil(health)/Math.ceil(maxHealth)
        ));
        StringBuilder healthBar = new StringBuilder(lengthOfHealthBarString);
        healthBar.append(healthBarPrefix);
        if (healthIndicators < numberOfHealthIndicators/5) {
            healthBar.append(healthBarColorDoomed);
        }
        else if (healthIndicators < numberOfHealthIndicators/2) {
            healthBar.append(healthBarColorHurt);
        }
        else {
            healthBar.append(healthBarColorHealthy);
        }
        for (int i = 0;i < healthIndicators;i++) {
            healthBar.append(healthIndicatorCharacter);
        }
        healthBar.append(healthBarColorSwitch);
        for (int i = healthIndicators;i < numberOfHealthIndicators;i++) {
            healthBar.append(healthIndicatorCharacter);
        }
        healthBar.append(healthBarSuffix);

        getEntity().setCustomName(healthBar.toString());
    }
}
