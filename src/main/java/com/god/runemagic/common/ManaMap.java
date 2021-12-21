package com.god.runemagic.common;

import java.util.HashMap;
import java.util.Map;

import net.minecraft.entity.player.PlayerEntity;

public class ManaMap {
	private static ManaMap INSTANCE = null;
	private Map<PlayerEntity, Mana> map;
	
	public static ManaMap get() {
		if (ManaMap.INSTANCE == null) {
			ManaMap.INSTANCE = new ManaMap();
		}
		
		return ManaMap.INSTANCE;
	}
	
	public ManaMap() {
		this.map = new HashMap<>();
	}
	
	public void addPlayer(PlayerEntity player) {
		this.map.put(player, new Mana(100));
	}
	
	public Mana getPlayerMana(PlayerEntity player) {
		return this.map.get(player);
	}
	
	public static class Mana {
		private int value;
		private int maxValue;
		
		public Mana(int maxValue) {
			this.maxValue = maxValue;
			this.value = this.maxValue;
		}
		
		public int getValue() {
			return this.value;
		}
		
		public int getMaxValue() {
			return this.maxValue;
		}
		
		public void setValue(int value) {
			this.value = Math.min(this.maxValue, value);
		}
		
		@Override
		public String toString() {
			return "mana: %d/%d".formatted(this.value, this.maxValue);
		}
	}
}
