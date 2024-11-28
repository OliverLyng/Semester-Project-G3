package org.example.utils;

public class Converter {
    public static STATES showState(int state){
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
    public static BEERTYPE showBeerType(int state) {
        return switch (state) {
            case 0 -> BEERTYPE.PILSNER;
            case 1 -> BEERTYPE.WHEAT;
            case 2 -> BEERTYPE.IPA;
            case 3 -> BEERTYPE.STOUT;
            case 4 -> BEERTYPE.ALE;
            case 5 -> BEERTYPE.ALCOHOL_FREE;
            default -> null;
        };
    }

    public static Float showBeerType(String state) {
        return switch (state) {
            case "Pilsner" -> 0.0f;
            case "Wheat" -> 1.0f;
            case "IPA" -> 2.0f;
            case "Stout" -> 3.0f;
            case "Ale" -> 4.0f;
            case "AlcoholFree" -> 5.0f;
            default -> null;
        };
    }

    public static String showBeerType(Float state) {
        int stateAsInt = state.intValue();
        return switch (stateAsInt) {
            case 0 -> "Pilsner";
            case 1 -> "Wheat";
            case 2 -> "IPA";
            case 3 -> "Stout";
            case 4 -> "Ale";
            case 5 -> "AlcoholFree";
            default -> null;
        };
    }
    public static STOPPED_REASON showStopReason(int state) {
        return switch (state) {
            case 10 -> STOPPED_REASON.EMPTY_INVENTORY;
            case 11 -> STOPPED_REASON.MAINTENANCE;
            case 12 -> STOPPED_REASON.MANUAL_STOP;
            case 13 -> STOPPED_REASON.MOTOR_POWER_LOSS;
            case 14 -> STOPPED_REASON.MANUAL_ABORT;
            default -> null;
        };
    }
}
