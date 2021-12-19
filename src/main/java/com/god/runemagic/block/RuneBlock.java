package com.god.runemagic.block;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Stack;

import com.god.runemagic.RuneMagicMod;
import com.god.runemagic.RunemagicModElements;
import com.god.runemagic.item.AlchemistsGloves;
import com.god.runemagic.item.ChalkItem;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;
import net.minecraftforge.registries.ObjectHolder;

@RunemagicModElements.ModElement.Tag
public class RuneBlock extends RunemagicModElements.ModElement {
	@ObjectHolder("runemagic:rune")
	public static final Block block = null;

	public RuneBlock(RunemagicModElements instance) {
		super(instance, 1);
	}

	@Override
	public void initElements() {
		elements.blocks.add(() -> new CustomBlock());
		elements.items.add(() -> new ChalkItem.ItemCustom(block));
	}

	public static class CustomBlock extends Block {
		protected static final VoxelShape SHAPE = Block.box(0.0D, 0.0D, 0.0D, 16.0D, 1.0D, 16.0D);
		static final BooleanProperty activated = BooleanProperty.create("activated");

		public CustomBlock() {
			super(AbstractBlock.Properties.of(Material.STONE, MaterialColor.CLAY).strength(0.1f).sound(SoundType.SAND)
					.noCollission().lightLevel((state) -> {
						boolean isActivated = state.getValue(activated);
						RuneMagicMod.LOGGER.info("running lightlevel {}", state);
						return isActivated ? 12 : 1;
					}));
			setRegistryName("rune");
			this.registerDefaultState(this.stateDefinition.any().setValue(activated, false));
		}

		@Override
		protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> builder) {
			builder.add(activated);
		}

		public void activate(World world, BlockState state, BlockPos position) {
			// TODO this is not strictly how it should work
			RuneMagicMod.LOGGER.info("activating original pos: {}", position);
			
			this.activateNeightbours(world, position);
			world.destroyBlock(position, false);
		}

//		public ActionResultType use(BlockState blockState, World world, BlockPos blockPosition, PlayerEntity player) {
//			if (world.isClientSide() || !player.isHolding(AlchemistsGloves.block)) {
//				return ActionResultType.FAIL;
//			}
//
//			// TODO something perhaps to identify that this is the "main block"
//			this.activate(world, blockState, blockPosition);
//
//			return ActionResultType.SUCCESS;
//		}

		private void activateNeightbours(World world, BlockPos blockPosition) {
			Stack<BlockPos> toActivate = new Stack<BlockPos>();
			toActivate.addAll(this.getNeighbours(blockPosition));
			
			int minX = blockPosition.getX();
			int maxX = blockPosition.getX();
			int minZ = blockPosition.getZ();
			int maxZ = blockPosition.getZ();
			
			while (!toActivate.isEmpty()) {
				BlockPos position = toActivate.pop();
				
				minX = Math.min(minX, position.getX());
				maxX = Math.max(maxX, position.getX());
				
				minZ = Math.min(minZ, position.getZ());
				maxZ = Math.max(maxZ, position.getZ());
				
				RuneMagicMod.LOGGER.info("activating pos: {}", position);
				if (isActivatable(world, position)) {
					//TODO need to check if we consume items on top
					world.destroyBlock(position, false);
					toActivate.addAll(this.getNeighbours(position));
				}
			}
			
			//TODO this is not correct, bounding box might take some irrelevant items
			List<ItemEntity> items = world.getEntitiesOfClass(ItemEntity.class, new AxisAlignedBB(minX, blockPosition.getY(), minZ, maxX, blockPosition.getY() + 1, maxZ));
			RuneMagicMod.LOGGER.info("found items: {}", items);
		}

		private boolean isActivatable(World world, BlockPos blockPosition) {
			BlockState state = world.getBlockState(blockPosition);
			return state.getBlock().is(this);
		}

		private Collection<BlockPos> getNeighbours(BlockPos blockPosition) {
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
}
