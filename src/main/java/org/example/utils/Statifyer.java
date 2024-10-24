package org.example.utils;

public class Statifyer {
    public STATES showState(int state){
        return switch (state) {
            case 0 -> STATES.DEACTIVATED;
            case 1 -> STATES.CLEARING;
            case 2 -> STATES.STOPPED;
            case 3 -> STATES.STARTING;
            case 4 -> STATES.IDLE;
            case 5 -> STATES.SUSPENDED;
            case 6 -> STATES.EXECUTE;
            case 7 -> STATES.STOPPING;
            case 8 -> STATES.ABORTING;
            case 9 -> STATES.ABORTED;
            case 10 -> STATES.HOLDING;
            case 11 -> STATES.HELD;
            case 15 -> STATES.RESETTING;
            case 16 -> STATES.COMPLETING;
            case 17 -> STATES.COMPLETE;
            case 18 -> STATES.DEACTIVATING;
            case 19 -> STATES.ACTIVATING;
            default -> null;
        };
    }
}
