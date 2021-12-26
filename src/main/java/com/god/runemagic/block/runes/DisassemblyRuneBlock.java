package com.god.runemagic.block.runes;

import com.god.runemagic.RunemagicModElements;
import com.god.runemagic.common.DisassemblyMap;
import com.god.runemagic.common.ManaMap.Mana;
import com.god.runemagic.common.ManaMapSupplier;
import com.god.runemagic.common.RuneActivationContext;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.registries.ObjectHolder;

import java.util.List;

@RunemagicModElements.ModElement.Tag
public class DisassemblyRuneBlock extends RunemagicModElements.ModElement {
    @ObjectHolder("runemagic:disassembly_rune")
    public static final Block block = null;

    public DisassemblyRuneBlock(RunemagicModElements instance) {
        super(instance, 1);
    }

    @Override
    public void initElements() {
        elements.blocks.add(() -> new CustomBlock());
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

            this.getSacrificedItems(context).forEach(item -> {
                int value = (int) disassembly.findValue(item);
                item.remove();
                mana.setValue(mana.getValue() + value);
            });

            return true;
        }
    }
}
