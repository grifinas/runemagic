package com.god.runemagic.common.messages;

import com.god.runemagic.RuneMagicMod;
import net.minecraft.network.PacketBuffer;

public class ManaUpdate {
    private final int mana;
    private final int maxMana;

    public ManaUpdate(int mana, int maxMana) {
        this.mana = mana;
        this.maxMana = maxMana;
    }

    public static ManaUpdate decode(PacketBuffer buffer) {
        int mana = buffer.readVarInt();
        int maxMana = buffer.readVarInt();
        RuneMagicMod.LOGGER.info("running decode, {}/{}", mana, maxMana);
        return new ManaUpdate(mana, maxMana);
    }

    public static void encode(ManaUpdate manaUpdate, PacketBuffer buffer) {
        buffer.writeVarInt(manaUpdate.getMana());
        buffer.writeVarInt(manaUpdate.getMaxMana());
    }

    public int getMana() {
        return mana;
    }

    public int getMaxMana() {
        return maxMana;
    }
}
