package me.Lozke;

import co.aikar.commands.BukkitCommandManager;
import me.Lozke.commands.*;
import me.Lozke.listeners.*;
import me.Lozke.managers.MobManager;
import me.Lozke.managers.SpawnerManager;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandMap;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.lang.reflect.Field;
import java.util.Collection;

public class MobMechanics extends JavaPlugin {

    private static MobMechanics plugin;
    private BukkitCommandManager bukkitCommandManager;

    private SpawnerManager spawnerManager;
    private MobManager mobManager;

    @Override
    public void onEnable() {
        plugin = this;

        bukkitCommandManager = new BukkitCommandManager(this);

        mobManager = new MobManager(this);
        spawnerManager = new SpawnerManager(this);

        PluginManager pm = Bukkit.getPluginManager();
        pm.registerEvents(new SpawnerWandInteraction(this), this);
        pm.registerEvents(new SpawnerWandToggle(), this);
        pm.registerEvents(new MobDeath(this), this);
        pm.registerEvents(new MobDamagedListener(this), this);
        pm.registerEvents(new SlimeSplitListener(mobManager), this);
        pm.registerEvents(new SlimeJumpListener(), this);

        bukkitCommandManager.registerCommand(new Mobs());
        bukkitCommandManager.registerCommand(new Spawners());
        bukkitCommandManager.registerCommand(new SpawnerWand());
        bukkitCommandManager.registerCommand(new SpawnMob());

        try {
            String name = AgorianRifts.getPluginInstance().getName();
            Field bukkitCommandMap = Bukkit.getServer().getClass().getDeclaredField("commandMap");
            bukkitCommandMap.setAccessible(true);
            CommandMap commandMap = (CommandMap) bukkitCommandMap.get(Bukkit.getServer());
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

    public void registerCommandCompletion(String id, Collection<String> completions) {
        bukkitCommandManager.getCommandCompletions().registerAsyncCompletion(id, c -> completions);
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
