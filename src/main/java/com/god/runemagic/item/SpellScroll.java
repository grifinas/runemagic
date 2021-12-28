package com.god.runemagic.item;

import com.god.runemagic.RuneMagicMod;
import com.god.runemagic.RunemagicModElements;
import com.god.runemagic.common.ManaMapSupplier;
import com.god.runemagic.common.entities.Mana;
import com.god.runemagic.common.messages.ManaUpdate;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.entity.projectile.FireballEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Rarity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.PacketDistributor;
import net.minecraftforge.registries.ObjectHolder;

@RunemagicModElements.ModElement.Tag
public class SpellScroll extends RunemagicModElements.ModElement {
	@ObjectHolder("runemagic:spell_scroll")
	public static final Item block = null;

	public SpellScroll(RunemagicModElements instance) {
		super(instance, 4);
	}

	@Override
	public void initElements() {
		elements.items.add(() -> new ItemCustom());
	}

	public static class ItemCustom extends Item {
		public ItemCustom() {
			super(new Properties().tab(ItemGroup.TAB_MISC).stacksTo(1).rarity(Rarity.RARE));
			setRegistryName("spell_scroll");
		}

		@Override
		public ItemStack getContainerItem(ItemStack itemstack) {
			return new ItemStack(this);
		}

		public ActionResult<ItemStack> use(World world, PlayerEntity playerEntity, Hand hand) {
			Mana mana = ManaMapSupplier.getStatic().getPlayerMana(playerEntity);
			ItemStack itemstack = playerEntity.getItemInHand(hand);

			if (mana.getValue() < 100) {
				return ActionResult.fail(itemstack);
			}

			world.playSound(null, playerEntity.getX(), playerEntity.getY(), playerEntity.getZ(), SoundEvents.GHAST_SHOOT, SoundCategory.NEUTRAL, 0.5F, 0.4F / (random.nextFloat() * 0.4F + 0.8F));
			if (!world.isClientSide) {
				ServerPlayerEntity serverPlayer = world.getServer().getPlayerList().getPlayer(playerEntity.getUUID());
				mana.decrement(100);
				RuneMagicMod.PACKET_HANDLER.send(PacketDistributor.PLAYER.with(() -> serverPlayer), new ManaUpdate(mana.getValue(), mana.getMaxValue()));


				Vector3d lookAngle = playerEntity.getLookAngle();

				FireballEntity fireballentity = new FireballEntity(world, playerEntity, lookAngle.x, lookAngle.y, lookAngle.z);
				fireballentity.explosionPower = 1;
				fireballentity.setPos(playerEntity.getX(), playerEntity.getY(0.5D), fireballentity.getZ());
				fireballentity.setDeltaMovement(lookAngle.scale(2D));
				world.addFreshEntity(fireballentity);
			}

			return ActionResult.sidedSuccess(itemstack, world.isClientSide());
		}
	}
}
