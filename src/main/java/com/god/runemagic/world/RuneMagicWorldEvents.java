package com.god.runemagic.world;

import com.god.runemagic.RuneMagicMod;
import com.god.runemagic.block.LimestoneBlock;
import com.god.runemagic.common.ManaMapSupplier;

import com.god.runemagic.common.RuneMetaMapSupplier;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
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
	public static void onEntityJoinWorldEvent(EntityJoinWorldEvent event) {
		if (event.getEntity() instanceof PlayerEntity && !event.getWorld().isClientSide) {
			ManaMapSupplier.getStatic().addPlayer((PlayerEntity) event.getEntity());
			RuneMetaMapSupplier.getStatic().addPlayer((PlayerEntity) event.getEntity());
		} 
	}

	@SubscribeEvent
	@OnlyIn(Dist.CLIENT)
	public static void onRenderGameOverlayEvent(RenderGameOverlayEvent.Post event) {
		if (event.getType() == RenderGameOverlayEvent.ElementType.TEXT && !event.isCanceled()) {
			RuneMagicMod.manaBar.render(event.getMatrixStack(), event.getPartialTicks());
		}
	}
}
