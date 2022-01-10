package com.god.runemagic.item;

import com.god.runemagic.RuneMagicMod;
import com.god.runemagic.RunemagicModElements;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.*;
import net.minecraft.particles.RedstoneParticleData;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ObjectHolder;

@RunemagicModElements.ModElement.Tag
public class PhilosophersStone extends RunemagicModElements.ModElement {
    private static final int DURABILITY_FOR_MANA = 5000;

    @ObjectHolder("runemagic:philosophers_stone")
    public static final Item block = null;

    public PhilosophersStone(RunemagicModElements instance) {
        super(instance, 4);
    }

    @Override
    public void initElements() {
        elements.items.add(ItemCustom::new);
    }

    @Mod.EventBusSubscriber(modid = RuneMagicMod.MOD_ID)
    public static class ItemCustom extends Item {
        public ItemCustom() {
            super(new Properties().tab(ItemGroup.TAB_MISC).durability(DURABILITY_FOR_MANA).rarity(Rarity.EPIC).setNoRepair());
            setRegistryName("philosophers_stone");
        }

        @Override
        public ItemStack getContainerItem(ItemStack itemstack) {
            return new ItemStack(this);
        }

        @Override
        public void onUsingTick(ItemStack stack, LivingEntity player, int count) {
            if (player.level.isClientSide) {
                Vector3d pos = player.getPosition(1).add(0, 1.5, 0).subtract(player.getLookAngle().scale(15D).multiply(-1, -1, -1));
                player.level.addParticle(RedstoneParticleData.REDSTONE, pos.x, pos.y, pos.z, 0D, 0D, 0D);
                player.level.addParticle(RedstoneParticleData.REDSTONE, pos.x + 0.5, pos.y + 0.50, pos.z + 0.5, 0D, 0D, 0D);
                player.level.addParticle(RedstoneParticleData.REDSTONE, pos.x + 0.5, pos.y - 0.50, pos.z + 0.5, 0D, 0D, 0D);
                player.level.addParticle(RedstoneParticleData.REDSTONE, pos.x - 0.5, pos.y + 0.50, pos.z - 0.5, 0D, 0D, 0D);
                player.level.addParticle(RedstoneParticleData.REDSTONE, pos.x - 0.5, pos.y - 0.50, pos.z - 0.5, 0D, 0D, 0D);
            }
        }

        @Override
        public UseAction getUseAnimation(ItemStack p_77661_1_) {
            return UseAction.NONE;
        }

        @Override
        public void releaseUsing(ItemStack p_77615_1_, World p_77615_2_, LivingEntity p_77615_3_, int p_77615_4_) {
            RuneMagicMod.LOGGER.info("release using stone");
            super.releaseUsing(p_77615_1_, p_77615_2_, p_77615_3_, p_77615_4_);
        }

        @Override
        public int getUseDuration(ItemStack p_77626_1_) {
            return 72000;
        }

        @Override
        public ActionResult<ItemStack> use(World p_77659_1_, PlayerEntity player, Hand hand) {
            player.startUsingItem(hand);
            return ActionResult.consume(player.getItemInHand(hand));
        }
    }
}
