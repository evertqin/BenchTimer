package com.benchtimer.time;

import android.os.CountDownTimer;

/**
 * Created by evert on 3/9/14.
 */
public abstract class ProtocolTimer {

    private long mMillisInFuture = 0;
    private long mCountDownInterval = 0;
    private boolean isPaused = true;
    private long mMillisRemaining;
    private long mCurrentElapse = 0;

    private CountDownTimer mCountDownTimer;
    private CountUpTimer mCountUpTimer;


    public ProtocolTimer(long millisInFuture, long countDownInterval) {

        mMillisInFuture = millisInFuture;
        mCountDownInterval = countDownInterval;
        mMillisRemaining = mMillisInFuture;

    }

    private void createCountDownTimer() {
        mCountDownTimer = new CountDownTimer((mMillisRemaining == 0) ? mMillisInFuture : mMillisRemaining, mCountDownInterval) {
            @Override
            public void onTick(long l) {
                mMillisRemaining = l;
                ProtocolTimer.this.onTick(l);

            }

            @Override
            public void onFinish() {
                mMillisRemaining = 0;
                ProtocolTimer.this.onFinish();
                createCountUpTimer();
                mCountUpTimer.start();
            }
        };
    }


    private void createCountUpTimer() {
        mCountUpTimer = new CountUpTimer(mCurrentElapse) {
            @Override
            public void onTick(long millis) {
                mCurrentElapse = millis;
                ProtocolTimer.this.onUpTick(millis);
            }
        };
    }


    public abstract void onUpTick(long millis);

    /**
     * Callback fired on regular interval.
     *
     * @param millisUntilFinished The amount of time until finished.
     */
    public abstract void onTick(long millisUntilFinished);

    /**
     * Callback fired when the time is up.
     */
    public abstract void onFinish();


    /**
     * Cancel the countdown
     */
    public final void cancel() {
        if (mCountDownTimer != null) {
            mCountDownTimer.cancel();
        }
        this.mMillisRemaining = 0;
    }

    /**
     * Start or Resume the countdown.
     *
     * @return ProtocolTimer current instance
     */
    public synchronized final ProtocolTimer start() {
        if (isPaused) {
            createCountUpTimer();
            if (mMillisRemaining != 0) {
                createCountDownTimer();
                mCountDownTimer.start();
            } else {
                mCountUpTimer.start();
            }
            isPaused = false;
        }
        return this;
    }

    /**
     * Pauses the ProtocolTimer, so it could be resumed(start)
     * later from the same point where it was paused.
     */
    public void pause() throws IllegalStateException {
        if (isPaused == false) {
            if (mMillisRemaining == 0) {
                mCountUpTimer.pause();
            } else {
                mCountDownTimer.cancel();
            }
        } else {
            throw new IllegalStateException("ProtocolTimer is already in pause state, start counter before pausing it.");
        }
        isPaused = true;
    }

    public boolean isPaused() {
        return isPaused;
    }


}
