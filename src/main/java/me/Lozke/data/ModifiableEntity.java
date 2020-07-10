package me.Lozke.data;

import me.Lozke.utils.Items;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.*;
import org.bukkit.inventory.EquipmentSlot;

import java.util.HashMap;
import java.util.Map;

public class ModifiableEntity {

    private String id;
    private String name;
    private EntityType type;
    private int followRange = -1;
    private boolean showName;
    private int size;
    private boolean baby;
    private boolean angry;
    private boolean armsRaised;
    private boolean powered;
    private boolean knockbackImmune;
    private boolean burnImmune;
    private boolean fallImmune;
    private boolean drownImmune;
    private boolean dryOutImmune;
    private boolean dryStreak;
    private Map<EquipmentSlot, String> equipment = new HashMap<>();

    public LivingEntity spawnEntity(Location location) {
        LivingEntity le = (LivingEntity) location.getWorld().spawnEntity(location, type);

        if (le instanceof Zombie) {
            ((Zombie) le).setBaby(baby);
        }
        else if (le instanceof Rabbit) {
            ((Rabbit) le).setRabbitType(Rabbit.Type.THE_KILLER_BUNNY);
            ((Rabbit) le).setAdult();
        }
        else if (le instanceof Creeper) {
            ((Creeper) le).setPowered(powered);
        }
        else if (le instanceof Slime) {
            if (size < 1) {
                ((Slime) le).setSize(2 + (int) (Math.random() * 3));
            }
            else {
                ((Slime) le).setSize(Math.min(size, 256));
            }
        }
        else if (le instanceof Phantom) {
            if (size < 1) {
                ((Slime) le).setSize(1 + (int) (Math.random() * 3));
            }
            else {
                ((Slime) le).setSize(Math.min(size, 64));
            }
        }
        else if (le instanceof Raider) {
            ((Raider) le).setCanJoinRaid(false);
            ((Raider) le).setPatrolLeader(false);
        }
        else if (le instanceof Bee) {
            ((Bee) le).setCannotEnterHiveTicks(Integer.MAX_VALUE);
        }
        else if (le instanceof Piglin) {
            ((Piglin) le).setImmuneToZombification(true);
            ((Piglin) le).setIsAbleToHunt(false);
            ((Piglin) le).setBaby(baby);
        }
        else if (le instanceof Hoglin) {
            ((Hoglin) le).setImmuneToZombification(true);
        }

        if (le instanceof Ageable) {
            ((Ageable) le).setAgeLock(true);
            if (baby) {
                ((Ageable) le).setBaby();
            }
            else {
                ((Ageable) le).setAdult();
            }
        }

        if (angry) {
            if (le instanceof Wolf) {
                ((Wolf) le).setAngry(true);
            }
            else if (le instanceof Bee) {
                ((Bee) le).setAnger(Integer.MAX_VALUE);
            }
        }

        if (knockbackImmune && le.getAttribute(Attribute.GENERIC_KNOCKBACK_RESISTANCE) != null) {
            le.getAttribute(Attribute.GENERIC_KNOCKBACK_RESISTANCE).setBaseValue(100);
        }

        le.setCustomName(name);
        le.setCustomNameVisible(showName);
        le.setCanPickupItems(false);
        le.getEquipment().setHelmet(Items.formatItem(Material.STONE_BUTTON, ""));

        return le;
    }

    public void apply(ModifiableEntity newModifiableEntity) {
        this.id = newModifiableEntity.id;
        this.name = newModifiableEntity.name;
        this.type = newModifiableEntity.type;
        this.followRange = newModifiableEntity.followRange;
        this.showName = newModifiableEntity.showName;
        this.size = newModifiableEntity.size;
        this.baby = newModifiableEntity.baby;
        this.angry = newModifiableEntity.angry;
        this.armsRaised = newModifiableEntity.armsRaised;
        this.knockbackImmune = newModifiableEntity.knockbackImmune;
        this.burnImmune = newModifiableEntity.burnImmune;
        this.fallImmune = newModifiableEntity.fallImmune;
        this.drownImmune = newModifiableEntity.drownImmune;
        this.dryOutImmune = newModifiableEntity.dryOutImmune;
        this.dryStreak = newModifiableEntity.dryStreak;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isShowName() {
        return showName;
    }

    public void setShowName(boolean showName) {
        this.showName = showName;
    }

    public boolean isBaby() {
        return baby;
    }

    public void setBaby(boolean baby) {
        this.baby = baby;
    }

    public boolean isAngry() {
        return angry;
    }

    public void setAngry(boolean angry) {
        this.angry = angry;
    }

    public EntityType getType() {
        return type;
    }

    public void setType(EntityType type) {
        this.type = type;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public boolean isArmsRaised() {
        return armsRaised;
    }

    public void setArmsRaised(boolean armsRaised) {
        this.armsRaised = armsRaised;
    }

    public boolean isKnockbackImmune() {
        return knockbackImmune;
    }

    public void setKnockbackImmune(boolean knockbackImmune) {
        this.knockbackImmune = knockbackImmune;
    }

    public boolean isBurnImmune() {
        return burnImmune;
    }

    public void setBurnImmune(boolean burnImmune) {
        this.burnImmune = burnImmune;
    }

    public boolean isFallImmune() {
        return fallImmune;
    }

    public void setFallImmune(boolean fallImmune) {
        this.fallImmune = fallImmune;
    }

    public boolean isDryStreak() {
        return dryStreak;
    }

    public void setDryStreak(boolean dryStreak) {
        this.dryStreak = dryStreak;
    }

    public int getFollowRange() {
        return followRange;
    }

    public void setFollowRange(int followRange) {
        this.followRange = followRange;
    }

    public String getEquipmentInSlot(EquipmentSlot slot) {
        return equipment.get(slot);
    }

    public Map<EquipmentSlot, String> getEquipment() {
        return equipment;
    }

    public void setEquipment(Map<EquipmentSlot, String> equipment) {
        this.equipment = equipment;
    }

    public boolean isDrownImmune() {
        return drownImmune;
    }

    public void setDrownImmune(boolean drownImmune) {
        this.drownImmune = drownImmune;
    }

    public boolean isDryOutImmune() {
        return dryOutImmune;
    }

    public void setDryOutImmune(boolean dryOutImmune) {
        this.dryOutImmune = dryOutImmune;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public boolean isPowered() {
        return powered;
    }

    public void setPowered(boolean powered) {
        this.powered = powered;
    }
}
