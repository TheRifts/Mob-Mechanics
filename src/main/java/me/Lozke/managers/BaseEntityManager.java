package me.Lozke.managers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import me.Lozke.MobMechanics;
import me.Lozke.data.*;
import me.Lozke.utils.*;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.*;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import java.io.*;
import java.lang.reflect.Field;
import java.util.*;

public class BaseEntityManager  {

    private MobMechanics plugin;
    private HashMap<String, BaseEntity> loadedMobs = new HashMap<>();

    public BaseEntityManager(MobMechanics plugin) {
        this.plugin = plugin;
        loadMobs();
    }

    public void loadMobs() {
        //TODO handle MobManager folder/file creation (probably in main onEnable)
        if (!new File(plugin.getDataFolder().getPath() + File.separator + "Mobs.json").exists()) {
            Logger.log("No mobs file (Mobs.json) detected");
            return;
        }
        loadedMobs = new HashMap<>(); //Guarantees only mobs on Mobs.json will be loaded.
        try {
            ArrayList<BaseEntity> mobs = new GsonBuilder().setPrettyPrinting().create().fromJson(new FileReader(plugin.getDataFolder().getPath() + "/Mobs.json"), new TypeToken<ArrayList<BaseEntity>>(){}.getType());
            mobs.forEach(modifiableEntity -> loadedMobs.put(modifiableEntity.getId(), modifiableEntity));
        } catch (FileNotFoundException exception) {
            //todo: handle this exception
            exception.printStackTrace();
        }
        plugin.registerCommandCompletion("mob-ids", loadedMobs.keySet());
    }

