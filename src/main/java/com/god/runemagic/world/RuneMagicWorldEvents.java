package com.god.runemagic.world;

import com.god.runemagic.RuneMagicMod;
import com.god.runemagic.block.LimestoneBlock;
import com.god.runemagic.common.ManaMap;
import com.god.runemagic.common.data.DefaultManaCapability;
import com.god.runemagic.common.data.ManaCapability;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.world.BiomeLoadingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = RuneMagicMod.MOD_ID)
public class RuneMagicWorldEvents {
	
	@SubscribeEvent
	public static void biomeLoadingEvent(final BiomeLoadingEvent event) {
		LimestoneBlock.addFeatureToBiomes(event);
	}
	
	@SubscribeEvent
	public static void onAttachCapabilitiesEvent(AttachCapabilitiesEvent<Entity> event) {
		if (event.getObject() instanceof PlayerEntity) {
//			event.addCapability(ManaCapability.RESOURCE, ManaCapability.createProvider(new DefaultManaCapability()));
			ManaMap.get().addPlayer((PlayerEntity) event.getObject());
		} 
	}
}
