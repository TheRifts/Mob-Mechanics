package me.Lozke.data.Abilities;

public class AbilityCooldownContainer {

    private String ID;
    private long length;
    private long stamp;

    public AbilityCooldownContainer(Ability ability) {
        this.ID = ability.getID();
        this.length = ability.getCooldown();
        this.stamp = System.currentTimeMillis();
    }

    public String getID() {
        return ID;
    }

    public long getStamp() {
        return stamp;
    }

    public long getLength() {
        return length;
    }

    public boolean isOver() {
        return System.currentTimeMillis() > (stamp + (length * 1000));
    }
}
