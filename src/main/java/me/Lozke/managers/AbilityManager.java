package me.Lozke.managers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import me.Lozke.MobMechanics;
import me.Lozke.data.Abilities.Ability;
import me.Lozke.data.Abilities.AbilityCooldownContainer;
import me.Lozke.data.Abilities.AbilityTriggerReason;
import me.Lozke.data.RiftsMob;
import me.Lozke.utils.Logger;
import org.bukkit.entity.LivingEntity;

import java.io.*;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class AbilityManager {

    private MobMechanics plugin;

    private Map<String, Ability> loadedAbilities = new HashMap<>();
    private Map<LivingEntity, Set<AbilityCooldownContainer>> coolingAbilities = new ConcurrentHashMap<>();

    public AbilityManager(MobMechanics plugin) {
        this.plugin = plugin;
        loadAbilities();
    }

    public void loadAbilities() {
        //TODO handle AbilityManager folder/file creation (probably in main onEnable)
        if (!new File(plugin.getDataFolder().getPath() + File.separator + "Abilities.json").exists()) {
            Logger.log("No ability file (Abilities.json) detected");
            return;
        }
        loadedAbilities = new HashMap<>();
        try {
            ArrayList<Ability> abilities = new GsonBuilder().setPrettyPrinting().create().fromJson(new FileReader(plugin.getDataFolder().getPath() + File.separator + "Abilities.json"), new TypeToken<ArrayList<Ability>>(){}.getType());
            if (abilities == null || abilities.isEmpty()) return;
            abilities.forEach(ability -> loadedAbilities.put(ability.getID(), ability));
        } catch (FileNotFoundException exception) {
            //todo: handle this exception
            exception.printStackTrace();
        }
        plugin.registerCommandCompletion("ability-ids", loadedAbilities.keySet());
    }

    public void saveAbilities() {
        if (loadedAbilities.isEmpty()) {
            Logger.log("No abilities to save to Abilities.json");
            return;
        }
        try (FileWriter writer = new FileWriter(new File(plugin.getDataFolder() + File.separator + "Abilities.json"))) {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            gson.toJson(loadedAbilities.values(), writer);
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    public void tickCooldowns() {
        for (Map.Entry<LivingEntity, Set<AbilityCooldownContainer>> entry : coolingAbilities.entrySet()) {
            if (entry.getKey() == null || !entry.getKey().isValid() || entry.getValue() == null || entry.getValue().isEmpty()) {
                coolingAbilities.remove(entry.getKey());
            }
            entry.getValue().removeIf(AbilityCooldownContainer::isOver);
        }
    }

    public void cooldownAbility(LivingEntity entity, Ability ability) {
        Set<AbilityCooldownContainer> coolingSet = coolingAbilities.getOrDefault(entity, new HashSet<>());
        coolingSet.add(new AbilityCooldownContainer(ability));
        coolingAbilities.put(entity, coolingSet);
    }

    public AbilityCooldownContainer getAbilityContainer(LivingEntity entity, Ability ability) {
        Set<AbilityCooldownContainer> coolingSet = coolingAbilities.get(entity);

        if (coolingSet == null || coolingSet.isEmpty()) {
            return null;
        }

        for (AbilityCooldownContainer container : coolingSet) {
            if (container.getID().equals(ability.getID())) {
                return container;
            }
        }

        return null;
    }

    public boolean canCast(LivingEntity entity, Ability ability) {
        AbilityCooldownContainer container = getAbilityContainer(entity, ability);
        return container == null;
    }

    public void cast(RiftsMob mob, AbilityTriggerReason reason) {
        ArrayList<String> abilities = plugin.getBaseEntityManager().getBaseEntity(mob.getBaseEntityID()).getAbilities();
        if (abilities == null) return;
        for (String id : abilities) {
            Ability ability = loadedAbilities.get(id);
            if (ability == null) return;
            if (canCast(mob.getEntity(), ability)) {
                cooldownAbility(mob.getEntity(), ability);
                plugin.getEffectManager().execute(mob, loadedAbilities.get(id).getEffects());
            }
        }
    }
}
