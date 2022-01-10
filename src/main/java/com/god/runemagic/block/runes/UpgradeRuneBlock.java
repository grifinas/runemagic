package com.god.runemagic.block.runes;

import com.god.runemagic.RunemagicModElements;
import com.god.runemagic.common.entities.RuneActivationContext;
import com.god.runemagic.common.Transmutation;
import com.god.runemagic.common.TransmutationMap;

import com.god.runemagic.item.chalk.AbstractChalk;
import net.minecraft.block.BlockState;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.registries.ObjectHolder;

@RunemagicModElements.ModElement.Tag
public class UpgradeRuneBlock extends RunemagicModElements.ModElement {
	@ObjectHolder("runemagic:upgrade_rune")
	public static final AbstractRune block = null;

	public UpgradeRuneBlock(RunemagicModElements instance) {
		super(instance, 1);
	}

	@Override
	public void initElements() {
		elements.blocks.add(CustomBlock::new);
		elements.items.add(() -> new AbstractChalk(block));
	}

	@Override
	public void clientLoad(FMLClientSetupEvent event) {
		RenderTypeLookup.setRenderLayer(block, RenderType.cutoutMipped());
		super.clientLoad(event);
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

			context.getSacrificedItems().forEach(item -> {
				Transmutation tr = transmutation.findUpgrade(item);
				if (tr != null) {
					tr.transmute(world, item, player, context.count(this));
				}
			});
			
			return true;
		}
	}
}
