package com.god.runemagic.util;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import org.jetbrains.annotations.NotNull;

public class RegistryHelper {
    public static @NotNull Item getItemFromRegistry(String location) {
        return Registry.ITEM.get(new ResourceLocation(location));
    }
    public static @NotNull Item getItemFromRegistry(ResourceLocation location) {
        return Registry.ITEM.get(location);
    }

    public static @NotNull Block getBlockFromRegistry(String location) {
        return Registry.BLOCK.get(new ResourceLocation(location));
    }
}
