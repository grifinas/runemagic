package com.god.runemagic.common;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

import com.god.runemagic.RuneMagicMod;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;

public class DisassemblyMap {
	public static enum TYPES {
		UPGRADE, DOWNGRADE
	}

	private Map<String, Integer> disassemblies;
	private static DisassemblyMap instance = null;

	public static DisassemblyMap get() {
		if (DisassemblyMap.instance == null) {
			DisassemblyMap.instance = new DisassemblyMap();
		}

		return DisassemblyMap.instance;
	}

	public DisassemblyMap() {
		// TODO add all wool and all wood
		ResourceLocation loc = new ResourceLocation(RuneMagicMod.MOD_ID + ":custom/disassembly.json");
		InputStream in;
		try {
			in = Minecraft.getInstance().getResourceManager().getResource(loc).getInputStream();
			BufferedReader reader = new BufferedReader(new InputStreamReader(in));
			Gson gson = new Gson();
			JsonElement je = gson.fromJson(reader, JsonElement.class);
			JsonObject json = je.getAsJsonObject();

			this.disassemblies = new HashMap<>();

			this.parseTransmutationJson(json);
		} catch (

		IOException e) {
			RuneMagicMod.LOGGER.info("transmutation behaviouir errored out {}", e.getMessage());
			e.printStackTrace();
		}
	}

	public int findValue(ItemEntity item) {
		return this.disassemblies.get(getKey(item)) * item.getItem().getCount();
	}

	private String getKey(ItemEntity item) {
		return this.getKey(Registry.ITEM.getKey(item.getItem().getItem()));
	}

	private String getKey(ResourceLocation location) {
		return "%s:%s".formatted(location.getNamespace(), location.getPath());
	}

	private void parseTransmutationJson(JsonObject json) throws RuntimeException {
		for (Map.Entry<String, JsonElement> entry : json.entrySet()) {
			this.disassemblies.put(entry.getKey(), entry.getValue().getAsInt());
		}
	}
}
