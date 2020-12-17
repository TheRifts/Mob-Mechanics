package me.Lozke.data.Abilities.Effects;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.wrappers.BlockPosition;
import me.Lozke.data.RiftsMob;
import me.Lozke.utils.NumGenerator;
import org.bukkit.block.Block;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

public class BlockBreak extends Effect {

    private int radius;

    @Override
    public void execute(RiftsMob caster, LivingEntity target) {
        super.execute(caster, target);
        if (!(target instanceof Player)) {
            return;
        }
        List<Block> blocks = getBlocks(caster.getEntity().getLocation().add(0, -1, 0).getBlock());
        PacketContainer container = new PacketContainer(PacketType.Play.Server.BLOCK_BREAK_ANIMATION);
        for (Block block : blocks) {
            container.getBlockPositionModifier().write(0, new BlockPosition(block.getLocation().toVector()));
            container.getIntegers().write(0, NumGenerator.index(1000));
            container.getIntegers().write(1, NumGenerator.rollInclusive(4, 9));
            try {
                ProtocolLibrary.getProtocolManager().sendServerPacket((Player) target, container);
            } catch (InvocationTargetException e) {
                throw new RuntimeException("Cannot send block break effect packet");
            }
        }
    }

    private List<Block> getBlocks(Block start){
        if (radius < 0) {
            return new ArrayList<>(0);
        }
        int iterations = (radius * 2) + 1;
        List<Block> blocks = new ArrayList<>(iterations * iterations * iterations);
        for (int x = -radius; x <= radius; x++) {
            for (int y = -radius; y <= radius; y++) {
                for (int z = -radius; z <= radius; z++) {
                    blocks.add(start.getRelative(x, y, z));
                }
            }
        }
        return blocks;
    }

}
