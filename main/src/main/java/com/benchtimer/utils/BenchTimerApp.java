package com.benchtimer.utils;

import android.app.Application;

/**
 * Created by evert on 3/20/2014.
 * This is used as a global variable to save state of this application
 */
public class BenchTimerApp extends Application {
    public String getCurrentProtocolName() {
        return currentProtocolName;
    }

    public void setCurrentProtocolName(String currentProtocolName) {
        this.currentProtocolName = currentProtocolName;
    }

    private String currentProtocolName;

    public int getCurrentProtocolId() {
        return mCurrentProtocolId;
    }

    public void setCurrentProtocolId(int mCurrentProtocolId) {
        this.mCurrentProtocolId = mCurrentProtocolId;
    }

    private int mCurrentProtocolId;

}
