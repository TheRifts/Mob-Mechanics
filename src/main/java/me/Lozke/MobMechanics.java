package me.Lozke;

import co.aikar.commands.BukkitCommandManager;
import me.Lozke.commands.*;
import me.Lozke.data.Rarity;
import me.Lozke.data.Tier;
import me.Lozke.listeners.*;
import me.Lozke.managers.AbilityManager;
import me.Lozke.managers.EffectManager;
import me.Lozke.managers.BaseEntityManager;
import me.Lozke.managers.MobManager;
import me.Lozke.managers.SpawnerManager;
import me.Lozke.tasks.TickAbilitiesTask;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandMap;
import org.bukkit.entity.EntityType;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class MobMechanics extends JavaPlugin {

    private static MobMechanics plugin;
    private BukkitCommandManager bukkitCommandManager;

    private BaseEntityManager baseEntityManager;
    private AbilityManager abilityManager;
    private EffectManager effectManager;
    private SpawnerManager spawnerManager;
    private MobManager mobManager;

    @Override
    public void onEnable() {
        plugin = this;

        bukkitCommandManager = new BukkitCommandManager(this);

        effectManager = new EffectManager(this);
        abilityManager = new AbilityManager(this);
        baseEntityManager = new BaseEntityManager(this);
        mobManager = new MobManager(this);
        spawnerManager = new SpawnerManager(this);

        new TickAbilitiesTask(abilityManager).runTaskTimer(this, 60L, 5L);

        PluginManager pm = Bukkit.getPluginManager();
        pm.registerEvents(new SpawnerWandInteraction(this), this);
        pm.registerEvents(new SpawnerWandToggle(), this);
        pm.registerEvents(new SpawnerSneakListener(spawnerManager), this);
        pm.registerEvents(new MobDeath(this), this);
        pm.registerEvents(new MobDamagedListener(this), this);
        pm.registerEvents(new SlimeSplitListener(this), this);
        pm.registerEvents(new SlimeJumpListener(), this);
        pm.registerEvents(new MobUnloadListener(this), this);
        pm.registerEvents(new CreatureSpawnListener(), this);

        registerCommandCompletion("tier", Stream.of(Tier.types).map(Enum::name).collect(Collectors.toList()));
        registerCommandCompletion("rarity", Stream.of(Rarity.types).map(Enum::name).collect(Collectors.toList()));
        registerCommandCompletion("entity", Stream.of(EntityType.values()).map(Enum::name).collect(Collectors.toList()));

        bukkitCommandManager.registerCommand(new Abilities());
        bukkitCommandManager.registerCommand(new Effects());
        bukkitCommandManager.registerCommand(new CreateMob(baseEntityManager));
        bukkitCommandManager.registerCommand(new Mobs());
        bukkitCommandManager.registerCommand(new Slaughter(mobManager));
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

        mobManager.stopTrackingAllMobs();

        Bukkit.getScheduler().cancelTasks(this);
    }

    public void registerCommandCompletion(String id, Collection<String> completions) {
        bukkitCommandManager.getCommandCompletions().registerAsyncCompletion(id, c -> completions);
    }

    public static MobMechanics getInstance() {
        return plugin;
    }

    public BaseEntityManager getBaseEntityManager() {
        return baseEntityManager;
    }

    public AbilityManager getAbilityManager() {
        return abilityManager;
    }

    public EffectManager getEffectManager() {
        return effectManager;
    }

    public SpawnerManager getSpawnerManager() {
        return spawnerManager;
    }

    public MobManager getMobManager() {
        return mobManager;
    }
}
