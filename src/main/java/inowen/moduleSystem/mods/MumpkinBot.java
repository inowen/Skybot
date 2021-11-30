package inowen.moduleSystem.mods;

import inowen.moduleSystem.Module;
import inowen.skybot.bots.melonPumpkinBot.MumpkinBotHFSM;
import inowen.utils.ForgeKeys;
import inowen.utils.InventoryHelper;
import inowen.utils.RayTraceHelper;
import net.minecraft.block.Block;
import net.minecraft.block.StemGrownBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.event.TickEvent;

public class MumpkinBot extends Module {

    public MumpkinBotHFSM theStateMachine = null;

    public float lastRealYaw = 0;
    public float lastRealPitch = 0;

    public MumpkinBot() {
        super(ForgeKeys.KEY_I);
    }

    @Override
    public void onEnable() {
        theStateMachine = new MumpkinBotHFSM();
        theStateMachine.start();
    }


    @Override
    public void onClientTick(TickEvent.ClientTickEvent event) {
        // Propagate tick to the state machine.
        if (mc.player != null && mc.world != null) {
            theStateMachine.onTick();
        }
        else {
            if (this.isToggled()) {
                this.onDisable();
                this.setToggled(false);
            }
        }

        // Check if Euler Angles somehow became NaN.
        checkForNaNAngles();
        // Don't attack if it isn't a melon or pumpkin
        preventBreakingFarm();
    }

    @Override
    public void onRenderGuiOverlayEvent(RenderGameOverlayEvent.Post event) {
        mc.fontRenderer.drawString("Items left until selling: " + InventoryHelper.howManyMoreCanStore(theStateMachine.farmedItem), 100, 90, 0xffffff);
        mc.fontRenderer.drawString("State: " + theStateMachine.getStatePath(), 100, 100, 0xffffff);
    }

    @Override
    public void onDisable() {
        if (mc.player != null && mc.world != null) {
            theStateMachine.onShutdown();
        }
    }


    /**
     * Check that the Euler Angles are real numbers, if they are save the last valid angle, if not reset
     * to the last real value.
     */
    public void checkForNaNAngles() {
        // Check Yaw
        if (Float.isNaN(mc.player.rotationYaw)) {
            mc.player.rotationYaw = lastRealYaw;
        }
        else {
            lastRealYaw = mc.player.rotationYaw;
        }

        // Check Pitch
        if (Float.isNaN(mc.player.rotationPitch)) {
            mc.player.rotationPitch = lastRealPitch;
        }
        else {
            lastRealPitch = mc.player.rotationPitch;
        }
    }


    /**
     * Use raytracing to determine whether the bot is looking at a block of the farmed item.
     * If it isn't, suppress breaking keypress.
     */
    public void preventBreakingFarm() {
        BlockRayTraceResult lookingAt = RayTraceHelper.firstSeenBlockInDirection(mc.player.getLookVec(), 6);
        BlockPos posLookingAt = lookingAt.getPos();
        Block blockLookingAt = mc.world.getBlockState(posLookingAt).getBlock();

        if (!(blockLookingAt instanceof StemGrownBlock)) {
            mc.gameSettings.keyBindAttack.setPressed(false);
        }
    }

}
