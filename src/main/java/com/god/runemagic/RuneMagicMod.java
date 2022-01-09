package com.god.runemagic;

import java.util.function.Supplier;

import com.god.runemagic.common.ManaMap;
import com.god.runemagic.common.ManaMapSupplier;
import com.god.runemagic.common.RuneMetaMap;
import com.god.runemagic.common.RuneMetaMapSupplier;
import com.god.runemagic.gui.ManaBarGui;
import com.god.runemagic.util.ServerResourceReader;
import net.minecraft.client.Minecraft;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import net.minecraft.block.Block;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.EntityType;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.simple.SimpleChannel;

@Mod("runemagic")
public class RuneMagicMod {
	public static final String MOD_ID = "runemagic";
	public static final Logger LOGGER = LogManager.getLogger(RuneMagicMod.class);
	private static final String PROTOCOL_VERSION = "1";
	public static final SimpleChannel PACKET_HANDLER = NetworkRegistry.newSimpleChannel(
			new ResourceLocation(MOD_ID, "runemagic"), () -> PROTOCOL_VERSION, PROTOCOL_VERSION::equals,
			PROTOCOL_VERSION::equals);
	public RunemagicModElements elements;
	@OnlyIn(Dist.CLIENT)
	public static ManaBarGui manaBar;

	public RuneMagicMod() {
		elements = new RunemagicModElements();
		FMLJavaModLoadingContext.get().getModEventBus().register(this);
		FMLJavaModLoadingContext.get().getModEventBus().addListener(this::init);
		FMLJavaModLoadingContext.get().getModEventBus().addListener(this::clientLoad);
		MinecraftForge.EVENT_BUS.register(new RunemagicModFMLBusEvents(this));
	}

	private void init(FMLCommonSetupEvent event) {
		elements.getElements().forEach(element -> element.init(event));
	}

	public void clientLoad(FMLClientSetupEvent event) {
		elements.getElements().forEach(element -> element.clientLoad(event));
		// TODO make into an element
		manaBar = new ManaBarGui(Minecraft.getInstance());
	}

	@SubscribeEvent
	public void registerBlocks(RegistryEvent.Register<Block> event) {
		event.getRegistry().registerAll(elements.getBlocks().stream().map(Supplier::get).toArray(Block[]::new));
	}

	@SubscribeEvent
	public void registerItems(RegistryEvent.Register<Item> event) {
		event.getRegistry().registerAll(elements.getItems().stream().map(Supplier::get).toArray(Item[]::new));
	}

	@SubscribeEvent
	public void registerEntities(RegistryEvent.Register<EntityType<?>> event) {
		event.getRegistry().registerAll(elements.getEntities().stream().map(Supplier::get).toArray(EntityType[]::new));
	}

	@SubscribeEvent
	public void registerEnchantments(RegistryEvent.Register<Enchantment> event) {
		event.getRegistry()
				.registerAll(elements.getEnchantments().stream().map(Supplier::get).toArray(Enchantment[]::new));
	}

	@SubscribeEvent
	public void registerSounds(RegistryEvent.Register<net.minecraft.util.SoundEvent> event) {
		elements.registerSounds(event);
	}

	private static class RunemagicModFMLBusEvents {
		private final RuneMagicMod parent;

		RunemagicModFMLBusEvents(RuneMagicMod parent) {
			this.parent = parent;
		}

		@SubscribeEvent
		public void serverLoad(FMLServerStartingEvent event) {
			new ServerResourceReader(event);
			event.getServer().overworld().getChunkSource().getDataStorage().computeIfAbsent(new ManaMapSupplier(), ManaMap.NBT_KEY);
			event.getServer().overworld().getChunkSource().getDataStorage().computeIfAbsent(new RuneMetaMapSupplier(), RuneMetaMap.NBT_KEY);
			this.parent.elements.getElements().forEach(element -> element.serverLoad(event));
		}
	}
}
