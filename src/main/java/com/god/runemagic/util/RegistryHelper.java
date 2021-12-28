package com.god.runemagic.util;

import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;

public class RegistryHelper {
    public static Item getItemFromRegistry(String location) {
        return Registry.ITEM.get(new ResourceLocation(location));
    }
    public static Item getItemFromRegistry(ResourceLocation location) {
        return Registry.ITEM.get(location);
    }
}
