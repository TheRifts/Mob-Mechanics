package me.Lozke.data;

import org.bukkit.entity.*;
import org.bukkit.inventory.EquipmentSlot;

import java.util.*;

public class BaseEntity {

    private String id;
    private String name;
    private ArrayList<String> abilities;
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
    private Villager.Profession professionType;
    private Villager.Type villagerType;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<String> getAbilities() {
        return abilities;
    }

    public void setAbilities(ArrayList<String> abilities) {
        this.abilities = abilities;
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
        if (showName == null) return false;
        return showName;
    }

    public void setShowName(boolean showName) {
        this.showName = showName;
    }

    public boolean isBaby() {
        if (baby == null) return false;
        return baby;
    }

    public void setBaby(boolean baby) {
        this.baby = baby;
    }

    public boolean isAngry() {
        if (angry == null) return false;
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
        if (armsRaised == null) return false;
        return armsRaised;
    }

    public void setArmsRaised(boolean armsRaised) {
        this.armsRaised = armsRaised;
    }

    public boolean isKnockbackImmune() {
        if (knockbackImmune == null) return false;
        return knockbackImmune;
    }

    public void setKnockbackImmune(boolean knockbackImmune) {
        this.knockbackImmune = knockbackImmune;
    }

    public boolean isBurnImmune() {
        if (burnImmune == null) return false;
        return burnImmune;
    }

    public void setBurnImmune(boolean burnImmune) {
        this.burnImmune = burnImmune;
    }

    public boolean isFallImmune() {
        if (fallImmune == null) return false;
        return fallImmune;
    }

    public void setFallImmune(boolean fallImmune) {
        this.fallImmune = fallImmune;
    }

    public boolean isDryStreak() {
        if (dryStreak == null) return false;
        return dryStreak;
    }

    public void setDryStreak(boolean dryStreak) {
        this.dryStreak = dryStreak;
    }

    public int getFollowRange() {
        if (showName == null) return 0;
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
        if (drownImmune == null) return false;
        return drownImmune;
    }

    public void setDrownImmune(boolean drownImmune) {
        this.drownImmune = drownImmune;
    }

    public boolean isDryOutImmune() {
        if (dryOutImmune == null) return false;
        return dryOutImmune;
    }

    public void setDryOutImmune(boolean dryOutImmune) {
        this.dryOutImmune = dryOutImmune;
    }

    public int getSize() {
        if (size == null) return 0;
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public boolean isPowered() {
        if (powered == null) return false;
        return powered;
    }

    public void setPowered(boolean powered) {
        this.powered = powered;
    }

    public boolean isSplittable() {
        if (canSplit == null) return false;
        return canSplit;
    }

    public void setCanSplit(boolean canSplit) {
        this.canSplit = canSplit;
    }

    public int getMinSize() {
        if (minSize == null) return 0;
        return minSize;
    }

    public void setMinSize(int minSize) {
        this.minSize = minSize;
    }

    public int getSplitSpawnCount() {
        if (splitSpawnCount == null) return 0;
        return splitSpawnCount;
    }

    public void setSplitSpawnCount(int splitSpawnCount) {
        this.splitSpawnCount = splitSpawnCount;
    }

    public Villager.Type getVillagerType() {
        return villagerType;
    }
    public void setVillagerType(Villager.Type villagerType) {
        this.villagerType = villagerType;
    }

    public Villager.Profession getVillagerProfession() {
        return professionType;
    }
    public void setVillagerProfession(Villager.Profession villagerProfession) {
        this.professionType = villagerProfession;
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
