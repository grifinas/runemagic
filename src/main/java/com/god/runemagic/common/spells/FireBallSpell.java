package com.god.runemagic.common.spells;

import com.god.runemagic.RuneMagicMod;
import com.god.runemagic.RunemagicModElements;
import com.god.runemagic.block.runes.AlterationRuneBlock;
import com.god.runemagic.block.runes.ConjurationRuneBlock;
import com.god.runemagic.block.runes.DestructionRuneBlock;
import com.god.runemagic.common.entities.Mana;
import com.god.runemagic.common.entities.RuneCraftingResult;
import com.god.runemagic.common.entities.SpellCastingContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.FireballEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;

import java.util.Random;

@RunemagicModElements.ModElement.Tag
public class FireBallSpell extends AbstractSpell {
    public static String NAME = "fire_ball_spell";
    private final Random random = new Random();

    public FireBallSpell(RunemagicModElements instance) {
        super(instance, SpellType.FIREBALL);
    }

    @Override
    public String getRegistryName() {
        return FireBallSpell.NAME;
    }

    @Override
    public CompoundNBT initialize(RuneCraftingResult craftingResult) {
        CompoundNBT nbt = super.initialize(craftingResult);
        nbt.putInt("damage", craftingResult.count(DestructionRuneBlock.block));
        nbt.putInt("count", Math.max(1, craftingResult.count(ConjurationRuneBlock.block) / 3));
        nbt.putInt("cost", (int) Math.floor(100 - 8 * craftingResult.count(AlterationRuneBlock.block)));

        return nbt;
    }

    @Override
    public AbstractSpellInstance getInstance(CompoundNBT nbt) {
        return new Instance(this, nbt.getInt("damage"), nbt.getInt("count"), nbt.getInt("cost"));
    }

    @Override
    public ActionResultType spellBehaviour(SpellCastingContext context) {
        Instance instance = (Instance) context.getSpell();
        Mana mana = context.getMana();
        World world = context.getWorld();
        PlayerEntity playerEntity = context.getPlayerEntity();


        if (mana.getValue() < instance.getCost()) {
            return ActionResultType.FAIL;
        }

        world.playSound(null, playerEntity.getX(), playerEntity.getY(), playerEntity.getZ(), SoundEvents.GHAST_SHOOT, SoundCategory.NEUTRAL, 0.5F, 0.4F / (random.nextFloat() * 0.4F + 0.8F));
        if (!world.isClientSide) {
            mana.decrement(instance.getCost());

            Vector3d lookAngle = playerEntity.getLookAngle();

            FireballEntity fireballentity = new FireballEntity(world, playerEntity, lookAngle.x, lookAngle.y, lookAngle.z);
            fireballentity.explosionPower = 1;
            fireballentity.setPos(playerEntity.getX(), playerEntity.getY(0.5D), fireballentity.getZ());
            fireballentity.setDeltaMovement(lookAngle.scale(2D));
            world.addFreshEntity(fireballentity);
        }

        return ActionResultType.SUCCESS;
    }

    private static class Instance extends AbstractSpellInstance {
        private final int damage;
        private final int count;
        private final int cost;

        public Instance(AbstractSpell spell, int damage, int count, int cost) {
            super(spell);
            this.damage = damage;
            this.count = count;
            this.cost = cost;

            RuneMagicMod.LOGGER.info("created fire ball instance with {}dmg, {}count, {}cost", this.damage, this.count, this.cost);
        }

        public int getDamage() {
            return damage;
        }

        public int getCount() {
            return count;
        }

        public int getCost() {
            return cost;
        }

        @Override
        public String toString() {
            return String.format("D%d x%d, cost: %d", this.damage, this.count, this.cost);
        }
    }
}
