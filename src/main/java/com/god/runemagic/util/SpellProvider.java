package com.god.runemagic.util;

import com.god.runemagic.common.SpellRegistry;
import com.god.runemagic.common.spells.AbstractSpellInstance;
import com.god.runemagic.common.spells.FireBallSpell;
import com.god.runemagic.common.spells.SpellType;
import com.god.runemagic.common.spells.TeleportationSpell;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;

import javax.annotation.Nullable;
import java.util.function.Function;

public class SpellProvider {
    public static String SPELL_KEY = "spell_key";
    public static String SPELL_TYPE_KEY = "spell_type_key";

    public static @Nullable
    AbstractSpellInstance get(ItemStack stack) {
        if (!stack.hasTag()) {
            return null;
        }

        CompoundNBT nbt = (CompoundNBT) stack.getTag().get(SPELL_KEY);
        if (nbt == null) {
            return null;
        }

        String type = nbt.getString(SPELL_TYPE_KEY);

        return SpellProvider.getNBTFunction(type).apply(nbt).bindStack(stack);
    }

    private static Function<CompoundNBT, AbstractSpellInstance> getNBTFunction(String type) {
        if (type.equals(SpellType.FIREBALL.name())) {
            return SpellRegistry.getSpell(FireBallSpell.NAME)::getInstance;
        } else if (type.equals(SpellType.TELEPORTATION.name())) {
            return SpellRegistry.getSpell(TeleportationSpell.NAME)::getInstance;
        }

        throw new RuntimeException("unknown spell type");
    }
}
