package me.leefeng.library.promptview;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.graphics.Color;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import me.leefeng.library.R;

/**
 * Created by limxing on 2017/5/7.
 */

public class PromptView {
    //    private static final int PROMPT_SUCCESS = 101;
//    private static final int PROMPT_LOADING = 102;
//    private static final int PROMPT_ERROR = 103;
//    private static final int PROMPT_NONE = 104;
//    private static final int PROMPT_INFO = 105;
//    private static final int PROMPT_WARN = 106;
    private static final String TAG = "PromptView";
    private int currentType;
    private Animation outAnim;
    private Animation inAnim;
    private LoadView loadView;
    //    private Context context;
    private ViewGroup decorView;
    private ValueAnimator dissmissAnim;
    private boolean dissmissAnimCancle;

    public PromptView(Activity context) {
//        this.context = context;
//        LayoutInflater layoutInflater = LayoutInflater.from(context);
        this(new Builder(), context);
    }

    public Builder getBuilder() {
        return loadView.getBuilder();
    }

    public PromptView(Builder builder, Activity context) {
        decorView = (ViewGroup) context.getWindow().getDecorView().findViewById(android.R.id.content);
        inAnim = AnimationUtils.loadAnimation(context, R.anim.promptview_in);
        outAnim = AnimationUtils.loadAnimation(context, R.anim.promptview_out);
        loadView = new LoadView(context, builder);
    }


    public PromptView showLoading(String msg) {
        checkLoadView();
        if (currentType != LoadView.PROMPT_LOADING) {
            currentType = LoadView.PROMPT_LOADING;
            loadView.setLoading(msg);
        } else {
            loadView.setText(msg);
        }
        return this;

    }


    /**
     * close
     */
    public void dismiss() {
        if (loadView.getParent() != null && currentType != LoadView.PROMPT_LOADING) {
            if (getBuilder().withAnim) {
                loadView.startAnimation(outAnim);
                outAnim.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        decorView.removeView(loadView);
                        currentType = LoadView.PROMPT_NONE;
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });
            } else {
                decorView.removeView(loadView);
            }

        }

    }

    public void showError(String msg) {
        showSomthing(LoadView.PROMPT_ERROR, msg);

    }

    public void showInfo(String msg) {
        showSomthing(LoadView.PROMPT_INFO, msg);
    }

    public void showWarn(String msg) {
        showSomthing(LoadView.PROMPT_WARN, msg);
    }

    public void showSuccess(String msg) {

        showSomthing(LoadView.PROMPT_SUCCESS, msg);
    }

    private void showSomthing(int promptError, String msg) {
        checkLoadView();
        if (loadView.getParent() != null && currentType != promptError) {
            currentType = promptError;
            loadView.showSomthing(currentType, msg);
            dissmissAni();
        }
    }

    private void checkLoadView() {
        if (loadView.getParent() == null) {
            decorView.addView(loadView);
            if (loadView.getBuilder().withAnim)
                loadView.startAnimation(inAnim);
        }
    }

    /**
     * 消失的动画
     */
    private void dissmissAni() {
        if (dissmissAnim == null) {
            dissmissAnim = ValueAnimator.ofInt(0, 1);
            dissmissAnim.setDuration(getBuilder().stayDuration);
            dissmissAnim.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {

                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    if (!dissmissAnimCancle) {
                        dismiss();
                    }
                }

                @Override
                public void onAnimationCancel(Animator animation) {

                }

                @Override
                public void onAnimationRepeat(Animator animation) {

                }
            });
        } else if (dissmissAnim.isRunning()) {
            dissmissAnimCancle = true;
            dissmissAnim.end();

        }
        dissmissAnim.start();
        dissmissAnimCancle = false;
    }


}
