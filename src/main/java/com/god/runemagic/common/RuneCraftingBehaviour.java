package com.god.runemagic.common;

import com.god.runemagic.RuneMagicMod;
import com.god.runemagic.RunemagicModElements;
import com.god.runemagic.block.runes.AbstractRune;
import com.god.runemagic.common.entities.RuneActivationContext;
import com.god.runemagic.common.entities.RuneCraftingRecipe;
import com.god.runemagic.common.entities.RuneCraftingRune;
import com.god.runemagic.common.entities.RuneCraftingSacrifice;
import com.god.runemagic.common.spells.AbstractSpell;
import com.god.runemagic.util.RegistryHelper;
import com.god.runemagic.util.ServerResourceReader;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;

@RunemagicModElements.ModElement.Tag
public class RuneCraftingBehaviour extends RunemagicModElements.ModElement {
    private static RuneCraftingBehaviour instance = null;
    private final HashMap<AbstractRune, HashSet<RuneCraftingRecipe>> recipes = new HashMap<>();

    public RuneCraftingBehaviour(RunemagicModElements elements) {
        super(elements, 300);
        instance = this;
    }

    public static RuneCraftingBehaviour get() {
        return RuneCraftingBehaviour.instance;
    }

    public @Nullable
    RuneCraftingRecipe match(RuneActivationContext context) {
        if (!this.recipes.containsKey(context.getPrimary())) {
            RuneMagicMod.LOGGER.info("does not contain key: {}", context.getPrimary());
            return null;
        }

        HashSet<RuneCraftingRecipe> recipeSet = this.recipes.get(context.getPrimary());

        Optional<RuneCraftingRecipe> optionalRecipe = recipeSet.stream().filter(recipe -> recipe.matchesRequirements(context)).findFirst();

        return optionalRecipe.orElse(null);
    }

    @Override
    public void serverLoad(FMLServerStartingEvent event) {
        for (AbstractSpell spell : SpellRegistry.getSpells()) {
            this.parseRuneCraftingJson(ServerResourceReader.getInstance().readJson(String.format("rune_recipes/%s.json", spell.getRegistryName())));
        }
    }

    private void parseRuneCraftingJson(JsonObject json) {
        if (json == null) {
            return;
        }

        JsonObject sacrifices = json.get("sacrifices").getAsJsonObject();
        JsonObject runes = json.get("runes").getAsJsonObject();
        JsonObject result = json.get("result").getAsJsonObject();

        AbstractRune primaryRune = (AbstractRune) RegistryHelper.getBlockFromRegistry(json.get("primary").getAsString());
        RuneCraftingRecipe recipe = new RuneCraftingRecipe(primaryRune);

        this.parseSacrifices(recipe, sacrifices);
        this.parseRunes(recipe, runes);
        this.parseResults(recipe, result);
        this.addRecipe(recipe);
    }

    private void addRecipe(RuneCraftingRecipe recipe) {
        if (!this.recipes.containsKey(recipe.getPrimary())) {
            this.recipes.put(recipe.getPrimary(), new HashSet<>());
        }

        this.recipes.get(recipe.getPrimary()).add(recipe);
    }

    private void parseResults(RuneCraftingRecipe recipe, JsonObject result) {
        if (result.has("spell")) {
            recipe.addResultSpell(result.get("spell").getAsString());
        }
    }

    private void parseRunes(RuneCraftingRecipe recipe, JsonObject runes) {
        for (Map.Entry<String, JsonElement> runeJson : runes.entrySet()) {
            RuneCraftingRune rune = new RuneCraftingRune((AbstractRune) RegistryHelper.getBlockFromRegistry(runeJson.getKey()));

            JsonObject contents = runeJson.getValue().getAsJsonObject();

            if (contents.has("cost")) {
                rune.setCost(contents.get("cost").getAsInt());
            }

            if (contents.has("min")) {
                rune.setMin(contents.get("min").getAsInt());
            }

            if (contents.has("max")) {
                rune.setMax(contents.get("max").getAsInt());
            }

            recipe.addRune(rune);
        }
    }

    private void parseSacrifices(RuneCraftingRecipe recipe, JsonObject sacrifices) {
        for (Map.Entry<String, JsonElement> sacrificeJson : sacrifices.entrySet()) {
            RuneCraftingSacrifice sacrifice = new RuneCraftingSacrifice(RegistryHelper.getItemFromRegistry(sacrificeJson.getKey()));

            JsonObject contents = sacrificeJson.getValue().getAsJsonObject();
            if (contents.has("count")) {
                sacrifice.setCount(contents.get("count").getAsInt());
            }

            if (contents.has("min")) {
                sacrifice.setMin(contents.get("min").getAsInt());
            }

            if (contents.has("max")) {
                sacrifice.setMax(contents.get("max").getAsInt());
            }

            recipe.addSacrifice(sacrifice);
        }
    }
}
