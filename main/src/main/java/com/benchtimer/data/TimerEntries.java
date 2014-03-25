package com.benchtimer.data;

import android.provider.BaseColumns;

/**
 * Created by evert on 3/12/14.
 */
public final class TimerEntries {
    public static abstract class TimerEntry implements BaseColumns {
        public static final String PROTOCOL_TABLE_NAME = "protocol_detail";
        public static final String COLUMN_PROTOCOL_NAME = "protocol_name";
        public static final String COLUMN_PROTOCOL_ID = "protocol_id";

        public static final String STEP_TABLE_NAME = "step_detail";
        public static final String COLUMN_STEP_ID = "step_id";
        public static final String COLUMN_STEP_NAME = "step_name";
        public static final String COLUMN_STEP_SETTIME = "step_set_time";
        public static final String COLUMN_STEP_REAL_TIME = "step_real_time";
        public static final String COLUMN_NEXT_STEP_ID = "next_id";
        public static final String COLUMN_PARENT_STEP_ID = "parent_id";

        public static final int PARENT_HEAD = -1;
        public static final int CHILD_END = -1;
    }
}
