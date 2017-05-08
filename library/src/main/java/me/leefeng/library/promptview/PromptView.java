package me.leefeng.library.promptview;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;


/**
 * Created by limxing on 2017/5/7.
 */

public class PromptView {
    private static final String TAG = "PromptView";

    private InputMethodManager inputmanger;
    private int currentType;
    private AnimationSet outAnim;
    private AnimationSet inAnim;
    private LoadView loadView;
    private ViewGroup decorView;
    private ValueAnimator dissmissAnim;
    private boolean dissmissAnimCancle;
    private boolean outAnimRunning;

    public void onDetach() {
//        inputmanger=null;
//        loadView=null;
//        decorView=null;
//        dissmissAnim=null;
//        context.getWindowManager().
    }

    public PromptView(Activity context) {
//        this.context = context;
//        LayoutInflater layoutInflater = LayoutInflater.from(context);
        this(Builder.getDefaultBuilder(), context);
    }

    public Builder getBuilder() {
        return loadView.getBuilder();
    }

    public PromptView(Builder builder, Activity context) {
        decorView = (ViewGroup) context.getWindow().getDecorView().findViewById(android.R.id.content);
        initAnim(context.getResources().getDisplayMetrics().widthPixels, context.getResources().getDisplayMetrics().heightPixels);
//        inAnim = AnimationUtils.loadAnimation(context, R.anim.promptview_in);
//        outAnim = AnimationUtils.loadAnimation(context, R.anim.promptview_out);
        loadView = new LoadView(context, builder, this);
        inputmanger = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
//        window=context.getWindow();

//        m = new TouchView(context);
//        m.setBackgroundColor(Color.BLUE);
//        WindowManager.LayoutParams wl = new WindowManager.LayoutParams();
//        wl.height = 10;
//        wl.width = 10;
//        wl.format = PixelFormat.TRANSLUCENT;// 支持透明
//        windowManager = context.getWindowManager();
//        m.setVisibility(View.GONE);
//        windowManager.addView(m, wl);


    }

    private void initAnim(int widthPixels, int heightPixels) {
        inAnim = new AnimationSet(true);
        ScaleAnimation scaleAnimation = new ScaleAnimation(2, 1f, 2,
                1f, widthPixels * 0.5f, heightPixels * 0.45f);
//        scaleAnimation.setDuration(200);
        inAnim.addAnimation(scaleAnimation);

        AlphaAnimation alphaAnimation = new AlphaAnimation(0, 1);
        inAnim.addAnimation(alphaAnimation);
//        scaleAnimation = new ScaleAnimation(0.8f, 1, 0.8f,
//                1, widthPixels * 0.5f, heightPixels * 0.45f);
//        scaleAnimation.setDuration(100);
//        inAnim.addAnimation(scaleAnimation);

        inAnim.setDuration(300);

        outAnim = new AnimationSet(true);
        scaleAnimation = new ScaleAnimation(1, 2, 1,
                2, widthPixels * 0.5f, heightPixels * 0.45f);
        alphaAnimation = new AlphaAnimation(1, 0);
        alphaAnimation.setDuration(200);
        outAnim.addAnimation(scaleAnimation);
        outAnim.addAnimation(alphaAnimation);
        outAnim.setDuration(300);


    }


    /**
     * close
     */
    public void dismiss() {

        if (loadView.getParent() != null && currentType != LoadView.PROMPT_LOADING && !outAnimRunning) {
            if (getBuilder().withAnim) {
//                outAnim.setStartOffset(delayTime);
                loadView.startAnimation(outAnim);
                outAnim.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {
                        outAnimRunning = true;
                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
//                        windowManager.removeView(loadView);
                        decorView.removeView(loadView);
                        currentType = LoadView.PROMPT_NONE;
                        outAnimRunning = false;
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });
            } else {
//                windowManager.removeView(loadView);
                decorView.removeView(loadView);
                currentType = LoadView.PROMPT_NONE;
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
        closeInput();
        checkLoadView();
        if (loadView.getParent() != null && currentType != promptError) {
            loadView.setBuilder(Builder.getDefaultBuilder());
            currentType = promptError;
            loadView.showSomthing(currentType, msg);
            dissmissAni();
//            dismiss(getBuilder().stayDuration);
        }
    }

    public void showWarnAlert(String text, PromptButton... button) {

        closeInput();
        checkLoadView();
        if (currentType != LoadView.PROMPT_ALERT_WARN) {
            loadView.setBuilder(Builder.getAlertDefaultBuilder());
            currentType = LoadView.PROMPT_ALERT_WARN;
            loadView.showSomthingAlert(currentType, text, button);
            if (dissmissAnim != null && dissmissAnim.isRunning()) {
                dissmissAnimCancle = true;
                dissmissAnim.end();
            }
        } else {
            loadView.setText(text);
        }

    }

    public void showLoading(String msg) {
        closeInput();
        checkLoadView();
        if (currentType != LoadView.PROMPT_LOADING) {
            currentType = LoadView.PROMPT_LOADING;
            loadView.setLoading(msg);

        } else {
            loadView.setText(msg);
        }

    }

    private void checkLoadView() {
        if (loadView.getParent() == null) {
//            windowManager.addView(loadView,wl);
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

    protected void closeInput() {
        if (loadView != null) {
            inputmanger.hideSoftInputFromWindow(loadView.getWindowToken(), 0);
        }
//        window.closeAllPanels();
    }

}
