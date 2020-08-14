package inowen.skybot.bots.sugarcaneBot.context;


/**
 * Flags to follow the initialization process of the farm context.
 * Whether the player is on the homerow, whether there are any lanes,
 * whether the farm barriers were found, etc.
 *
 * @author PrinceChaos
 */
public class InitializationTracker {

    public boolean foundFarmConstraints = false;
    public boolean onHomeRow = false;
    public boolean foundAtLeastOneLane = false;


    public boolean isCompletelyInit() {
        return foundFarmConstraints && onHomeRow && foundAtLeastOneLane;
    }

}


