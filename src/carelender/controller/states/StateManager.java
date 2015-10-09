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

    public AppState getPrevState() {
        return prevState;
    }

    public AppState getAppState() {
        return appState;
    }
}
