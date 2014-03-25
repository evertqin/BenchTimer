package com.benchtimer.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.benchtimer.utils.Parameters;

import java.util.ArrayList;

/**
 * Created by evert on 3/12/14.
 */
public class TimerDatabaseWorker extends SQLiteOpenHelper {

    private static TimerDatabaseWorker mTimerDatebaseWorderInstance = null;

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "BenchTimer.db";

    private static final String TEXT_TYPE = " TEXT";


    public TimerDatabaseWorker(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public static synchronized TimerDatabaseWorker getInstance(Context context) {
        if (mTimerDatebaseWorderInstance == null) {
            mTimerDatebaseWorderInstance = new TimerDatabaseWorker(context);
            return mTimerDatebaseWorderInstance;
        } else {
            return mTimerDatebaseWorderInstance;
        }
    }

    public static synchronized TimerDatabaseWorker getInstance() throws RuntimeException {
        if (mTimerDatebaseWorderInstance == null) {
            throw new RuntimeException("The instance for databaseworker was not previously constructed.");
        } else {
            return mTimerDatebaseWorderInstance;
        }
    }


    private static final String DATATIME_TYPE = " TEXT";
    private static final String INTEGER_TYPE = " INTEGER";
    private static final String PRIMARY_KEY = " PRIMARY KEY";
    private static final String COMMA_SEQ = ", ";


    /**
     * This SQL query creates a table that holds the protocol detail information
     */
    private static final String SQL_CREATE_PROTOCOL_TABLE = "CREATE TABLE "
            + TimerEntries.TimerEntry.PROTOCOL_TABLE_NAME + " ("
            + TimerEntries.TimerEntry.COLUMN_PROTOCOL_ID + INTEGER_TYPE + PRIMARY_KEY + COMMA_SEQ
            + TimerEntries.TimerEntry.COLUMN_PROTOCOL_NAME + TEXT_TYPE
            + ")";

    /**
     * This table detail the steps in each timer
     * http://stackoverflow.com/questions/398425/maintaining-sort-order-of-database-table-rows
     * Maintaining sort order of database table rows
     * If you really want to consider correct, consider rethinking the question in terms of data structures
     * – what you might be asking is how to implement a linked list in a database.
     * |step_id|protocol_id|parent_id|next_id|name|set_time|real_time
     */
    private static final String SQL_CREATE_TIMER_TABLE = "CREATE TABLE "
            + TimerEntries.TimerEntry.STEP_TABLE_NAME + " ("
            + TimerEntries.TimerEntry.COLUMN_STEP_ID + INTEGER_TYPE + COMMA_SEQ
            + TimerEntries.TimerEntry.COLUMN_PROTOCOL_ID + INTEGER_TYPE + COMMA_SEQ
            + TimerEntries.TimerEntry.COLUMN_PARENT_STEP_ID + INTEGER_TYPE + COMMA_SEQ
            + TimerEntries.TimerEntry.COLUMN_NEXT_STEP_ID + INTEGER_TYPE + COMMA_SEQ
            + TimerEntries.TimerEntry.COLUMN_STEP_NAME + TEXT_TYPE + COMMA_SEQ
            + TimerEntries.TimerEntry.COLUMN_STEP_SETTIME + DATATIME_TYPE + COMMA_SEQ
            + TimerEntries.TimerEntry.COLUMN_STEP_REAL_TIME + DATATIME_TYPE
            + ")";


    private static final String SQL_DELETE_TABLE = "DROP TABLE IF EXISTS "
            + TimerEntries.TimerEntry.STEP_TABLE_NAME;


    private static final String[] TEST_QUERY =
            {"INSERT INTO " + TimerEntries.TimerEntry.STEP_TABLE_NAME + " VALUES(0,0,-1, 1, 'Remove Antibody','00:00:3' ,-1)",
                    "INSERT INTO " + TimerEntries.TimerEntry.STEP_TABLE_NAME + " VALUES(1, 0, 0, 2, 'Centrifuge 1',  '0:02:00',-1)",
                    "INSERT INTO " + TimerEntries.TimerEntry.STEP_TABLE_NAME + " VALUES(2, 0, 1,-1, 'Centrifuge 2', '0:01:02',3)",
                    //"INSERT INTO " + TimerEntries.TimerEntry.STEP_TABLE_NAME + " VALUES(0, 'Remove Liquid', '0:30:00',3)",
                    //"INSERT INTO " + TimerEntries.TimerEntry.STEP_TABLE_NAME + " VALUES(0, 'HPLC Analysis', '1:30:00',3)",
                    //"INSERT INTO " + TimerEntries.TimerEntry.STEP_TABLE_NAME + " VALUES(0, 'Shake', '4:30:00',3)",
                    //"INSERT INTO " + TimerEntries.TimerEntry.STEP_TABLE_NAME + " VALUES(0, 'Sit', '9:30:00',3)",
                    //Other stuff
                    "INSERT INTO " + TimerEntries.TimerEntry.PROTOCOL_TABLE_NAME + " VALUES(0,'Western Block')",
                    //"INSERT INTO " + TimerEntries.TimerEntry.PROTOCOL_TABLE_NAME + " VALUES(1,'Animal Test')",
                    //"INSERT INTO " + TimerEntries.TimerEntry.PROTOCOL_TABLE_NAME + " VALUES(2,'Clinical Trial')",
                    //"INSERT INTO " + TimerEntries.TimerEntry.PROTOCOL_TABLE_NAME + " VALUES(3,'Test Subject')",

            };


    public String queryProtocolName(int id) throws IllegalArgumentException {
        if (id < 0) {
            throw new IllegalArgumentException(Integer.toString(id) + " is less than 0");
        }

        final String query = "SELECT " + TimerEntries.TimerEntry.COLUMN_PROTOCOL_NAME + " "
                + "FROM " + TimerEntries.TimerEntry.PROTOCOL_TABLE_NAME + " "
                + "WHERE " + TimerEntries.TimerEntry.COLUMN_PROTOCOL_ID + "=" + Integer.toString(id);

        Cursor cursor = this.getReadableDatabase().rawQuery(query, null);
        if (cursor != null && cursor.moveToFirst()) {
            return cursor.getString(cursor.getColumnIndex(TimerEntries.TimerEntry.COLUMN_PROTOCOL_NAME));

        }
        return "";

    }


    public void addNewProtocol(String protocolName) {
        int id = getProtocolCount();
        System.out.println(id);
        if (id < Parameters.TOTAL_NUM_OF_TIMERS) {
            ContentValues contentValues = new ContentValues();
            contentValues.put(TimerEntries.TimerEntry.COLUMN_PROTOCOL_ID, id);
            contentValues.put(TimerEntries.TimerEntry.COLUMN_PROTOCOL_NAME, protocolName);
            this.getWritableDatabase().insert(TimerEntries.TimerEntry.PROTOCOL_TABLE_NAME, null, contentValues);
        }
    }

    public void updateProtocol(int id, String protocolName) {
        if (id < Parameters.TOTAL_NUM_OF_TIMERS) {
            final String updateQuery = "UPDATE " + TimerEntries.TimerEntry.PROTOCOL_TABLE_NAME + " SET "
                    + TimerEntries.TimerEntry.COLUMN_PROTOCOL_NAME + "='" + protocolName
                    + "' WHERE " + TimerEntries.TimerEntry.COLUMN_PROTOCOL_ID + "=" + Integer.toString(id);
            this.getWritableDatabase().execSQL(updateQuery);
        } else {
            System.out.println("Error, cannot update");
        }

    }

    public int getProtocolCount() {
        final String query = "SELECT COUNT(" + TimerEntries.TimerEntry.COLUMN_PROTOCOL_ID + ") FROM " + TimerEntries.TimerEntry.PROTOCOL_TABLE_NAME;
        Cursor cursor = this.getReadableDatabase().rawQuery(query, null);
        int count = 0;
        if (cursor != null && cursor.moveToFirst()) {
            count = cursor.getInt(0);
        }
        return count;
    }


/*
    public StepEntry querySingeProtocolDetail(int protocolId, int stepId) {
        final String[] columns = {TimerEntries.TimerEntry.COLUMN_STEP_ID,
                TimerEntries.TimerEntry.COLUMN_STEP_NAME,
                TimerEntries.TimerEntry.COLUMN_STEP_SETTIME,
                TimerEntries.TimerEntry.COLUMN_PARENT_STEP_ID,
                TimerEntries.TimerEntry.COLUMN_NEXT_STEP_ID};
        final String selection = TimerEntries.TimerEntry.COLUMN_PROTOCOL_ID + "=?";
        final String[] selectionArgs = {Integer.toString(protocolId)};
        Cursor cursor = this.getReadableDatabase().query(TimerEntries.TimerEntry.STEP_TABLE_NAME, columns, selection, selectionArgs, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {

        }
    }
*/


    public ArrayList<StepEntry> queryProtocolDetail(int protocolId) throws IllegalArgumentException {
        final String[] columns = {TimerEntries.TimerEntry.COLUMN_STEP_ID,
                TimerEntries.TimerEntry.COLUMN_STEP_NAME,
                TimerEntries.TimerEntry.COLUMN_STEP_SETTIME};
        final String selection = TimerEntries.TimerEntry.COLUMN_PROTOCOL_ID + "=?";
        final String[] selectionArgs = {Integer.toString(protocolId)};

        Cursor cursor = this.getReadableDatabase().query(TimerEntries.TimerEntry.STEP_TABLE_NAME, columns, selection, selectionArgs, null, null, null);
        ArrayList<StepEntry> entries = new ArrayList<StepEntry>();

        if (cursor != null && cursor.moveToFirst()) {
            do {
                String stepId = cursor.getString(cursor.getColumnIndex(TimerEntries.TimerEntry.COLUMN_STEP_ID));
                String name = cursor.getString(cursor.getColumnIndex(TimerEntries.TimerEntry.COLUMN_STEP_NAME));
                String setTime = cursor.getString(cursor.getColumnIndex(TimerEntries.TimerEntry.COLUMN_STEP_SETTIME));
                StepEntry stepEntry = new StepEntry();
                stepEntry.stepId = Integer.valueOf(stepId);
                stepEntry.name = name;
                stepEntry.setTime = setTime;
                entries.add(stepEntry);
            } while (cursor.moveToNext());
        }
        return entries;
    }


    public StepEntry getStep(int protocolId, int stepId) {
/*        String[] columns = {
                TimerEntries.TimerEntry.COLUMN_STEP_NAME,
                TimerEntries.TimerEntry.COLUMN_STEP_SETTIME,
                TimerEntries.TimerEntry.COLUMN_NEXT_STEP_ID};*/
        final String query = "SELECT " + TimerEntries.TimerEntry.COLUMN_STEP_NAME + COMMA_SEQ
                + TimerEntries.TimerEntry.COLUMN_STEP_SETTIME + COMMA_SEQ
                + TimerEntries.TimerEntry.COLUMN_NEXT_STEP_ID + " "
                + "FROM " + TimerEntries.TimerEntry.STEP_TABLE_NAME + " "
                + "WHERE " + TimerEntries.TimerEntry.COLUMN_PROTOCOL_ID + "=" + Integer.toString(protocolId)
                + " and " + TimerEntries.TimerEntry.COLUMN_STEP_ID + "=" + Integer.toString(stepId);
        Cursor cursor = this.getReadableDatabase().rawQuery(query, null);
        if (cursor != null && cursor.moveToFirst()) {
            System.out.println("in get Step");
            String name = cursor.getString(cursor.getColumnIndex(TimerEntries.TimerEntry.COLUMN_STEP_NAME));
            String setTime = cursor.getString(cursor.getColumnIndex(TimerEntries.TimerEntry.COLUMN_STEP_SETTIME));
            int stepNextStepId = cursor.getInt(cursor.getColumnIndex(TimerEntries.TimerEntry.COLUMN_NEXT_STEP_ID));
            StepEntry stepEntry = new StepEntry();
            stepEntry.name = name;
            stepEntry.setTime = setTime;
            stepEntry.nextId = stepNextStepId;
            return stepEntry;
        } else {
            return null;
        }
    }


    public int getStepCounts(int protocolId) {
        final String query = "SELECT COUNT(" + TimerEntries.TimerEntry.COLUMN_STEP_ID + ") FROM"
                + TimerEntries.TimerEntry.STEP_TABLE_NAME + " WHERE " + TimerEntries.TimerEntry.COLUMN_PROTOCOL_ID + "= ?";
        Cursor cursor = this.getReadableDatabase().rawQuery(query, new String[]{Integer.toString(protocolId)});
        if (cursor != null && cursor.moveToFirst()) {
            return cursor.getInt(0);
        } else {
            return 0;
        }
    }


    public void insertStep(int protocolId, int stepId, int newNextStepId) {
        // first get the nextStepId of the stepId
        Cursor cursor = this.getReadableDatabase().query(TimerEntries.TimerEntry.STEP_TABLE_NAME, new String[]{TimerEntries.TimerEntry.COLUMN_NEXT_STEP_ID},
                TimerEntries.TimerEntry.COLUMN_PROTOCOL_ID + "=? and " + TimerEntries.TimerEntry.COLUMN_STEP_ID + "=?",
                new String[]{Integer.toString(protocolId), Integer.toString(stepId)}, null, null, null);
        Integer nextId = null;
        if (cursor != null && cursor.moveToFirst()) {
            nextId = cursor.getInt(0);
        }

        ContentValues cvCurrent = new ContentValues();
        cvCurrent.put(TimerEntries.TimerEntry.COLUMN_NEXT_STEP_ID, newNextStepId);
        this.getWritableDatabase().update(TimerEntries.TimerEntry.STEP_TABLE_NAME, cvCurrent, TimerEntries.TimerEntry.COLUMN_STEP_ID + "=?", new String[]{Integer.toString(stepId)});

        // we also need to point the newAddedStep's nextStepId to the next Chain
        ContentValues cvNext = new ContentValues();
        cvNext.put(TimerEntries.TimerEntry.COLUMN_NEXT_STEP_ID, nextId);
        this.getWritableDatabase().update(TimerEntries.TimerEntry.STEP_TABLE_NAME, cvNext, TimerEntries.TimerEntry.COLUMN_STEP_ID + "=?", new String[]{Integer.toString(newNextStepId)});
    }

    public void addNewStep(int protocolId, int selectedStepId, StepEntry newStepInfo) {

        int stepCounts = getStepCounts(protocolId);
        int previousStepId = selectedStepId;
        int currentStepId = stepCounts;
        if (selectedStepId < 0) {
            // This means we didn't select a step, so we will append to the end of the steps
            if (stepCounts > 0) {
                previousStepId = stepCounts - 1;
            }
        }

        ContentValues contentValues = new ContentValues();

        contentValues.put(TimerEntries.TimerEntry.COLUMN_STEP_NAME, newStepInfo.name);
        contentValues.put(TimerEntries.TimerEntry.COLUMN_STEP_ID, currentStepId);
        contentValues.put(TimerEntries.TimerEntry.COLUMN_PROTOCOL_ID, protocolId);
        contentValues.put(TimerEntries.TimerEntry.COLUMN_STEP_SETTIME, newStepInfo.setTime);
        contentValues.put(TimerEntries.TimerEntry.COLUMN_STEP_REAL_TIME, Parameters.NO_REAL_TIME_YET);
        // we also need to update the previous step's next id field
        if (previousStepId >= 0) {
            insertStep(protocolId, selectedStepId, stepCounts);
        }
        this.getWritableDatabase().insert(TimerEntries.TimerEntry.STEP_TABLE_NAME, null, contentValues);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(SQL_CREATE_TIMER_TABLE);
        sqLiteDatabase.execSQL(SQL_CREATE_PROTOCOL_TABLE);
        for (String query : TEST_QUERY) {
            sqLiteDatabase.execSQL(query);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i2) {

    }
}
