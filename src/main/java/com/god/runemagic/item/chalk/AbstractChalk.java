package com.god.runemagic.item.chalk;

import com.god.runemagic.block.runes.AbstractRune;
import com.god.runemagic.util.RuneMagicTags;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.item.*;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class AbstractChalk extends BlockItem {
    public AbstractChalk(Block block) {
        super(block, new Item.Properties().tab(ItemGroup.TAB_BUILDING_BLOCKS).stacksTo(64).rarity(Rarity.COMMON));
        setRegistryName(block.getRegistryName());
    }

    @Override
    public ActionResultType useOn(ItemUseContext context) {
        World world = context.getLevel();
        if (world.isClientSide()) {
            return ActionResultType.PASS;
        }

        BlockPos pos = context.getClickedPos();
        BlockState state = world.getBlockState(pos);
        if (state.getBlock().is(RuneMagicTags.Blocks.RUNES)) {
            AbstractRune block = (AbstractRune) state.getBlock();
            block.changeType(world, pos);
            return ActionResultType.SUCCESS;
        }

        return super.useOn(context);
    }
}
