package com.god.runemagic.common.entities;

import com.god.runemagic.block.runes.AbstractRune;

import java.util.HashMap;
import java.util.Map;

public class RuneCraftingResult {
    private final RuneCraftingRecipe recipe;
    private final RuneActivationContext context;

    public RuneCraftingResult(RuneCraftingRecipe recipe, RuneActivationContext context) {
        this.recipe = recipe;
        this.context = context;
    }

    public Integer count(AbstractRune key) {
        HashMap<AbstractRune, RuneCraftingRune> runes = this.recipe.getRunes();
        if (runes.containsKey(key)) {
            return this.context.count(key, runes.get(key).getMax());
        }

        return 0;
    }

    public int getTotalCost() {
        int totalCost = 0;
        for (Map.Entry<AbstractRune, RuneCraftingRune> rune : this.recipe.getRunes().entrySet()) {
            int count = context.count(rune.getKey());

            totalCost += Math.min(count, rune.getValue().getMax()) * rune.getValue().getCost();
        }

        return totalCost;
    }

    public RuneCraftingRecipe getRecipe() {
        return recipe;
    }

    public RuneActivationContext getContext() {
        return context;
    }
}
