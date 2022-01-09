package com.god.runemagic.block.runes;

import com.god.runemagic.RunemagicModElements;
import com.god.runemagic.item.chalk.AbstractChalk;
import net.minecraft.block.BlockState;
import net.minecraftforge.registries.ObjectHolder;

@RunemagicModElements.ModElement.Tag
public class AlterationRuneBlock extends RunemagicModElements.ModElement {
    @ObjectHolder("runemagic:alteration_rune")
    public static final AbstractRune block = null;

    public AlterationRuneBlock(RunemagicModElements instance) {
        super(instance, 1);
    }

    @Override
    public void initElements() {
        elements.blocks.add(CustomBlock::new);
        elements.items.add(() -> new AbstractChalk(block));
    }

    public static class CustomBlock extends AbstractRune {
        public CustomBlock() {
            super();
            setRegistryName("alteration_rune");
        }

        @Override
        protected BlockState getChangeState() {
            return null;
        }
    }
}
