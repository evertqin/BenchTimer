package com.benchtimer.data;


import android.content.Context;
import java.util.List;

/**
 * Created by evert on 3/12/14.
 */
public class TimerDatabaseWorker {

    private static TimerDatabaseWorker mTimerDatebaseWorderInstance = null;
    private SQLiteProcessor mSQLiteProcessor;


    private TimerDatabaseWorker(){

    }

    public void init(Context context) {
        if(mSQLiteProcessor == null) {
            mSQLiteProcessor = new SQLiteProcessor(context);
        }
    }


    public static TimerDatabaseWorker getInstance() throws RuntimeException {
        if (mTimerDatebaseWorderInstance == null) {
            mTimerDatebaseWorderInstance = new TimerDatabaseWorker();
        }
        return mTimerDatebaseWorderInstance;
    }

    public String queryProtocolName(int id) {
        if(mSQLiteProcessor == null) {
            throw new RuntimeException("Cannot find the SQL worker instance, did you forget to call init()?");
        }
        return mSQLiteProcessor.queryProtocolName(id);
    }

    public int getProtocolCount() {
        if(mSQLiteProcessor == null) {
            throw new RuntimeException("Cannot find the SQL worker instance, did you forget to call init()?");
        }
        return mSQLiteProcessor.getProtocolCount();
    }

    public StepEntry getStep(int protocolId, int stepId) {
        if(mSQLiteProcessor == null) {
            throw new RuntimeException("Cannot find the SQL worker instance, did you forget to call init()?");
        }
        return mSQLiteProcessor.getStep(protocolId, stepId);
    }

    public void addNewProtocol(String protocolName) {
        if(mSQLiteProcessor == null) {
            throw new RuntimeException("Cannot find the SQL worker instance, did you forget to call init()?");
        }
        mSQLiteProcessor.addNewProtocol(protocolName);
    }

    public void updateProtocol(int id, String protocolName) {
        if(mSQLiteProcessor == null) {
            throw new RuntimeException("Cannot find the SQL worker instance, did you forget to call init()?");
        }
        mSQLiteProcessor.updateProtocol(id, protocolName);
    }

    public List<StepEntry> queryProtocolDetail(int id) {
        if(mSQLiteProcessor == null) {
            throw new RuntimeException("Cannot find the SQL worker instance, did you forget to call init()?");
        }
        return mSQLiteProcessor.queryProtocolDetail(id);
    }

}
