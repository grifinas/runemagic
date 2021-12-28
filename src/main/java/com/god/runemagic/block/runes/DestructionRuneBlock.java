package com.god.runemagic.block.runes;


import com.god.runemagic.RunemagicModElements;
import com.god.runemagic.common.entities.RuneActivationContext;
import com.god.runemagic.item.SpellScroll;
import com.god.runemagic.item.chalk.AbstractChalk;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.item.ItemStack;
import net.minecraftforge.registries.ObjectHolder;

@RunemagicModElements.ModElement.Tag
public class DestructionRuneBlock extends RunemagicModElements.ModElement {
    @ObjectHolder("runemagic:destruction_rune")
    public static final Block block = null;

    public DestructionRuneBlock(RunemagicModElements instance) {
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
            setRegistryName("destruction_rune");
        }

        @Override
        protected BlockState getChangeState() {
            return null;
        }

        @Override
        protected boolean runeBehaviour(RuneActivationContext context) {
            this.getSacrificedItems(context).forEach(itemEntity -> {
                ItemStack stack = itemEntity.getItem();
                if (stack.getItem().getRegistryName().toString().equals("minecraft:paper")) {
                    int count = stack.getCount();
                    if (count < 3) {
                        return;
                    }
                    stack.setCount(count - 3);

                    ItemStack resultingItemStack = new ItemStack(SpellScroll.block, 1);
                    ItemEntity resultingItemEntity = new ItemEntity(context.getWorld(), itemEntity.xo, itemEntity.yo, itemEntity.zo, resultingItemStack);
                    boolean added = context.getWorld().addFreshEntity(resultingItemEntity);
                }
            });
            return super.runeBehaviour(context);
        }
    }
}
