package com.limxing.library.IOSLoading;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;

import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.limxing.library.R;

import java.lang.ref.SoftReference;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by limxing on 16/1/7.
 */
public class LoadView extends ImageView {
    private MyRunable runnable;
    private int width;
    private int height;

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
        runnable.startload();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        runnable.stopload();
        runnable = null;
    }

    private void init() {
        setScaleType(ScaleType.MATRIX);
        Drawable drawable= getDrawable();
        if (drawable==null) {
            Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.svpload);
            setImageBitmap(bitmap);
            width = bitmap.getWidth() / 2;
            height = bitmap.getHeight() / 2;
        }else{
            measure(0,0);
            width = getMeasuredWidth() / 2;
            height = getMeasuredHeight() / 2;
        }
        runnable = new MyRunable(this);
    }

//    public void startLoad() {
//        if (runnable != null) {
//            runnable.startload();
//        }
//    }
//
//    public void stopLoad() {
//        if (runnable != null) {
//            runnable.stopload();
//        }
//    }

    static class MyRunable implements Runnable {
        private boolean flag;
        private SoftReference<LoadView> loadingViewSoftReference;
        private float degrees = 0f;
        private Matrix max;

        public MyRunable(LoadView loadingView) {
            loadingViewSoftReference = new SoftReference<LoadView>(loadingView);
            max = new Matrix();
        }

        @Override
        public void run() {
            if (loadingViewSoftReference.get().runnable != null && max != null) {
                degrees += 30f;
                max.setRotate(degrees, loadingViewSoftReference.get().width, loadingViewSoftReference.get().height);
                loadingViewSoftReference.get().setImageMatrix(max);
                if (degrees == 360) {
                    degrees = 0;
                }
                if (flag) {
                    loadingViewSoftReference.get().postDelayed(loadingViewSoftReference.get().runnable, 80);
                }
            }
        }

        public void stopload() {
            flag = false;
        }

        public void startload() {
            flag = true;
            if (loadingViewSoftReference.get().runnable != null && max != null) {
                loadingViewSoftReference.get().postDelayed(loadingViewSoftReference.get().runnable, 80);
            }
        }
    }
}