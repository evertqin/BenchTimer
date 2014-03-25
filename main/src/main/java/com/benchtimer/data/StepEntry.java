package com.benchtimer.data;

/**
 * Created by evert on 3/13/14.
 */
public class StepEntry {
    public int stepId;
    public String name;
    public String setTime;
    public String realTime;
    public Integer nextId;

    public static final int NEW_STEP_ENTRY = -2;
    public static final int USER_GIVE_UP = -1;

    public StepEntry() {
        stepId = NEW_STEP_ENTRY;
        this.name = "Please Enter A Name";
        this.setTime = "00:00:00";
    }

    public StepEntry(String name, String setTime) {
        this.name = name;
        this.setTime = name;
        stepId = NEW_STEP_ENTRY;
        nextId = null;
    }

}
