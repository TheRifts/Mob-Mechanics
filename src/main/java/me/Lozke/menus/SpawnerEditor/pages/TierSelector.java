package me.Lozke.menus.SpawnerEditor.pages;

import me.Lozke.data.MobSpawner;
import me.Lozke.data.Tier;
import me.Lozke.utils.ItemMenu.menus.ItemMenu;
import me.Lozke.utils.Items;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;

public class TierSelector extends ItemMenu {
    public TierSelector(MobSpawner spawner) {
        super(InventoryType.CHEST, 1, "Tier Editor");
        for (Tier tier : Tier.types) {
            if (spawner.getTier() == tier) {
                addDisplayItem(new me.Lozke.menus.SpawnerEditor.icons.TierSelector(Items.makeGlow(new ItemStack(tier.getMaterial())), spawner, tier));
            }
            else {
                addDisplayItem(new me.Lozke.menus.SpawnerEditor.icons.TierSelector(Items.formatItem(tier.getMaterial(), ""), spawner, tier));
            }
        }
    }
}
