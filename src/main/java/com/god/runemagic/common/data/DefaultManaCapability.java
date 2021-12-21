package com.god.runemagic.common.data;

public class DefaultManaCapability implements IManaStorage {
	private int mana = 100;
	private int maxMana = 100;
	
	@Override
	public int getMana() {
		return this.mana;
	}

	@Override
	public int getMaxMana() {
		return this.maxMana;
	}

	@Override
	public void setMana(int amount) {
		this.mana = Math.min(amount, this.maxMana);
	}

}
