package com.god.runemagic.common.entities;

import com.god.runemagic.RuneMagicMod;
import com.god.runemagic.block.runes.AbstractRune;
import com.god.runemagic.common.ManaMapSupplier;
import net.minecraft.block.BlockState;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.state.Property;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

public class RuneActivationContext {
    private final World world;
    private final BlockState state;
    private final BlockPos position;
    private final PlayerEntity player;
    private final Map<Class<?>, Integer> runeCounts = new HashMap<>();
    private HashSet<BlockPos> activatedRunePositions;

    public RuneActivationContext(World world, BlockState state, BlockPos position, PlayerEntity player) {
        this.world = world;
        this.state = state;
        this.position = position;
        this.player = player;
    }

    public Integer count(AbstractRune key) {
        return this.runeCounts.getOrDefault(key.getClass(), 0);
    }

    public Integer count(AbstractRune key, int max) {
        return Math.min(this.count(key), max);
    }

    public AbstractRune getPrimary() {
        return (AbstractRune) this.state.getBlock();
    }

    public <T extends Comparable<T>, V extends T> void modifyState(Property<T> property, V value) {
        this.world.setBlock(this.position, this.state.setValue(property, value), 3);
    }

    public void activateRune(AbstractRune rune) {
        Integer count = this.runeCounts.getOrDefault(rune.getClass(), 0);
        this.runeCounts.put(rune.getClass(), count + 1);
    }

    public Mana getMana() {
        return ManaMapSupplier.getStatic().getPlayerMana(this.player);
    }

    public List<ItemEntity> getSacrificedItems() {
        AxisAlignedBB bb = this.getBoundingBox();

        List<ItemEntity> items = this.world.getEntitiesOfClass(ItemEntity.class, bb);
        RuneMagicMod.LOGGER.info("found items: {}, bb {}", items, bb);
        return items;
    }

    public List<LivingEntity> getEntitiesOnCircle() {
        AxisAlignedBB bb = this.getBoundingBox();

        List<LivingEntity> living = this.world.getEntitiesOfClass(LivingEntity.class, bb);
        RuneMagicMod.LOGGER.info("found entities: {}, bb {}", living, bb);
        return living;
    }

    private AxisAlignedBB getBoundingBox() {
        int minX = this.position.getX();
        int maxX = this.position.getX();
        int minZ = this.position.getZ();
        int maxZ = this.position.getZ();

        // TODO this is not correct, bounding box might take some irrelevant items
        for (BlockPos position : this.activatedRunePositions) {
            minX = Math.min(minX, position.getX());
            maxX = Math.max(maxX, position.getX());

            minZ = Math.min(minZ, position.getZ());
            maxZ = Math.max(maxZ, position.getZ());
        }

        return new AxisAlignedBB(minX, this.position.getY(), minZ, maxX + 0.99, this.position.getY() + 1, maxZ + 0.99);
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
