package me.Lozke.data.Abilities.Effects;

import org.bukkit.Sound;

public class Noise {

    private String sound;
    private float volume;
    private float pitch;

    public Sound getSound() {
        return Sound.valueOf(sound);
    }

    public float getVolume() {
        return volume;
    }

    public float getPitch() {
        return pitch;
    }
}
