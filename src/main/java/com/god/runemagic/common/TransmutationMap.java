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
public class TransmutationMap extends RunemagicModElements.ModElement {
    private static TransmutationMap instance = null;
    private Map<String, Transmutation> upgrades;
    private Map<String, Transmutation> downgrades;

    public TransmutationMap(RunemagicModElements elements) {
        super(elements, 200);
        instance = this;
    }

    public static TransmutationMap get() {
        return TransmutationMap.instance;
    }

    @Override
    public void serverLoad(FMLServerStartingEvent event) {
        super.serverLoad(event);

        ResourceLocation loc = new ResourceLocation(RuneMagicMod.MOD_ID + ":custom/transmutation.json");
        InputStream in;
        try {
            in = event.getServer().getDataPackRegistries().getResourceManager().getResource(loc).getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            Gson gson = new Gson();
            JsonElement je = gson.fromJson(reader, JsonElement.class);
            JsonObject json = je.getAsJsonObject();

            this.upgrades = new HashMap<>();
            this.downgrades = new HashMap<>();

            this.parseTransmutationJson(json);

            RuneMagicMod.LOGGER.info("upgrades: {}, downgrades: {}", this.upgrades, this.downgrades);
        } catch (IOException e) {
            RuneMagicMod.LOGGER.info("transmutation behaviour errored out {}", e.getMessage());
            e.printStackTrace();
        }
    }

    public Transmutation findUpgrade(ItemEntity item) {
        return this.upgrades.get(getKey(item));
    }

    public Transmutation findDowngrade(ItemEntity item) {
        return this.downgrades.get(getKey(item));
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
