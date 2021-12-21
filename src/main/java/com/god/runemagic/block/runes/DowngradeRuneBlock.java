package com.god.runemagic.block.runes;

import java.util.List;

import com.god.runemagic.RunemagicModElements;
import com.god.runemagic.common.Transmutation;
import com.god.runemagic.common.TransmutationMap;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.registries.ObjectHolder;

@RunemagicModElements.ModElement.Tag
public class DowngradeRuneBlock extends RunemagicModElements.ModElement {
	@ObjectHolder("runemagic:downgrade_rune")
	public static final Block block = null;

	public DowngradeRuneBlock(RunemagicModElements instance) {
		super(instance, 1);
	}

	@Override
	public void initElements() {
		elements.blocks.add(() -> new CustomBlock());
	}
	
	public static class CustomBlock extends AbstractRune {
		public CustomBlock() {
			super();
			setRegistryName("downgrade_rune");
		}

		@Override
		protected BlockState getChangeState() {
			return DisassemblyRuneBlock.block.defaultBlockState();
		}
		
		@Override
		protected boolean runeBehaviour(World world, BlockState state, BlockPos position, PlayerEntity player, List<ItemEntity> items) {
			TransmutationMap transmutation = TransmutationMap.get();

			items.forEach(item -> {
				Transmutation tr = transmutation.findDowngrade(item);
				if (tr != null) {
					tr.transmute(world, item, player);
				}
			});
			
			return true;
		}
	}
}
