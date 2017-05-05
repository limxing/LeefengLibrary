package me.leefeng.library.view;

import android.animation.ValueAnimator;
import android.bluetooth.le.ScanCallback;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.LinearInterpolator;

import me.leefeng.library.R;

/**
 * Created by FengTing on 2017/5/4.
 */

public class FailView extends View {

    private static final String TAG = "FailView";
    private static final String CLICKFRESH = "糟糕，网络遇到问题，点击刷新";
    private static final String REFRESHING = "正在加载...";
    public static final int MODE_NONET = 1002;
    public static final int MODE_FAIL = 1003;
    public static final int MODE_CRY = 1004;
    public static final int MODE_RESULT = 1005;
    private static final int MODE_REFRESH = 1001;
    private static final String TEXT_FAIL = "获取失败，请点击重试";
    private static final String TEXT_CRY = "";
    private static final String TEXT_RESULT = "没有结果";
    private int textfocusColor;
    private int textnormalColor;
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

    public FailView(Context context, AttributeSet attrs) {
        super(context, attrs);
        Log.i(TAG, "FailView: ");
        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.FailView);
        int bacColor = typedArray.getColor(R.styleable.FailView_backgroundColor, Color.parseColor("#eeeeee"));
        textnormalColor = typedArray.getColor(R.styleable.FailView_textNomorColor, Color.parseColor("#bfbfbf"));
        textfocusColor = typedArray.getColor(R.styleable.FailView_textFocusColor, Color.parseColor("#8a8a8a"));
        typedArray.recycle();
        setBackgroundColor(bacColor);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();

        Log.i(TAG, "onAttachedToWindow: ");
        density = getResources().getDisplayMetrics().density;
        paint = new Paint();
        paint.setColor(textnormalColor);
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
            case MODE_FAIL:
                text = TEXT_FAIL;
                bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ic_failview_fail);
                break;
            case MODE_CRY:
                text = TEXT_FAIL;
                bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ic_failview_nodata);
                break;
            case MODE_RESULT:
                text = TEXT_RESULT;
                bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ic_failview_noresult);
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
            case MODE_FAIL:
            case MODE_NONET:
            case MODE_CRY:
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        paint.setColor(textfocusColor);
                        break;
                    case MotionEvent.ACTION_UP:
                        paint.setColor(textnormalColor);
                        postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                setVisibility(View.GONE);
                                if (listener != null) {
                                    listener.onClick();
                                }
                            }
                        }, 300);
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
