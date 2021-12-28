package com.god.runemagic.item;

import com.god.runemagic.RunemagicModElements;
import com.god.runemagic.common.spells.AbstractSpell;
import com.god.runemagic.common.spells.FireBallSpell;
import com.god.runemagic.util.SpellProvider;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Rarity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.*;
import net.minecraft.world.World;
import net.minecraftforge.registries.ObjectHolder;

@RunemagicModElements.ModElement.Tag
public class SpellScroll extends RunemagicModElements.ModElement {
    @ObjectHolder("runemagic:spell_scroll")
    public static final Item block = null;

    public SpellScroll(RunemagicModElements instance) {
        super(instance, 4);
    }

    @Override
    public void initElements() {
        elements.items.add(ItemCustom::new);
    }

    public static ItemStack makeSpellScroll(AbstractSpell spell) {
        ItemStack resultingItemStack = new ItemStack(block, 1);

        CompoundNBT nbt = new CompoundNBT();
        nbt.put(SpellProvider.SPELL_KEY, spell.toNBT());
        resultingItemStack.setTag(nbt);
        return resultingItemStack;
    }

    public static class ItemCustom extends Item {
        public ItemCustom() {
            super(new Properties().tab(ItemGroup.TAB_MISC).stacksTo(1).rarity(Rarity.RARE));
            setRegistryName("spell_scroll");
        }

        @Override
        public ItemStack getContainerItem(ItemStack itemstack) {
            return new ItemStack(this);
        }

        public ActionResult<ItemStack> use(World world, PlayerEntity playerEntity, Hand hand) {
            ItemStack itemstack = playerEntity.getItemInHand(hand);

            AbstractSpell spell = SpellProvider.get(itemstack);
            if (spell == null) {
                return ActionResult.pass(itemstack);
            }

            ActionResultType result = spell.spellBehaviour(world, playerEntity, itemstack);

            switch (result) {
                case FAIL:
                    return ActionResult.fail(itemstack);
                case SUCCESS:
                    return ActionResult.sidedSuccess(itemstack, world.isClientSide());
                case PASS:
                default:
                    return ActionResult.pass(itemstack);
            }
        }
    }
}
