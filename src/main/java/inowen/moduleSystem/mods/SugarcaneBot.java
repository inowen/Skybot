package inowen.moduleSystem.mods;

import inowen.moduleSystem.Module;
import inowen.skybot.bots.sugarcaneBot.SugarcaneBotHFSM;
import inowen.utils.ForgeKeys;
import inowen.utils.InventoryHelper;
import inowen.utils.PlayerMovementHelper;
import net.minecraft.item.Items;

public class SugarcaneBot extends Module {

    // The state machine for the sugarcane bot
    SugarcaneBotHFSM theStateMachine = null;

    public SugarcaneBot() { super("SugarcaneBot", ForgeKeys.KEY_T); }

    @Override
    public void onEnable() {
        if (mc.player != null && mc.world != null) {
            theStateMachine = new SugarcaneBotHFSM();
            theStateMachine.start();
        }
    }


    @Override
    public void onUpdate() {
        if (mc.player != null && mc.world != null) {
            theStateMachine.onTick();
        }
        // Shut the module off if it's not ingame.
        else if (this.isToggled()) {
            this.toggled = false;
        }
    }


    @Override
    public void onGui() {
        if (mc.player != null && mc.world != null) {
            mc.fontRenderer.drawString("Current state: " + theStateMachine.getStatePath(), 100, 50, 0xffffff);
            mc.fontRenderer.drawString("Items left till sell: " + InventoryHelper.howManyMoreCanStore(Items.SUGAR_CANE), 100, 60, 0xffffff);
        }
    }

    @Override
    public void onDisable() {
        if (mc.world != null && mc.player != null) {
            PlayerMovementHelper.desetAllkeybinds();
        }
    }
}
