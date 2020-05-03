package me.Lozke;

import me.Lozke.commands.*;
import me.Lozke.events.ItemInteraction;
import me.Lozke.events.SpawnerWandToggleListener;
import me.Lozke.managers.SpawnerManager;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandMap;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.lang.reflect.Field;

public class MobMechanics extends JavaPlugin {

    private static MobMechanics plugin;
    private SpawnerManager spawnerManager;

    @Override
    public void onEnable() {
        plugin = this;

        PluginManager pm = Bukkit.getPluginManager();
        pm.registerEvents(new ItemInteraction(this), this);
        pm.registerEvents(new SpawnerWandToggleListener(), this);

        try {
            String name = FallingAutism.getPluginInstance().getName();
            Field bukkitCommandMap = Bukkit.getServer().getClass().getDeclaredField("commandMap");
            bukkitCommandMap.setAccessible(true);
            CommandMap commandMap = (CommandMap) bukkitCommandMap.get(Bukkit.getServer());
            commandMap.register(name, new Spawners());
            commandMap.register(name, new SpawnerWand());
            commandMap.register(name, new SpawnMob());
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }

        spawnerManager = new SpawnerManager(this);
        spawnerManager.loadSpawners();
    }

    @Override
    public void onDisable() {
        spawnerManager.saveSpawners();
        spawnerManager.hideSpawners();
    }

    public static MobMechanics getInstance() {
        return plugin;
    }

    public SpawnerManager getSpawnerManager() {
        return spawnerManager;
    }
}
