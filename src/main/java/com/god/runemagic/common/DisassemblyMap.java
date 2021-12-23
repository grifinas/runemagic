package com.god.runemagic.common;

import com.god.runemagic.RuneMagicMod;
import com.god.runemagic.RunemagicModElements;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
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
        ResourceLocation loc = new ResourceLocation(RuneMagicMod.MOD_ID + ":custom/disassembly.json");
        InputStream in;
        try {
            in = event.getServer().getDataPackRegistries().getResourceManager().getResource(loc).getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            Gson gson = new Gson();
            JsonElement je = gson.fromJson(reader, JsonElement.class);
            JsonObject json = je.getAsJsonObject();

            this.disassemblies = new HashMap<>();

            this.parseTransmutationJson(json);
        } catch (IOException e) {
            RuneMagicMod.LOGGER.info("disassembly behaviour errored out {}", e.getMessage());
            e.printStackTrace();
        }
    }

    public int findValue(ItemEntity item) {
        return this.disassemblies.getOrDefault(getKey(item), 1) * item.getItem().getCount();
    }

    private String getKey(ItemEntity item) {
        return this.getKey(Registry.ITEM.getKey(item.getItem().getItem()));
    }

    private String getKey(ResourceLocation location) {
        return String.format("%s:%s", location.getNamespace(), location.getPath());
    }

    private void parseTransmutationJson(JsonObject json) throws RuntimeException {
        for (Map.Entry<String, JsonElement> entry : json.entrySet()) {
            this.disassemblies.put(entry.getKey(), entry.getValue().getAsInt());
        }
    }
}
