package me.Lozke.data;

import me.Lozke.MobMechanics;
import me.Lozke.utils.Logger;
import me.Lozke.utils.NumGenerator;
import me.Lozke.utils.Text;
import org.bukkit.Location;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.entity.LivingEntity;

import java.util.HashMap;
import java.util.Map;

public class RiftsMob implements Cloneable {

    private LivingEntity entity;
    private String baseEntityID;

    private final Map<RiftsStat, Integer> baseStats = new HashMap<>();
    private final int[] damageRange = new int[2];

    private Tier tier;
    private Rarity rarity;

    private WeaponType weaponType;

    private MobSpawner spawner;
    private Location location; //Position mob is leashed to.

    public RiftsMob(LivingEntity entity, Tier tier, Rarity rarity) {
        this.entity = entity;
        this.tier = tier;
        this.rarity = rarity;
    }

    public RiftsMob clone(){
        RiftsMob clone = null;
        try {
            clone = (RiftsMob) super.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return clone;
    }

    public String getBaseEntityID() {
        return baseEntityID;
    }

    public void setBaseEntityID(String string) {
        this.baseEntityID = string;
    }

    public void formatName() {
        if (entity == null) {
            Logger.log("Attempted to formatName for " + baseEntityID + " but entity is invalid!");
            return;
        }
        String name = (String) entity.getPersistentDataContainer().get(MobNamespacedKey.CUSTOM_NAME.getNamespacedKey(), MobNamespacedKey.CUSTOM_NAME.getDataType());
        entity.setCustomName(Text.colorize(tier.getColorCode() + "[" + rarity.getSymbol() + "] " + name));
    }

    public LivingEntity getEntity() {
        return entity;
    }

    public MobSpawner getSpawner() {
        return spawner;
    }

    public void setSpawner(MobSpawner mobSpawner) {
        this.spawner = mobSpawner;
        setHome(mobSpawner.getLocation());
    }

    public void setHome(Location location) {
        this.location = location;
    }

    public Tier getTier() {
        return tier;
    }

    public void setTier(Tier tier) {
        this.tier = tier;
    }

    public Rarity getRarity() {
        return rarity;
    }

    public void setWeaponType(WeaponType weaponType) {
        this.weaponType = weaponType;
    }

    public WeaponType getWeaponType() {
        return weaponType;
    }

    public int getStat(RiftsStat stat) {
        return baseStats.getOrDefault(stat, 0);
    }

    public void setBaseStat(RiftsStat stat, int value) {
        baseStats.put(stat, value);
    }

    public Map<RiftsStat, Integer> getBaseStats() {
        return baseStats;
    }

    //TODO: Add damage modifiers from stats
    public int getDamage() {
        return NumGenerator.rollInclusive(getDamageLo(), getDamageHi());
    }

    public int getDamageLo() {
        return damageRange[0];
    }

    public int getDamageHi() {
        return damageRange[1];
    }

    public void updateStats() {
        damageRange[0] = getStat(RiftsStat.DMG_LO);
        damageRange[1] = getStat(RiftsStat.DMG_HI);

        AttributeInstance healthAttribute = entity.getAttribute(Attribute.GENERIC_MAX_HEALTH);
        if (healthAttribute != null) {
            int HP = getStat(RiftsStat.HP);
            healthAttribute.setBaseValue(HP);
            entity.setHealth(HP);
            MobMechanics.getInstance().getMobManager().updateHealthDisplay(this);
        }
    }
}
