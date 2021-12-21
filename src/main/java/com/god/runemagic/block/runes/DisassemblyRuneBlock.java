package com.god.runemagic.block.runes;

import java.util.List;

import com.god.runemagic.RuneMagicMod;
import com.god.runemagic.RunemagicModElements;
import com.god.runemagic.common.DisassemblyMap;
import com.god.runemagic.common.ManaMap;
import com.god.runemagic.common.ManaMap.Mana;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.registries.ObjectHolder;

@RunemagicModElements.ModElement.Tag
public class DisassemblyRuneBlock extends RunemagicModElements.ModElement {
	@ObjectHolder("runemagic:disassembly_rune")
	public static final Block block = null;

	public DisassemblyRuneBlock(RunemagicModElements instance) {
		super(instance, 1);
	}

	@Override
	public void initElements() {
		elements.blocks.add(() -> new CustomBlock());
	}
	
	public static class CustomBlock extends AbstractRune {
		public CustomBlock() {
			super();
			setRegistryName("disassembly_rune");
		}

		@Override
		protected BlockState getChangeState() {
			return UpgradeRuneBlock.block.defaultBlockState();
		}
		
		@Override
		protected boolean runeBehaviour(World world, BlockState state, BlockPos position, PlayerEntity player, List<ItemEntity> items) {
			DisassemblyMap disassembly = DisassemblyMap.get();
			Mana mana = ManaMap.get().getPlayerMana(player);
			
			items.forEach(item -> {
				int value = disassembly.findValue(item);
				item.remove();
				mana.setValue(mana.getValue() + value);
			});
			
			return true;
		}
	}
}
