package me.Lozke.menus.SpawnerEditor.pages;

import me.Lozke.MobMechanics;
import me.Lozke.data.MobSpawner;
import me.Lozke.menus.SpawnerEditor.icons.SpawnerTimerButton;
import me.Lozke.menus.SpawnerEditor.icons.ToggleStatusButton;
import me.Lozke.utils.ItemMenu.icons.OpenMenuIcon;
import me.Lozke.utils.ItemMenu.menus.ItemMenu;
import me.Lozke.utils.Items;
import me.Lozke.utils.Text;
import org.bukkit.Material;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.scheduler.BukkitRunnable;

public class SpawnerEditorMenu extends ItemMenu {

    private MobSpawner spawner;

    public SpawnerEditorMenu(MobSpawner spawner) {
        super(InventoryType.HOPPER, "Spawner Editor");
        this.spawner = spawner;
        updateMenu();
    }

    @Override
    public void updateMenu() {
        clearItems();

        addDisplayItem(new OpenMenuIcon(
                Items.formatItem(spawner.getTier().getMaterial(), spawner.getTier().getColorCode() + spawner.getTier() + " &r(" + spawner.getRarity().getColorCode() + spawner.getRarity().getSymbol() + "&r)", new String[]{Text.colorize("&8Left click to edit tier"), Text.colorize("&8Right click to edit rarity")}))
                .addClickAction(ClickType.LEFT, new TierSelector(spawner).setParent(this))
                .addClickAction(ClickType.RIGHT, new RaritySelector(spawner).setParent(this)));

        addDisplayItem(new OpenMenuIcon(
                Items.formatItem(Material.ZOMBIE_HEAD, Text.colorize("&fMob Type Selector")),
                new MobTypeSelector(spawner).setParent(this)));

        addDisplayItem(new OpenMenuIcon(
                Items.formatItem(Material.NETHER_STAR, Text.colorize("&fSpawner Mechanics")),
                new MechanicEditor(spawner).setParent(this)));

        SpawnerTimerButton timer = new SpawnerTimerButton(spawner);
        timer.setParent(this);
        addDisplayItem(timer);
        new BukkitRunnable() {
            @Override
            public void run() {
                if (getInventory().getViewers().size() == 0 || !spawner.isSpawnerActive()) {
                    cancel();
                    return;
                }
                timer.updateTimer();
                updateIcon(timer);
            }
        }.runTaskTimer(MobMechanics.getInstance(), 0, 20);

        addDisplayItem(new ToggleStatusButton(spawner));
    }

    /*
                menu.addDisplayItem(Items.formatItem(new ItemStack(Material.ZOMBIE_HEAD),
                        Text.colorize("&fType: " + spawner.getMob().getName()), //Change this to display mobType
                        new String[]{Text.colorize("&8Click to change type")}));
                menu.addDisplayItem(Items.formatItem(new ItemStack(Material.NAME_TAG),
                        Text.colorize("&fRename Mob"),
                        new String[]{
                                Text.colorize("&fCurrent Name: " + spawner.getMob().getName()),
                                Text.colorize("&7Click to change name")}));
                menu.addDisplayItem(Items.formatItem(new ItemStack(Material.CHEST), Text.colorize("&fEdit/Add Drops")));
                menu.addDisplayItem(Items.formatItem(new ItemStack(Material.ITEM_FRAME), Text.colorize("&fEdit Stats")));
                menu.addDisplayItem(Items.formatItem(new ItemStack(Material.WRITABLE_BOOK), Text.colorize("&fEdit/Add Dialogue")));
                if (spawner.isElite()) {
                    ItemStack item = Items.formatItem(new ItemStack(Material.ENDER_EYE),
                            Text.colorize("&fToggle Elite Status"));
                    Items.makeGlow(item);
                    menu.addDisplayItem(item);
                }
                else {
                    menu.addDisplayItem(Items.formatItem(new ItemStack(Material.ENDER_PEARL),
                            Text.colorize("&fToggle Elite Status")));
                }
                menu.addDisplayItem(Items.formatItem(new ItemStack(Material.ZOMBIE_SPAWN_EGG), Text.colorize("&fChange Mob Type")));
                menu.addDisplayItem(Items.formatItem(new ItemStack(Material.GOLDEN_SWORD), Text.colorize("&fWeapon Type")));
                menu.addDisplayItem(Items.formatItem(new ItemStack(Material.TRIPWIRE), Text.colorize("&fToggle Dry Streak")));
                menu.setDisplayItem(menu.getInventory().getSize() - 1, Items.formatItem(new ItemStack(Material.RED_CONCRETE),
                        Text.colorize("&cReturn")));
                break;
     */
}
