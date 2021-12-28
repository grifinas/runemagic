package com.god.runemagic.util;

import com.god.runemagic.RuneMagicMod;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.Tags;

public class RuneMagicTags {
	public static class Blocks {
		public static final Tags.IOptionalNamedTag<Block> RUNES = createTag("runes");
		
		private static Tags.IOptionalNamedTag<Block> createTag(String name) {
			return BlockTags.createOptional(new ResourceLocation(RuneMagicMod.MOD_ID, name));
		}
	}
	
	public static class Items {
		private static Tags.IOptionalNamedTag<Item> createTag(String name) {
			return ItemTags.createOptional(new ResourceLocation(RuneMagicMod.MOD_ID, name));
		}
	}
}
