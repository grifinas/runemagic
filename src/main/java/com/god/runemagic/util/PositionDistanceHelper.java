package com.god.runemagic.util;

import net.minecraft.util.math.BlockPos;

public class PositionDistanceHelper {
    public static double distance(BlockPos start, BlockPos end) {
        return Math.sqrt(
                Math.pow(end.getX() - start.getX(), 2) +
                Math.pow(end.getY() - start.getY(), 2) +
                Math.pow(end.getZ() - start.getZ(), 2)
        );
    }
}
