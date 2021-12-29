package com.god.runemagic.common;

import com.god.runemagic.RuneMagicMod;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.storage.WorldSavedData;

import javax.annotation.Nullable;
import java.util.*;

public class RuneMetaMap extends WorldSavedData {
    public static final String NBT_KEY = "rune_meta_map";

    private final Map<String, RuneMeta> map;

    public RuneMetaMap(String fileName) {
        super(fileName);
        RuneMagicMod.LOGGER.info("creating RuneMetaMap");
        this.map = new HashMap<>();
    }

    public void addPlayer(PlayerEntity player) {
        if (this.map.get(player.getStringUUID()) != null) {
            return;
        }
        RuneMagicMod.LOGGER.info("Adding new player, existing data: {}", this.map);
        this.map.put(player.getStringUUID(), new RuneMeta(this, player));
        this.setDirty();
    }

    public RuneMeta getPlayerRuneMeta(PlayerEntity player) {
        return this.map.get(player.getStringUUID());
    }

    @Override
    public void load(CompoundNBT nbt) {
        RuneMagicMod.LOGGER.info("Loading rune meta");
        CompoundNBT map = (CompoundNBT) nbt.get(NBT_KEY);
        if (map == null) {
            return;
        }
        map.getAllKeys().forEach(playerUUID -> {
            // TODO null as player is a bug waiting to happen
            this.map.put(playerUUID, RuneMeta.fromNBT((CompoundNBT) map.get(playerUUID), this, null));
        });
    }

    @Override
    public CompoundNBT save(CompoundNBT nbt) {
        RuneMagicMod.LOGGER.info("Saving rune meta");
        CompoundNBT map = new CompoundNBT();
        this.map.forEach((playerUUID, mana) -> {
            map.put(playerUUID, mana.toNBT());
        });
        nbt.put(NBT_KEY, map);
        return map;
    }

    public static class RuneMeta {
        private final RuneMetaMap parent;
        private final PlayerEntity player;
        private final BlockPos[] tpPoints;
        private int tpPointFinalIndex = 0;

        public RuneMeta(RuneMetaMap parent, PlayerEntity player) {
            this.parent = parent;
            this.player = player;
            this.tpPoints = new BlockPos[2];
        }

        public static RuneMeta fromNBT(@Nullable CompoundNBT manaNBT, RuneMetaMap parent, PlayerEntity player) {
            if (manaNBT == null) {
                return new RuneMeta(parent, player);
            }

            RuneMeta mana = new RuneMeta(parent, player);
            return mana;
        }

        public void addTpPoint(BlockPos pos) {
            tpPointFinalIndex = (tpPointFinalIndex + 1) % 2;
            this.tpPoints[tpPointFinalIndex] = pos;
            RuneMagicMod.LOGGER.info("tpPoints: {}", (Object) this.tpPoints);
        }

        public @Nullable BlockPos getPreviousPointTo() {
            int tpPointOtherIndex = (tpPointFinalIndex + 1) % 2;
            return this.tpPoints[tpPointOtherIndex];
        }

        public CompoundNBT toNBT() {
            CompoundNBT manaNBT = new CompoundNBT();
//            manaNBT.putInt("mana", this.getValue());
//            manaNBT.putInt("maxMana", this.getMaxValue());

            return manaNBT;
        }
    }
}
