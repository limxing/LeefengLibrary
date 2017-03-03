package me.leefeng.library.SVProgressHUD;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;

import me.leefeng.library.R;


/**
 * Created by Sai on 15/8/15.
 */
public class SVProgressHUD {
    private Context context;
    private static final long DISMISSDELAYED = 2000;
    private SVProgressHUDMaskType mSVProgressHUDMaskType;

    public enum SVProgressHUDMaskType {
        None,  //
        Clear,     //
        Black,     //
        Gradient,   //
        ClearCancel,     //
        BlackCancel,     //
        GradientCancel   //
        ;
    }

    private final FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, Gravity.BOTTOM
    );
    private ViewGroup decorView;//
    private ViewGroup rootView;//
    private SVProgressDefaultView mSharedView;

    private Animation outAnim;
    private Animation inAnim;
    private int gravity = Gravity.CENTER;


    public SVProgressHUD(Context context) {
        this.context = context;
        gravity = Gravity.CENTER;
        initViews();
        initDefaultView();
        initAnimation();
    }

    protected void initViews() {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        decorView = (ViewGroup) ((Activity) context).getWindow().getDecorView().findViewById(android.R.id.content);
        rootView = (ViewGroup) layoutInflater.inflate(R.layout.layout_svprogresshud, null, false);
        rootView.setLayoutParams(new FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT
        ));
    }

    protected void initDefaultView() {
        mSharedView = new SVProgressDefaultView(context);
        params.gravity = gravity;
        mSharedView.setLayoutParams(params);
    }

    protected void initAnimation() {
        if (inAnim == null)
            inAnim = getInAnimation();
        if (outAnim == null)
            outAnim = getOutAnimation();
    }

    /**
     */
    private void onAttached() {
        decorView.addView(rootView);
        if (mSharedView.getParent() != null)
            ((ViewGroup) mSharedView.getParent()).removeView(mSharedView);
        rootView.addView(mSharedView);
    }

    /**
     */
    private void svShow() {

        mHandler.removeCallbacksAndMessages(null);
        if (!isShowing()) {
            onAttached();
        }

        mSharedView.startAnimation(inAnim);

    }

    public void show() {
        setMaskType(SVProgressHUDMaskType.Clear);
        mSharedView.show();
        svShow();
    }

    public void showWithMaskType(SVProgressHUDMaskType maskType) {
        //判断maskType
        setMaskType(maskType);
        mSharedView.show();
        svShow();
    }

    public void showWithStatus(String string) {
        setMaskType(SVProgressHUDMaskType.Clear);
        mSharedView.showWithStatus(string);
        svShow();
    }

    /**
     *
     * @param string
     * @param maskType
     */
    public void showWithStatus(String string, SVProgressHUDMaskType maskType) {
        setMaskType(maskType);
        mSharedView.showWithStatus(string);
        svShow();
    }

    /**
     *
     * @param string
     * @param maskType
     */
    public void showLoading(String string, SVProgressHUDMaskType maskType) {
        setMaskType(maskType);
        mSharedView.showLoading(string);
        svShow();
    }

    public void showLoading(String string) {
        setMaskType(SVProgressHUDMaskType.Clear);
        mSharedView.showLoading(string);
        svShow();
    }

    public void showLmWithStatus(String string, SVProgressHUDMaskType maskType) {
        setMaskType(maskType);
        mSharedView.showLmWithStatus(string);
        svShow();
    }

    public void showInfoWithStatus(String string) {
        setMaskType(SVProgressHUDMaskType.None);
        mSharedView.showInfoWithStatus(string);
        svShow();
        scheduleDismiss();
    }

    public void showInfoWithStatus(String string, SVProgressHUDMaskType maskType) {
        setMaskType(maskType);
        mSharedView.showInfoWithStatus(string);
        svShow();
        scheduleDismiss();
    }

    public void showSuccessWithStatus(String string) {
        setMaskType(SVProgressHUDMaskType.None);
        mSharedView.showSuccessWithStatus(string);
        svShow();
        scheduleDismiss();
    }

    public void showSuccessWithStatus(String string, SVProgressHUDMaskType maskType) {
        setMaskType(maskType);
        mSharedView.showSuccessWithStatus(string);
        svShow();
        scheduleDismiss();
    }

    public void showErrorWithStatus(String string) {
        setMaskType(SVProgressHUDMaskType.None);
        mSharedView.showErrorWithStatus(string);
        svShow();
        scheduleDismiss();
    }

    public void showErrorWithStatus(String string, SVProgressHUDMaskType maskType) {
        setMaskType(maskType);
        mSharedView.showErrorWithStatus(string);
        svShow();
        scheduleDismiss();
    }

    public void showWithProgress(String string, SVProgressHUDMaskType maskType) {
        setMaskType(maskType);
        mSharedView.showWithProgress(string);
        svShow();
    }

    public SVCircleProgressBar getProgressBar() {
        return mSharedView.getCircleProgressBar();
    }

    public void setText(String string) {
        mSharedView.setText(string);
    }

    private void setMaskType(SVProgressHUDMaskType maskType) {
        mSVProgressHUDMaskType = maskType;
        switch (mSVProgressHUDMaskType) {
            case None:
                configMaskType(android.R.color.transparent, false, false);
                break;
            case Clear:
                configMaskType(android.R.color.transparent, true, false);
                break;
            case ClearCancel:
                configMaskType(android.R.color.transparent, true, true);
                break;
            case Black:
                configMaskType(R.color.bgColor_overlay, true, false);
                break;
            case BlackCancel:
                configMaskType(R.color.bgColor_overlay, true, true);
                break;
            case Gradient:
                configMaskType(R.drawable.bg_overlay_gradient, true, false);
                break;
            case GradientCancel:
                configMaskType(R.drawable.bg_overlay_gradient, true, true);
                break;
            default:
                break;
        }
    }

    private void configMaskType(int bg, boolean clickable, boolean cancelable) {
        rootView.setBackgroundResource(bg);
        rootView.setClickable(clickable);
        setCancelable(cancelable);
    }

    /**
     *
     * @return 如果视图已经存在该View返回true
     */
    public boolean isShowing() {
        return rootView.getParent() != null;
    }

    public void dismiss() {
        //消失动画
        outAnim.setAnimationListener(outAnimListener);
        mSharedView.startAnimation(outAnim);
    }

    public void dismissImmediately() {
        mSharedView.dismiss();
        rootView.removeView(mSharedView);
        decorView.removeView(rootView);
        context = null;
    }

    public Animation getInAnimation() {
        int res = SVProgressHUDAnimateUtil.getAnimationResource(this.gravity, true);
        return AnimationUtils.loadAnimation(context, res);
    }

    public Animation getOutAnimation() {
        int res = SVProgressHUDAnimateUtil.getAnimationResource(this.gravity, false);
        return AnimationUtils.loadAnimation(context, res);
    }

    private void setCancelable(boolean isCancelable) {
        View view = rootView.findViewById(R.id.sv_outmost_container);

        if (isCancelable) {
            view.setOnTouchListener(onCancelableTouchListener);
        } else {
            view.setOnTouchListener(null);
        }
    }

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            dismiss();
        }
    };

    private void scheduleDismiss() {
        mHandler.removeCallbacksAndMessages(null);
        mHandler.sendEmptyMessageDelayed(0, DISMISSDELAYED);
    }

    /**
     * Called when the user touch on black overlay in order to dismiss the dialog
     */
    private final View.OnTouchListener onCancelableTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                dismiss();
                setCancelable(false);
            }
            return false;
        }
    };

    Animation.AnimationListener outAnimListener = new Animation.AnimationListener() {
        @Override
        public void onAnimationStart(Animation animation) {

        }

        @Override
        public void onAnimationEnd(Animation animation) {
            dismissImmediately();
        }

        @Override
        public void onAnimationRepeat(Animation animation) {

        }
    };
}
