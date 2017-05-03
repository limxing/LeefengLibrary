package me.leefeng.library.view;

import android.animation.Animator;
import android.animation.TimeInterpolator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.TextView;


/**
 * Created by FengTing on 2017/5/2.
 */

public class WelcomePassView extends TextView {

    private int mOutLineColor = 0xFF888888;
    private int mOutLineWidth = 4;

    private int mCircleColor = 0x99888888;
    private int mCircleRadius;

    private int mTextColor = Color.WHITE;

    private int mProgressLineColor = Color.RED;
    private int mProgressLineWidth = 4;
//    private int mProgress = 0;

    private int mCenterX;
    private int mCenterY;


    private Paint mPaint;
    private Rect mBounds;
    private RectF mArcRectF;


    private Action mAction;
    private String mText = "跳过";
    private float floatPro = 0;
    private long time = 3000;


    public WelcomePassView(Context context) {
        this(context, null);
    }

    public WelcomePassView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public WelcomePassView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public void init() {
        mPaint = new Paint();
        mBounds = new Rect();
        mArcRectF = new RectF();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = getMeasuredWidth();
        int height = getMeasuredHeight();
        if (width > height) {
            height = width;
        } else {
            width = height;
        }
        mCircleRadius = width / 2;
        setMeasuredDimension(width, height);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        getDrawingRect(mBounds); //找到view的边界

        mCenterX = mBounds.centerX();
        mCenterY = mBounds.centerY();

        //画大圆
        mPaint.setAntiAlias(true);  //防锯齿
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setColor(mCircleColor);
        canvas.drawCircle(mBounds.centerX(), mBounds.centerY(), mCircleRadius, mPaint);

        //画外边框
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(mOutLineWidth);
        mPaint.setColor(mOutLineColor);
        canvas.drawCircle(mBounds.centerX(), mBounds.centerY(), mCircleRadius - mOutLineWidth, mPaint);

        //画字
        Paint paint = getPaint();
        paint.setColor(mTextColor);
        paint.setAntiAlias(true);  //防锯齿
        paint.setTextAlign(Paint.Align.CENTER);
        float textY = mCenterY - (paint.descent() + paint.ascent()) / 2;
        canvas.drawText(mText, mCenterX, textY, paint);

        //画进度条
        mPaint.setStrokeWidth(mProgressLineWidth);
        mPaint.setColor(mProgressLineColor);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mArcRectF.set(mBounds.left + mProgressLineWidth, mBounds.top + mProgressLineWidth,
                mBounds.right - mProgressLineWidth, mBounds.bottom - mProgressLineWidth);
        canvas.drawArc(mArcRectF, -90,
                360 * floatPro, false, mPaint);
    }

    public void setText(String text) {
        mText = text;
    }

    public void setProgressColor(int color) {
        mProgressLineColor = color;
    }

    public void setCircleBackgroundColor(int color) {
        mCircleColor = color;
    }

    public void setTextColor(int color) {
        mTextColor = color;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN && mAction != null) {
            mAction.onAction();
        }
        return super.onTouchEvent(event);
    }

    public void setTime(long time) {
        this.time = time;
    }

    public void start() {
        ValueAnimator valueAnimator = ValueAnimator.ofInt(0, 1);
        valueAnimator.setDuration(time);
        valueAnimator.setInterpolator(new ThisInterpolator());
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {

                floatPro = valueAnimator.getAnimatedFraction();
                Log.i("leefeng", "onAnimationUpdate: "+floatPro);
                postInvalidate();
            }
        });
        valueAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {

            }

            @Override
            public void onAnimationEnd(Animator animator) {

                if (mAction != null) mAction.onAction();
            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });
        valueAnimator.start();
    }

    public void setmAction(Action mAction) {
        this.mAction = mAction;
    }

    @Override
    protected void onDetachedFromWindow() {
        mAction=null;
        super.onDetachedFromWindow();
    }

    public interface Action {
        void onAction();
    }

    class ThisInterpolator implements TimeInterpolator{

        @Override
        public float getInterpolation(float v) {
            return (float) Math.sin((v / 2) * Math.PI);
        }
    }
}
