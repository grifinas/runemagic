package com.god.runemagic.common.spells;

import com.god.runemagic.RunemagicModElements;
import com.god.runemagic.common.SpellRegistry;
import com.god.runemagic.common.entities.RuneCraftingResult;
import com.god.runemagic.common.entities.SpellCastingContext;
import com.god.runemagic.util.SpellProvider;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ActionResultType;

abstract public class AbstractSpell extends RunemagicModElements.ModElement {
    protected final SpellType type;

    public AbstractSpell(RunemagicModElements instance, SpellType type) {
        super(instance, 8);
        this.type = type;
        SpellRegistry.registerSpell(this.getRegistryName(), this);

    }

    /**
     * Runs when the spell is created
     * */
    public CompoundNBT initialize(RuneCraftingResult craftingResult) {
        CompoundNBT c = new CompoundNBT();
        c.putString("name", this.getRegistryName());
        //TODO does not seem correct
        c.putString(SpellProvider.SPELL_TYPE_KEY, this.type.name());
        return c;
    }

    abstract public ActionResultType spellBehaviour(SpellCastingContext context);
    abstract public String getRegistryName();
    abstract public AbstractSpellInstance getInstance(CompoundNBT nbt);

}
