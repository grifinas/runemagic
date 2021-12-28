package com.god.runemagic.block.runes;

import com.god.runemagic.RunemagicModElements;
import com.god.runemagic.common.entities.RuneActivationContext;
import com.god.runemagic.common.Transmutation;
import com.god.runemagic.common.TransmutationMap;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
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
		elements.blocks.add(CustomBlock::new);
	}
	
	public static class CustomBlock extends AbstractRune {
		private static final String CONTEXT_KEY = "downgrade_rune";

		public CustomBlock() {
			super();
			setRegistryName("downgrade_rune");
		}

		@Override
		protected BlockState getChangeState() {
			return DisassemblyRuneBlock.block.defaultBlockState();
		}
		
		@Override
		protected boolean runeBehaviour(RuneActivationContext context) {
			TransmutationMap transmutation = TransmutationMap.get();
			World world = context.getWorld();
			PlayerEntity player = context.getPlayer();

			this.getSacrificedItems(context).forEach(item -> {
				Transmutation tr = transmutation.findDowngrade(item);
				if (tr != null) {
					tr.transmute(world, item, player, context.count(this));
				}
			});
			
			return true;
		}
	}
}
