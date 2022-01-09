package com.god.runemagic.item.chalk;

import com.god.runemagic.RunemagicModElements;
import net.minecraft.item.Item;
import net.minecraftforge.registries.ObjectHolder;

@RunemagicModElements.ModElement.Tag
public class BlueChalkItem extends RunemagicModElements.ModElement {
    @ObjectHolder("runemagic:alteration_rune")
    public static final Item block = null;

    public BlueChalkItem(RunemagicModElements instance) {
        super(instance, 20);
    }
}
