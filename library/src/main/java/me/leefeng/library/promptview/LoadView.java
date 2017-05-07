package me.leefeng.library.promptview;

import android.animation.ValueAnimator;
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
public class LoadView extends ImageView {

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
    private RectF leftTopRect;
    private RectF rightTopRect;
    private RectF roundRect;
    private float round;
    private boolean touchAble=true;

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

    @Override
    protected void onDraw(Canvas canvas) {
        if (canvasWidth == 0) {
            canvasWidth = getResources().getDisplayMetrics().widthPixels;
            canvasHeight = getResources().getDisplayMetrics().heightPixels;
        }
        paint.reset();
        paint.setAntiAlias(true);
        paint.setColor(Color.BLACK);
        paint.setAlpha(90);
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
        paint.setColor(Color.WHITE);
        paint.setStrokeWidth(1 * density);
        paint.setTextSize(density * 16);
        paint.setAntiAlias(true);
        paint.getTextBounds(text, 0, text.length(), textRect);

        float popWidth = Math.max(100 * density, textRect.width() + pad * 2);
        float popHeight = textRect.height() + 3 * pad + height * 2;


        float top = canvasHeight / 2 - height * 3 - pad;
        float left = canvasWidth / 2 - popWidth / 2;

        canvas.translate(left, top);
        paint.reset();
        paint.setAntiAlias(true);
        paint.setColor(Color.BLACK);
        paint.setAlpha(120);

//        canvas.drawRect(0, 0, popWidth, popHeight, paint);


//        paint.setColor(Color.RED);

//        if (leftTopRect == null)
//            leftTopRect = new RectF(0, 0, pad, pad);
//        canvas.drawArc(leftTopRect, 0, 90, false, paint);    //绘制圆弧
//        if (rightTopRect == null)
//            rightTopRect = new RectF(popWidth, 0, pad, pad);
//        canvas.drawArc(leftTopRect, 90, 180, false, paint);    //绘制圆弧
//        if (leftBottomRect)
        if (roundRect == null)
            roundRect = new RectF(0, 0, popWidth, popHeight);
        roundRect.set(0, 0, popWidth, popHeight);
        canvas.drawRoundRect(roundRect, round, round, paint);


        paint.reset();
        paint.setColor(Color.WHITE);
        paint.setStrokeWidth(1 * density);
        paint.setTextSize(density * 16);
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
        pad = 15 * density;
        round = 10 * density;
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
            drawable = getDrawable();
            if (drawable == null) {
                drawable = getResources().getDrawable(R.drawable.svpload);
                setImageDrawable(drawable);
            }

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

    private void start() {
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
        animator.start();
    }


    public void setText(String text) {
        this.text = text;
        invalidate();
    }

//    @Override
//    public boolean onTouchEvent(MotionEvent event) {
//        return !touchAble;
//    }

    public void setTouchAble(boolean touchAble) {
        this.touchAble = touchAble;
    }

    public void setSuccess(String success) {

        if (animator.isRunning())
            animator.end();
        setImageDrawable(getResources().getDrawable(R.drawable.ic_prompt_success));
        setText(success);
    }

    public void setError(String error) {

        if (animator.isRunning())
            animator.end();
        setImageDrawable(getResources().getDrawable(R.drawable.ic_prompt_error));
        setText(error);
    }

    public void setLoading(String loading) {

        setImageDrawable(drawable);
        start();
        setText(loading);
    }
}