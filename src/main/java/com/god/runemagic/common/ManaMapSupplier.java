package com.god.runemagic.common;

import java.util.function.Supplier;

public class ManaMapSupplier implements Supplier<ManaMap> {
    private static ManaMap INSTANCE = null;

    public ManaMap get() {
        return ManaMapSupplier.getStatic();
    }

    public static ManaMap getStatic() {
        if (INSTANCE == null) {
            INSTANCE = new ManaMap(ManaMap.NBT_KEY);
        }

        return INSTANCE;
    }
}
