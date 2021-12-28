package com.god.runemagic.item;

import com.god.runemagic.RuneMagicMod;
import com.god.runemagic.RunemagicModElements;
import com.god.runemagic.block.runes.AbstractRune;
import com.god.runemagic.util.RuneMagicTags;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.*;
import net.minecraft.util.ActionResultType;
import net.minecraft.world.World;
import net.minecraftforge.registries.ObjectHolder;


@RunemagicModElements.ModElement.Tag
public class AlchemistsGloves extends RunemagicModElements.ModElement {
    @ObjectHolder("runemagic:alchemists_gloves")
    public static final Item block = null;

    public AlchemistsGloves(RunemagicModElements instance) {
        super(instance, 4);
    }

    @Override
    public void initElements() {
        elements.items.add(ItemCustom::new);
    }

    public static class ItemCustom extends Item {
        public ItemCustom() {
            super(new Item.Properties().tab(ItemGroup.TAB_MISC).stacksTo(1).rarity(Rarity.RARE));
            setRegistryName("alchemists_gloves");
        }

        @Override
        public ActionResultType onItemUseFirst(ItemStack stack, ItemUseContext context) {
            World world = context.getLevel();

            if (world.isClientSide()) {
                return ActionResultType.PASS;
            }

            PlayerEntity player = context.getPlayer();
            assert player != null;

            BlockState state = world.getBlockState(context.getClickedPos());
            if (state.getBlock().is(RuneMagicTags.Blocks.RUNES)) {
                RuneMagicMod.LOGGER.info("glove used on rune {}", state);
                AbstractRune rune = (AbstractRune) state.getBlock();
                rune.activate(world, state, context.getClickedPos(), player);

                ServerPlayerEntity serverPlayer = world.getServer().getPlayerList().getPlayer(player.getUUID());
                assert serverPlayer != null;

                return ActionResultType.SUCCESS;
            }
            return ActionResultType.FAIL;
        }
    }
}