    public void saveMobs() {
        if (loadedMobs.isEmpty()) {
            Logger.log("No mobs to save to Mobs.json");
            return;
        }
        try (FileWriter writer = new FileWriter(new File(plugin.getDataFolder() + File.separator + "Mobs.json"))) {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            gson.toJson(loadedMobs.values(), writer);
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    public boolean isLoaded(String mobID) {
        for (BaseEntity loaded : loadedMobs.values()) {
            if (loaded.getId().equals(mobID)) {
                return true;
            }
        }
        return false;
    }

    public Collection<BaseEntity> getLoadedMobs() {
        return loadedMobs.values();
    }

    public BaseEntity getBaseEntity(String string) {
        return loadedMobs.get(string);
    }

    public void addBaseEntity(BaseEntity mob) {
        loadedMobs.put(mob.getId(), mob);
    }

    public RiftsMob spawnBaseEntity(MobSpawner spawner, Location location) {
        return spawnBaseEntity(spawner.getEntityID(), location, spawner.getTier(), spawner.getRarity());
    }
    public RiftsMob spawnBaseEntity(String entityID, Location location, Tier tier, Rarity rarity) {
        BaseEntity baseEntity = loadedMobs.get(entityID);
        if (baseEntity == null) {
            Logger.log("Unable to spawn mob with the id \"" + entityID + "\" at " + location);
            return null;
        }
        return spawnBaseEntity(baseEntity, location, tier, rarity);
    }
    public RiftsMob spawnBaseEntity(BaseEntity baseEntity, Location location, Tier tier, Rarity rarity) {
        if (location == null) {
            Logger.log("Unable to spawn mob with the id \"" + baseEntity.getId() + "\" due to null location");
        }

        assert baseEntity.getType().getEntityClass() != null;
        LivingEntity le = (LivingEntity) Objects.requireNonNull(location.getWorld().spawn(
                location, baseEntity.getType().getEntityClass()));

        if (le instanceof Zombie) {
            ((Zombie) le).setBaby(baseEntity.isBaby());
            if (le instanceof ZombieVillager) {
                if (baseEntity.getVillagerType() != null) {
                    ((ZombieVillager) le).setVillagerType(baseEntity.getVillagerType());
                }
                if (baseEntity.getVillagerProfession() != null) {
                    ((ZombieVillager) le).setVillagerProfession(baseEntity.getVillagerProfession());
                }
            }
        }
        else if (le instanceof Villager) {
            if (baseEntity.getVillagerType() != null) {
                ((Villager) le).setVillagerType(baseEntity.getVillagerType());
            }
            if (baseEntity.getVillagerProfession() != null) {
                ((Villager) le).setProfession(baseEntity.getVillagerProfession());
            }
        }
        else if (le instanceof Rabbit) {
            ((Rabbit) le).setRabbitType(Rabbit.Type.THE_KILLER_BUNNY);
            ((Rabbit) le).setAdult();
        }
        else if (le instanceof Creeper) {
            ((Creeper) le).setPowered(baseEntity.isPowered());
        }
        else if (le instanceof Slime) {
            if (baseEntity.getSize() < 1) {
                ((Slime) le).setSize(2 + (int) (Math.random() * 3));
            }
            else {
                ((Slime) le).setSize(Math.min(baseEntity.getSize(), 256));
            }
        }
        else if (le instanceof Phantom) {
            if (baseEntity.getSize() < 1) {
                ((Phantom) le).setSize(1 + (int) (Math.random() * 3));
            }
            else {
                ((Phantom) le).setSize(Math.min(baseEntity.getSize(), 64));
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
            ((Piglin) le).setBaby(baseEntity.isBaby());
        }
        else if (le instanceof PiglinBrute) {
            ((PiglinBrute) le).setImmuneToZombification(true);
            ((PiglinBrute) le).setBaby(baseEntity.isBaby());
        }
        else if (le instanceof Hoglin) {
            ((Hoglin) le).setImmuneToZombification(true);
        }

        if (le instanceof Ageable) {
            ((Ageable) le).setAgeLock(true);
            if (baseEntity.isBaby()) {
                ((Ageable) le).setBaby();
            }
            else {
                ((Ageable) le).setAdult();
            }
        }

        if (baseEntity.isAngry()) {
            if (le instanceof Wolf) {
                ((Wolf) le).setAngry(true);
            }
            else if (le instanceof Bee) {
                ((Bee) le).setAnger(Integer.MAX_VALUE);
            }
        }

        if (baseEntity.isKnockbackImmune() && le.getAttribute(Attribute.GENERIC_KNOCKBACK_RESISTANCE) != null) {
            le.getAttribute(Attribute.GENERIC_KNOCKBACK_RESISTANCE).setBaseValue(100);
        }

        StringBuilder mobName = new StringBuilder();
        ArrayList<String> prefixDictionary = baseEntity.getPrefixDictionary();
        if (prefixDictionary != null && !prefixDictionary.isEmpty()) {
            mobName.append(prefixDictionary.get(NumGenerator.index(prefixDictionary.size()))).append(" ");
        }
        mobName.append(baseEntity.getName());
        ArrayList<String> suffixDictionary = baseEntity.getSuffixDictionary();
        if (suffixDictionary != null && !suffixDictionary.isEmpty()) {
            mobName.append(" ").append(suffixDictionary.get(NumGenerator.index(suffixDictionary.size())));
        }
        le.getPersistentDataContainer().set(MobNamespacedKey.CUSTOM_NAME.getNamespacedKey(), MobNamespacedKey.CUSTOM_NAME.getDataType(), mobName.toString());
        le.setCustomName(mobName.toString());
        le.setCustomNameVisible(baseEntity.isShowName());

        le.setRemoveWhenFarAway(false);
        le.setCanPickupItems(false);

        NamespacedKeyWrapper wrapper = new NamespacedKeyWrapper(le.getPersistentDataContainer());
        wrapper.addKey(ARNamespacedKey.TIER, tier.name());
        wrapper.addKey(ARNamespacedKey.RARITY, rarity.name());
        wrapper.addKey(ARNamespacedKey.MOB_ID, baseEntity.getId());

        RiftsMob riftsMob = new RiftsMob(le, tier, rarity);
        riftsMob.setHome(location);
        riftsMob.setBaseEntityID(baseEntity.getId());

        int minHp = ItemFactory.getMobHP(tier, rarity, ItemFactory.RangeType.LOW);
        int maxHP = ItemFactory.getMobHP(tier, rarity, ItemFactory.RangeType.HIGH);
        riftsMob.setBaseStat(RiftsStat.HP, NumGenerator.rollInclusive(minHp, maxHP));
        riftsMob.setBaseStat(RiftsStat.DMG_LO, ItemFactory.getDamage(tier, rarity, ItemFactory.RangeType.LOW));
        riftsMob.setBaseStat(RiftsStat.DMG_HI, ItemFactory.getDamage(tier, rarity, ItemFactory.RangeType.HIGH));
        riftsMob.updateStats();

        applyEquipment(baseEntity, le, riftsMob);
        applyMount(riftsMob);

        return riftsMob;
    }

    private void applyEquipment(BaseEntity be, LivingEntity le, RiftsMob rm) {
        if (le.getEquipment() == null) return;
        le.getEquipment().setHelmet(Items.formatItem(Material.STONE_BUTTON, ""));

        Map<EquipmentSlot, String> equipment = be.getEquipment();
        if (equipment == null) equipment = new HashMap<>();
        for (EquipmentSlot slot : EquipmentSlot.values()) {
            ItemStack stack;
            String value = equipment.get(slot);
            if (value == null) {
                if (slot == EquipmentSlot.OFF_HAND) continue;
                if (slot == EquipmentSlot.HEAD && be.getHeadBase64() != null) {
                    applyBase64Head(be, le);
                    continue;
                }
                if (slot == EquipmentSlot.HAND) {
                    if (be.getWeaponTypes() != null)
                        stack = be
                                .getWeaponTypes()
                                .get(NumGenerator.index(be.getWeaponTypes().size()))
                                .getItem(rm.getTier());
                    else stack = WeaponType.getRandomItem(rm.getTier());
                }
                else stack = ArmourType.fromEquipmentSlot(slot).getItem(rm.getTier());
            }
            else {
                stack = new ItemStack(Material.valueOf(value));
            }
            if (slot == EquipmentSlot.HAND) {
                rm.setWeaponType(WeaponType.getWeaponType(stack));
            }
            applyEquipment(le, slot, stack);
        }
    }

    private void applyBase64Head(BaseEntity baseEntity, LivingEntity le) {
        ItemStack head = new ItemStack(Material.PLAYER_HEAD, 1, (short)3);
        SkullMeta meta = (SkullMeta) head.getItemMeta();
        GameProfile profile = new GameProfile(UUID.randomUUID(), "");
        profile.getProperties().put("textures", new Property("textures", baseEntity.getHeadBase64()));
        Field profileField;
        try {
            profileField = meta.getClass().getDeclaredField("profile");
            profileField.setAccessible(true);
            profileField.set(meta, profile);
        } catch (IllegalArgumentException | IllegalAccessException | NoSuchFieldException | SecurityException e) {
            e.printStackTrace();
        }
        head.setItemMeta(meta);
        le.getEquipment().setHelmet(head);
    }

    private void applyEquipment(LivingEntity le, EquipmentSlot slot, ItemStack equipment) {
        if (le == null || !le.isValid()) return;
        if (le.getEquipment() != null) {
            le.getEquipment().setItem(slot, equipment);
        }
    }

    public void applyMount(RiftsMob riftsMob) {
        if (riftsMob.getEntity() == null || !riftsMob.getEntity().isValid()) return;
        BaseEntity baseEntity = getBaseEntity(riftsMob.getBaseEntityID());
        BaseEntity mountTemplate = getBaseEntity(baseEntity.getMount());
        if (mountTemplate == null) return;
        spawnBaseEntity(mountTemplate, riftsMob.getEntity().getLocation(), riftsMob.getTier(), riftsMob.getRarity())
                .getEntity()
                .setPassenger(riftsMob.getEntity());
    }
}