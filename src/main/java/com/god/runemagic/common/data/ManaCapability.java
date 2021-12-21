package com.god.runemagic.common.data;

import java.util.ArrayList;
import java.util.concurrent.Callable;

import com.god.runemagic.RuneMagicMod;

import net.minecraft.nbt.INBT;
import net.minecraft.nbt.IntNBT;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;

public class ManaCapability {
	@CapabilityInject(IManaStorage.class)
	public static Capability<IManaStorage> MANA_CAPABILITY = null;
	public static final ResourceLocation RESOURCE = new ResourceLocation(RuneMagicMod.MOD_ID, "mana_capability");

	public static void register() {
		CapabilityManager.INSTANCE.injectCapabilities(new ArrayList());
		CapabilityManager.INSTANCE.register(IManaStorage.class, new Storage(), new Factory());
	}

	public static class Storage implements Capability.IStorage<IManaStorage> {

		@Override
		public INBT writeNBT(Capability<IManaStorage> capability, IManaStorage instance, Direction side) {
			return IntNBT.valueOf(instance.getMana());
		}

		@Override
		public void readNBT(Capability<IManaStorage> capability, IManaStorage instance, Direction side, INBT nbt) {
			IntNBT qqq = (IntNBT)nbt;
			instance.setMana(qqq.getAsInt());
		}

	}
	
	public static ICapabilityProvider createProvider(final IManaStorage mana) {
		return new SimpleCapabilityProvider(MANA_CAPABILITY, mana);
	}

	private static class Factory implements Callable<IManaStorage> {
		@Override
		public IManaStorage call() throws Exception {
			return new DefaultManaCapability();
		}
	}
}
