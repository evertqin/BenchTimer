package com.benchtimer.widgets;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.benchtimer.main.R;

/**
 * Created by evert on 3/6/14.
 */
public class CircleProgress extends SurfaceView implements SurfaceHolder.Callback {
    private float mCircleCenterPointX;
    private float mCircleCenterPointY;
    private int mColor;
    private float mCircleStrokWidth;
    private float mCircleRadius;

    private String mText;
    private float mTextLocationX;
    private float mTextLocationY;
    private int mTextColor;
    private float mTextSize;

    private float mCirclePercentage;

    private SurfaceHolder mSurfaceHolder;


    public CircleProgress(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        init();
        initAttributes(context, attributeSet);
    }


    private void initAttributes(Context context, AttributeSet attributeSet) {
        TypedArray ta = context.obtainStyledAttributes(attributeSet, R.styleable.CircleProgressWidget);
        mColor = ta.getColor(R.styleable.CircleProgressWidget_circleColor, Color.BLUE);
        mCircleStrokWidth = ta.getFloat(R.styleable.CircleProgressWidget_circleStrokeWidth, 10f);
        mCircleCenterPointX = ta.getFloat(R.styleable.CircleProgressWidget_circleCenterPointX, getWidth());
        mCircleCenterPointY = ta.getFloat(R.styleable.CircleProgressWidget_circleCenterPointY, getHeight());
        mCircleRadius = ta.getFloat(R.styleable.CircleProgressWidget_circleRadius, 20f);
        mTextColor = ta.getColor(R.styleable.CircleProgressWidget_textColor, Color.GREEN);
        mTextSize = ta.getFloat(R.styleable.CircleProgressWidget_textSize, 40f);
        mTextLocationX = ta.getFloat(R.styleable.CircleProgressWidget_circleCenterPointX, 40f);
        mTextLocationY = ta.getFloat(R.styleable.CircleProgressWidget_circleCenterPointY, 40f);
        mCirclePercentage = ta.getFloat(R.styleable.CircleProgressWidget_circlePercentage, 60f);
        mText = ta.getString(R.styleable.CircleProgressWidget_text);

        ta.recycle();
    }

    private void init() {
        setWillNotDraw(false);
        getHolder().addCallback(this);
        setFocusable(true);
        if (!this.isInEditMode()) {
            setZOrderOnTop(true);
        }
        getHolder().setFormat(PixelFormat.TRANSPARENT);
    }


    private void drawCircle(Paint paint, Canvas canvas) {
        paint.setDither(true);
        paint.setColor(mColor);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(mCircleStrokWidth);
        canvas.drawCircle(mCircleCenterPointX, mCircleCenterPointY, mCircleRadius, paint);
    }

    private void drawArcLoading(Paint paint, Canvas canvas) {
        drawArcLoading(mCirclePercentage, paint, canvas);
    }

    private void drawArcLoading(float circlePercentage, Paint paint, Canvas canvas) {
        paint.setDither(true);
        paint.setColor(mColor);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(mCircleStrokWidth);

        float delta = mCircleCenterPointX - mCircleRadius;
        float arcSize = (mCircleCenterPointX - (delta / 2f)) * 2f;
        RectF box = new RectF(delta, delta, arcSize, arcSize);
        float sweep = 360 * circlePercentage * 0.01f;
        canvas.drawArc(box, -90, sweep, false, paint);
    }

    public void drawText(String timeStr, Canvas canvas) {
        Paint textPaint = new Paint();
        textPaint.setColor(mTextColor);
        textPaint.setTextSize(mTextSize);
        textPaint.setStyle(Paint.Style.FILL);
        textPaint.setAntiAlias(true);
        textPaint.setTextAlign(Paint.Align.CENTER);

        int positionX = (getMeasuredWidth() / 2);
        int positionY = (int) ((getMeasuredWidth() / 2) - ((textPaint.descent() + textPaint.ascent()) / 2));
        canvas.drawText(timeStr, positionX, positionY, textPaint);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Paint paint = new Paint(Paint.FILTER_BITMAP_FLAG | Paint.DITHER_FLAG | Paint.ANTI_ALIAS_FLAG);
        drawArcLoading(paint, canvas);
        drawText(mText,canvas);
    }



    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {

    }

    public SurfaceHolder getSurfaceHolder() {
        return mSurfaceHolder;
    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i2, int i3) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {

    }
}
