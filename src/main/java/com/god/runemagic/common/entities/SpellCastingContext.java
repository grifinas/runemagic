package com.god.runemagic.common.entities;

import com.god.runemagic.common.ManaMapSupplier;
import com.god.runemagic.common.spells.AbstractSpellInstance;
import com.god.runemagic.util.SpellProvider;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class SpellCastingContext {
    private final World world;
    private final PlayerEntity playerEntity;
    private final ItemStack stack;
    private final AbstractSpellInstance spell;

    public SpellCastingContext(World world, PlayerEntity playerEntity, ItemStack stack) {
        this.world = world;
        this.playerEntity = playerEntity;
        this.stack = stack;
        this.spell = SpellProvider.get(stack);
    }

    public Mana getMana() {
        return ManaMapSupplier.getStatic().getPlayerMana(this.playerEntity);
    }

    public World getWorld() {
        return world;
    }

    public PlayerEntity getPlayerEntity() {
        return playerEntity;
    }

    public ItemStack getStack() {
        return stack;
    }

    public AbstractSpellInstance getSpell() {
        return spell;
    }
}
