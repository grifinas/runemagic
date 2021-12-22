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

public class TransmutationMap {
	public static enum TYPES {
		UPGRADE, DOWNGRADE
	}

	private Map<String, Transmutation> upgrades;
	private Map<String, Transmutation> downgrades;
	private static TransmutationMap instance = null;

	public static TransmutationMap get() {
		if (TransmutationMap.instance == null) {
			TransmutationMap.instance = new TransmutationMap();
		}

		return TransmutationMap.instance;
	}

	public TransmutationMap() {
		// TODO add all wool and all wood
		ResourceLocation loc = new ResourceLocation(RuneMagicMod.MOD_ID + ":custom/transmutation.json");
		InputStream in;
		try {
			in = Minecraft.getInstance().getResourceManager().getResource(loc).getInputStream();
			BufferedReader reader = new BufferedReader(new InputStreamReader(in));
			Gson gson = new Gson();
			JsonElement je = gson.fromJson(reader, JsonElement.class);
			JsonObject json = je.getAsJsonObject();

			this.upgrades = new HashMap<>();
			this.downgrades = new HashMap<>();

			this.parseTransmutationJson(json);

			RuneMagicMod.LOGGER.info("upgrades: {}, downgrades: {}", this.upgrades, this.downgrades);
		} catch (

		IOException e) {
			RuneMagicMod.LOGGER.info("transmutation behaviouir errored out {}", e.getMessage());
			e.printStackTrace();
		}
	}

	public Transmutation findUpgrade(ItemEntity item) {
		return this.upgrades.get(getKey(item));
	}

	public Transmutation findDowngrade(ItemEntity item) {
		return this.downgrades.get(getKey(item));
	}

	public boolean isTransmutable(ItemEntity item) {
		String key = getKey(item);
		return this.upgrades.containsKey(key) || this.downgrades.containsKey(key);
	}

	private String getKey(ItemEntity item) {
		return this.getKey(Registry.ITEM.getKey(item.getItem().getItem()));
	}

	private String getKey(ResourceLocation location) {
		return String.format("%s:%s", location.getNamespace(), location.getPath());
	}

	private void parseTransmutationJson(JsonObject json) throws RuntimeException {
		for (Map.Entry<String, JsonElement> entry : json.entrySet()) {
			String transmutationSubject = entry.getKey();

			JsonElement transmutationRecipeElements = entry.getValue();
			if (!transmutationRecipeElements.isJsonObject()) {
				throw new RuntimeException("invalid transmutation.json structure");
			}

			JsonObject transmutationRecipes = transmutationRecipeElements.getAsJsonObject();

			for (Map.Entry<String, JsonElement> recipe : transmutationRecipes.entrySet()) {
				String stringRecipeType = recipe.getKey();
				JsonElement recipeContents = recipe.getValue();

				if (!recipeContents.isJsonObject()) {
					throw new RuntimeException("invalid transmutation.json structure");
				}

				JsonObject tr = recipeContents.getAsJsonObject();

				Transmutation transmutation = this.buildTransmutation(tr);
				if (stringRecipeType.contains("upgrade")) {
					this.upgrades.put(transmutationSubject, transmutation);
				} else {
					this.downgrades.put(transmutationSubject, transmutation);
				}
			}
		}
	}

	private Transmutation buildTransmutation(JsonObject tr) {
		float cost = tr.get("cost").getAsFloat();
		String rate = tr.get("rate").getAsString();
		String result = tr.get("result").getAsString();

		int fromToSeparator = rate.indexOf(':');
		int fromRate = Integer.parseInt(rate.substring(0, fromToSeparator));
		int toRate = Integer.parseInt(rate.substring(fromToSeparator + 1));

		return new Transmutation(result, fromRate, toRate, cost);
	}
}
