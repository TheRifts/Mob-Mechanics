package me.Lozke.data;


import me.Lozke.MobMechanics;
import me.Lozke.data.PersistentDataType.BooleanDataType;
import me.Lozke.data.PersistentDataType.ListDataType;
import me.Lozke.data.PersistentDataType.MapDataType;
import me.Lozke.data.PersistentDataType.UUIDDataType;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.*;

public enum MobNamespacedKey {
    CUSTOM_NAME(new NamespacedKey(getPlugin(), "custom-name"), PersistentDataType.STRING, "");

    public static ARNamespacedKey[] types = ARNamespacedKey.values();

    private NamespacedKey namespacedKey;
    private PersistentDataType dataType;
    private Object defaultKey;

    MobNamespacedKey(NamespacedKey namespacedKey, PersistentDataType dataType, Object defaultKey) {
        this.namespacedKey = namespacedKey;
        this.dataType = dataType;
        this.defaultKey = defaultKey;
    }

    public NamespacedKey getNamespacedKey() {
        return namespacedKey;
    }

    public PersistentDataType getDataType() {
        return dataType;
    }

    public Object getDefaultKey() {
        return defaultKey;
    }

    public static ItemStack addToItem(ItemStack item, NamespacedKey namespacedKey, PersistentDataType dataType, Object key) {
        ItemMeta itemMeta = item.getItemMeta() == null ? Bukkit.getServer().getItemFactory().getItemMeta(item.getType()) : item.getItemMeta();
        itemMeta.getPersistentDataContainer().set(namespacedKey, dataType, key);
        item.setItemMeta(itemMeta);
        return item;
    }

    public static ItemStack addToItem(ItemStack item, MobNamespacedKey namespacedKey, Object key) {
        return addToItem(item, namespacedKey.namespacedKey, namespacedKey.dataType, key);
    }

    public static ItemStack addToItem(ItemStack item, MobNamespacedKey namespacedKey) {
        return addToItem(item, namespacedKey.namespacedKey, namespacedKey.dataType, namespacedKey.defaultKey);
    }

    private static PersistentDataType<byte[], Map> getMapDataType() {
        return new MapDataType();
    }

    private static PersistentDataType<byte[], List> getListDataType() {
        return new ListDataType();
    }

    private static PersistentDataType<byte[], Boolean> getBooleanDataType() {
        return new BooleanDataType();
    }

    private static PersistentDataType<byte[], java.util.UUID> getUUIDDataType() {
        return new UUIDDataType();
    }

    private static MobMechanics getPlugin() {
        return MobMechanics.getInstance();
    }
}
