package com.god.runemagic.common;

import com.god.runemagic.RuneMagicMod;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.world.storage.WorldSavedData;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;

public class ManaMap extends WorldSavedData {
    public static final String NBT_KEY = "mana_map";

    private final Map<String, Mana> map;

    public ManaMap(String fileName) {
        super(fileName);
        RuneMagicMod.LOGGER.info("creating ManaMap");
        this.map = new HashMap<>();
    }

    public void addPlayer(PlayerEntity player) {
        if (this.map.get(player.getStringUUID()) != null) {
            return;
        }
        RuneMagicMod.LOGGER.info("Adding new player, existing data: {}", this.map);
        this.map.put(player.getStringUUID(), new Mana(100, this));
        this.setDirty();
    }

    public Mana getPlayerMana(PlayerEntity player) {
        return this.map.get(player.getStringUUID());
    }

    @Override
    public void load(CompoundNBT nbt) {
        RuneMagicMod.LOGGER.info("Loading mana");
        CompoundNBT map = (CompoundNBT) nbt.get(NBT_KEY);
        if (map == null) {
            return;
        }
        map.getAllKeys().forEach(playerUUID -> {
            this.map.put(playerUUID, Mana.fromNBT((CompoundNBT) map.get(playerUUID), this));
        });
    }

    @Override
    public CompoundNBT save(CompoundNBT nbt) {
        RuneMagicMod.LOGGER.info("Saving mana");
        CompoundNBT map = new CompoundNBT();
        this.map.forEach((playerUUID, mana) -> {
            map.put(playerUUID, mana.toNBT());
        });
        nbt.put(NBT_KEY, map);
        return map;
    }

    public static class Mana {
        private final int maxValue;
        private final ManaMap parent;
        private int value;

        public Mana(int maxValue, ManaMap parent) {
            this.maxValue = maxValue;
            this.value = this.maxValue;
            this.parent = parent;
        }

        public static Mana fromNBT(@Nullable CompoundNBT manaNBT, ManaMap parent) {
            if (manaNBT == null) {
                return new Mana(100, parent);
            }

            Mana mana = new Mana(manaNBT.getInt("maxMana"), parent);
            mana.setValue(manaNBT.getInt("mana"));
            return mana;
        }

        public int getValue() {
            return this.value;
        }

        public void setValue(int value) {
            this.value = Math.min(this.maxValue, value);
            this.parent.setDirty();
        }

        public int getMaxValue() {
            return this.maxValue;
        }

        @Override
        public String toString() {
            return String.format("mana: %d/%d", this.value, this.maxValue);
        }

        public CompoundNBT toNBT() {
            CompoundNBT manaNBT = new CompoundNBT();
            manaNBT.putInt("mana", this.getValue());
            manaNBT.putInt("maxMana", this.getMaxValue());

            return manaNBT;
        }
    }
}
