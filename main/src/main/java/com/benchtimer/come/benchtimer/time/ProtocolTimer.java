package com.benchtimer.come.benchtimer.time;

import android.content.Context;
import android.os.CountDownTimer;

import com.benchtimer.utils.Utils;
import com.benchtimer.widgets.CircleProgress;

/**
 * Created by evert on 3/9/14.
 */
public class ProtocolTimer extends CountDownTimer {

    private long mMillisInFuture;
    private long mCountDownInterval;
    private Context mContext;
    private CircleProgress mCircleProgress;


    public ProtocolTimer(long millisInFuture, long countDownInterval, Context context, CircleProgress circleProgress) {
        super(millisInFuture, countDownInterval);
        mMillisInFuture = millisInFuture;
        mCountDownInterval = countDownInterval;
        mContext = context;
        mCircleProgress = circleProgress;

    }

    @Override
    public void onTick(long l) {
        String formattedTime = Utils.formatTimeIntoDisplay(l);
        float percent = l / (float) mMillisInFuture * 100;
        mCircleProgress.changeView(percent, formattedTime);
    }

    @Override
    public void onFinish() {
        String formattedTime = Utils.formatTimeIntoDisplay(0);
        mCircleProgress.changeView(0f, formattedTime);
    }

}
