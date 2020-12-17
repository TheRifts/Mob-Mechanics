package me.Lozke.commands;


import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.Default;
import me.Lozke.MobMechanics;
import me.Lozke.managers.AbilityManager;

@CommandAlias("abilities|ability")
public class Abilities extends BaseCommand {

    private static AbilityManager manager;

    public Abilities() {
        manager = MobMechanics.getInstance().getAbilityManager();
    }

    @Default
    @CommandAlias("reload")
    public static void onReload() {
        onSave();
        onLoad();
    }

    @CommandAlias("save")
    public static void onSave() {
        manager.saveAbilities();
    }

    @CommandAlias("load")
    public static void onLoad() {
        manager.loadAbilities();
    }

}
