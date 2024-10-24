package org.example;

public class Statifyer {
    public STATES showState(int state){
        switch (state)
        {
            case 0:
                return STATES.DEACTIVATED;
            case 1:
                return STATES.CLEARING;
            case 2:
                return STATES.STOPPED;
            case 3:
                return STATES.STARTING;
            case 4:
                return STATES.IDLE;
            case 5:
                return STATES.SUSPENDED;
            case 6:
                return STATES.EXECUTE;
            case 7:
                return STATES.STOPPING;
            case 8:
                return STATES.ABORTING;
            case 9:
                return STATES.ABORTED;
            case 10:
                return STATES.HOLDING;
            case 11:
                return STATES.HELD;
            case 15:
                return STATES.RESETTING;
            case 16:
                return STATES.COMPLETING;
            case 17:
                return STATES.COMPLETE;
            case 18:
                return STATES.DEACTIVATING;
            case 19:
                return STATES.ACTIVATING;
            default: return null;
        }
    }
}
