package com.adventurous.game.collisions;

/**
 * Enum class for collision mask bits.
 */
public enum MaskBits {
    GROUND((short)Math.pow(2, 1)),
    PLAYER((short)Math.pow(2, 2)),
    ITEM((short)Math.pow(2, 3)),
    PORTAL((short)Math.pow(2, 4)),
    WALL((short)Math.pow(2, 5)),
    BUTTON((short)Math.pow(2, 6));

    public short bits;

    MaskBits(short bits) {
        this.bits = bits;
    }
}
