package me.Lozke.guis;

import me.Lozke.MobMechanics;
import me.Lozke.data.Rarity;
import me.Lozke.data.Tier;
import me.Lozke.handlers.ItemMenu;
import me.Lozke.utils.Items;
import me.Lozke.utils.Text;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

public class SpawnerEditor implements Listener {

    private me.Lozke.data.MobSpawner spawner;
    private ItemMenu menu;
    private Page currentPage;
    private Page previousPage;


    public SpawnerEditor(me.Lozke.data.MobSpawner spawner) {
        this.spawner = spawner;
        Bukkit.getPluginManager().registerEvents(this, MobMechanics.getInstance());
        setPage(Page.Main);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onInventoryClick(InventoryClickEvent event) {
        if (!(event.getInventory().equals(menu.getInventory()))) {
            return;
        }
        event.setCancelled(true);
        int slot = event.getSlot();
        ClickType clickType = event.getClick();
        switch (currentPage) {
            case Main:
                switch (slot) {
                    case 0:
                        if (clickType.isLeftClick()) {
                            previousPage = currentPage;
                            setPage(Page.Tier);
                            break;
                        }
                        if (clickType.isRightClick()) {
                            previousPage = currentPage;
                            setPage(Page.Rarity);
                            break;
                        }
                    case 1:
                        previousPage = currentPage;
                        setPage(Page.MobEditor);
                        break;
                    case 2:
                        previousPage = currentPage;
                        setPage(Page.SpawnerEditor);
                        break;
                    case 3:
                        int spawnTime = spawner.getSpawnTime();
                        switch(clickType) {
                            case LEFT:
                                spawner.setSpawnTime(spawnTime + 10);
                                break;
                            case SHIFT_LEFT:
                                spawner.setSpawnTime(spawnTime + 100);
                                break;
                            case RIGHT:
                                if(spawnTime>10) {
                                    spawner.setSpawnTime(spawnTime - 10);
                                }
                                break;
                            case SHIFT_RIGHT:
                                if(spawnTime>100) {
                                    spawner.setSpawnTime(spawnTime - 100);
                                }
                                else {
                                    spawner.setSpawnTime(10);
                                }
                                break;
                            case MIDDLE:
                                spawner.setTimeLeft(0);
                                break;
                            case DROP:
                                spawner.setSpawnTime(10);
                                break;
                        }
                        setPage(Page.Main);
                        break;
                    case 4:
                        spawner.toggleSpawnerActive();
                        setPage(Page.Main);
                        break;
                }
                break;
            case Tier:
                previousPage = currentPage;
                spawner.setTier(Tier.valueOf(Text.decolorize(event.getCurrentItem().getItemMeta().getDisplayName())));
                setPage(Page.Main);
                break;
            case Rarity:
                previousPage = currentPage;
                spawner.setRarity(Rarity.valueOf(Text.decolorize(event.getCurrentItem().getItemMeta().getDisplayName())));
                setPage(Page.Main);
                break;
            case MobEditor:
                switch (slot) {
                    case 2:
                        spawner.toggleElite();
                        setPage(currentPage);
                        break;
                    case 4:
                        setPage(Page.Main);
                        break;
                }
            case SpawnerEditor:
                switch (slot) {
                    case 0:
                        int radius = spawner.getRadius();
                        switch(clickType) {
                            case LEFT:
                                spawner.setRadius(radius + 1);
                                break;
                            case SHIFT_LEFT:
                                spawner.setRadius(radius + 10);
                                break;
                            case RIGHT:
                                if(radius>0) {
                                    spawner.setRadius(radius - 1);
                                }
                                break;
                            case SHIFT_RIGHT:
                                if(radius>=10) {
                                    spawner.setRadius(radius - 10);
                                }
                                else {
                                    spawner.setRadius(0);
                                }
                                break;
                            case DROP:
                                spawner.setRadius(0);
                                break;
                        }
                        setPage(Page.SpawnerEditor);
                        break;
                    case 1:
                        int amount = spawner.getAmount();
                        switch(clickType) {
                            case LEFT:
                                spawner.setAmount(amount + 1);
                                break;
                            case SHIFT_LEFT:
                                spawner.setAmount(amount + 10);
                                break;
                            case RIGHT:
                                if(amount>1) {
                                    spawner.setAmount(amount - 1);
                                }
                                break;
                            case SHIFT_RIGHT:
                                if(amount>10) {
                                    spawner.setAmount(amount - 10);
                                }
                                else {
                                    spawner.setAmount(1);
                                }
                                break;
                            case DROP:
                                spawner.setAmount(1);
                                break;
                        }
                        setPage(Page.SpawnerEditor);
                        break;
                    case 4:
                        setPage(Page.Main);
                        break;
                }
        }
    }

    private void createIcons() {
        //TODO durrrrrr uhrurhhhhh
    }

    private void setPage(Page page) {
        if (menu == null || !menu.getInventory().getType().equals(page.inventoryType)) {
            menu = new ItemMenu(page.inventoryType, "");
        }
        else {
            menu.clearItems();
        }
        switch (page) {
            case Main:
                currentPage = page;
                menu.addDisplayItem(Items.formatItem(new ItemStack(spawner.getTier().getMaterial()),
                        spawner.getTier().getColorCode() + spawner.getTier() + " &r(" + spawner.getRarity().getColorCode() + spawner.getRarity().getSymbol() + "&r)",
                        new String[]{Text.colorize("&8Left click to edit tier"),
                                Text.colorize("&8Right click to edit rarity")}));
                menu.addDisplayItem(Items.formatItem(new ItemStack(Material.ZOMBIE_HEAD),
                        Text.colorize("&fMob Editor")));
                menu.addDisplayItem(Items.formatItem(new ItemStack(Material.NETHER_STAR),
                        Text.colorize("&fSpawner Mechanics")));
                menu.addDisplayItem(Items.formatItem(new ItemStack(Material.CLOCK),
                        "&fSpawn Timer: " + spawner.getTimeLeft() + "/" + spawner.getSpawnTime(),
                        new String[]{Text.colorize("&8Left click to increase spawn time by 10"),
                                Text.colorize("&8Right click to decrease spawn time by 10"),
                                Text.colorize("&8Shift click to change by 100"),
                                Text.colorize("&8Middle click to force spawn (spawner must be on)"),
                                Text.colorize("&8Press drop key on this item to set to minimum value")}));
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        if (!currentPage.equals(Page.Main) || menu.getInventory().getViewers().size() == 0 || !spawner.isSpawnerActive()){
                            cancel();
                            return;
                        }
                        menu.updateSlot(3, Items.formatItem(new ItemStack(Material.CLOCK),
                                "&fSpawn Timer: " + spawner.getTimeLeft() + "/" + spawner.getSpawnTime(),
                                new String[]{Text.colorize("&8Left click to increase spawn time by 10"),
                                        Text.colorize("&8Right click to decrease spawn time by 10"),
                                        Text.colorize("&8Shift click to change by 100"),
                                        Text.colorize("&8Middle click to force spawn (spawner must be on)"),
                                        Text.colorize("&8Press drop key on this item to set to minimum value")}));
                    }
                }.runTaskTimer(MobMechanics.getInstance(), 0, 20);
                if (spawner.isSpawnerActive()) {
                    menu.addDisplayItem(Items.formatItem(new ItemStack(Material.LIME_DYE),
                            "&fSpawner Status: &a&lON"));
                }
                else {
                    menu.addDisplayItem(Items.formatItem(new ItemStack(Material.GRAY_DYE),
                            "&fSpawner Status: &c&lOFF"));
                }
                break;
            case Tier:
                currentPage = page;
                for (Tier tier : Tier.types) {
                    if (spawner.getTier() == tier) {
                        ItemStack itemStack = Items.makeGlow(Items.formatItem(new ItemStack(tier.getMaterial()),
                                Text.colorize(tier.getColorCode() + tier.name())));
                        Items.makeGlow(itemStack);
                        menu.addDisplayItem(itemStack);
                    }
                    else {
                        menu.addDisplayItem(Items.formatItem(new ItemStack(tier.getMaterial()),
                                Text.colorize(tier.getColorCode() + tier.name())));
                    }
                }
                break;
            case Rarity:
                currentPage = page;
                for (Rarity rarity : Rarity.types) {
                    menu.addDisplayItem(Items.formatItem(rarity.getIcon(),
                            Text.colorize(rarity.getColorCode() + rarity.name())));
                }
                break;
            case MobEditor:
                currentPage = page;
                menu.addDisplayItem(Items.formatItem(new ItemStack(Material.ZOMBIE_HEAD),
                        Text.colorize("&fType: " + spawner.getMobType()),
                        new String[] {Text.colorize("&8Click to change type")}));
                menu.addDisplayItem(Items.formatItem(new ItemStack(Material.NAME_TAG),
                        Text.colorize("&fName: " + spawner.getMobType()),
                        new String[] {Text.colorize("&8Click to change name")}));
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
                menu.setDisplayItem(menu.getInventory().getSize() - 1, Items.formatItem(new ItemStack(Material.RED_CONCRETE),
                        Text.colorize("&cReturn")));
                break;
            case SpawnerEditor:
                currentPage = page;
                menu.addDisplayItem(Items.formatItem(new ItemStack(Material.BARRIER),
                        Text.colorize("&fEdit Radius (" + spawner.getRadius() + ")"),
                        new String[]{Text.colorize("&8Left click to increase by 1"), Text.colorize("&8Right click to decrease by 1"),
                                Text.colorize("&8Shift click to change by 10"),
                                Text.colorize("&8Press drop key on this item to set to minimum value")}));
                menu.addDisplayItem(Items.formatItem(new ItemStack(Material.BARRIER),
                        Text.colorize("&fEdit Amount (" + spawner.getAmount() + ")"),
                        new String[]{Text.colorize("&8Left click to increase by 1"),
                                Text.colorize("&8Right click to decrease by 1"),
                                Text.colorize("&8Shift click to change by 10"),
                                Text.colorize("&8Press drop key on this item to set to minimum value")}));
                menu.setDisplayItem(menu.getInventory().getSize() - 1, Items.formatItem(new ItemStack(Material.RED_CONCRETE),
                        Text.colorize("&cReturn")));
                break;
        }
    }

    public void openGUI(Player player) {
        player.openInventory(menu.getInventory());
    }

    public Inventory getGui() {
        return menu.getInventory();
    }

    private enum Page {
        Main(InventoryType.HOPPER),
        Tier(InventoryType.HOPPER),
        Rarity(InventoryType.HOPPER),
        MobEditor(InventoryType.HOPPER),
        SpawnerEditor(InventoryType.HOPPER);

        InventoryType inventoryType;

        Page(InventoryType inventoryType) {
            this.inventoryType = inventoryType;
        }

        public InventoryType getInventoryType() {
            return inventoryType;
        }
    }
}
