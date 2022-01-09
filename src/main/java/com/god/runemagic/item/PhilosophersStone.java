package com.god.runemagic.item;

import com.god.runemagic.RuneMagicMod;
import com.god.runemagic.RunemagicModElements;
import com.god.runemagic.common.ManaMap;
import com.god.runemagic.common.ManaMapSupplier;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Rarity;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ObjectHolder;

@RunemagicModElements.ModElement.Tag
public class PhilosophersStone extends RunemagicModElements.ModElement {
    @ObjectHolder("runemagic:philosophers_stone")
    public static final Item block = null;

    public PhilosophersStone(RunemagicModElements instance) {
        super(instance, 4);
    }

    @Override
    public void initElements() {
        elements.items.add(ItemCustom::new);
    }

    @Mod.EventBusSubscriber(modid = RuneMagicMod.MOD_ID)
    public static class ItemCustom extends Item {
        public ItemCustom() {
            super(new Properties().tab(ItemGroup.TAB_MISC).stacksTo(1).rarity(Rarity.EPIC));
            setRegistryName("philosophers_stone");
        }

        @Override
        public ItemStack getContainerItem(ItemStack itemstack) {
            return new ItemStack(this);
        }

        @Override
        public boolean onDroppedByPlayer(ItemStack item, PlayerEntity player) {
            ManaMap.Mana mana = ManaMapSupplier.getStatic().getPlayerMana(player);
            mana.withPhilosopherStone(false);
            return super.onDroppedByPlayer(item, player);
        }

        @SubscribeEvent
        public static void onItemPickupEvent(PlayerEvent.ItemPickupEvent event) {
            if (event.getStack().getItem() instanceof ItemCustom) {
                RuneMagicMod.LOGGER.info("pickup philosophers stone {}", event.getStack());
                ManaMap.Mana mana = ManaMapSupplier.getStatic().getPlayerMana(event.getPlayer());
                mana.withPhilosopherStone(true);
            }
        }

        @SubscribeEvent
        public static void onItemCraftedEvent(PlayerEvent.ItemCraftedEvent event) {
            if (event.getCrafting().getItem() instanceof ItemCustom) {
                RuneMagicMod.LOGGER.info("craft philosophers stone {}", event.getCrafting());
                ManaMap.Mana mana = ManaMapSupplier.getStatic().getPlayerMana(event.getPlayer());
                mana.withPhilosopherStone(true);
            }
        }
    }
}
