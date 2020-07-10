package me.Lozke.data;

import me.Lozke.utils.Text;
import org.bukkit.Location;
import org.bukkit.entity.LivingEntity;

public class CalamityMob extends ModifiableEntity {

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

    @Override
    public LivingEntity spawnEntity(Location location) {
        entity = super.spawnEntity(location);

        entity.setCustomName(Text.colorize(tier.getColorCode() + "[" + rarity.getSymbol() + "] " + entity.getCustomName()));

        if (this.location == null) {
            this.location = location;
        }

        return entity;
    }

    public LivingEntity getEntity() {
        return entity;
    }

    public MobSpawner getSpawner() {
        return spawner;
    }

    public void setSpawner(MobSpawner spawner) {
        this.spawner = spawner;
        setHome(spawner);
    }

    public void setHome(MobSpawner mobSpawner) {
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
