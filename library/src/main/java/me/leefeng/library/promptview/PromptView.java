package me.leefeng.library.promptview;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.app.AlertDialog;
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

import me.leefeng.library.R;


/**
 * Created by limxing on 2017/5/7.
 */

public class PromptView {
    private static final String TAG = "PromptView";

    private InputMethodManager inputmanger;
    private int currentType;
    private Animation outAnim;
    private Animation inAnim;
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

//    public Builder getBuilder() {
//        return loadView.getBuilder();
//    }

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

        AnimationSet inAnim = new AnimationSet(true);
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
        this.inAnim = inAnim;
        AnimationSet outAnim = new AnimationSet(true);
        scaleAnimation = new ScaleAnimation(1, 2, 1,
                2, widthPixels * 0.5f, heightPixels * 0.45f);
        alphaAnimation = new AlphaAnimation(1, 0);
        alphaAnimation.setDuration(200);
        outAnim.addAnimation(scaleAnimation);
        outAnim.addAnimation(alphaAnimation);
        outAnim.setDuration(300);
        this.outAnim = outAnim;

    }

    public void setOutAnim(Animation outAnim) {
        this.outAnim = outAnim;
    }

    public void setInAnim(Animation inAnim) {
        this.inAnim = inAnim;
    }

    /**
     * 立刻关闭窗口
     */
    public void dismissImmediately() {
        if (loadView.getParent() != null) {
            decorView.removeView(loadView);
            currentType = LoadView.PROMPT_NONE;
        }
    }

    /**
     * close,程序调用的关闭，非LOADING状态
     */
    public void dismiss() {

        if (loadView.getParent() != null && currentType != LoadView.PROMPT_LOADING && !outAnimRunning) {
            if (loadView.getBuilder().withAnim) {
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
        showSomthing(R.drawable.ic_prompt_error, LoadView.PROMPT_ERROR, msg);

    }

    public void showInfo(String msg) {
        showSomthing(R.drawable.ic_prompt_info, LoadView.PROMPT_INFO, msg);
    }

    public void showWarn(String msg) {
        showSomthing(R.drawable.ic_prompt_warn, LoadView.PROMPT_WARN, msg);
    }

    public void showSuccess(String msg) {

        showSomthing(R.drawable.ic_prompt_success, LoadView.PROMPT_SUCCESS, msg);
    }

    /**
     * 展示自定义的状态提示框
     *
     * @param icon
     * @param msg
     */
    public void showCustom(int icon, String msg) {
//        Builder builder = Builder.getDefaultBuilder();
//        builder.icon(icon);
//        builder.text(msg);

        showSomthing(icon, LoadView.PROMPT_CUSTOM, msg);
    }

    private void showSomthing(int icon, int promptError, String msg) {
        Builder builder = Builder.getDefaultBuilder();
        builder.text(msg);
        builder.icon(icon);

        closeInput();
        checkLoadView();
        if (loadView.getParent() != null && currentType != promptError) {
            loadView.setBuilder(builder);
            currentType = promptError;
            loadView.showSomthing(promptError);
            dissmissAni();
//            dismiss(getBuilder().stayDuration);
        }
    }

    public void showWarnAlert(String text, PromptButton... button) {
        Builder builder = Builder.getAlertDefaultBuilder();
        builder.text(text);
        builder.icon(R.drawable.ic_prompt_alert_warn);
        closeInput();
        checkLoadView();
//        if (currentType != LoadView.PROMPT_ALERT_WARN) {
        loadView.setBuilder(builder);
        currentType = LoadView.PROMPT_ALERT_WARN;
        loadView.showSomthingAlert(currentType, button);
        if (dissmissAnim != null && dissmissAnim.isRunning()) {
            dissmissAnimCancle = true;
            dissmissAnim.end();
        }
//        } else {
//            loadView.invalidate();
//        }

    }

    public void showLoading(String msg) {
        Builder builder = Builder.getDefaultBuilder();
        builder.icon(R.drawable.svpload);
        builder.text(msg);
        closeInput();
        checkLoadView();
        if (currentType != LoadView.PROMPT_LOADING) {
            currentType = LoadView.PROMPT_LOADING;
            loadView.setBuilder(builder);
            loadView.showLoading();
        } else {
            loadView.invalidate();
        }

    }

    /**
     * 检查 loadview是否咋屏幕中，没有就添加
     */
    private void checkLoadView() {
        if (loadView.getParent() == null) {
//            windowManager.addView(loadView,wl);
            decorView.addView(loadView);
            if (loadView.getBuilder().withAnim)
                loadView.startAnimation(inAnim);
        }
    }

    /**
     * 消失停留一秒的动画,如正在执行动画 停止
     */
    private void dissmissAni() {
        if (dissmissAnim == null) {
            dissmissAnim = ValueAnimator.ofInt(0, 1);
            dissmissAnim.setDuration(loadView.getBuilder().stayDuration);
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
        if (decorView != null) {
            inputmanger.hideSoftInputFromWindow(decorView.getWindowToken(), 0);

        }
//        window.closeAllPanels();
    }

    public Builder getDefaultBuilder() {
        return Builder.getDefaultBuilder();
    }

    public Builder getAlertDefaultBuilder() {
        return Builder.getAlertDefaultBuilder();
    }
}
