package inowen.skybot.utils;

/**
 * Base class for the initialization trackers for all the farms.
 * (all farms have at least a physical space).
 */
public class InitTracker {

    public boolean foundFarmConstraints = false;

    public boolean isCompletelyInit() {
        return (foundFarmConstraints);
    }
}
