package com.benchtimer.widgets;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;

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

}
