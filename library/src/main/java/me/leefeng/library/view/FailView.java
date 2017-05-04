package me.leefeng.library.view;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.LinearInterpolator;

import me.leefeng.library.R;
import me.leefeng.library.utils.LogUtils;

/**
 * Created by FengTing on 2017/5/4.
 */

public class FailView extends View {

    private static final String TAG = "FailView";
    private static final String CLICKFRESH = "糟糕，网络遇到问题，点击刷新";
    private static final String REFRESHING = "正在加载...";
    private static final int MODE_NONET = 1002;
    private static final int MODE_REFRESH = 1001;
    private int width;
    private int height;
    private Paint paint;
    private Rect textR;
    private float density;

    private int currentMode = MODE_NONET;
    private float degrees = 90;

    private FailViewListener listener;

    public FailView(Context context) {
        super(context);

    }

    public FailView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        Log.i(TAG, "FailView: ");
        setBackgroundColor(Color.parseColor("#eeeeee"));
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();

        Log.i(TAG, "onAttachedToWindow: ");
        density = getResources().getDisplayMetrics().density;
        paint = new Paint();
        paint.setColor(Color.parseColor("#bfbfbf"));
        paint.setStrokeWidth(1);
        paint.setStyle(Paint.Style.FILL_AND_STROKE);
        paint.setStrokeJoin(Paint.Join.ROUND);
        paint.setStrokeCap(Paint.Cap.ROUND);
        paint.setTextSize(density * 16);
        paint.setAntiAlias(true);
        textR = new Rect();
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (width == 0) {
            width = getWidth();
            height = getHeight();
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Bitmap bitmap = null;
        String text = "";
        int top = height / 5;
        switch (currentMode) {
            case MODE_REFRESH:
                text = REFRESHING;
                bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.failview_loading);
                break;
            case MODE_NONET:
                text = CLICKFRESH;
                bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ic_failview_nonet);
                break;
        }
//        Log.i(TAG, "onDraw: "+bitmap.getHeight());
        canvas.drawBitmap(bitmap, width / 2 - bitmap.getWidth() / 2, top, null);
        int textTop = (int) (top + bitmap.getHeight() + density * 40);
        paint.getTextBounds(text, 0, text.length(), textR);
        canvas.drawText(text, width / 2 - textR.width() / 2, textTop, paint);

    }

    /**
     * which mode
     *
     * @param currentMode
     */
    public void setMode(int currentMode) {
        setVisibility(View.VISIBLE);
        this.currentMode = currentMode;
        invalidate();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (currentMode) {
            case MODE_REFRESH:
                break;
            case MODE_NONET:
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        paint.setColor(Color.parseColor("#8a8a8a"));
                        break;
                    case MotionEvent.ACTION_UP:
                        paint.setColor(Color.parseColor("#bfbfbf"));
                        postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                setVisibility(View.GONE);
                                if (listener != null) {
                                    listener.onClick();
                                }
                            }
                        }, 500);
                        break;
                }
                break;
        }
        invalidate();
        return true;
    }

    private void start() {

        ValueAnimator animator = ValueAnimator.ofInt(0, 12);
        animator.setDuration(12 * 80);
        animator.setInterpolator(new LinearInterpolator());
        animator.setRepeatCount(Integer.MAX_VALUE);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                degrees = 30 * (Integer) valueAnimator.getAnimatedValue();
                postInvalidate();
            }
        });
        animator.start();
    }

    public void setListener(FailViewListener listener) {
        this.listener = listener;
    }

    public interface FailViewListener {
        void onClick();
    }

}
