package me.Lozke.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandCompletion;
import co.aikar.commands.annotation.Default;
import co.aikar.commands.annotation.Syntax;
import me.Lozke.MobMechanics;
import me.Lozke.data.ModifiableEntity;
import me.Lozke.data.Rarity;
import me.Lozke.data.Tier;
import me.Lozke.utils.Text;
import org.bukkit.entity.Player;

@CommandAlias("spawn")
public class SpawnMob extends BaseCommand {

    @Default
    @Syntax("<mob id> <rarity>")
    @CommandCompletion("@mob-ids")
    public static void onSpawnMob(Player player, String mobID, @Default("T1") Tier tier, @Default("COMMON")  Rarity rarity) {
        ModifiableEntity entity = MobMechanics.getInstance().getMobManager().getModifiableEntity(mobID);
        if (entity == null) {
            player.sendMessage(Text.colorize("&cInvalid Mob ID! Mob ID is case sensitive."));
            return;
        }
        MobMechanics.getInstance().getMobManager().spawnMob(tier, rarity, entity, player.getLocation());
    }
}
