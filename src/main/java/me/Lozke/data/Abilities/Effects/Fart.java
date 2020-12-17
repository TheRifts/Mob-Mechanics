package me.Lozke.data.Abilities.Effects;

import me.Lozke.data.MobNamespacedKey;
import me.Lozke.data.RiftsMob;
import me.Lozke.utils.Logger;
import org.bukkit.Particle;
import org.bukkit.entity.LivingEntity;

public class Fart extends Effect {

    @Override
    public void execute(RiftsMob caster, LivingEntity target) {
        super.execute(caster, target);
        caster.getEntity().getWorld().spawnParticle(Particle.CLOUD, caster.getEntity().getLocation(), 12);
        Logger.broadcast(caster.getEntity().getPersistentDataContainer().get(MobNamespacedKey.CUSTOM_NAME.getNamespacedKey(), MobNamespacedKey.CUSTOM_NAME.getDataType()) + ": I just farted.");
    }
}
