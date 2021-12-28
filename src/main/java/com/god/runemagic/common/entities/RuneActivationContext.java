package com.god.runemagic.common.entities;

import com.god.runemagic.block.runes.AbstractRune;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public class RuneActivationContext {
    private final World world;
    private final BlockState state;
    private final BlockPos position;
    private final PlayerEntity player;
    private HashSet<BlockPos> activatedRunePositions;
    private final Map<Class<?>, Integer> runeCounts = new HashMap<>();

    public RuneActivationContext(World world, BlockState state, BlockPos position, PlayerEntity player) {
        this.world = world;
        this.state = state;
        this.position = position;
        this.player = player;
    }

    public Integer count(AbstractRune key) {
        return this.runeCounts.getOrDefault(key.getClass(), 0);
    }

    public void activateRune(AbstractRune rune) {
        Integer count = this.runeCounts.getOrDefault(rune.getClass(), 0);
        this.runeCounts.put(rune.getClass(), count + 1);
    }

    public HashSet<BlockPos> getActivatedRunePositions() {
        return activatedRunePositions;
    }

    public void setActivatedRunePositions(HashSet<BlockPos> activatedRunePositions) {
        this.activatedRunePositions = activatedRunePositions;
    }

    public World getWorld() {
        return world;
    }

    public BlockState getState() {
        return state;
    }

    public BlockPos getPosition() {
        return position;
    }

    public PlayerEntity getPlayer() {
        return player;
    }
}
