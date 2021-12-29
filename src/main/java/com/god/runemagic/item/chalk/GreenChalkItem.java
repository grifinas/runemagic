package com.god.runemagic.item.chalk;

import com.god.runemagic.RuneMagicMod;
import com.god.runemagic.RunemagicModElements;
import com.god.runemagic.block.runes.TeleportationRuneBlock;
import com.god.runemagic.common.RuneMetaMap;
import com.god.runemagic.common.RuneMetaMapSupplier;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemUseContext;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.registries.ObjectHolder;

@RunemagicModElements.ModElement.Tag
public class GreenChalkItem extends RunemagicModElements.ModElement {
    @ObjectHolder("runemagic:teleportation_rune")
    public static final Item block = null;

    public GreenChalkItem(RunemagicModElements instance) {
        super(instance, 20);
    }

    public static class ItemCustom extends AbstractChalk {
        public ItemCustom(Block block) {
            super(block);
        }

        @Override
        public ActionResultType useOn(ItemUseContext context) {
            ActionResultType result = super.useOn(context);
            World world = context.getLevel();

            if (world.isClientSide) {
                return result;
            }

            if (result == ActionResultType.CONSUME) {
                RuneMetaMap.RuneMeta meta = RuneMetaMapSupplier.getStatic().getPlayerRuneMeta(context.getPlayer());

                BlockPos pos = meta.getPreviousPointTo();
                if (pos != null) {
                    if (world.getBlockState(pos).is(TeleportationRuneBlock.block)) {
                        world.removeBlock(pos, false);
                    }
                }

                meta.addTpPoint(context.getClickedPos().above());
            }

            return result;
        }
    }
}
