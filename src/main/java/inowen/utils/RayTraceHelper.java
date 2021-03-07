package inowen.utils;

import net.minecraft.client.Minecraft;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.RayTraceContext;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;

/**
 * Utility related to rayTracing.
 *
 * @author PrinceChaos
 */
public class RayTraceHelper {

    protected static Minecraft mc = Minecraft.getInstance();

    /**
     * Use RayTracing to get the first block that is on the straight line drawn from
     * the head of the player in the direction of the @p direction vector (which is normalized first).
     * This result can later be cast to BlockRayTraceResult, from which you can get the position of the block hit
     * ( for example ).
     * -------
     * @param direction Which direction it should trace in.
     * @param maxDistance How far it should look for a block before accepting whatever is there (like air).
     * @param acceptFluids Whether it should count water/lava as blocks.
     * @return The RayTraceResult (can cast to BlockRayTraceResult, most sensible use).
     */
    public static RayTraceResult firstSeenCubeInDirection(Vec3d direction, double maxDistance, boolean acceptFluids) {
        direction = direction.normalize();
        Vec3d playerHeadPos = mc.player.getEyePosition(0.0F);
        Vec3d endRayPos = playerHeadPos.add(direction.x*maxDistance, direction.y*maxDistance, direction.z*maxDistance);
        RayTraceResult result = mc.world.rayTraceBlocks(
                new RayTraceContext(playerHeadPos, endRayPos, RayTraceContext.BlockMode.OUTLINE, RayTraceContext.FluidMode.NONE, mc.player)
        );
        return result;
    }


    /**
     * Get the first non-fluid block on the straight line between the head of the player
     * and some point in the direction.
     * @param direction
     * @param maxDistance
     * @return
     */
    public static BlockRayTraceResult firstSeenBlockInDirection(Vec3d direction, double maxDistance) {
        return ((BlockRayTraceResult) firstSeenCubeInDirection(direction, maxDistance, false));

    }
}
