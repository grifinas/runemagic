package com.god.runemagic.block.runes;

import com.god.runemagic.RunemagicModElements;
import com.god.runemagic.common.entities.RuneActivationContext;
import com.god.runemagic.common.spells.AbstractSpellInstance;
import com.god.runemagic.common.spells.TeleportationSpell;
import com.god.runemagic.item.chalk.AbstractChalk;
import com.god.runemagic.util.PositionDistanceHelper;
import com.god.runemagic.util.SpellProvider;
import net.minecraft.block.BlockState;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.registries.ObjectHolder;

@RunemagicModElements.ModElement.Tag
public class ConjurationRuneBlock extends RunemagicModElements.ModElement {
    @ObjectHolder("runemagic:conjuration_rune")
    public static final AbstractRune block = null;
    private static final int DISTANCE_PER_CONJURATION_RUNE = 25;

    public ConjurationRuneBlock(RunemagicModElements instance) {
        super(instance, 1);
    }

    @Override
    public void initElements() {
        elements.blocks.add(CustomBlock::new);
        elements.items.add(() -> new AbstractChalk(block));
    }

    @Override
    public void clientLoad(FMLClientSetupEvent event) {
        RenderTypeLookup.setRenderLayer(block, RenderType.cutoutMipped());
        super.clientLoad(event);
    }

    public static class CustomBlock extends AbstractRune {
        public CustomBlock() {
            super();
            setRegistryName("conjuration_rune");
        }

        @Override
        protected BlockState getChangeState() {
            return null;
        }

        @Override
        protected boolean runeBehaviour(RuneActivationContext context) {
            if (this.spellCraftingBehaviour(context)) {
                return true;
            }

            for (ItemEntity item : context.getSacrificedItems()) {
                AbstractSpellInstance spell = SpellProvider.get(item.getItem());
                if (spell != null && spell.is(TeleportationSpell.NAME)) {
                    return this.teleportationBehaviour((TeleportationSpell.Instance) spell, context, item);
                }
            }

            return false;
        }

        private boolean teleportationBehaviour(TeleportationSpell.Instance spell, RuneActivationContext context, ItemEntity item) {
            BlockPos destination = spell.getLocation();
            BlockPos start = context.getPosition();
            double distance = PositionDistanceHelper.distance(start, destination);

            int runeDistance = context.count(ConjurationRuneBlock.block, 9) * DISTANCE_PER_CONJURATION_RUNE;
            int spellDistance = spell.getDistance();

            if (distance > runeDistance + spellDistance) {
                return false;
            }

            item.remove();

            for (LivingEntity entity : context.getEntitiesOnCircle()) {
                entity.teleportTo(destination.getX(), destination.getY(), destination.getZ());
            }

            //TODO mana cost
            //TODO partial item teleportation

            return true;
        }
    }
}
