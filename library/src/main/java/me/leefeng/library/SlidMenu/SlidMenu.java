package me.leefeng.library.SlidMenu;

import android.content.Context;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Scroller;

import me.leefeng.library.utils.LogUtils;

/**
 * Created by limxing on 16/3/27.
 */
public class SlidMenu extends ViewGroup implements MyGestureActionListener{
    private View mContent;
    private int mContentWidth;
    private int mScreenWidth;
    private int mScreenHeight;
    private View mMenu;
    private int mMenuWidth;
    private int mLastX;
    private int mLastY;
    private Scroller mScroller;
    private boolean isOpen;
    private int mLastXIntercept;
    private int mLastYIntercept;
    private GestureDetector listener;

    public SlidMenu(Context context) {
        super(context);
        init(context);
    }

    private void init(Context context) {
        LogUtils.i("init");
        DisplayMetrics metrics = new DisplayMetrics();
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        wm.getDefaultDisplay().getMetrics(metrics);
        mScreenWidth = metrics.widthPixels;
        mScreenHeight = metrics.heightPixels;

        mScroller = new Scroller(context);
        MyGestureListener gestureListener= new MyGestureListener();
        gestureListener.setListener(this);
        listener = new GestureDetector(context,gestureListener);

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        LogUtils.i("onMeasure");
        mMenu = getChildAt(0);
        measureChild(mMenu, widthMeasureSpec, heightMeasureSpec);
        mMenuWidth = mMenu.getLayoutParams().width =mScreenWidth;
        mContent = getChildAt(1);
        mContentWidth = mContent.getLayoutParams().width = mScreenWidth;
        measureChild(mContent, widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(mScreenWidth, mScreenHeight);

    }
    @Override
    public void computeScroll() {
        if (mScroller.computeScrollOffset()){
            scrollTo(mScroller.getCurrX(), mScroller.getCurrY());
            mMenu.setTranslationX(2 * (mMenuWidth + getScrollX()) / 3);//
            invalidate();
        }
    }

    @Override
    protected void onLayout(boolean b, int i, int i1, int i2, int i3) {
        LogUtils.i("onLayout");
        mMenu.layout(-mMenuWidth, 0, 0, mScreenHeight);
        mContent.layout(0, 0, mScreenWidth, mScreenHeight);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        LogUtils.i("onInterceptTouchEvent");
        boolean intercept = false;
        int x = (int) ev.getX();
        int y = (int) ev.getY();
        switch (ev.getAction()){
            case MotionEvent.ACTION_DOWN:
                intercept = false;
                break;
            case MotionEvent.ACTION_MOVE:
                int deltaX = (int) ev.getX() - mLastXIntercept;
                int deltaY = (int) ev.getY() - mLastYIntercept;
                if (Math.abs(deltaX) > Math.abs(deltaY)){//
                    intercept = true;
                }else{//
                    intercept = false;
                }
                break;
            case MotionEvent.ACTION_UP:
                intercept = false;
                break;
        }
        mLastX = x;
        mLastY = y;
        mLastXIntercept = x;
        mLastYIntercept = y;
        if(isOpen&&x>mMenuWidth){
            intercept = true;
        }
        return intercept;
    }
    @Override
    public boolean onTouchEvent(MotionEvent event) {
//        LogUtils.i("onTouchEvent");

        int action = event.getAction();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                mLastX = (int) event.getX();
                mLastY = (int) event.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                int currentX = (int) event.getX();
                int currentY = (int) event.getY();
                int dx = currentX - mLastX;
                if (dx < 0) {//
                    if (getScrollX() + Math.abs(dx) >= 0) {
                        scrollTo(0, 0);

                    } else {//
                        scrollBy(-dx, 0);

                    }

                } else {
                    if (getScrollX() - dx <= -mMenuWidth) {
                        scrollTo(-mMenuWidth, 0);

                    } else {
                        scrollBy(-dx, 0);

                    }

                }
                mLastX = currentX;
                mLastY = currentY;
                mMenu.setTranslationX(2 * (mMenuWidth + getScrollX()) / 3);

                break;
            case MotionEvent.ACTION_UP:
                if (getScrollX() < -mMenuWidth / 2){
                    mScroller.startScroll(getScrollX(), 0, -mMenuWidth - getScrollX(), 0, 300);
                    isOpen = true;
                    invalidate();
                }else{
                    mScroller.startScroll(getScrollX(), 0, -getScrollX(), 0, 300);
                    isOpen = false;
                    invalidate();
                }

                break;

        }
        listener.onTouchEvent(event);
        return true;
    }

    public SlidMenu(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public SlidMenu(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    /**
     */
    public void toggleMenu(){
        if (isOpen){
            closeMenu();
        }else{
            openMenu();
        }
    }

    /**
     */
    private void closeMenu() {
        mScroller.startScroll(getScrollX(),0,-getScrollX(),0,500);
        invalidate();
        isOpen = false;
    }

    /**
     */
    private void openMenu() {
        mScroller.startScroll(getScrollX(),0,-mMenuWidth-getScrollX(),0,500);
        invalidate();
        isOpen = true;
    }


    @Override
    public void actionRight() {
        openMenu();
    }

    @Override
    public void actionLeft() {
        closeMenu();
    }
}
