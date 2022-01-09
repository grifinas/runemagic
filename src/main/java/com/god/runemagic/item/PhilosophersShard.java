package com.god.runemagic.item;

import com.god.runemagic.RunemagicModElements;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Rarity;
import net.minecraftforge.registries.ObjectHolder;

@RunemagicModElements.ModElement.Tag
public class PhilosophersShard extends RunemagicModElements.ModElement {
    @ObjectHolder("runemagic:philosophers_shard")
    public static final Item block = null;

    public PhilosophersShard(RunemagicModElements instance) {
        super(instance, 4);
    }

    @Override
    public void initElements() {
        elements.items.add(ItemCustom::new);
    }

    public static class ItemCustom extends Item {
        public ItemCustom() {
            super(new Properties().tab(ItemGroup.TAB_MISC).stacksTo(64).rarity(Rarity.RARE));
            setRegistryName("philosophers_shard");
        }

        @Override
        public ItemStack getContainerItem(ItemStack itemstack) {
            return new ItemStack(this);
        }
    }
}
