package com.god.runemagic.common.messages;

import net.minecraft.network.PacketBuffer;

import java.util.function.BiConsumer;

public class ManaUpdate implements BiConsumer<ManaUpdate, PacketBuffer> {

    @Override
    public void accept(ManaUpdate mana, PacketBuffer packetBuffer) {

    }

    public ManaUpdate decode(PacketBuffer packetBuffer) {
        return new ManaUpdate();
    }

    @Override
    public BiConsumer<ManaUpdate, PacketBuffer> andThen(BiConsumer<? super ManaUpdate, ? super PacketBuffer> after) {
        return BiConsumer.super.andThen(after);
    }
}
