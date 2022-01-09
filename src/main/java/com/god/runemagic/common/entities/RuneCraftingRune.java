package com.god.runemagic.common.entities;

import com.god.runemagic.block.runes.AbstractRune;

public class RuneCraftingRune {
    private final AbstractRune rune;
    private Integer max = null;
    private Integer min = null;
    private Integer cost = null;

    public RuneCraftingRune(AbstractRune rune) {
        this.rune = rune;
    }

    public AbstractRune getRune() {
        return rune;
    }

    public Integer getMax() {
        return max;
    }

    public Integer getMin() {
        return min;
    }

    public Integer getCost() {
        return cost;
    }

    public void setMax(int max) {
        this.max = max;
    }

    public void setMin(int min) {
        this.min = min;
    }

    public void setCost(Integer cost) {
        this.cost = cost;
    }
}
