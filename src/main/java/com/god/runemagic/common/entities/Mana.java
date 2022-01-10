package com.god.runemagic.common.entities;

import com.god.runemagic.RuneMagicMod;
import com.god.runemagic.common.messages.ManaUpdate;
import com.god.runemagic.item.PhilosophersStone;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraftforge.fml.network.PacketDistributor;

public class Mana {
    private static final int FOOD_PER_10_MANA = 4;
    private static final int HEALTH_PER_10_MANA = 2;
    private static final int MAX_DRAIN_FOR_MANA = 20;

    protected final int maxValue;
    protected int value;
    protected final PlayerEntity player;
    protected boolean withPhilosopherStone = false;

    public Mana(int maxValue, PlayerEntity player) {
        this.maxValue = maxValue;
        this.player = player;
        this.value = this.maxValue;
    }

    public Mana(int maxValue, int value, PlayerEntity player) {
        this.maxValue = maxValue;
        this.value = value;
        this.player = player;
    }

    public boolean hasEnough(int cost) {
        return true;
//        return this.withPhilosopherStone || this.value >= cost;
    }

    public int getValue() {
        return this.value;
    }

    public void setValue(int value) {
        int realValue = Math.max(Math.min(this.getMaxValue(), value), 0);
        RuneMagicMod.LOGGER.info("set value {}, real {}", value, realValue);
        this.value = realValue;

        // If we overdrew, need to use food and health
        if (realValue != value && !this.drainPhilosopherStone(value)) {
            this.drainPlayer(value);
        }

        this.sendManaUpdate();
    }

//    public void withPhilosopherStone(boolean withPhilosopherShard) {
//        this.withPhilosopherStone = withPhilosopherShard;
//    }
//
//    public boolean hasPhilosopherStone() {
//        return this.withPhilosopherStone;
//    }

    public void decrement(int value) {
        this.setValue(this.value - value);
    }

    public void increment(int value) {
        this.setValue(this.value + value);
    }

    public int getMaxValue() {
        return this.maxValue + (this.withPhilosopherStone ? 100 : 0);
    }

    @Override
    public String toString() {
        return String.format("mana: %d/%d", this.value, this.getMaxValue());
    }

    protected ManaUpdate toManaUpdateMessage() {
        return new ManaUpdate(this.value, this.getMaxValue());
    }

    protected void sendManaUpdate() {
        //TODO does not work after save and quiting to menu and loading in again
        ServerPlayerEntity serverPlayer = this.player.level.getServer().getPlayerList().getPlayer(this.player.getUUID());
        RuneMagicMod.PACKET_HANDLER.send(PacketDistributor.PLAYER.with(() -> serverPlayer), this.toManaUpdateMessage());
    }

    protected void drainPlayer(int desiredValue) {
        int overdraft = this.value - desiredValue;

        int fromFood = FOOD_PER_10_MANA * overdraft / 10;
        int fromHealth = HEALTH_PER_10_MANA * overdraft / 10;
        int realFromFood = Math.min(fromFood, MAX_DRAIN_FOR_MANA);
        int realFromHealth = Math.min(fromHealth, MAX_DRAIN_FOR_MANA);

        RuneMagicMod.LOGGER.info("overdraft: {}, wanted to take from food {}, but could only take {}", overdraft, fromFood, realFromFood);
        RuneMagicMod.LOGGER.info("current food level: {}", this.player.getFoodData().getFoodLevel());;

        this.player.getFoodData().setFoodLevel(this.player.getFoodData().getFoodLevel() - realFromFood);
        this.player.hurt(DamageSource.GENERIC, realFromHealth);
    }

    protected boolean drainPhilosopherStone(int desiredValue) {
        int overdraft = this.value - desiredValue;

        for (ItemStack itemStack: this.player.inventory.items) {
            if (itemStack.getItem() instanceof PhilosophersStone.ItemCustom) {
                itemStack.setDamageValue(itemStack.getDamageValue() + overdraft);
                return true;
            }
        }

        return false;
    }
}
