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
    private CircleProgress mCircleProgress;

    public TimerViewPager(Context context) {
        super(context);
        mContext = context;
    }

    public TimerViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
    }


    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if(ev.getY() > getHeight() * 2 / 3) {
            if (ev.getX() < getWidth() / 3) {
                System.out.println("left");

            } else if (ev.getX() < getWidth() * 2 / 3 && ev.getX() >= getWidth() / 3) {
                System.out.println("middle");

            } else {
                System.out.println("right");

            }
        }
        return super.onTouchEvent(ev);
    }
}
