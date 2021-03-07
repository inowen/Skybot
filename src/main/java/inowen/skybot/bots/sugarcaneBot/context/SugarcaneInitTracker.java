package inowen.skybot.bots.sugarcaneBot.context;


import inowen.skybot.utils.InitTracker;

/**
 * Flags to follow the initialization process of the farm context.
 * Whether the player is on the homerow, whether there are any lanes,
 * whether the farm barriers were found, etc.
 *
 * @author PrinceChaos
 */
public class SugarcaneInitTracker extends InitTracker {

    public boolean onHomeRow = false;
    public boolean foundAtLeastOneLane = false;


    @Override
    public boolean isCompletelyInit() {
        return foundFarmConstraints && onHomeRow && foundAtLeastOneLane;
    }

}


