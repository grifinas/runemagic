package com.god.runemagic.common.spells;

import com.god.runemagic.util.SpellProvider;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ActionResultType;
import net.minecraft.world.World;

abstract public class AbstractSpell {
    protected final String name = "Spell";
    protected final int cost;
    protected final SpellType type;

    public AbstractSpell(int cost, SpellType type) {
        this.cost = cost;
        this.type = type;
    }

    public CompoundNBT toNBT() {
        CompoundNBT c = new CompoundNBT();
        c.putInt("cost", this.cost);
        c.putString("name", this.name);
        //TODO does not seem correct
        c.putString(SpellProvider.SPELL_TYPE_KEY, this.type.name());
        return c;
    }

    abstract public ActionResultType spellBehaviour(World world, PlayerEntity playerEntity, ItemStack stack);
    abstract public AbstractSpell fromNBT(CompoundNBT nbt);
}
