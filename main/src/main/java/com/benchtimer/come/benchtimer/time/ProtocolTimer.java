package com.benchtimer.come.benchtimer.time;

import android.content.Context;
import android.graphics.Canvas;
import android.os.CountDownTimer;
import android.view.SurfaceHolder;

import com.benchtimer.utils.Utils;
import com.benchtimer.widgets.CircleProgress;

/**
 * Created by evert on 3/9/14.
 */
public class ProtocolTimer extends CountDownTimer {

    private long mMillisInFuture;
    private long mCountDownInterval;
    private SurfaceHolder mSurfaceHolder;
    private Context mContext;
    private CircleProgress mCircleProgress;
    private Canvas mCanvas;


    public ProtocolTimer(long millisInFuture, long countDownInterval, SurfaceHolder holder, Context context, CircleProgress circleProgress) {
        super(millisInFuture, countDownInterval);
        mMillisInFuture = millisInFuture;
        mCountDownInterval = countDownInterval;
        mSurfaceHolder = holder;
        mContext = context;
        mCircleProgress = circleProgress;

    }

    @Override
    public void onTick(long l) {
        String formattedTime = Utils.formatTimeIntoDisplay(l);
        mCanvas = mSurfaceHolder.lockCanvas();
        if(mCanvas != null && mCircleProgress != null) {
            mCircleProgress.drawText(formattedTime, mCanvas);
            mSurfaceHolder.unlockCanvasAndPost(mCanvas);
        }
    }

    @Override
    public void onFinish() {

    }

}
