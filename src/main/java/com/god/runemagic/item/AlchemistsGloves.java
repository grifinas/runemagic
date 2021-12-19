package com.god.runemagic.item;

import com.god.runemagic.RuneMagicMod;
import com.god.runemagic.RunemagicModElements;
import com.god.runemagic.block.RuneBlock;
import com.god.runemagic.block.RuneBlock.CustomBlock;

import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.item.Rarity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.registries.ObjectHolder;

@RunemagicModElements.ModElement.Tag
public class AlchemistsGloves extends RunemagicModElements.ModElement {
	@ObjectHolder("runemagic:alchemists_gloves")
	public static final Item block = null;

	public AlchemistsGloves(RunemagicModElements instance) {
		super(instance, 4);
	}

	@Override
	public void initElements() {
		elements.items.add(() -> new ItemCustom());
	}

	public static class ItemCustom extends Item {
		public ItemCustom() {
			super(new Item.Properties().tab(ItemGroup.TAB_MISC).stacksTo(1).rarity(Rarity.RARE));
			setRegistryName("alchemists_gloves");
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
		public ActionResultType onItemUseFirst(ItemStack stack, ItemUseContext context) {
			World world = context.getLevel();
			
			if (world.isClientSide()) {
				return ActionResultType.PASS;
			}
			
			BlockState state = world.getBlockState(context.getClickedPos());
			if (state.getBlock().is(RuneBlock.block)) {
				RuneMagicMod.LOGGER.info("glove used on rune{}", state);
				RuneBlock.CustomBlock rune = (CustomBlock) state.getBlock();
				rune.activate(world, state, context.getClickedPos());
				return ActionResultType.SUCCESS;
			}
			return ActionResultType.FAIL;
		}
	}
}
