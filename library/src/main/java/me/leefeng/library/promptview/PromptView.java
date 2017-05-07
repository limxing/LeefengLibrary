package me.leefeng.library.promptview;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;

import me.leefeng.library.R;

/**
 * Created by limxing on 2017/5/7.
 */

public class PromptView {
    private static final int PROMPT_SUCCESS = 101;
    private static final int PROMPT_LOADING = 102;
    private static final int PROMPT_ERROR = 103;
    private int currentType;
    private Animation outAnim;
    private Animation inAnim;
    private LoadView loadView;
    private Context context;
    private ViewGroup decorView;

    public PromptView(Activity context) {
        this.context = context;
//        LayoutInflater layoutInflater = LayoutInflater.from(context);
        decorView = (ViewGroup) context.getWindow().getDecorView().findViewById(android.R.id.content);
        inAnim = AnimationUtils.loadAnimation(context, R.anim.promptview_in);
        outAnim = AnimationUtils.loadAnimation(context, R.anim.promptview_out);
        loadView = new LoadView(context);
    }

    public PromptView showLoading(String msg, boolean withAnimation) {
        if (currentType != PROMPT_LOADING) {
            currentType = PROMPT_LOADING;
            loadView.setLoading(msg);
            if (loadView.getParent() == null) {
                decorView.addView(loadView);
                if (withAnimation)
                    loadView.startAnimation(inAnim);
            }
        } else {
            loadView.setText(msg);
        }
//        rootView = (ViewGroup) layoutInflater.inflate(R.layout.layout_alertview, decorView, false);
//        rootView.setLayoutParams(new FrameLayout.LayoutParams(
//                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT
//        ));
        return this;

    }

    public void showSuccess(String msg) {
        if (loadView.getParent() != null && currentType != PROMPT_SUCCESS) {
            currentType = PROMPT_SUCCESS;
            loadView.setSuccess(msg);
            loadView.postDelayed(new Runnable() {
                @Override
                public void run() {
                    dismiss(true);
                }
            }, 1000);
        }
    }

    public void dismiss(boolean withAnimation) {
        if (loadView.getParent() != null) {
            if (withAnimation) {
                loadView.startAnimation(outAnim);
                outAnim.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        decorView.removeView(loadView);
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
        if (loadView.getParent() != null && currentType != PROMPT_ERROR) {
            currentType = PROMPT_ERROR;
            loadView.setError(msg);
            loadView.postDelayed(new Runnable() {
                @Override
                public void run() {
                    dismiss(true);
                }
            }, 1000);
        }
    }
}
