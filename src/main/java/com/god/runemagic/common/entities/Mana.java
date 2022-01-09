package com.god.runemagic.common.entities;

import com.god.runemagic.RuneMagicMod;
import com.god.runemagic.common.messages.ManaUpdate;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraftforge.fml.network.PacketDistributor;

public class Mana {
    protected final int maxValue;
    protected int value;
    protected final PlayerEntity player;

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

    public int getValue() {
        return this.value;
    }

    public void setValue(int value) {
        this.value = Math.max(Math.min(this.maxValue, value), 0);
        //TODO does not work after save and quiting to menu and loading in again
        ServerPlayerEntity serverPlayer = this.player.level.getServer().getPlayerList().getPlayer(this.player.getUUID());
        RuneMagicMod.PACKET_HANDLER.send(PacketDistributor.PLAYER.with(() -> serverPlayer), this.toManaUpdateMessage());
    }

    public void decrement(int value) {
        this.setValue(this.value - value);
    }

    public void increment(int value) {
        this.setValue(this.value + value);
    }

    public int getMaxValue() {
        return this.maxValue;
    }

    @Override
    public String toString() {
        return String.format("mana: %d/%d", this.value, this.maxValue);
    }

    protected ManaUpdate toManaUpdateMessage() {
        return new ManaUpdate(this.value, this.maxValue);
    }
}
