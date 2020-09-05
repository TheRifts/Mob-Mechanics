package me.Lozke.data;

import me.Lozke.managers.ItemFactory;
import me.Lozke.managers.ItemWrapper;
import me.Lozke.utils.NumGenerator;
import me.Lozke.utils.Text;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.attribute.Attributable;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Item;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;

public class CustomMob {
    //UNUSED
    //Saved for reference to old mob health bar system
    //Saved for reminder to do stat handling

    public static final int baseHP = 50;

    private static final int numberOfHealthIndicators = 40;
    private static final char healthIndicatorCharacter = '‖'; //╏
    private static final String healthBarPrefix = "§F§L⟦§A§L";
    private static final String healthBarColorHealthy = "§A§L";
    private static final String healthBarColorSwitch = "§7§L";
    private static final String healthBarSuffix = "§F§L⟧";

    private Tier tier;
    private Rarity rarity;

    public boolean isDropping() {
        return NumGenerator.roll(5) == 5;
    }

    public ItemStack getDrop() {
        if (isDropping()) {
            if (NumGenerator.roll(2) == 2) {
                return ItemFactory.newWeapon(tier, rarity, WeaponType.SWORD);
            }
            ArmourType[] armourTypes = ArmourType.values();
            return ItemFactory.newArmour(tier, rarity, armourTypes[NumGenerator.index(armourTypes.length)]);
        }
        else return null;
    }
}
