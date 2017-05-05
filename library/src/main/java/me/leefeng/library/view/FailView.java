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
import android.graphics.RectF;
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

    private static final String CLICKFRESH = "糟糕，网络遇到问题，点击刷新";
    private static final String REFRESHING = "正在加载...";
    public static final int MODE_NONET = 1002;
    public static final int MODE_FAIL = 1003;
    public static final int MODE_CRY = 1004;
    public static final int MODE_RESULT = 1005;
    public static final int MODE_SUCCESS = 1006;
    public static final int MODE_REFRESH = 1001;
    private static final String TEXT_FAIL = "获取失败，请点击重试";
    private static final String TEXT_CRY = "";
    private static final String TEXT_RESULT = "没有结果";
    private static final String TAG = "FailView";
    private int failCircleRadius;
    private int failCircleDuration;
    private int failCircleColor;
    private int failCircleBacColor;
    private float radius;
    private float textSize;
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
    private float startRadius;
    private ValueAnimator animator;
    private RectF circleRect;//画圆弧
    private Rect touchRect;
    private Bitmap bitmap;
    private String text = "";
    private int top;
    private int textTop;

    public FailView(Context context) {
        super(context);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        paint = null;
        textR = null;
        touchRect = null;
        if (bitmap != null)
            bitmap.recycle();
        bitmap = null;
        circleRect = null;
    }

    public FailView(Context context, AttributeSet attrs) {
        super(context, attrs);
        density = getResources().getDisplayMetrics().density;
        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.FailView);
        int bacColor = typedArray.getColor(R.styleable.FailView_backgroundColor, Color.parseColor("#eeeeee"));
        textnormalColor = typedArray.getColor(R.styleable.FailView_textNomorColor, Color.parseColor("#bfbfbf"));
        textfocusColor = typedArray.getColor(R.styleable.FailView_textFocusColor, Color.parseColor("#8a8a8a"));
        failCircleBacColor = typedArray.getColor(R.styleable.FailView_failCircleBacColor, Color.GRAY);
        failCircleColor = typedArray.getColor(R.styleable.FailView_failCircleColor, Color.RED);
        failCircleDuration = typedArray.getInteger(R.styleable.FailView_failCircleDuration, 1000);
        failCircleRadius = typedArray.getInteger(R.styleable.FailView_failCircleRadius, 60);
        textSize = typedArray.getDimension(R.styleable.FailView_failTextSize, density * 16);
        radius = typedArray.getDimension(R.styleable.FailView_failRadius, density * 30);
        typedArray.recycle();
        setBackgroundColor(bacColor);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        paint = new Paint();
        textR = new Rect();
        touchRect = new Rect();

        setVisibility(View.GONE);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    private boolean isFocus;

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (width == 0) {
            width = getWidth();
            height = getHeight();
        }
        top = height / 5;

        textTop = 0;
        if (currentMode == MODE_REFRESH) {
            paint.reset();
            paint.setStyle(Paint.Style.STROKE);
            paint.setStrokeJoin(Paint.Join.ROUND);
            paint.setStrokeCap(Paint.Cap.ROUND);
            paint.setAntiAlias(true);
            paint.setStrokeWidth(density * 5);
            paint.setColor(failCircleBacColor);
            paint.setAlpha(120);
            canvas.drawCircle(width / 2, top + radius, radius, paint);

            paint.setColor(failCircleColor);
            if (circleRect == null) {
                circleRect = new RectF();                     //RectF对象
                circleRect.left = width / 2 - radius;                              //左边
                circleRect.top = top;                                   //上边
                circleRect.right = width / 2 + radius;                             //右边
                circleRect.bottom = top + 2 * radius;
            }//下边
            canvas.drawArc(circleRect, startRadius, failCircleRadius, false, paint);    //绘制圆弧


            textTop = (int) (circleRect.bottom + density * 40);

        } else if (bitmap != null) {
            canvas.drawBitmap(bitmap, width / 2 - bitmap.getWidth() / 2, top, null);
            textTop = (int) (top + bitmap.getHeight() + density * 40);

        }
        paint.reset();
        if (isFocus) {
            paint.setColor(textfocusColor);
        } else {
            paint.setColor(textnormalColor);
        }
        paint.setStrokeWidth(1);
        paint.setStyle(Paint.Style.FILL_AND_STROKE);
        paint.setStrokeJoin(Paint.Join.ROUND);
        paint.setStrokeCap(Paint.Cap.ROUND);
        paint.setTextSize(textSize);
        paint.setAntiAlias(true);
        paint.getTextBounds(text, 0, text.length(), textR);
        canvas.drawText(text, width / 2 - textR.width() / 2, textTop, paint);
        //touchRect
        if (currentMode != MODE_REFRESH) {
            touchRect.top = top;
            touchRect.bottom = (int) (textTop + 10 * density+textR.height());
            Log.i(TAG, "onDraw: " + textR.height());
            touchRect.left = Math.max(width / 2 - bitmap.getWidth() / 2, width / 2 - textR.width() / 2);
            touchRect.right = Math.max(width / 2 + bitmap.getWidth() / 2, width / 2 + textR.width() / 2);
        }

    }

    /**
     * which mode
     *
     * @param currentMode
     */
    public void setMode(int currentMode) {
        if (currentMode == MODE_SUCCESS) {
            setVisibility(View.GONE);
        } else {
            setVisibility(View.VISIBLE);
        }
        this.currentMode = currentMode;
        switch (currentMode) {
            case MODE_REFRESH:
                if (animator == null) {
                    animator = ValueAnimator.ofInt(-90, 270);
                    animator.setDuration(failCircleDuration);
                    animator.setInterpolator(new LinearInterpolator());
                    animator.setRepeatCount(Integer.MAX_VALUE);
                    animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                        @Override
                        public void onAnimationUpdate(ValueAnimator valueAnimator) {
                            startRadius = (int) valueAnimator.getAnimatedValue();
                            postInvalidate();
                        }
                    });
                }
                animator.start();
                break;
            default:
                if (animator != null && animator.isRunning()) {
                    animator.end();
                }
                invalidate();
                break;
        }

        switch (currentMode) {
            case MODE_SUCCESS:
                bitmap = null;
                break;
            case MODE_REFRESH:
                text = REFRESHING;
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

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int x = (int) event.getX();
        int y = (int) event.getY();
        if (touchRect.contains(x, y)) {
            switch (currentMode) {
                case MODE_REFRESH:
                    break;
                case MODE_FAIL:
                case MODE_NONET:
                case MODE_CRY:
                    switch (event.getAction()) {
                        case MotionEvent.ACTION_DOWN:
                            isFocus = true;
                            break;
                        case MotionEvent.ACTION_UP:
                            isFocus = false;
                            postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    if (listener != null) {
                                        setMode(MODE_REFRESH);
                                        listener.onClick();
                                    }
                                }
                            }, 100);
                            break;
                    }
                    break;
            }
            invalidate();
        }
        return true;
    }

    public void setText(String text) {
        this.text = text;
        postInvalidate();
    }

    public void setListener(FailViewListener listener) {
        this.listener = listener;
    }

    public interface FailViewListener {
        void onClick();
    }

}
