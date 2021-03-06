package me.leefeng.library.IOSLoading;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Matrix;

import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
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
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        init();
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

}