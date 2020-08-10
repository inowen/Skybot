package inowen.skybot.hfsmBase;

public abstract class StateMachine {

    public State currentState = null;

    /**
     * When the state machine turned on.
     */
    public abstract void start();

    /**
     * For each ingame tick.
     */
    public abstract void onTick();

    /**
     * When the state machine is turned off.
     */
    public abstract void onShutdown();
}
