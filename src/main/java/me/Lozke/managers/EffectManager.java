package me.Lozke.managers;

import com.google.gson.reflect.TypeToken;
import me.Lozke.MobMechanics;
import me.Lozke.data.Abilities.Effects.BlockBreak;
import me.Lozke.data.Abilities.Effects.Damage;
import me.Lozke.data.Abilities.Effects.Effect;
import me.Lozke.data.Abilities.Effects.Fart;
import me.Lozke.data.RiftsMob;
import me.Lozke.utils.GsonUtils;
import me.Lozke.utils.Logger;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

import java.io.*;
import java.util.*;

public class EffectManager {

    private MobMechanics plugin;

    private HashMap<String, Effect> loadedEffects = new HashMap<>();

    public EffectManager(MobMechanics plugin) {
        this.plugin = plugin;
        new Fart();
        new Damage();
        new BlockBreak();
        loadEffects();
    }

    public void loadEffects() {
        //TODO handle AbilityManager folder/file creation (probably in main onEnable)
        if (!new File(plugin.getDataFolder().getPath() + File.separator + "Abilities.json").exists()) {
            Logger.log("No effect file (Abilities.json) detected");
            return;
        }
        loadedEffects = new HashMap<>();
        try {
            ArrayList<Effect> effects = GsonUtils.getGson().fromJson(new FileReader(plugin.getDataFolder().getPath() + File.separator + "Effects.json"), new TypeToken<ArrayList<Effect>>(){}.getType());
            if (effects == null || effects.isEmpty()) return;
            effects.forEach(effect -> loadedEffects.put(effect.getID(), effect));
        } catch (FileNotFoundException exception) {
            //todo: handle this exception
            exception.printStackTrace();
        }
        plugin.registerCommandCompletion("effect-ids", loadedEffects.keySet());
    }

    public void saveEffects() {
        if (loadedEffects.isEmpty()) {
            Logger.log("No effects to save to Effects.json");
            return;
        }
        try (FileWriter writer = new FileWriter(new File(plugin.getDataFolder() + File.separator + "Effects.json"))) {
            GsonUtils.getGson().toJson(loadedEffects.values(), writer);
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    public boolean isValidTarget(Entity entity) {
        if (!entity.isValid() || entity.isInvulnerable() || !(entity instanceof LivingEntity) || entity instanceof ArmorStand) {
            return true;
        }
        if (entity instanceof Player) {
            return ((Player) entity).getGameMode() == GameMode.CREATIVE || ((Player) entity).getGameMode() == GameMode.SPECTATOR;
        }
        return false;
    }

    public Set<LivingEntity> getTargets(RiftsMob caster, Effect effect) {
        HashSet<LivingEntity> targets = new HashSet<>();
        switch (effect.getTarget()) {
            case AREA:
                Location loc = caster.getEntity().getLocation();
                targets = new HashSet<>(loc.getNearbyLivingEntities(effect.getRange()));
                targets.removeIf(this::isValidTarget);
                break;
            case SELF:
            default:
                targets.add(caster.getEntity());
        }
        return targets;
    }

    public void execute(RiftsMob caster, ArrayList<String> effects) {
        for (String effectID : effects) {
            Effect effect = loadedEffects.get(effectID);
            if (effect == null) {
                Logger.warn(plugin, caster.getBaseEntityID() + " has an ability with an invalid effect: " + effectID);
                return;
            }
            for (LivingEntity target : getTargets(caster, effect)) {
                effect.execute(caster, target);
            }
        }
    }
}
