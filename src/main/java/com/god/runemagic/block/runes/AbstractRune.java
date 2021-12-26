package com.god.runemagic.block.runes;

import com.god.runemagic.RuneMagicMod;
import com.god.runemagic.common.RuneActivationContext;
import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.util.Direction;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;

import java.util.*;

public abstract class AbstractRune extends Block {
    protected static final VoxelShape SHAPE = Block.box(0.0D, 0.0D, 0.0D, 16.0D, 1.0D, 16.0D);
    static final BooleanProperty activated = BooleanProperty.create("activated");
    protected boolean isChaining = true;

    public AbstractRune() {
        super(AbstractBlock.Properties.of(Material.STONE, MaterialColor.CLAY).strength(0.1f).sound(SoundType.SAND)
                .noCollission().lightLevel((state) -> {
                    boolean isActivated = state.getValue(activated);
                    return isActivated ? 12 : 1;
                }));
        this.registerDefaultState(this.stateDefinition.any().setValue(activated, false));
    }

    @Override
    protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> builder) {
        builder.add(activated);
    }

    protected boolean runeBehaviour(RuneActivationContext context) {
        return true;
    }

    protected void afterSuccessfulActivation(RuneActivationContext context) {
        World world = context.getWorld();
        // Remove original block
        world.removeBlock(context.getPosition(), false);
        // Remove all activated blocks
        for (BlockPos activated : context.getActivatedRunePositions()) {
            world.removeBlock(activated, false);
        }
    }

    /**
     * When rune is primary activation subject
     */
    public void activate(World world, BlockState state, BlockPos position, PlayerEntity player) {
        RuneMagicMod.LOGGER.info("activating original pos: {}", position);
        RuneActivationContext context = new RuneActivationContext(world, state, position, player);

        if (isChaining) {
            this.activateNeighbours(context);
        }
        if (this.runeBehaviour(context)) {
            this.afterSuccessfulActivation(context);
        }
    }

    /**
     * When rune is chain activated by one of it's neighbours
     */
    public void chainActivate(RuneActivationContext context) {
    }

    public void changeType(World world, BlockPos position) {
        world.removeBlock(position, false);
        BlockState newState = this.getChangeState();
        world.destroyBlock(position, false);
        world.setBlock(position, newState, 0);
    }

    protected abstract BlockState getChangeState();

    protected void activateNeighbours(RuneActivationContext context) {
        Stack<BlockPos> toActivate = new Stack<BlockPos>();
        BlockPos blockPosition = context.getPosition();
        World world = context.getWorld();
        HashSet<BlockPos> activated = new HashSet<>();
        activated.add(blockPosition);
        context.activateRune(this);

        toActivate.addAll(this.getNeighbours(blockPosition));

        while (!toActivate.isEmpty()) {
            BlockPos position = toActivate.pop();
            if (activated.contains(position)) {
                continue;
            }

            if (world.getBlockState(position).getBlock().is(this)) {
                // TODO maybe use tags or something to not always get as chainActivate is nothing by default
                AbstractRune rune = ((AbstractRune) world.getBlockState(position).getBlock());
                rune.chainActivate(context);
                context.activateRune(rune);
                activated.add(position);
                toActivate.addAll(this.getNeighbours(position));
            }
        }

        context.setActivatedRunePositions(activated);
    }

    protected List<ItemEntity> getSacrificedItems(RuneActivationContext context) {
        BlockPos blockPosition = context.getPosition();

        int minX = blockPosition.getX();
        int maxX = blockPosition.getX();
        int minZ = blockPosition.getZ();
        int maxZ = blockPosition.getZ();

        // TODO this is not correct, bounding box might take some irrelevant items
        for (BlockPos position : context.getActivatedRunePositions()) {
            minX = Math.min(minX, position.getX());
            maxX = Math.max(maxX, position.getX());

            minZ = Math.min(minZ, position.getZ());
            maxZ = Math.max(maxZ, position.getZ());
        }

        AxisAlignedBB bb = new AxisAlignedBB(minX, blockPosition.getY(), minZ, maxX + 0.99, blockPosition.getY() + 1, maxZ + 0.99);

        List<ItemEntity> items = context.getWorld().getEntitiesOfClass(ItemEntity.class,
                bb);
        RuneMagicMod.LOGGER.info("found items: {}, bb {}", items, bb);
        return items;
    }

    protected Collection<BlockPos> getNeighbours(BlockPos blockPosition) {
        return Arrays.asList(blockPosition.north(), blockPosition.south(), blockPosition.east(), blockPosition.west());
    }

    /// ESOTERIC ZONE--------------------------

    public VoxelShape getShape(BlockState p_220053_1_, IBlockReader p_220053_2_, BlockPos p_220053_3_,
                               ISelectionContext p_220053_4_) {
        return SHAPE;
    }

    public BlockState updateShape(BlockState p_196271_1_, Direction p_196271_2_, BlockState p_196271_3_,
                                  IWorld p_196271_4_, BlockPos p_196271_5_, BlockPos p_196271_6_) {
        return !p_196271_1_.canSurvive(p_196271_4_, p_196271_5_) ? Blocks.AIR.defaultBlockState()
                : super.updateShape(p_196271_1_, p_196271_2_, p_196271_3_, p_196271_4_, p_196271_5_, p_196271_6_);
    }

    public boolean canSurvive(BlockState state, IWorldReader world, BlockPos pos) {
        return !world.isEmptyBlock(pos.below());
    }
}
