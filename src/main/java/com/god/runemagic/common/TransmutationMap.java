package com.god.runemagic.common;

import com.god.runemagic.RunemagicModElements;
import com.god.runemagic.util.ServerResourceReader;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

        this.upgrades = new HashMap<>();
        this.downgrades = new HashMap<>();

        this.parseTransmutationJson(ServerResourceReader.getInstance().readJson("transmutation.json"));
    }

    public Transmutation findUpgrade(ItemEntity item) {
        String key = getKey(item);
        Transmutation custom = this.customBehaviour(key);
        if (custom == null) {
            return this.upgrades.get(key);
        }

        return custom;
    }

    public Transmutation findDowngrade(ItemEntity item) {
        return this.downgrades.get(getKey(item));
    }

    private @Nullable Transmutation customBehaviour(String key) {
        return this.customWoolBehaviour(key);
    }

    // TODO it might be possible do this with tags if all wool elements have a wool tag
    private @Nullable Transmutation customWoolBehaviour(String key) {
        Pattern woolColorRegex = Pattern.compile("minecraft:([a-z_]+)_wool");
        Matcher m = woolColorRegex.matcher(key);
        if (m.matches()) {
            String woolColor = m.group(1);
            String dyeName = String.format("minecraft:%s_dye", woolColor);
            if (Registry.ITEM.containsKey(new ResourceLocation(dyeName))) {
                return new Transmutation(dyeName, 1, 1, 2);
            }
        }

        return null;
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
