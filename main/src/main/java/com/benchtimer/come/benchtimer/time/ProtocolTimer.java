package com.benchtimer.come.benchtimer.time;

import android.os.CountDownTimer;

/**
 * Created by evert on 3/9/14.
 */
public class ProtocolTimer extends CountDownTimer {

    private boolean isActive;
    public ProtocolTimer(long millisInFuture, long countDownInterval) {
        super(millisInFuture, countDownInterval);
    }

    @Override
    public void onTick(long l) {

    }

    @Override
    public void onFinish() {

    }
}
