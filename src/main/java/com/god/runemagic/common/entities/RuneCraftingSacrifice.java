package com.god.runemagic.common.entities;

import net.minecraft.item.Item;

public class RuneCraftingSacrifice {
    private Integer max = null;
    private Integer min = null;
    private final Item item;

    public RuneCraftingSacrifice(Item item) {
        this.item = item;
    }

    public Item getItem() {
        return item;
    }

    public Integer getMax() {
        return max;
    }

    public Integer getMin() {
        return min;
    }

    public void setCount(int count) {
        this.min = count;
        this.max = count;
    }

    public void setMax(int max) {
        this.max = max;
    }

    public void setMin(int min) {
        this.min = min;
    }
}
