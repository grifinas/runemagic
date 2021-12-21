package com.god.runemagic.block.runes;

import java.util.List;

import com.god.runemagic.RunemagicModElements;
import com.god.runemagic.common.Transmutation;
import com.god.runemagic.common.TransmutationMap;
import com.god.runemagic.item.ChalkItem;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.registries.ObjectHolder;

@RunemagicModElements.ModElement.Tag
public class UpgradeRuneBlock extends RunemagicModElements.ModElement {
	@ObjectHolder("runemagic:upgrade_rune")
	public static final Block block = null;

	public UpgradeRuneBlock(RunemagicModElements instance) {
		super(instance, 1);
	}

	@Override
	public void initElements() {
		elements.blocks.add(() -> new CustomBlock());
		elements.items.add(() -> new ChalkItem.ItemCustom(block));
	}

	public static class CustomBlock extends AbstractRune {
		public CustomBlock() {
			super();
			setRegistryName("upgrade_rune");
		}

		@Override
		protected BlockState getChangeState() {
			return DowngradeRuneBlock.block.defaultBlockState();
		}

		@Override
		protected void runeBehaviour(World world, BlockState state, BlockPos position, List<ItemEntity> items) {
			TransmutationMap transmutation = TransmutationMap.get();

			items.forEach(item -> {
				Transmutation tr = transmutation.findUpgrade(item);
				if (tr != null) {
					tr.transmute(world, item, 100.0f);
				}
			});
		}
	}
}
