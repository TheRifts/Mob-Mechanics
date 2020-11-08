package me.Lozke.data;

import me.Lozke.MobMechanics;
import me.Lozke.utils.NumGenerator;
import org.bukkit.Location;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.entity.LivingEntity;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;

public class RiftsMob implements RiftsEntity {

    private final WeakReference<LivingEntity> entity;
    private String baseEntityID;

    private final Map<RiftsStat, Integer> baseStats = new HashMap<>();
    private final int[] damageRange = new int[2];

    private Tier tier;
    private Rarity rarity;

    private WeaponType weaponType;

    private MobSpawner spawner;
    private Location leashLocation; //Position mob is leashed to.

    public RiftsMob(LivingEntity entity, Tier tier, Rarity rarity) {
        this.entity = new WeakReference<>(entity);
        this.tier = tier;
        this.rarity = rarity;
    }

    public String getBaseEntityID() {
        return baseEntityID;
    }

    public void setBaseEntityID(String string) {
        this.baseEntityID = string;
    }

    public LivingEntity getEntity() {
        return entity.get();
    }

    public MobSpawner getSpawner() {
        return spawner;
    }

    public void setSpawner(MobSpawner mobSpawner) {
        this.spawner = mobSpawner;
        setHome(mobSpawner.getLocation());
    }

    public void setHome(Location location) {
        this.leashLocation = location;
    }
    public Location getHome() {
        return leashLocation;
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

        AttributeInstance healthAttribute = getEntity().getAttribute(Attribute.GENERIC_MAX_HEALTH);
        if (healthAttribute != null) {
            int HP = getStat(RiftsStat.HP);
            healthAttribute.setBaseValue(HP);
            getEntity().setHealth(HP);
            MobMechanics.getInstance().getMobManager().updateHealthDisplay(this);
        }
    }
}
