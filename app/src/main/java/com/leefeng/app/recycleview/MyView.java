package com.leefeng.app.recycleview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.Scroller;

/**
 * Created by limxing on 16/8/6.
 */
public class MyView extends View implements  View.OnClickListener {
    private static final int SCROLL_DURATION = 500;
    private Scroller mScroller;
    private static final int DOING=2;
    private static final int BIG=1;
    private static final int SMALL=0;
    private int state;

    public MyView(Context context) {
        super(context);
        init(context);
    }

    public MyView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public MyView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        mScroller = new Scroller(context, new AccelerateDecelerateInterpolator());
        setOnClickListener(this);
    }

    @Override
    public void computeScroll() {
        if (mScroller.computeScrollOffset()) {
            ViewGroup.LayoutParams para = getLayoutParams();
            para.height = mScroller.getCurrY();
           setLayoutParams(para);
            postInvalidate();
//            invokeOnScrolling();
        }

        super.computeScroll();
    }


    @Override
    public void onClick(View view) {
        if (mScroller.isFinished()) {
            switch (state) {
                case SMALL:
                    state = BIG;
                    mScroller.startScroll(0, getHeight(), 0, 400,
                            SCROLL_DURATION);
                    invalidate();
                    break;
                case BIG:
                    state = SMALL;
                    mScroller.startScroll(0, getHeight(), 0, -400,
                            SCROLL_DURATION);
                    invalidate();
                    break;
                default:
                    break;
            }
        }
    }
}
