package me.Lozke;

import me.Lozke.commands.*;
import me.Lozke.listeners.*;
import me.Lozke.managers.MobManager;
import me.Lozke.managers.SpawnerManager;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandMap;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.lang.reflect.Field;

public class MobMechanics extends JavaPlugin {

    private static MobMechanics plugin;

    private SpawnerManager spawnerManager;
    private MobManager mobManager;

    @Override
    public void onEnable() {
        plugin = this;

        mobManager = new MobManager(this);
        spawnerManager = new SpawnerManager(this);

        PluginManager pm = Bukkit.getPluginManager();
        pm.registerEvents(new SpawnerWandInteraction(this), this);
        pm.registerEvents(new SpawnerWandToggle(), this);
        pm.registerEvents(new MobDeath(this), this);
        pm.registerEvents(new CombatListener(this), this);

        try {
            String name = AgorianRifts.getPluginInstance().getName();
            Field bukkitCommandMap = Bukkit.getServer().getClass().getDeclaredField("commandMap");
            bukkitCommandMap.setAccessible(true);
            CommandMap commandMap = (CommandMap) bukkitCommandMap.get(Bukkit.getServer());
            commandMap.register(name, new Spawners());
            commandMap.register(name, new SpawnerWand());
            commandMap.register(name, new SpawnMob());
            commandMap.register(name, new Mobs());
            commandMap.register(name, new HoloTest());
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDisable() {
        spawnerManager.saveSpawners();
        spawnerManager.hideSpawners();
        Bukkit.getScheduler().cancelTasks(this);
    }

    public static MobMechanics getInstance() {
        return plugin;
    }

    public SpawnerManager getSpawnerManager() {
        return spawnerManager;
    }

    public MobManager getMobManager() {
        return mobManager;
    }
}
