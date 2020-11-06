package me.Lozke.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandCompletion;
import co.aikar.commands.annotation.Default;
import co.aikar.commands.annotation.Syntax;
import me.Lozke.MobMechanics;
import me.Lozke.data.BaseEntity;
import me.Lozke.data.Rarity;
import me.Lozke.data.Tier;
import me.Lozke.utils.Text;
import org.bukkit.entity.Player;

@CommandAlias("spawn")
public class SpawnMob extends BaseCommand {

    @Default
    @Syntax("<mob id> <tier> <rarity>")
    @CommandCompletion("@mob-ids")
    public static void onSpawnMob(Player player, String mobID, @Default("T1") Tier tier, @Default("COMMON")  Rarity rarity) {
        BaseEntity entity = MobMechanics.getInstance().getBaseEntityManager().getBaseEntity(mobID);
        if (entity == null) {
            player.sendMessage(Text.colorize("&cInvalid Mob ID! Mob ID is case sensitive."));
            return;
        }
        MobMechanics.getInstance().getBaseEntityManager().spawnBaseEntity(entity, player.getLocation(), tier, rarity);
    }
}
