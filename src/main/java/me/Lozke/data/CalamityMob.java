package me.Lozke.data;

import me.Lozke.utils.Logger;
import me.Lozke.utils.Text;
import org.bukkit.Location;
import org.bukkit.entity.LivingEntity;

public class CalamityMob extends ModifiableEntity implements Cloneable {

    private LivingEntity entity;

    private Tier tier;
    private Rarity rarity;

    private MobSpawner spawner;
    private Location location; //Position mob is leashed to.

    public CalamityMob(ModifiableEntity baseEntity, Tier tier, Rarity rarity) {
        this.tier = tier;
        this.rarity = rarity;
        apply(baseEntity);
    }

    public CalamityMob clone(){
        CalamityMob clone = null;
        try {
            clone = (CalamityMob)super.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return clone;
    }

    @Override
    public LivingEntity spawnEntity(Location location) {
        entity = super.spawnEntity(location);

        formatName();

        if (this.location == null) {
            this.location = location;
        }

        return entity;
    }

    public void formatName() {
        if (entity == null) {
            Logger.log("Attempted to formatName for " + getId() + " but entity is invalid!");
            return;
        }
        entity.setCustomName(Text.colorize(tier.getColorCode() + "[" + rarity.getSymbol() + "] " + entity.getCustomName()));
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
}
