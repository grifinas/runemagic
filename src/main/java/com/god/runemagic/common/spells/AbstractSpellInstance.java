package com.god.runemagic.common.spells;

import com.god.runemagic.common.entities.SpellCastingContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResultType;
import net.minecraft.world.World;

public class AbstractSpellInstance {
    private final AbstractSpell spell;
    private ItemStack stack;

    public AbstractSpellInstance(AbstractSpell spell) {
        this.spell = spell;
    }

    public AbstractSpell getSpell() {
        return spell;
    }

    public ActionResultType cast(World world, PlayerEntity playerEntity, ItemStack stack) {
        SpellCastingContext context = new SpellCastingContext(world, playerEntity, stack);
        return this.spell.spellBehaviour(context);
    }

    public boolean is(String name) {
        return this.getSpell().getRegistryName().equals(name);
    }

    public AbstractSpellInstance bindStack(ItemStack stack) {
        this.stack = stack;
        return this;
    }

    public ItemStack getStack() {
        return stack;
    }
}
