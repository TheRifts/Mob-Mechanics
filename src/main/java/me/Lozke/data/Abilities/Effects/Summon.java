package me.Lozke.data.Abilities.Effects;

import me.Lozke.data.BaseEntity;
import me.Lozke.data.RiftsMob;
import me.Lozke.utils.Logger;
import org.bukkit.entity.LivingEntity;

public class Summon extends Effect {

    private String minionID;

    public void execute(RiftsMob caster, LivingEntity target) {
        super.execute(caster, target);
        BaseEntity entity = baseEntityManager.getBaseEntity(minionID);
        if (entity == null) {
            Logger.log("Failed to summon " + minionID + " for: " + caster.getBaseEntityID());
            return;
        }
        RiftsMob spawnedMob = baseEntityManager.spawnBaseEntity(entity, caster.getEntity().getLocation(), caster.getTier(), caster.getRarity());
        mobManager.trackEntity(spawnedMob);
    }

    public void setMinionID(String id) {
        if (baseEntityManager.getBaseEntity(id) == null) {
            Logger.log("Failed to set minion for effect (" + ID + ") to " + id);
            return;
        }
        this.minionID = id;
    }

    public String getMinionID() {
        return minionID;
    }
}
