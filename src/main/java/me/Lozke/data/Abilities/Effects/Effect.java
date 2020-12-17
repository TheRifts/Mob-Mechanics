package me.Lozke.data.Abilities.Effects;

import com.google.gson.typeadapters.RuntimeTypeAdapterFactory;
import me.Lozke.MobMechanics;
import me.Lozke.data.Abilities.EffectTargetType;
import me.Lozke.data.RiftsMob;
import me.Lozke.managers.BaseEntityManager;
import me.Lozke.managers.MobManager;
import me.Lozke.managers.SpawnerManager;
import me.Lozke.utils.GsonUtils;
import org.bukkit.Location;
import org.bukkit.entity.LivingEntity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class Effect implements Serializable {

    //RuntimeTypeAdapterFactory
    protected String type = getClass().getSimpleName();
    private static final RuntimeTypeAdapterFactory<Effect> adapter = RuntimeTypeAdapterFactory.of(Effect.class, "type");
    private static final Set<Class<?>> registeredClasses = new HashSet<>();

    //Useful references to managers
    protected transient MobMechanics plugin = MobMechanics.getInstance();
    protected transient BaseEntityManager baseEntityManager = plugin.getBaseEntityManager();
    protected transient MobManager mobManager = plugin.getMobManager();
    protected transient SpawnerManager spawnerManager = plugin.getSpawnerManager();

    //Base Data
    protected String ID;
    protected ArrayList<Noise> noises;
    protected String target;
    protected Integer range; //Targeting Range

    static {
        GsonUtils.registerType(adapter);
    }

    public Effect() {
        if (!registeredClasses.contains(this.getClass())) {
            registeredClasses.add(this.getClass());
            adapter.registerSubtype(this.getClass(), this.getClass().getSimpleName());
        }
    }

    public void execute(RiftsMob caster, LivingEntity target) {
        if (noises != null) {
            Location loc = caster.getEntity().getLocation();
            for (Noise noise : noises) {
                loc.getWorld().playSound(loc, noise.getSound(), noise.getVolume(), noise.getPitch());
            }
        }
    }

    public String getID() {
        return ID;
    }
    public void setID(String ID) {
        this.ID = ID;
    }

    public EffectTargetType getTarget() {
        if (target == null) return EffectTargetType.SELF;
        return EffectTargetType.valueOf(target);
    }

    public Integer getRange() {
        return range;
    }
}
