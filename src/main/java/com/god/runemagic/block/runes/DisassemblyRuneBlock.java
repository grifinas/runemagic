package com.god.runemagic.block.runes;

import com.god.runemagic.RunemagicModElements;
import com.god.runemagic.common.DisassemblyMap;
import com.god.runemagic.common.ManaMap.Mana;
import com.god.runemagic.common.ManaMapSupplier;
import com.god.runemagic.common.entities.RuneActivationContext;
import net.minecraft.block.BlockState;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.registries.ObjectHolder;

@RunemagicModElements.ModElement.Tag
public class DisassemblyRuneBlock extends RunemagicModElements.ModElement {
    @ObjectHolder("runemagic:disassembly_rune")
    public static final AbstractRune block = null;

    public DisassemblyRuneBlock(RunemagicModElements instance) {
        super(instance, 1);
    }

    @Override
    public void initElements() {
        elements.blocks.add(CustomBlock::new);
    }

    @Override
    public void clientLoad(FMLClientSetupEvent event) {
        RenderTypeLookup.setRenderLayer(block, RenderType.cutoutMipped());
        super.clientLoad(event);
    }

    public static class CustomBlock extends AbstractRune {
        public CustomBlock() {
            super();
            setRegistryName("disassembly_rune");
        }

        @Override
        protected BlockState getChangeState() {
            return UpgradeRuneBlock.block.defaultBlockState();
        }

        @Override
        protected boolean runeBehaviour(RuneActivationContext context) {
            DisassemblyMap disassembly = DisassemblyMap.get();
            PlayerEntity player = context.getPlayer();
            Mana mana = ManaMapSupplier.getStatic().getPlayerMana(player);
            int totalGain = 0;

            for (ItemEntity item: context.getSacrificedItems()) {
                totalGain += (int) disassembly.findValue(item);
                item.remove();
            }

            mana.increment(totalGain);

            return true;
        }
    }
}
