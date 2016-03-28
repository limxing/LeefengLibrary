package com.limxing.library.SlidMenu;

import android.content.Context;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Scroller;

import com.limxing.library.utils.LogUtils;

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
        //获取屏幕的宽和高
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
    //保证Scroller自动滑动
    @Override
    public void computeScroll() {
        if (mScroller.computeScrollOffset()){
            scrollTo(mScroller.getCurrX(), mScroller.getCurrY());
            mMenu.setTranslationX(2 * (mMenuWidth + getScrollX()) / 3);//位移差的滑动
            invalidate();
        }
    }

    @Override
    protected void onLayout(boolean b, int i, int i1, int i2, int i3) {
        LogUtils.i("onLayout");
        mMenu.layout(-mMenuWidth, 0, 0, mScreenHeight);
        mContent.layout(0, 0, mScreenWidth, mScreenHeight);
    }

    //拦截事件分发,不让子view得到触摸事件
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
                if (Math.abs(deltaX) > Math.abs(deltaY)){//横向滑动
                    intercept = true;
                }else{//纵向滑动
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
//处理touch事件
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
                //拿到x方向的偏移量
                int dx = currentX - mLastX;
                if (dx < 0) {//向左滑动
                    //边界控制，如果Menu已经完全显示，再滑动的话
                    //Menu左侧就会出现白边了,进行边界控制
                    if (getScrollX() + Math.abs(dx) >= 0) {
                        //直接移动到（0，0）位置，不会出现白边
                        scrollTo(0, 0);

                    } else {//Menu没有完全显示呢
                        //其实这里dx还是-dx，大家不用刻意去记
                        //大家可以先使用dx，然后运行一下，发现
                        //移动的方向是相反的，那么果断这里加个负号就可以了
                        scrollBy(-dx, 0);

                    }

                } else {//向右滑动
                    //边界控制，如果Content已经完全显示，再滑动的话
                    //Content右侧就会出现白边了，进行边界控制
                    if (getScrollX() - dx <= -mMenuWidth) {
                        //直接移动到（-mMenuWidth,0）位置，不会出现白边
                        scrollTo(-mMenuWidth, 0);

                    } else {//Content没有完全显示呢
                        //根据手指移动
                        scrollBy(-dx, 0);

                    }

                }
                mLastX = currentX;
                mLastY = currentY;
                mMenu.setTranslationX(2 * (mMenuWidth + getScrollX()) / 3);//位移差的滑动

                break;
            case MotionEvent.ACTION_UP:
                if (getScrollX() < -mMenuWidth / 2){//打开Menu
                    //调用startScroll方法，第一个参数是起始X坐标，第二个参数
                    //是起始Y坐标，第三个参数是X方向偏移量，第四个参数是Y方向偏移量
                    mScroller.startScroll(getScrollX(), 0, -mMenuWidth - getScrollX(), 0, 300);
                    //设置一个已经打开的标识，当实现点击开关自动打开关闭功能时会用到
                    isOpen = true;
                    //一定不要忘了调用这个方法重绘，否则没有动画效果
                    invalidate();
                }else{//关闭Menu
                    //同上
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
     * 点击开关，开闭Menu，如果当前menu已经打开，则关闭，如果当前menu已经关闭，则打开
     */
    public void toggleMenu(){
        if (isOpen){
            closeMenu();
        }else{
            openMenu();
        }
    }

    /**
     * 关闭menu
     */
    private void closeMenu() {
        //也是使用startScroll方法，dx和dy的计算方法一样
        mScroller.startScroll(getScrollX(),0,-getScrollX(),0,500);
        invalidate();
        isOpen = false;
    }

    /**
     * 打开menu
     */
    private void openMenu() {
        mScroller.startScroll(getScrollX(),0,-mMenuWidth-getScrollX(),0,500);
        invalidate();
        isOpen = true;
    }


    @Override
    public void actionRight() {
        LogUtils.i("向右手势");
        openMenu();
    }

    @Override
    public void actionLeft() {
        LogUtils.i("向左手势");
        closeMenu();
    }
}
