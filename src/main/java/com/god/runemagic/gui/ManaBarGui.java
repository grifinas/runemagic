package com.god.runemagic.gui;

import com.god.runemagic.RuneMagicMod;
import com.god.runemagic.common.Mana;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class ManaBarGui extends AbstractGui {
    public static int foo = 0;
    private static final int MAX_BUBBLES = 10;
    private static final int TEXTURE_SIZE = 9;
    private static final int SCALE = 1;
    private final ResourceLocation GUI = new ResourceLocation(RuneMagicMod.MOD_ID, "textures/gui/mana_bar.png");
    private final Minecraft minecraft;
    private int emptyBars = 0;

    public ManaBarGui(Minecraft minecraft) {
        this.minecraft = minecraft;
    }

    public void setMana(Mana mana) {
        this.emptyBars = MAX_BUBBLES - mana.getValue() / (mana.getMaxValue() / 10);
        RuneMagicMod.LOGGER.info("got mana {}, calculated empty bars {}", mana, this.emptyBars);
    }

    public void render(MatrixStack stack, float partialTicks) {
        this.minecraft.getTextureManager().bind(GUI);
        int left = this.minecraft.getWindow().getGuiScaledWidth() / 2 + 10;
        int bottom = this.minecraft.getWindow().getGuiScaledHeight() / 2 - 48 + 120;

        this.renderEmpty(stack, left, bottom, this.emptyBars);
        this.renderFull(stack, left, bottom, MAX_BUBBLES - this.emptyBars);
    }

    private void renderEmpty(MatrixStack stack, int left, int bottom, int count) {
        for (int i = 0; i < count; i++) {
            AbstractGui.blit(stack, left + 8 * i, bottom, this.getBlitOffset(), 0, 0, TEXTURE_SIZE / SCALE, TEXTURE_SIZE / SCALE, 256 / SCALE, 256 / SCALE);
        }
    }

    private void renderFull(MatrixStack stack, int left, int bottom, int count) {
        for (int i = MAX_BUBBLES - count; i < MAX_BUBBLES; i++) {
            AbstractGui.blit(stack, left + 8 * i, bottom, this.getBlitOffset(), TEXTURE_SIZE / SCALE, 0, TEXTURE_SIZE / SCALE, TEXTURE_SIZE / SCALE, 256 / SCALE, 256 / SCALE);
        }
    }
}