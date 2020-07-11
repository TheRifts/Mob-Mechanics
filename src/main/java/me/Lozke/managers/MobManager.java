package me.Lozke.managers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import me.Lozke.MobMechanics;
import me.Lozke.data.ModifiableEntity;
import me.Lozke.data.CalamityMob;
import me.Lozke.data.Rarity;
import me.Lozke.data.Tier;
import me.Lozke.menus.MobSelector.MobSelectorMenu;
import me.Lozke.utils.Logger;
import me.Lozke.utils.Text;
import org.bukkit.Location;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent;

import java.io.*;
import java.util.*;

public class MobManager {

    private MobMechanics plugin;

    private HashMap<String, ModifiableEntity> loadedMobs = new HashMap<>();
    private HashMap<LivingEntity, CalamityMob> trackedEntities = new HashMap<>();

    //private MobSelector mobSelectorInstance;

    public MobManager(MobMechanics plugin) {
        this.plugin = plugin;
        //mobSelectorInstance = new MobSelector();
        loadMobs();
    }

    public void openEditor(Player player) {
        //mobSelectorInstance.showMenu(player);
        new MobSelectorMenu().openMenu(player);
    }

    public void loadMobs() {
        if (!new File(plugin.getDataFolder().getPath() + "/Mobs.json").exists()) {
            Logger.log("No mobs to load from Mobs.json");
            return;
        }
        try {
            ArrayList<ModifiableEntity> mobs = new GsonBuilder().setPrettyPrinting().create().fromJson(new FileReader(plugin.getDataFolder().getPath() + "/Mobs.json"), new TypeToken<ArrayList<ModifiableEntity>>(){}.getType());
            mobs.forEach(modifiableEntity -> loadedMobs.put(modifiableEntity.getId(), modifiableEntity));
        } catch (FileNotFoundException exception) {
            //todo: handle this exception
            exception.printStackTrace();
        }
    }

    public void saveMobs() {
        if (loadedMobs.isEmpty()) {
            Logger.log("No mobs to save to Mobs.json");
            return;
        }
        try (FileWriter writer = new FileWriter(new File(plugin.getDataFolder() + "/Mobs.json"))) {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            gson.toJson(loadedMobs.values(), writer);
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    public Collection<ModifiableEntity> getLoadedMobs() {
        return loadedMobs.values();
    }

    public void cacheMob(ModifiableEntity mob) {
        loadedMobs.put(mob.getId(), mob);
    }

    public CalamityMob spawnMob(Tier tier, Rarity rarity, String mobID, Location location) {
        ModifiableEntity mob = loadedMobs.get(mobID);
        if (mob == null) {
            Logger.log("Unable to spawn mob with the id \"" + mobID + "\" at " + location);
            return null;
        }
        return spawnMob(tier, rarity, mob, location);
    }

    public CalamityMob spawnMob(Tier tier, Rarity rarity, ModifiableEntity modifiableEntity, Location location) {
        CalamityMob spawnedMob = new CalamityMob(modifiableEntity, tier, rarity);
        spawnedMob.spawnEntity(location);
        trackEntity(spawnedMob);
        return spawnedMob;
    }

    public void trackEntity(CalamityMob mob) {
        trackedEntities.put(mob.getEntity(), mob);
    }

    public boolean isTracked(Entity entity) {
        return trackedEntities.containsKey(entity);
    }

    public CalamityMob asCalamityMob(Entity entity) {
        return trackedEntities.get(entity);
    }

    public boolean canResist(Entity entity, EntityDamageEvent.DamageCause damageCause) {
        if (!isTracked(entity)) {
            return false;
        }

        CalamityMob mob = trackedEntities.get((LivingEntity) entity);

        switch (damageCause) {
            case FIRE_TICK:
                entity.setFireTicks(0);
                return mob.isBurnImmune();
            default:
                return false;
        }
    }

    public void updateHealthDisplay(LivingEntity entity) {
        CalamityMob mob = trackedEntities.get(entity);

        if (mob == null) {
            return;
        }

        double hp = entity.getHealth();
        double maxHP = entity.getAttribute(Attribute.GENERIC_MAX_HEALTH).getBaseValue();
        double healthPercentage = hp/maxHP;
        String colorCode;
        if (healthPercentage >= 0.75) {
            colorCode = "&a";
        }
        else if (healthPercentage >= 0.25) {
            colorCode = "&e";
        }
        else {
            colorCode = "&c";
        }

        entity.setCustomName(Text.colorize(mob.getTier().getColorCode() + "[" + mob.getRarity().getSymbol() + "] " ) + trackedEntities.get(entity).getName() + Text.colorize(" " + colorCode + (int) Math.ceil(hp) + "&c❤"));
    }
}
