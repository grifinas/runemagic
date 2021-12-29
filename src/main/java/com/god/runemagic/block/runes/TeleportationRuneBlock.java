package com.god.runemagic.block.runes;

import com.god.runemagic.RuneMagicMod;
import com.god.runemagic.RunemagicModElements;
import com.god.runemagic.common.RuneMetaMap;
import com.god.runemagic.common.RuneMetaMapSupplier;
import com.god.runemagic.common.entities.RuneActivationContext;
import com.god.runemagic.item.chalk.GreenChalkItem;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.registries.ObjectHolder;

@RunemagicModElements.ModElement.Tag
public class TeleportationRuneBlock extends RunemagicModElements.ModElement {
    @ObjectHolder("runemagic:teleportation_rune")
    public static final Block block = null;

    public TeleportationRuneBlock(RunemagicModElements instance) {
        super(instance, 1);
    }

    @Override
    public void initElements() {
        elements.blocks.add(CustomBlock::new);
        elements.items.add(() -> new GreenChalkItem.ItemCustom(block));
    }

    public static class CustomBlock extends AbstractRune {
        public CustomBlock() {
            super();
            setRegistryName("teleportation_rune");
        }

        @Override
        protected BlockState getChangeState() {
            return null;
        }

        @Override
        public void chainActivate(RuneActivationContext context, BlockPos position) {
            // TODO not exactly correct
            context.getWorld().removeBlock(position, false);
        }

        @Override
        protected boolean runeBehaviour(RuneActivationContext context) {
            RuneMetaMap.RuneMeta meta = RuneMetaMapSupplier.getStatic().getPlayerRuneMeta(context.getPlayer());

            BlockPos position = meta.getPreviousPointTo();

            if (position == null) {
                return false;
            }

            BlockState state = context.getWorld().getBlockState(position);

            if (state.is(TeleportationRuneBlock.block)) {
                context.getPlayer().teleportTo(position.getX(), position.getY(), position.getZ());

                CustomBlock block = (CustomBlock) state.getBlock();
                block.chainActivate(context, position);
                meta.addTpPoint(null);
                meta.addTpPoint(null);
            }
            return true;
        }
    }
}
