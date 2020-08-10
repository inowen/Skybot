package inowen.oldCropBot.states;

import java.util.ArrayList;

import net.minecraft.client.Minecraft;

public class State {
    public String name;
    public State currentState;
    public ArrayList<State> subStates;

    protected Minecraft mc = Minecraft.getInstance();

    public State() {
        currentState = null;
        subStates = new ArrayList<State>();
        // Each child state inits its own name and the substates
        // in its own constructor.
    }

    public String getName() { return name; }

    /**
     * Get a state from its name, if the state isn't in the registered list, returns null.
     * (the checkConditions() function returns the name of the next state).
     * @param name
     * @return
     */
    public State getSubstateFromName(String name) {
        State result = null;
        for (int i=0; i<subStates.size() && result==null; i++) {
            if (subStates.get(i).name == name) {
                result = subStates.get(i);
            }
        }
        return result;
    }


    // Every substate overrides this.
    public void onEnter() {}
    public void run() {}
    public String checkConditions() { return ""; }
    public void onExit() {}

}
