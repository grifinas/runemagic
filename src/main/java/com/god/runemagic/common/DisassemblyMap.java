package com.god.runemagic.common;

import com.god.runemagic.RunemagicModElements;
import com.god.runemagic.util.ServerResourceReader;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;

@RunemagicModElements.ModElement.Tag
public class DisassemblyMap extends RunemagicModElements.ModElement {
    private static DisassemblyMap instance = null;
    private Map<String, Integer> disassemblies;

    public DisassemblyMap(RunemagicModElements elements) {
        super(elements, 200);
        instance = this;
    }

    public static DisassemblyMap get() {
        return DisassemblyMap.instance;
    }

    @Override
    public void serverLoad(FMLServerStartingEvent event) {
        this.disassemblies = new HashMap<>();
        this.parseTransmutationJson(ServerResourceReader.getInstance().readJson("disassembly.json"));
    }

    public double findValue(ItemEntity item) {
        return this.getMultiplier(item) * item.getItem().getCount();
    }

    private double getMultiplier(ItemEntity item) {
        Integer multiplier = this.disassemblies.get(getKey(item));

        if (multiplier != null) {
            return multiplier;
        }

        return item.getItem().getItem() instanceof BlockItem ? 0.5 : 1;
    }

    private String getKey(ItemEntity item) {
        return this.getKey(Registry.ITEM.getKey(item.getItem().getItem()));
    }

    private String getKey(ResourceLocation location) {
        return String.format("%s:%s", location.getNamespace(), location.getPath());
    }

    private void parseTransmutationJson(@Nullable JsonObject json) throws RuntimeException {
        if (json == null) {
            return;
        }
        for (Map.Entry<String, JsonElement> entry : json.entrySet()) {
            this.disassemblies.put(entry.getKey(), entry.getValue().getAsInt());
        }
    }
}
