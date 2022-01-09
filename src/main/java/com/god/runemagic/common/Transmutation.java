package com.god.runemagic.common;

import com.god.runemagic.RuneMagicMod;
import com.god.runemagic.common.ManaMap.Mana;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;

public class Transmutation {
    private final String entity;
    private final int fromRate;
    private final int toRate;
    private final float cost;

    public Transmutation(String entity, int fromRate, int toRate, float cost) {
        this.entity = entity;
        this.fromRate = fromRate;
        this.toRate = toRate;
        this.cost = cost;
    }

    public Result dryRun(ItemStack stack, Mana mana, Integer runeDiscount) {
        int discount = Math.min(runeDiscount, 9);
        float manaCostPerInstance = this.cost * this.fromRate * (1 - 0.05f * discount);
        int stackInstances = stack.getCount() / this.fromRate;

        int availableManaInstances = mana.hasEnough((int) (stackInstances * manaCostPerInstance))
                ? stackInstances
                : (int) (mana.getValue() / manaCostPerInstance);

        int realInstances = Math.min(availableManaInstances, stackInstances);

        int newEntityCount = realInstances * this.toRate;
        int remainingOldEntities = stack.getCount() - (realInstances * this.fromRate);
        float manaCost = realInstances * manaCostPerInstance;

        return new Result(this.entity, newEntityCount, remainingOldEntities, manaCost);
    }

    public Result transmute(World world, ItemEntity item, PlayerEntity player, Integer runeDiscount) {
        Mana mana = ManaMapSupplier.getStatic().getPlayerMana(player);
        Result result = this.dryRun(item.getItem(), mana, runeDiscount);

        Item resultingItem = Registry.ITEM.get(new ResourceLocation(result.entity));

        ItemStack resultingItemStack = new ItemStack(resultingItem, result.newEntityCount);
        ItemEntity resultingItemEntity = new ItemEntity(world, item.xo, item.yo, item.zo, resultingItemStack);
        boolean added = world.addFreshEntity(resultingItemEntity);

        if (added) {
            mana.decrement((int) Math.floor(result.manaUsed));

            if (result.remainingOldEntities > 0) {
                item.getItem().setCount(result.remainingOldEntities);
            } else {
                item.remove();
            }
        } else {
            RuneMagicMod.LOGGER.info("Failed to add entity {}", resultingItemEntity);
        }

        RuneMagicMod.LOGGER.info("Finished transmutation with {}", mana);

        return result;
    }

    @Override
    public String toString() {
        return String.format("%d:%d, @%f", this.fromRate, this.toRate, this.cost);
    }

    public class Result {
        public String entity;
        public int newEntityCount;
        public int remainingOldEntities;
        public float manaUsed;

        public Result(String entity, int newEntityCount, int remainingOldEntities, float manaUsed) {
            this.entity = entity;
            this.newEntityCount = newEntityCount;
            this.remainingOldEntities = remainingOldEntities;
            this.manaUsed = manaUsed;
        }
    }
}