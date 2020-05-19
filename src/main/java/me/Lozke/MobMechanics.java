package me.Lozke;

import me.Lozke.commands.*;
import me.Lozke.events.MobDamage;
import me.Lozke.events.SpawnerWandInteraction;
import me.Lozke.events.MobDeath;
import me.Lozke.events.SpawnerWandToggle;
import me.Lozke.managers.MobManager;
import me.Lozke.managers.SpawnerManager;
import me.Lozke.tasks.HealthNameFlashTask;
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

        PluginManager pm = Bukkit.getPluginManager();
        pm.registerEvents(new SpawnerWandInteraction(this), this);
        pm.registerEvents(new SpawnerWandToggle(), this);
        pm.registerEvents(new MobDeath(this), this);
        pm.registerEvents(new MobDamage(this), this);

        try {
            String name = AgorianRifts.getPluginInstance().getName();
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

        mobManager = new MobManager(this);

        HealthNameFlashTask healthNameFlashTask = new HealthNameFlashTask(mobManager);
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

    public MobManager getMobManager() {
        return mobManager;
    }
}
