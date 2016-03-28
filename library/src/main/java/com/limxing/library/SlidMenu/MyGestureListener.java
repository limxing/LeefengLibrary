package com.limxing.library.SlidMenu;

import android.view.GestureDetector;
import android.view.MotionEvent;

import com.limxing.library.utils.LogUtils;

/**
 * Created by limxing on 16/3/28.
 */
public class MyGestureListener implements GestureDetector.OnGestureListener {

    private MyGestureActionListener listener;
    public void setListener(MyGestureActionListener listener){
        this.listener=listener;
    }

    @Override
    public boolean onDown(MotionEvent motionEvent) {
        LogUtils.i("onDown");
        return false;
    }

    @Override
    public void onShowPress(MotionEvent motionEvent) {
        LogUtils.i("onShowPress");
    }

    @Override
    public boolean onSingleTapUp(MotionEvent motionEvent) {
        LogUtils.i("onSingleTapUp");
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {
        LogUtils.i("onScroll"+v+"=="+v1);
        return false;
    }

    @Override
    public void onLongPress(MotionEvent motionEvent) {
        LogUtils.i("onLongPress");
    }


    /**
     * @param e1        手势起点的移动事件
     * @param e2        当前手势点的移动事件
     * @param velocityX 每秒x移动的距离,也就是速度
     * @param velocityY 每秒y移动的距离,也就是速度
     * @return
     */
    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        LogUtils.i("onFling==" + velocityX + "==" + velocityY);

        if (velocityX > 0 && Math.abs(velocityX) > 3000) {

            if(listener!=null){
                listener.actionRight();
            }
        }
        if (velocityX < 0 && Math.abs(velocityX) > 3000) {
            listener.actionLeft();
        }

        return false;
    }

}
