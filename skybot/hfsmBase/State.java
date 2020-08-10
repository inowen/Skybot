package inowen.skybot.hfsmBase;

import net.minecraft.client.Minecraft;

/**
 * A state that a State Machine can be in.
 *
 * @author PrinceChaos
 */
public abstract class State {

    protected static Minecraft mc = Minecraft.getInstance();

    public String name = ""; // Set the name in constructor.
    public State currentState = null;

    /**
     * When the state machine enters this state
     */
    public abstract void onEnter();

    /**
     * Is executed every tick. Keeps doing whatever the
     * state machine should be doing in this state.
     */
    public abstract void run();

    /**
     * Based on arbitrary information, figure out
     * the state that the machine should transition to.
     * @return
     */
    public abstract State getNextState();

    /**
     * Final actions when shutting down.
     */
    public abstract void onExit();


    // Getter for name of this state.
    public String getName() {
        return name;
    }

    // Get current substate
    public State getCurrentSubstate() {
        return currentState;
    }
}
