package com.god.runemagic.item.chalk;

import com.god.runemagic.RunemagicModElements;

import net.minecraft.item.Item;
import net.minecraftforge.registries.ObjectHolder;

@RunemagicModElements.ModElement.Tag
public class ChalkItem extends RunemagicModElements.ModElement {
	@ObjectHolder("runemagic:upgrade_rune")
	public static final Item block = null;

	public ChalkItem(RunemagicModElements instance) {
		super(instance, 20);
	}
}
