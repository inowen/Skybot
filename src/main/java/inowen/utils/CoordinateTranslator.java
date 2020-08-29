package inowen.utils;

import net.minecraft.client.Minecraft;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

/**
 * Makes transformations from chunk coords to block coords,
 * global block coordinates to coordinates inside the scanZone
 * (to adjust pathfinding)...
 *
 *
 * @author PrinceChaos
 *
 */

public class CoordinateTranslator {
    protected static Minecraft mc = Minecraft.getInstance();

    public CoordinateTranslator() {} // Empty, won't create objects.


    /**
     * Turn a BlockPos from the blocks grid into the vector position of the center of said block.
     * If it receives a null value, it returns a null value.
     * @param bp
     * @return
     */
    public static Vec3d blockPosToVectorPosition(BlockPos bp) {

        if (bp == null) { return null; }

        double x = bp.getX() + 0.5;
        double y = bp.getY();
        double z = bp.getZ() + 0.5;

        return new Vec3d(x,y,z);
    }


    /**
     * The head is 1+5/8 = 1.625 above the feet. The vectorPosition for the player will be at its feet, use
     * this to get the head position.
     * @param atFeet
     * @return
     */
    public static Vec3d offsetFromFeetToHeadHeight(Vec3d atFeet) {
        return new Vec3d(atFeet.getX(), atFeet.getY()+1.625D, atFeet.getZ());
    }


    /**
     * Get the BlockPos at which you would be looking at if you looked straight down from above the position.
     * I'm using this mainly to get the actual BlockPos that the player's legs are in.
     * mc.thePlayer.getPosition() is faulty, it returns positions that are close but still wrong.
     * If it receives a null value, it returns a null value.
     * ---------------------
     * @param position
     * @return
     */
    public static BlockPos vec3ToBlockPos(Vec3d position) {

        if (position == null) { return null; }

        // Truncating translates from position vector to real BlockPos
        // (the one block that you are looking at when you look straight down from a bit above this one's y coordinate)
        int bpX = (int)position.getX();
        if (position.getX() < 0) {
            bpX--;
        }
        int bpY = (int)position.getY(); // Can't be negative.
        int bpZ = (int)position.getZ();
        if (position.getZ() < 0) {
            bpZ--;
        }

        return new BlockPos(bpX, bpY, bpZ);
    }

}