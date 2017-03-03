package me.leefeng.library.view;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.view.View;

/**
 * Created by limxing on 16/3/7.
 */
public class AnimUtils {


    /**
     */
    public static void translationX(final View view, final boolean b, int distance) {
        ValueAnimator animator = null;
        if (b) {
            animator = ValueAnimator.ofFloat(0, distance);
        } else {
            animator = ValueAnimator.ofFloat(distance, 0);
        }
        animator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                if (b) {
                    view.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                if (!b) {
                    view.setVisibility(View.GONE);
                }
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        animator.setTarget(view);
        animator.setDuration(500).start();
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float f = (Float) animation.getAnimatedValue();
                view.setTranslationX(f);

            }

        });
    }
    /**
     */
    public static void translationY(final View view,int fromY, int toY) {
        ValueAnimator animator = null;
            animator = ValueAnimator.ofFloat(fromY, toY);
        animator.setTarget(view);
        animator.setDuration(500).start();
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float f = (Float) animation.getAnimatedValue();
                view.setTranslationY(f);

            }

        });
    }

    /**
     *
     * @param view
     * @param b
     */
    public static void animAlpha(final View view, final boolean b) {
        ValueAnimator animator = null;
        if (b) {
            animator = ValueAnimator.ofFloat(1, 0);
        } else {
            animator = ValueAnimator.ofFloat(0, 1);
        }
        animator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                if (!b) {
                    view.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                if (b) {
                    view.setVisibility(View.GONE);
                }
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        animator.setTarget(view);
        animator.setDuration(500).start();
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                view.setAlpha((Float) animation.getAnimatedValue());
            }
        });
    }

    /**
     */
    public static void tr(final View view, boolean b, int distance) {
        ValueAnimator animator = null;
        if (b) {
            animator = ValueAnimator.ofFloat(0, distance);
        } else {
            animator = ValueAnimator.ofFloat(distance, 0);
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
