package carelender.controller.states;

import carelender.model.AppSettings;

/**
 * Manages the states of the application
 */
public class StateManager {
    private AppState appState;
    private AppState prevState;

    public StateManager() {
        appState = AppState.FIRSTSTART;
        prevState = null;
    }

    /**
     * Changes the application state
     * @param newState State to change to
     */
    public void changeState ( AppState newState ) {
        if ( appState == newState ) return;
        prevState = appState;
        appState = newState;
        System.out.println("State changed: " + newState.toString());
    }

    public AppState getPrevState() {
        return prevState;
    }

    public AppState getAppState() {
        return appState;
    }

    public boolean isState ( AppState state ) {
        return state == appState;
    }


}
