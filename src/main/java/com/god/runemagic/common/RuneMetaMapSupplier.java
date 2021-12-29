package com.god.runemagic.common;

import java.util.function.Supplier;

public class RuneMetaMapSupplier implements Supplier<RuneMetaMap> {
    private static RuneMetaMap INSTANCE = null;

    public RuneMetaMap get() {
        return RuneMetaMapSupplier.getStatic();
    }

    public static RuneMetaMap getStatic() {
        if (INSTANCE == null) {
            INSTANCE = new RuneMetaMap(RuneMetaMap.NBT_KEY);
        }

        return INSTANCE;
    }
}
