package com.god.runemagic.common.spells;

import com.god.runemagic.RunemagicModElements;
import com.god.runemagic.block.runes.ConjurationRuneBlock;
import com.god.runemagic.block.runes.DestructionRuneBlock;
import com.god.runemagic.common.entities.RuneCraftingResult;
import com.god.runemagic.common.entities.SpellCastingContext;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.math.BlockPos;

@RunemagicModElements.ModElement.Tag
public class TeleportationSpell extends AbstractSpell {
    private static final int DISTANCE_PER_RUNE = 25;
    public static String NAME = "teleportation_spell";

    public TeleportationSpell(RunemagicModElements instance) {
        super(instance, SpellType.TELEPORTATION);
    }

    @Override
    public String getRegistryName() {
        return TeleportationSpell.NAME;
    }

    @Override
    public CompoundNBT initialize(RuneCraftingResult craftingResult) {
        CompoundNBT nbt = super.initialize(craftingResult);

        //TODO runes maybe don't make sense for what this does
        nbt.putInt("distance", craftingResult.count(ConjurationRuneBlock.block) * DISTANCE_PER_RUNE);
        nbt.putInt("itemCount", craftingResult.count(DestructionRuneBlock.block));
//        nbt.putInt("cost", (int) Math.floor(100 - 8 * craftingResult.count(AlterationRuneBlock.block)));
        BlockPos position = craftingResult.getContext().getPosition();
        nbt.putIntArray("location", new int[]{position.getX(), position.getY(), position.getZ()});

        return nbt;
    }

    @Override
    public AbstractSpellInstance getInstance(CompoundNBT nbt) {
        return new Instance(this, nbt.getInt("itemCount"), nbt.getInt("distance"), nbt.getIntArray("location"));
    }

    @Override
    public ActionResultType spellBehaviour(SpellCastingContext context) {
        return ActionResultType.FAIL;
    }

    public static class Instance extends AbstractSpellInstance {
        private final int itemCount;
        private final int distance;
        private final int[] location;

        public Instance(AbstractSpell spell, int itemCount, int distance, int[] location) {
            super(spell);

            this.itemCount = itemCount;
            this.distance = distance;
            this.location = location;
        }

        public int getItemCount() {
            return itemCount;
        }

        public int getDistance() {
            return distance;
        }

        public BlockPos getLocation() {
            return new BlockPos(location[0], location[1], location[2]);
        }
    }
}
