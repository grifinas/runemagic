package com.god.runemagic.world;

import com.god.runemagic.RuneMagicMod;
import com.god.runemagic.block.LimestoneBlock;

import net.minecraftforge.event.world.BiomeLoadingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = RuneMagicMod.MOD_ID)
public class RuneMagicWorldEvents {
	
	@SubscribeEvent
	public static void biomeLoadingEvent(final BiomeLoadingEvent event) {
		LimestoneBlock.addFeatureToBiomes(event);
	}
}
