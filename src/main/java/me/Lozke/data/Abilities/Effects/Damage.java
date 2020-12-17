package me.Lozke.data.Abilities.Effects;

import me.Lozke.data.RiftsMob;
import org.bukkit.entity.LivingEntity;

public class Damage extends Effect {

    private Double damage;

    @Override
    public void execute(RiftsMob caster, LivingEntity target) {
        super.execute(caster, target);
        target.damage(damage, caster.getEntity());
    }
}
