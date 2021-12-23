package com.god.runemagic.block;

import java.util.Random;

import com.god.runemagic.RunemagicModElements;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.WorldGenRegistries;
import net.minecraft.world.ISeedReader;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.OreFeature;
import net.minecraft.world.gen.feature.OreFeatureConfig;
import net.minecraftforge.common.ToolType;
import net.minecraftforge.event.world.BiomeLoadingEvent;
import net.minecraftforge.registries.ObjectHolder;

@RunemagicModElements.ModElement.Tag
public class LimestoneBlock extends RunemagicModElements.ModElement {
	@ObjectHolder("runemagic:limestone")
	public static final Block block = null;

	public LimestoneBlock(RunemagicModElements instance) {
		super(instance, 1);
	}

	@Override
	public void initElements() {
		elements.blocks.add(CustomBlock::new);
		// TODO Sometimes fails??
		elements.items.add(() -> new BlockItem(block,
				new Item.Properties().addToolType(ToolType.PICKAXE, 2).tab(ItemGroup.TAB_BUILDING_BLOCKS))
						.setRegistryName(block.getRegistryName()));
	}

	public static class CustomBlock extends Block {

		public CustomBlock() {
			super(AbstractBlock.Properties.of(Material.STONE, MaterialColor.STONE).strength(1.5f)
					.sound(SoundType.STONE));
			setRegistryName("limestone");
		}
	}

	public static void addFeatureToBiomes(BiomeLoadingEvent event) {
		Feature<OreFeatureConfig> feature = new OreFeature(OreFeatureConfig.CODEC) {
			public boolean place(ISeedReader world, ChunkGenerator generator, Random rand, BlockPos pos,
					OreFeatureConfig config) {
				return super.place(world, generator, rand, pos, config);
			}
		};
		ConfiguredFeature<?, ?> configuredFeature = feature.configured(
				new OreFeatureConfig(OreFeatureConfig.FillerBlockType.NATURAL_STONE, block.defaultBlockState(), 10))
				.range(128).squared().count(10);

		Registry.register(WorldGenRegistries.CONFIGURED_FEATURE, block.getRegistryName(), configuredFeature);

		event.getGeneration().getFeatures(GenerationStage.Decoration.UNDERGROUND_ORES).add(() -> configuredFeature);
	}
}
