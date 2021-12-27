package com.god.runemagic.common;

public class Mana {
    protected final int maxValue;
    protected int value;

    public Mana(int maxValue) {
        this.maxValue = maxValue;
        this.value = this.maxValue;
    }

    public Mana(int maxValue, int value) {
        this.maxValue = maxValue;
        this.value = value;
    }

    public int getValue() {
        return this.value;
    }

    public void setValue(int value) {
        this.value = Math.min(this.maxValue, value);
    }

    public int getMaxValue() {
        return this.maxValue;
    }

    @Override
    public String toString() {
        return String.format("mana: %d/%d", this.value, this.maxValue);
    }
}
