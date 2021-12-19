package com.god.runemagic.item;

import com.god.runemagic.RuneMagicMod;
import com.god.runemagic.RunemagicModElements;
import com.god.runemagic.block.RuneBlock;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.item.BlockItem;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.item.Rarity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.world.World;
import net.minecraftforge.registries.ObjectHolder;

@RunemagicModElements.ModElement.Tag
public class ChalkItem extends RunemagicModElements.ModElement {
	@ObjectHolder("runemagic:chalk")
	public static final Item block = null;

	public ChalkItem(RunemagicModElements instance) {
		super(instance, 2);
	}

	public static class ItemCustom extends BlockItem {
		public ItemCustom(Block block) {
			super(block, new Item.Properties().tab(ItemGroup.TAB_BUILDING_BLOCKS).stacksTo(64).rarity(Rarity.COMMON));
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

//		@Override
//		public ActionResultType onItemUseFirst(ItemStack stack, ItemUseContext context) {
//			World world = context.getLevel();
//			
//			if (context.getClickedFace() != Direction.UP || world.isClientSide()) {
//				return ActionResultType.PASS;
//			}
//			
//			BlockState bs = world.getBlockState(context.getClickedPos());
//			
//			BlockItemUseContext blockContext = new BlockItemUseContext(context);
//			if (blockContext.canPlace()) {
//				RuneMagicMod.LOGGER.info("Can place");
//				world.setBlock(context.getClickedPos().above(), RuneBlock.block.defaultBlockState(), getEnchantmentValue());
//			}
//			RuneMagicMod.LOGGER.info("block state {}", bs);
//			// TODO Auto-generated method stub
//			return super.onItemUseFirst(stack, context);
//		}
	}
}
