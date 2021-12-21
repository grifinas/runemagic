package com.god.runemagic.item;

import com.god.runemagic.RuneMagicMod;
import com.god.runemagic.RunemagicModElements;
import com.god.runemagic.block.runes.AbstractRune;
import com.god.runemagic.util.RuneMagicTags;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.item.Rarity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.registries.ObjectHolder;

@RunemagicModElements.ModElement.Tag
public class ChalkItem extends RunemagicModElements.ModElement {
	@ObjectHolder("runemagic:chalk")
	public static final Item block = null;

	public ChalkItem(RunemagicModElements instance) {
		super(instance, 20);
	}

	public static class ItemCustom extends BlockItem {
		public ItemCustom(Block block) {
			super(block, new Item.Properties().tab(ItemGroup.TAB_BUILDING_BLOCKS).stacksTo(64).rarity(Rarity.COMMON));
			RuneMagicMod.LOGGER.info("chalk block {}", block);
			setRegistryName(block.getRegistryName());
		}

		@Override
		public int getUseDuration(ItemStack itemstack) {
			return 0;
		}

		@Override
		public float getDestroySpeed(ItemStack par1ItemStack, BlockState par2Block) {
			return 1F;
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
				RuneMagicMod.LOGGER.info("clicking on rune");
				AbstractRune block = (AbstractRune) state.getBlock();
				block.changeType(world, pos);
				RuneMagicMod.LOGGER.info("changed state, {}", state);
				return ActionResultType.SUCCESS;
			}
			
			return super.useOn(context);
		}
	}
}
