package com.god.runemagic.common.entities;

import com.god.runemagic.RuneMagicMod;
import com.god.runemagic.block.runes.AbstractRune;
import com.god.runemagic.common.SpellRegistry;
import com.god.runemagic.common.spells.AbstractSpell;
import com.god.runemagic.item.SpellScroll;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public class RuneCraftingRecipe {
    private final AbstractRune primary;
    private final HashMap<Item, RuneCraftingSacrifice> sacrifices = new HashMap<>();
    private final HashMap<AbstractRune, RuneCraftingRune> runes = new HashMap<>();
    private final HashSet<AbstractSpell> resultingSpells = new HashSet<>();

    public RuneCraftingRecipe(AbstractRune primary) {
        this.primary = primary;
    }

    public void craft(RuneActivationContext context) {
        RuneCraftingResult result = new RuneCraftingResult(this, context);

        boolean success = this.removeSacrificedItems(context) && this.createResultingSpells(result);

        if (success) {
            context.getMana().decrement(result.getTotalCost());
        }
    }

    public boolean matchesRequirements(RuneActivationContext context) {
        return this.matchesRuneRequirements(context)
                && this.matchesSacrificeRequirements(context);
    }

    public AbstractRune getPrimary() {
        return primary;
    }

    public HashMap<Item, RuneCraftingSacrifice> getSacrifices() {
        return sacrifices;
    }

    public HashMap<AbstractRune, RuneCraftingRune> getRunes() {
        return runes;
    }

    public void addSacrifice(RuneCraftingSacrifice sacrifice) {
        this.sacrifices.put(sacrifice.getItem(), sacrifice);
    }

    public void addRune(RuneCraftingRune rune) {
        this.runes.put(rune.getRune(), rune);
    }

    public void addResultSpell(String spell) {
        this.resultingSpells.add(SpellRegistry.getSpell(spell));
    }

    private boolean matchesRuneRequirements(RuneActivationContext context) {
        int totalCost = 0;
        for (Map.Entry<AbstractRune, RuneCraftingRune> rune : this.runes.entrySet()) {
            int count = context.count(rune.getKey());

            if (count < rune.getValue().getMin()) {
                RuneMagicMod.LOGGER.info("did not match requirements for rune: {}. {}/{}", rune.getKey(), count, rune.getValue().getMin());
                return false;
            }

            totalCost += Math.min(count, rune.getValue().getMax()) * rune.getValue().getCost();
        }
        Mana mana = context.getMana();

        // TODO this is a little iffy, makes it so when not enough mana just don't find crafting recipe
        return mana.hasEnough(totalCost);
    }

    private boolean matchesSacrificeRequirements(RuneActivationContext context) {
        int keyCount = this.sacrifices.keySet().size();

        for (ItemEntity items : context.getSacrificedItems()) {
            ItemStack stack = items.getItem();
            if (this.sacrifices.containsKey(stack.getItem())) {
                RuneCraftingSacrifice sacrifice = this.sacrifices.get(stack.getItem());

                if (stack.getCount() < sacrifice.getMin()) {
                    RuneMagicMod.LOGGER.info("did not match requirements for items: {}. {}/{}", stack.getItem(), stack.getCount(), sacrifice.getMin());
                    return false;
                }
                keyCount--;
            }
        }

        RuneMagicMod.LOGGER.info("finishing match with keycount {}", keyCount);

        return keyCount == 0;
    }

    private boolean removeSacrificedItems(RuneActivationContext context) {
        for (ItemEntity item: context.getSacrificedItems()) {
            RuneCraftingSacrifice sacrifice = this.sacrifices.getOrDefault(item.getItem().getItem(), null);
            if (sacrifice == null) {
                continue;
            }

            int itemCount = item.getItem().getCount();

            if (sacrifice.getMax() > itemCount) {
                item.remove();
            } else {
                item.getItem().setCount(itemCount - sacrifice.getMax());
            }
        }

        return true;
    }

    private boolean createResultingSpells(RuneCraftingResult result) {
        boolean success = true;
        World world = result.getContext().getWorld();
        BlockPos position = result.getContext().getPosition();

        for (AbstractSpell spell: this.resultingSpells) {
            ItemEntity resultingItemEntity = new ItemEntity(world, position.getX(), position.getY(), position.getZ(), SpellScroll.makeSpellScroll(spell, result));
            if (!world.addFreshEntity(resultingItemEntity)) {
                success = false;
                break;
            }
        }

        return success;
    }
}
