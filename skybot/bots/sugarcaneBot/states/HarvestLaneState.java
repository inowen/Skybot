package inowen.skybot.bots.sugarcaneBot.states;

import inowen.skybot.bots.sugarcaneBot.context.SugarcaneFarm;
import inowen.skybot.hfsmBase.State;

public class HarvestLaneState extends State {

    public SugarcaneFarm theFarm;

    public static final double ANGLE_LENIENCY = 2.5;

    // State variables
    boolean strafingXMinus = true;

    public HarvestLaneState(SugarcaneFarm farm) {
        this.theFarm = farm;
        this.name = "HarvestLaneState";
    }

    @Override
    public void onEnter() {
        mc.player.rotationYaw = 0;
        mc.player.rotationPitch = 0;
    }

    @Override
    public void run() {
        // Look at 0,0
        if (Math.abs(mc.player.rotationPitch) > ANGLE_LENIENCY) {
            mc.player.rotationPitch = 0;
        }
        if (Math.abs(mc.player.rotationYaw) > ANGLE_LENIENCY) {
            mc.player.rotationYaw = 0;
        }

        // 

    }

    @Override
    public State getNextState() {
        return null;
    }

    @Override
    public void onExit() {

    }
}
