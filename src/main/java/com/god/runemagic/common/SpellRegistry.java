package com.god.runemagic.common;

import com.god.runemagic.common.spells.AbstractSpell;

import java.util.Collection;
import java.util.HashMap;

public class SpellRegistry {
    private static final HashMap<String, AbstractSpell> spells = new HashMap<>();

    public static AbstractSpell getSpell(String key) {
        if (!spells.containsKey(key)) {
            throw new RuntimeException(String.format("Failed to find spell with key: %s", key));
        }
        return spells.get(key);
    }

    public static Collection<AbstractSpell> getSpells() {
        return spells.values();
    }

    public static void registerSpell(String key, AbstractSpell spell) {
        spells.put(key, spell);
    }
}
