package me.Lozke.data;

import me.Lozke.MobMechanics;
import me.Lozke.utils.NamespacedKeyWrapper;
import org.bukkit.NamespacedKey;
import org.bukkit.persistence.PersistentDataAdapterContext;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

public class SpawnerWandPersistentDataType implements PersistentDataType<PersistentDataContainer, MobSpawner> {

    public static final NamespacedKey DATA_TAG = key("spawner-data");

    @Override
    public Class<PersistentDataContainer> getPrimitiveType() {
        return PersistentDataContainer.class;
    }

    @Override
    public Class<MobSpawner> getComplexType() {
        return MobSpawner.class;
    }

    @Override
    public PersistentDataContainer toPrimitive(MobSpawner spawner, PersistentDataAdapterContext persistentDataAdapterContext) {
        NamespacedKeyWrapper wrapper = new NamespacedKeyWrapper(persistentDataAdapterContext.newPersistentDataContainer());
        wrapper.addKey(ARNamespacedKey.TIER, spawner.getTier().name());
        wrapper.addKey(ARNamespacedKey.RARITY, spawner.getRarity().name());
        wrapper.addString(key("ENTITY-ID"), spawner.getEntityID());
        wrapper.addBoolean(key("ELITE"), spawner.isElite());
        wrapper.addBoolean(key("ACTIVE"), spawner.isSpawnerActive());
        wrapper.addInt(key("TIMER"), spawner.getSpawnTime());
        wrapper.addInt(key("RADIUS"), spawner.getRadius());
        wrapper.addInt(key("RANGE"), spawner.getActiveRange());
        wrapper.addInt(key("AMOUNT"), spawner.getAmount());
        wrapper.addInt(key("MAX-AMOUNT"), spawner.getMaxMobAmount());
        wrapper.addInt(key("LEASH"), spawner.getLeashRange());
        return wrapper.getDataContainer();
    }

    @Override
    public MobSpawner fromPrimitive(PersistentDataContainer container, PersistentDataAdapterContext persistentDataAdapterContext) {
        NamespacedKeyWrapper wrapper = new NamespacedKeyWrapper(container);
        Tier tier = Tier.valueOf(wrapper.getString(ARNamespacedKey.TIER));
        Rarity rarity = Rarity.valueOf(wrapper.getString(ARNamespacedKey.RARITY));
        String entityID = wrapper.getString(key("ENTITY-ID"));
        boolean elite = wrapper.getBoolean(key("ELITE"));
        boolean spawnerActive = wrapper.getBoolean(key("ACTIVE"));
        int timer = wrapper.getInt(key("TIMER"));
        int radius = wrapper.getInt(key("RADIUS"));
        int activeRange = wrapper.getInt(key("RANGE"));
        int spawnAmount = wrapper.getInt(key("AMOUNT"));
        int maxMobAmount = wrapper.getInt(key("MAX-AMOUNT"));
        int leashRange = wrapper.getInt(key("LEASH"));
        return new MobSpawner(tier, rarity, entityID, elite, spawnerActive, timer, radius, activeRange, spawnAmount, maxMobAmount, leashRange);
    }

    private static NamespacedKey key(String string) {
        return new NamespacedKey(MobMechanics.getInstance(), string);
    }
}
