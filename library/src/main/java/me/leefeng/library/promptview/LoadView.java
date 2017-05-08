package me.leefeng.library.promptview;

import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;

import me.leefeng.library.R;

/**
 * Created by limxing on 16/1/7.
 */
class LoadView extends ImageView {
    public static final int PROMPT_SUCCESS = 101;
    public static final int PROMPT_LOADING = 102;
    public static final int PROMPT_ERROR = 103;
    public static final int PROMPT_NONE = 104;
    public static final int PROMPT_INFO = 105;
    public static final int PROMPT_WARN = 106;
    private  Builder builder;
    private int width;
    private int height;
    private ValueAnimator animator;
    private Paint paint;
    private float density;
    private Rect textRect;
    private String text = "加载中...";
    private int canvasWidth;
    private int canvasHeight;

    private float pad;
    private RectF roundRect;
    private float round;

    private Drawable drawable;//loadingdrawable

    public LoadView(Context context) {
        super(context);
    }

    public LoadView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public LoadView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public LoadView(Activity context, Builder builder) {
        super(context);
        this.builder = builder;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (canvasWidth == 0) {
            canvasWidth = getResources().getDisplayMetrics().widthPixels;
            canvasHeight = getResources().getDisplayMetrics().heightPixels;
        }
        paint.reset();
        paint.setAntiAlias(true);
        paint.setColor(builder.backColor);
        paint.setAlpha(builder.backAlpha);
        canvas.drawRect(0, 0, canvasWidth, canvasHeight, paint);
//        paint.reset();
//        paint.setAntiAlias(true);
//        paint.setColor(Color.RED);
//        paint.setStrokeWidth(2);
//        paint.setStyle(Paint.Style.STROKE);
//
//        canvas.drawRect(0, 0, canvasWidth / 2, canvasHeight / 2, paint);

        /**
         * 计算文字的宽度确定总宽
         */
        paint.reset();
        paint.setColor(builder.textColor);
        paint.setStrokeWidth(1 * density);
        paint.setTextSize(density * builder.textSize);
        paint.setAntiAlias(true);
        paint.getTextBounds(text, 0, text.length(), textRect);

        float popWidth = Math.max(100 * density, textRect.width() + pad * 2);
        float popHeight = textRect.height() + 3 * pad + height * 2;


        float top = canvasHeight / 2 - height * 3 - pad;
        float left = canvasWidth / 2 - popWidth / 2;

        canvas.translate(left, top);
        paint.reset();
        paint.setAntiAlias(true);
        paint.setColor(builder.roundColor);
        paint.setAlpha(builder.roundAlpha);

        if (roundRect == null)
            roundRect = new RectF(0, 0, popWidth, popHeight);
        roundRect.set(0, 0, popWidth, popHeight);
        canvas.drawRoundRect(roundRect, round, round, paint);


        paint.reset();
        paint.setColor(builder.textColor);
        paint.setStrokeWidth(1 * density);
        paint.setTextSize(density * builder.textSize);
        paint.setAntiAlias(true);

        top = pad * 2 + height * 2 + textRect.height();
        left = popWidth / 2 - textRect.width() / 2;
        canvas.drawText(text, left, top, paint);


        canvas.translate(popWidth / 2 - width, pad);
        super.onDraw(canvas);
//        canvas.translate(0, 0);
//        paint.reset();
//        paint.setAntiAlias(true);
//        paint.setColor(Color.RED);
//        paint.setStrokeWidth(2);
//        paint.setStyle(Paint.Style.STROKE);
//        canvas.drawRect(0, 0, width * 2, height * 2, paint);

    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();

        init();
        initData();
    }

    private void initData() {
        textRect = new Rect();
        density = getResources().getDisplayMetrics().density;
        pad = builder.padding * density;
        round = builder.round * density;
    }

    /**
     */
    public void setDrawable(Drawable drawable) {
        setImageDrawable(drawable);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (animator != null)
            animator.cancel();
        animator = null;
//        runnable.stopload();
        textRect = null;
        drawable = null;
        Log.i("leefeng", "onDetachedFromWindow: ");

    }

    private void initLoadingDrawable() {
        if (drawable == null) {
//            drawable = getDrawable();
//            if (drawable == null) {
            drawable = getResources().getDrawable(R.drawable.svpload);
            setImageDrawable(drawable);
//            }

            measure(0, 0);

            width = getMeasuredWidth() / 2;
            height = getMeasuredHeight() / 2;
        }
    }

    private void init() {
        setScaleType(ScaleType.MATRIX);
        initLoadingDrawable();
        measure(0, 0);

        paint = new Paint();
    }

    private Matrix max;

    /**
     * 加载旋转动画
     */
    private void start() {
        if (max == null || animator == null) {
            max = new Matrix();
            animator = ValueAnimator.ofInt(0, 12);
            animator.setDuration(12 * 80);
            animator.setInterpolator(new LinearInterpolator());
            animator.setRepeatCount(Integer.MAX_VALUE);
            animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator valueAnimator) {
                    float degrees = 30 * (Integer) valueAnimator.getAnimatedValue();
                    max.setRotate(degrees, width, height);
                    setImageMatrix(max);
                }
            });
        }
        animator.start();
    }


    public void setText(String text) {
        this.text = text;
        invalidate();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return !builder.touchAble;
    }


    /**
     * 停止旋转
     */
    private void endAnimator() {
        if (animator != null && animator.isRunning()) {
            animator.end();
            max.setRotate(0, width, height);
            setImageMatrix(max);
        }
    }

    public void setLoading(String loading) {
        setImageDrawable(drawable);
        start();
        setText(loading);
    }

    public Builder getBuilder() {
        return builder;
    }

    public void showSomthing(int currentType, String msg) {
        endAnimator();
        int drawableId=R.drawable.ic_prompt_success;
        switch (currentType){
            case PROMPT_SUCCESS:
                drawableId=R.drawable.ic_prompt_success;
                break;
            case PROMPT_ERROR:
                drawableId=R.drawable.ic_prompt_error;
                break;
            case PROMPT_INFO:
                drawableId=R.drawable.ic_prompt_info;
                break;
            case PROMPT_WARN:
                drawableId=R.drawable.ic_prompt_warn;
                break;
        }
        setImageDrawable(getResources().getDrawable(drawableId));
        setText(msg);
    }
}