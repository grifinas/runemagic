package com.god.runemagic.common.spells;

import com.god.runemagic.common.ManaMapSupplier;
import com.god.runemagic.common.entities.Mana;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.FireballEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;

import java.util.Random;

public class FireBallSpell extends AbstractSpell {
    private final Random random = new Random();

    public FireBallSpell(int cost) {
        super(cost, SpellType.FIREBALL);
    }

    @Override
    public ActionResultType spellBehaviour(World world, PlayerEntity playerEntity, ItemStack stack) {
        Mana mana = ManaMapSupplier.getStatic().getPlayerMana(playerEntity);

        if (mana.getValue() < this.cost) {
            return ActionResultType.FAIL;
        }

        world.playSound(null, playerEntity.getX(), playerEntity.getY(), playerEntity.getZ(), SoundEvents.GHAST_SHOOT, SoundCategory.NEUTRAL, 0.5F, 0.4F / (random.nextFloat() * 0.4F + 0.8F));
        if (!world.isClientSide) {
            mana.decrement(this.cost);

            Vector3d lookAngle = playerEntity.getLookAngle();

            FireballEntity fireballentity = new FireballEntity(world, playerEntity, lookAngle.x, lookAngle.y, lookAngle.z);
            fireballentity.explosionPower = 1;
            fireballentity.setPos(playerEntity.getX(), playerEntity.getY(0.5D), fireballentity.getZ());
            fireballentity.setDeltaMovement(lookAngle.scale(2D));
            world.addFreshEntity(fireballentity);
        }

        return ActionResultType.SUCCESS;
    }

    @Override
    public AbstractSpell fromNBT(CompoundNBT nbt) {
        return new FireBallSpell(nbt.getInt("cost"));
    }
}
