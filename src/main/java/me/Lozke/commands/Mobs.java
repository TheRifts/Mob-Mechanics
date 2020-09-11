package me.Lozke.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.Subcommand;
import me.Lozke.MobMechanics;
import me.Lozke.managers.BaseEntityManager;
import org.bukkit.entity.Player;

@CommandAlias("mob|mobs")
public class Mobs extends BaseCommand {

    private static BaseEntityManager baseEntityManager;

    public Mobs() {
        baseEntityManager = MobMechanics.getInstance().getBaseEntityManager();
    }

    @Subcommand("save")
    public static void onSave() {
        baseEntityManager.saveMobs();
    }

    @Subcommand("load")
    public static void onLoad() {
        baseEntityManager.loadMobs();
    }

    /*
    @Subcommand("select")
    public static void onSelector(Player player) {
        baseEntityManager.openEditor(player);
    }
     */
}
