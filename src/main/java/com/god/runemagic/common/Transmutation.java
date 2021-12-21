package com.god.runemagic.common;

import com.god.runemagic.RuneMagicMod;

import net.minecraft.entity.item.ItemEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;

public class Transmutation {
	private String entity;
	private int fromRate;
	private int toRate;
	private float cost;

	public Transmutation(String entity, int fromRate, int toRate, float cost) {
		this.entity = entity;
		this.fromRate = fromRate;
		this.toRate = toRate;
		this.cost = cost;
	}

	public Result dryRun(ItemStack stack, float mana) {
		int availableManaInstances = (int) (mana / this.cost);
		int stackInstances = stack.getCount() / this.fromRate;
	
		int realInstances = Math.min(availableManaInstances, stackInstances);
		
		int newEntityCount = realInstances * this.toRate;
		int remainingOldEntities = stack.getCount() - (realInstances * this.fromRate);
		float manaCost = realInstances * this.cost;
		
		return new Result(this.entity, newEntityCount, remainingOldEntities, manaCost);
	}
	
	public Result transmute(World world, ItemEntity item, float mana) {
		Result result = this.dryRun(item.getItem(), mana);

		// TODO lower mana for user

		Item resultingItem = Registry.ITEM.get(new ResourceLocation(result.entity));
		
		ItemStack resultingItemStack = new ItemStack(resultingItem, result.newEntityCount);
		ItemEntity resultingItemEntity = new ItemEntity(world, item.xo, item.yo, item.zo, resultingItemStack);
		boolean added = world.addFreshEntity(resultingItemEntity);
		
		if (added) {
			if (result.remainingOldEntities > 0) {
				item.getItem().setCount(result.remainingOldEntities);
			} else {
				item.remove();
			}
		} else {
			RuneMagicMod.LOGGER.info("Failed to add entity {}", resultingItemEntity);
		}
		
		return result;
	}

	@Override
	public String toString() {
		return this.entity + "%d:%d, @%f".formatted(this.fromRate, this.toRate, this.cost);
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