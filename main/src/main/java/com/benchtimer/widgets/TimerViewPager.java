package com.benchtimer.widgets;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by evert on 3/9/14.
 */
public class TimerViewPager extends ViewPager {

    private Context mContext;

    private boolean isPagerEnabled = true;

    public TimerViewPager(Context context) {
        super(context);
        mContext = context;
    }

    public void disablePager() {
        isPagerEnabled = false;
    }

    public void enablePager() {
        isPagerEnabled = true;
    }


    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (isPagerEnabled) {
            return super.onTouchEvent(ev);
        } else {
            return false;
        }
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (isPagerEnabled) {
            return super.onInterceptTouchEvent(ev);
        } else {
            return false;
        }
    }

    public TimerViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
    }

}
