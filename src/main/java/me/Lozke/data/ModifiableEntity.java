package me.Lozke.data;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import me.Lozke.utils.Items;
import me.Lozke.utils.NumGenerator;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.*;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import java.lang.reflect.Field;
import java.util.*;

public class ModifiableEntity {

    private String id;
    private String name;
    private ArrayList<String> prefixDictionary;
    private ArrayList<String> suffixDictionary;
    private EntityType type;
    private String mount;
    private Integer followRange;
    private Boolean showName;
    private Integer size;
    private Integer minSize;
    private Boolean canSplit;
    private Integer splitSpawnCount;
    private Boolean baby;
    private Boolean angry;
    private Boolean armsRaised;
    private Boolean powered;
    private Boolean knockbackImmune;
    private Boolean burnImmune;
    private Boolean fallImmune;
    private Boolean drownImmune;
    private Boolean dryOutImmune;
    private Boolean dryStreak;
    private String headBase64;
    private ArrayList<WeaponType> weaponTypes;
    private Map<EquipmentSlot, String> equipment;

    public LivingEntity spawnEntity(Location location) {
        LivingEntity le = (LivingEntity) location.getWorld().spawnEntity(location, type);

        if (le instanceof Zombie) {
            if (baby == null) ((Zombie) le).setBaby(false);
            else ((Zombie) le).setBaby(baby);
        }
        else if (le instanceof Rabbit) {
            ((Rabbit) le).setRabbitType(Rabbit.Type.THE_KILLER_BUNNY);
            ((Rabbit) le).setAdult();
        }
        else if (le instanceof Creeper) {
            if (powered == null) ((Creeper) le).setPowered(false);
            else ((Creeper) le).setPowered(powered);
        }
        else if (le instanceof Slime) {
            if (size == null || size < 1) {
                ((Slime) le).setSize(2 + (int) (Math.random() * 3));
            }
            else {
                ((Slime) le).setSize(Math.min(size, 256));
            }
        }
        else if (le instanceof Phantom) {
            if (size == null || size < 1) {
                ((Phantom) le).setSize(1 + (int) (Math.random() * 3));
            }
            else {
                ((Phantom) le).setSize(Math.min(size, 64));
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
            if (baby == null) ((Piglin) le).setBaby(false);
            else ((Piglin) le).setBaby(baby);
        }
        else if (le instanceof Hoglin) {
            ((Hoglin) le).setImmuneToZombification(true);
        }

        if (le instanceof Ageable) {
            ((Ageable) le).setAgeLock(true);
            if (baby != null && baby) {
                ((Ageable) le).setBaby();
            }
            else {
                ((Ageable) le).setAdult();
            }
        }

        if (angry != null && angry) {
            if (le instanceof Wolf) {
                ((Wolf) le).setAngry(true);
            }
            else if (le instanceof Bee) {
                ((Bee) le).setAnger(Integer.MAX_VALUE);
            }
        }

        if (knockbackImmune != null && knockbackImmune && le.getAttribute(Attribute.GENERIC_KNOCKBACK_RESISTANCE) != null) {
            le.getAttribute(Attribute.GENERIC_KNOCKBACK_RESISTANCE).setBaseValue(100);
        }

        StringBuilder mobName = new StringBuilder();
        if (prefixDictionary != null && !prefixDictionary.isEmpty()) {
            mobName.append(prefixDictionary.get(NumGenerator.index(prefixDictionary.size()))).append(" ");
        }
        mobName.append(name);
        if (suffixDictionary != null && !suffixDictionary.isEmpty()) {
            mobName.append(" ").append(suffixDictionary.get(NumGenerator.index(suffixDictionary.size())));
        }
        le.getPersistentDataContainer().set(MobNamespacedKey.CUSTOM_NAME.getNamespacedKey(), MobNamespacedKey.CUSTOM_NAME.getDataType(), mobName.toString());
        le.setCustomName(mobName.toString());
        le.setCustomNameVisible(showName);

        le.setCanPickupItems(false);

        le.getEquipment().setHelmet(Items.formatItem(Material.STONE_BUTTON, ""));
        applyBase64Head(le);

        return le;
    }

    public void apply(ModifiableEntity newModifiableEntity) {
        this.id = newModifiableEntity.id;
        this.name = newModifiableEntity.name;
        this.prefixDictionary = newModifiableEntity.prefixDictionary;
        this.suffixDictionary = newModifiableEntity.suffixDictionary;
        this.type = newModifiableEntity.type;
        this.mount = newModifiableEntity.mount;
        this.followRange = newModifiableEntity.followRange;
        this.showName = newModifiableEntity.showName;
        this.size = newModifiableEntity.size;
        this.minSize = newModifiableEntity.minSize;
        this.canSplit = newModifiableEntity.canSplit;
        this.splitSpawnCount = newModifiableEntity.splitSpawnCount;
        this.baby = newModifiableEntity.baby;
        this.angry = newModifiableEntity.angry;
        this.armsRaised = newModifiableEntity.armsRaised;
        this.knockbackImmune = newModifiableEntity.knockbackImmune;
        this.burnImmune = newModifiableEntity.burnImmune;
        this.fallImmune = newModifiableEntity.fallImmune;
        this.drownImmune = newModifiableEntity.drownImmune;
        this.dryOutImmune = newModifiableEntity.dryOutImmune;
        this.dryStreak = newModifiableEntity.dryStreak;
        this.headBase64 = newModifiableEntity.headBase64;
        this.weaponTypes = newModifiableEntity.weaponTypes;
        this.equipment = newModifiableEntity.equipment;
    }

    private void applyBase64Head(LivingEntity entity) {
        if (headBase64 == null) return;
        ItemStack head = new ItemStack(Material.PLAYER_HEAD, 1, (short)3);
        SkullMeta meta = (SkullMeta) head.getItemMeta();
        GameProfile profile = new GameProfile(UUID.randomUUID(), "");
        profile.getProperties().put("textures", new Property("textures", headBase64.toString()));
        Field profileField;
        try {
            profileField = meta.getClass().getDeclaredField("profile");
            profileField.setAccessible(true);
            profileField.set(meta, profile);
        } catch (IllegalArgumentException | IllegalAccessException | NoSuchFieldException | SecurityException e) {
            e.printStackTrace();
        }
        head.setItemMeta(meta);
        entity.getEquipment().setHelmet(head);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public ArrayList<String> getPrefixDictionary() {
        return prefixDictionary;
    }

    public void setPrefixDictionary(ArrayList<String> prefixDictionary) {
        this.prefixDictionary = prefixDictionary;
    }

    public ArrayList<String> getSuffixDictionary() {
        return suffixDictionary;
    }

    public void setSuffixDictionary(ArrayList<String> suffixDictionary) {
        this.suffixDictionary = suffixDictionary;
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

    public String getMount() {
        return mount;
    }

    public void setMount(String mount) {
        this.mount = mount;
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

    public boolean isSplittable() {
        return canSplit;
    }

    public void setCanSplit(boolean canSplit) {
        this.canSplit = canSplit;
    }

    public int getMinSize() {
        return minSize;
    }

    public void setMinSize(int minSize) {
        this.minSize = minSize;
    }

    public int getSplitSpawnCount() {
        return splitSpawnCount;
    }

    public void setSplitSpawnCount(int splitSpawnCount) {
        this.splitSpawnCount = splitSpawnCount;
    }

    public String getHeadBase64() {
        return headBase64;
    }

    public void setHeadBase64(String headBase64) {
        this.headBase64 = headBase64;
    }

    public ArrayList<WeaponType> getWeaponTypes() {
        return weaponTypes;
    }

    public void setWeaponTypes(ArrayList<WeaponType> weaponTypes) {
        this.weaponTypes = weaponTypes;
    }

    public void addWeaponType(WeaponType type) {
        if (weaponTypes == null) weaponTypes = new ArrayList<>();
        weaponTypes.add(type);
    }

    public void removeWeaponType(WeaponType type) {
        if (weaponTypes != null) weaponTypes.remove(type);
    }
}
