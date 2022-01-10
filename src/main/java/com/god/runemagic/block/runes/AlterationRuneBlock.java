package com.god.runemagic.block.runes;

import com.god.runemagic.RunemagicModElements;
import com.god.runemagic.item.chalk.AbstractChalk;
import net.minecraft.block.BlockState;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
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

    @Override
    public void clientLoad(FMLClientSetupEvent event) {
        RenderTypeLookup.setRenderLayer(block, RenderType.cutoutMipped());
        super.clientLoad(event);
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
