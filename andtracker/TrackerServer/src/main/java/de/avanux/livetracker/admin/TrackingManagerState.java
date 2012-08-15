package de.avanux.livetracker.admin;

public class TrackingManagerState {

    private int registeredTrackings;
    private int activeTrackings;
    private int activeTrackers;
    
    public TrackingManagerState(int registeredTrackings, int activeTrackings, int activeTrackers) {
        this.registeredTrackings = registeredTrackings;
        this.activeTrackings = activeTrackings;
        this.activeTrackers = activeTrackers;
    }

    public int getRegisteredTrackings() {
        return registeredTrackings;
    }

    public int getActiveTrackings() {
        return activeTrackings;
    }

    public int getActiveTrackers() {
        return activeTrackers;
    }
}
