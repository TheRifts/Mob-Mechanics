package me.Lozke.data.Abilities;

import java.io.Serializable;
import java.util.ArrayList;

public class Ability implements Serializable {

    private String ID;
    private ArrayList<String> effects;
    private Long cooldown;

    private transient Long lastUsedTimeStamp;

    public void setID(String id) {
        this.ID = id;
    }
    public String getID() {
        return ID;
    }

    public void setEffects(ArrayList<String> effects) {
        this.effects = effects;
    }

    public ArrayList<String> getEffects() {
        return effects;
    }

    public void setCooldown(long cooldown) {
        this.cooldown = cooldown;
    }
    public Long getCooldown() {
        return (cooldown == null) ? 0 : cooldown;
    }
}
