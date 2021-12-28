package com.god.runemagic.block.runes;

import com.god.runemagic.RunemagicModElements;
import com.god.runemagic.common.entities.RuneActivationContext;
import com.god.runemagic.common.Transmutation;
import com.god.runemagic.common.TransmutationMap;

import com.god.runemagic.item.chalk.AbstractChalk;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
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
		elements.blocks.add(CustomBlock::new);
		elements.items.add(() -> new AbstractChalk(block));
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
		protected boolean runeBehaviour(RuneActivationContext context) {
			TransmutationMap transmutation = TransmutationMap.get();
			World world = context.getWorld();
			PlayerEntity player = context.getPlayer();

			this.getSacrificedItems(context).forEach(item -> {
				Transmutation tr = transmutation.findUpgrade(item);
				if (tr != null) {
					tr.transmute(world, item, player, context.count(this));
				}
			});
			
			return true;
		}
	}
}
