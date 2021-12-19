package com.god.runemagic.block;

import java.util.Collections;
import java.util.List;

import com.god.runemagic.RunemagicModElements;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.LootContext;
import net.minecraftforge.common.ToolType;
import net.minecraftforge.registries.ObjectHolder;

@RunemagicModElements.ModElement.Tag
public class LimestoneBlock extends RunemagicModElements.ModElement {
	@ObjectHolder("runemagic:limestone")
	public static final Block block = null;

	public LimestoneBlock(RunemagicModElements instance) {
		super(instance, 1);
	}

	@Override
	public void initElements() {
		elements.blocks.add(() -> new CustomBlock());
		elements.items.add(() -> new BlockItem(block,
				new Item.Properties().addToolType(ToolType.PICKAXE, 2).tab(ItemGroup.TAB_BUILDING_BLOCKS))
						.setRegistryName(block.getRegistryName()));
	}

	public static class CustomBlock extends Block {

		public CustomBlock() {
			super(AbstractBlock.Properties.of(Material.STONE, MaterialColor.STONE).strength(1.5f)
					.sound(SoundType.STONE));
			setRegistryName("limestone");
		}
	}
}
