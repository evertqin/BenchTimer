package com.benchtimer.time;

import android.os.SystemClock;

/**
 * Created by evert on 3/23/2014.
 * We need a countup timer so when the counterdown timer stops, unless
 * user clicks pause of stop, we still run the clock
 */
public abstract class CountUpTimer {


    private BaseCountUpTimer mBaseCountUpTimer;
    private long mCurrentElapse;
    private boolean isPaused = true;
    private Thread mThread;

    public CountUpTimer() {
        mBaseCountUpTimer = new BaseCountUpTimer();
    }

    public CountUpTimer(long currentElapse) {
        mBaseCountUpTimer = new BaseCountUpTimer();
        this.mCurrentElapse = currentElapse;
    }

    public synchronized final CountUpTimer start() {
        if(isPaused) {
            System.out.println(mCurrentElapse);
            mBaseCountUpTimer = new BaseCountUpTimer(mCurrentElapse);
            mThread = new Thread(mBaseCountUpTimer);
            mThread.start();
            isPaused = false;
        }
        return this;
    }


    public abstract void onTick(long millis);


    /**
     * Pauses the CountUpTimer, so it could be resumed(start)
     * later from the same point where it was paused.
     */
    public void pause() throws IllegalArgumentException {
        if(!isPaused) {

            mBaseCountUpTimer.cancel();
            mThread.interrupt();
            isPaused = true;
        } else {
            throw new IllegalArgumentException("Cannot pause, the Timer is already paused!");
        }
    }


    class BaseCountUpTimer implements Runnable {
        private long mStartTime = SystemClock.elapsedRealtime();
        private long mPreviousElapse;


        public BaseCountUpTimer() {
            mPreviousElapse = 0;
        }

        public BaseCountUpTimer(long currentElapse) {
            mPreviousElapse = currentElapse;
        }

        public long getBaseTime() {
            return mStartTime;
        }

        public void cancel() {
            Thread.currentThread().interrupt();
        }

        @Override
        public void run() {
            while (!Thread.currentThread().isInterrupted()) {
                try {
                    Thread.sleep(10);
                    mCurrentElapse = SystemClock.elapsedRealtime() - mStartTime + mPreviousElapse;
                    onTick(mCurrentElapse);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                } catch (Exception e) {

                }

            }

        }
    }


}
