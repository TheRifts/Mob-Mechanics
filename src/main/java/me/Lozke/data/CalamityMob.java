package me.Lozke.data;

import me.Lozke.utils.Logger;
import me.Lozke.utils.NumGenerator;
import me.Lozke.utils.Text;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

public class CalamityMob extends ModifiableEntity implements Cloneable {

    private ModifiableEntity baseEntity;
    private LivingEntity entity;

    private Tier tier;
    private Rarity rarity;

    private MobSpawner spawner;
    private Location location; //Position mob is leashed to.

    public CalamityMob(ModifiableEntity baseEntity, Tier tier, Rarity rarity) {
        this.baseEntity = baseEntity;
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

        applyDefaultEquipment();
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

    public void applyEquipment(EquipmentSlot slot, ItemStack stack) {
        if (entity == null || !entity.isValid()) return;
        if (entity.getEquipment() != null) {
            entity.getEquipment().setItem(slot, stack);
        }
    }

    public void applyDefaultEquipment() {
        if (entity.getEquipment() == null) return;
        Map<EquipmentSlot, String> equipment = baseEntity.getEquipment();
        if (equipment == null) equipment = new HashMap<>();
        for (EquipmentSlot slot : EquipmentSlot.values()) {
            ItemStack stack;
            String value = equipment.get(slot);
            if (value == null) {
                if (slot == EquipmentSlot.OFF_HAND) continue;
                if (slot == EquipmentSlot.HAND) {
                    if (baseEntity.getWeaponTypes() != null)
                        stack = baseEntity
                                .getWeaponTypes()
                                .get(NumGenerator.index(baseEntity.getWeaponTypes().size()))
                                .getItem(tier);
                    else stack = WeaponType.getRandomItem(tier);
                }
                else stack = ArmourType.fromEquipmentSlot(slot).getItem(tier);
            }
            else {
                stack = new ItemStack(Material.valueOf(value));
            }
            applyEquipment(slot, stack);
        }
    }
}
