package com.god.runemagic.item.chalk;

import com.god.runemagic.RunemagicModElements;
import net.minecraft.item.Item;
import net.minecraftforge.registries.ObjectHolder;

@RunemagicModElements.ModElement.Tag
public class GreenChalkItem extends RunemagicModElements.ModElement {
    @ObjectHolder("runemagic:conjuration_rune")
    public static final Item block = null;

    public GreenChalkItem(RunemagicModElements instance) {
        super(instance, 20);
    }
}
