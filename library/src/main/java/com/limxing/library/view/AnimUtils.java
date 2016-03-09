package com.limxing.library.view;

import android.animation.ValueAnimator;
import android.view.View;

/**
 * Created by limxing on 16/3/7.
 *
 */
public class AnimUtils {


    /**
     * X移动动画,属性动画
     * @param view 需要移动的控件
     * @param b 展示或者返回动画操作
     * @param distance 移动的距离
     * 真实的修改了控件的位置
     */
    public static void translationX(final View view, boolean b, int distance) {
        ValueAnimator animator = null;
        if (b) {
            animator = ValueAnimator.ofFloat(0,distance);
        } else {
            animator = ValueAnimator.ofFloat(distance,0);
        }
        animator.setTarget(view);
        animator.setDuration(500).start();
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                view.setTranslationX((Float) animation.getAnimatedValue());
            }

        });
    }

    /**
     * X移动动画,属性动画
     * @param view 需要移动的控件
     * @param b 展示或者返回动画操作
     * @param distance 移动的距离
     * 真实的修改了控件的位置
     */
    public static void tr(final View view, boolean b, int distance) {
        ValueAnimator animator = null;
        if (b) {
            animator = ValueAnimator.ofFloat(0,distance);
        } else {
            animator = ValueAnimator.ofFloat(distance,0);
        }
        animator.setTarget(view);
        animator.setDuration(500).start();
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                view.setTranslationX((Float) animation.getAnimatedValue());
            }

        });
    }
}
