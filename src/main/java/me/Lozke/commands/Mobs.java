package me.Lozke.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.Subcommand;
import me.Lozke.MobMechanics;
import me.Lozke.managers.MobManager;
import org.bukkit.entity.Player;

@CommandAlias("mob|mobs")
public class Mobs extends BaseCommand {

    private static MobManager mobManager;

    public Mobs() {
        mobManager = MobMechanics.getInstance().getMobManager();
    }

    @Subcommand("save")
    public static void onSave() {
        mobManager.saveMobs();
    }

    @Subcommand("load")
    public static void onLoad() {
        mobManager.loadMobs();
    }

    @Subcommand("select")
    public static void onSelector(Player player) {
        mobManager.openEditor(player);
    }
}
