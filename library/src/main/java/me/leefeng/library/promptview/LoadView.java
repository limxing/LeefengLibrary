package me.leefeng.library.promptview;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
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
        float popHeight = textRect.height() + 3 * pad+height*2;


        float top = canvasHeight / 2 - height * 2 - pad;
        float left = canvasWidth / 2 - popWidth / 2;

        canvas.translate(left, top);
        paint.reset();
        paint.setAntiAlias(true);
        paint.setColor(Color.BLACK);
        paint.setAlpha(160);


        canvas.drawRect(0, 0, popWidth, popHeight, paint);


        paint.reset();
        paint.setColor(Color.WHITE);
        paint.setStrokeWidth(1 * density);
        paint.setTextSize(density * 16);
        paint.setAntiAlias(true);

        top = pad * 2 + height * 2 + textRect.height();
        left=popWidth/2-textRect.width()/2;
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
        pad = 10 * density;
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

        Log.i("leefeng", "onDetachedFromWindow: ");

    }

    private void init() {
        setScaleType(ScaleType.MATRIX);
        Drawable drawable = getDrawable();
        if (drawable == null) {
            drawable = getResources().getDrawable(R.drawable.svpload);
            setImageDrawable(drawable);
        }
        measure(0, 0);

        width = getMeasuredWidth() / 2;
        height = getMeasuredHeight() / 2;
        start();

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

//    static class MyRunable implements Runnable {
//        private boolean flag;
//        private SoftReference<LoadView> loadingViewSoftReference;
//        private float degrees = 0f;
//        private Matrix max;
//
//        public MyRunable(LoadView loadingView) {
//            loadingViewSoftReference = new SoftReference<LoadView>(loadingView);
//            max = new Matrix();
//        }
//
//        @Override
//        public void run() {
//            if (loadingViewSoftReference.get().runnable != null && max != null) {
//                degrees += 30f;
//                max.setRotate(degrees, loadingViewSoftReference.get().width, loadingViewSoftReference.get().height);
//                loadingViewSoftReference.get().setImageMatrix(max);
//                if (degrees == 360) {
//                    degrees = 0;
//                }
//                if (flag) {
//                    loadingViewSoftReference.get().postDelayed(loadingViewSoftReference.get().runnable, 80);
//                } else {
//                    max.setRotate(0, loadingViewSoftReference.get().width, loadingViewSoftReference.get().height);
//                    loadingViewSoftReference.get().setImageMatrix(max);
//                }
//            }
//        }
//
//        public void stopload() {
//            flag = false;
//
//        }
//
//        public void startload() {
//            flag = true;
//            if (loadingViewSoftReference.get().runnable != null && max != null) {
//                loadingViewSoftReference.get().postDelayed(loadingViewSoftReference.get().runnable, 80);
//            }
//        }
//    }




    public void setText(String text) {
        this.text = text;
        invalidate();
    }
}