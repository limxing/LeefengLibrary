package com.limxing.library.IOSLoading;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.limxing.library.R;

/**
 * Created by limxing on 16/1/7.
 */
public class LoadingView extends ImageView {
    public LoadingView(Context context) {
        super(context);
        init();
    }

    public LoadingView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public LoadingView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        setImageResource(R.drawable.loading);
        AnimationDrawable animationDrawable = (AnimationDrawable)getDrawable();
        animationDrawable.start();
    }


}
