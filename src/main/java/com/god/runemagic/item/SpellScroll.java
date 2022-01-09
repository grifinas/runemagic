package com.god.runemagic.item;

import com.god.runemagic.RunemagicModElements;
import com.god.runemagic.common.entities.RuneCraftingResult;
import com.god.runemagic.common.spells.AbstractSpell;
import com.god.runemagic.common.spells.AbstractSpellInstance;
import com.god.runemagic.util.SpellProvider;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Rarity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.world.World;
import net.minecraftforge.registries.ObjectHolder;

@RunemagicModElements.ModElement.Tag
public class SpellScroll extends RunemagicModElements.ModElement {
    @ObjectHolder("runemagic:spell_scroll")
    public static final Item block = null;

    public SpellScroll(RunemagicModElements instance) {
        super(instance, 4);
    }

    public static ItemStack makeSpellScroll(AbstractSpell spell, RuneCraftingResult result) {
        ItemStack resultingItemStack = new ItemStack(block, 1);

        CompoundNBT nbt = new CompoundNBT();
        nbt.put(SpellProvider.SPELL_KEY, spell.initialize(result));
        resultingItemStack.setTag(nbt);
        return resultingItemStack;
    }

    @Override
    public void initElements() {
        elements.items.add(ItemCustom::new);
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

            AbstractSpellInstance spell = SpellProvider.get(itemstack);
            if (spell == null) {
                return ActionResult.pass(itemstack);
            }

            ActionResultType result = world.isClientSide ? ActionResultType.SUCCESS : spell.cast(world, playerEntity, itemstack);

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
