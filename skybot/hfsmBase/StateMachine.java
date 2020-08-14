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


    /**
     * Hierarchical report of the state that the machine is in. From bottom to top.
     * @return
     */
    public String getStatePath() {
        String statePath = "";
        State s = currentState;
        while(s != null) {
            System.out.println("While loop. Adding : " + s.getName());
            statePath += s.getName();
            if (s.getCurrentSubstate() != null) {
                statePath += " -- ";
            }
            s = s.getCurrentSubstate();
        }

        //DEBUG
        if (statePath == "") {
            statePath = "No state path";
        }
        //DEBUG_END

        return statePath;
    }
}
