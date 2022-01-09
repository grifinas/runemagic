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
        this.map.put(player.getStringUUID(), new Mana(100, this, player));
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
            // TODO null as player is a bug waiting to happen
            this.map.put(playerUUID, Mana.fromNBT((CompoundNBT) map.get(playerUUID), this, null));
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

    public static class Mana extends com.god.runemagic.common.entities.Mana {
        private final ManaMap parent;

        public Mana(int maxValue, ManaMap parent, PlayerEntity player) {
            super(maxValue, player);
            this.parent = parent;
        }

        public static Mana fromNBT(@Nullable CompoundNBT manaNBT, ManaMap parent, PlayerEntity player) {
            if (manaNBT == null) {
                return new Mana(100, parent, player);
            }

            Mana mana = new Mana(manaNBT.getInt("maxMana"), parent, player);
            mana.setValue(manaNBT.getInt("mana"));
            mana.withPhilosopherStone(manaNBT.getBoolean("hasPhilosopherShard"));
            return mana;
        }

        public void setValue(int value) {
            super.setValue(value);
            this.parent.setDirty();
        }

        public CompoundNBT toNBT() {
            CompoundNBT manaNBT = new CompoundNBT();
            manaNBT.putInt("mana", this.getValue());
            manaNBT.putInt("maxMana", this.getMaxValue());
            manaNBT.putBoolean("hasPhilosopherShard", this.hasPhilosopherStone());

            return manaNBT;
        }
    }
}
