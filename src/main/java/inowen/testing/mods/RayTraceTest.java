package inowen.testing.mods;

import inowen.moduleSystem.Module;
import inowen.utils.ForgeKeys;
import inowen.utils.RayTraceHelper;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;

public class RayTraceTest extends Module {

    protected RayTraceResult rayTraceBlock;
    protected RayTraceResult rayTraceFluid;

    public RayTraceTest() { super("RayTraceTest", ForgeKeys.KEY_NONE); }

    @Override
    public void onEnable() {

    }

    @Override
    public void onGui() {
        /*
        // Basic pick method with where the player is looking at
        Entity entity = mc.getRenderViewEntity();
        rayTraceBlock = entity.pick(20D, 0.0F, false);
        rayTraceFluid = entity.pick(20D, 0.0F, true);

        mc.fontRenderer.drawString("rayTraceBlock: " + ((BlockRayTraceResult)rayTraceBlock).getPos(), 100, 1, 0xffffff);
        */


        // RayTracing in a direction that isn't where the player is looking.
        BlockRayTraceResult result = RayTraceHelper.firstSeenBlockInDirection(new Vec3d(0,1,1), 20);
        mc.fontRenderer.drawString("RayTraceBlock: " + result.getPos(), 50, 1, 0xffffff);
    }




}
