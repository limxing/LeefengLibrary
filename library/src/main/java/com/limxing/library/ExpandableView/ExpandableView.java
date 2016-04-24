/*
 * Copyright (C) 2016 The Android Open Source Project
 * Copyright 2016 Limxing
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.limxing.library.ExpandableView;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.limxing.library.R;
import com.limxing.library.utils.DisplayUtil;


public class ExpandableView extends LinearLayout implements View.OnClickListener {
    private Context mContext;


    private static final int DEFAULT_ANIM_DURATION = 300;

    private static final float DEFAULT_ANIM_ALPHA_START = 0f;
    private static final boolean DEFAULT_SHOW = true;
//    protected LinearLayout mTv;
    protected ImageButton mButton; // Button to expand/collapse
    private boolean mCollapsed = true; // Show short version as default.

    private Drawable mExpandDrawable;

    private Drawable mCollapseDrawable;

    private int mAnimationDuration;

    private float mAnimAlphaStart;

    private boolean mAnimating;

    /* Listener for callback */
    private OnExpandStateChangeListener mListener;
    private int mHeight;//需要改变的高度
    private int mMinHeight;//最小距离

    public ExpandableView(Context context) {
        this(context, null);
    }

    public ExpandableView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public ExpandableView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context, attrs);
    }

    @Override
    public void setOrientation(int orientation) {
        if (LinearLayout.HORIZONTAL == orientation) {
            throw new IllegalArgumentException("ExpandableTextView only supports Vertical Orientation.");
        }
        super.setOrientation(orientation);
    }

    @Override
    public void onClick(View view) {
        mCollapsed = !mCollapsed;
        mButton.setImageDrawable(mCollapsed ? mExpandDrawable : mCollapseDrawable);
        mAnimating = true;
//        Log.i("hah:",mCollapsed+"=getHeight="+getHeight()+"=mHeight="+mHeight+"=mMinHeight="+mMinHeight);
        Animation animation;
        if (mCollapsed) {
            //false
            animation = new ExpandCollapseAnimation(this, mMinHeight, mHeight);//kuo
        } else {
            animation = new ExpandCollapseAnimation(this, mHeight, mMinHeight);//suo
        }

        animation.setFillAfter(true);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                clearAnimation();
                mAnimating = false;
//                if (mListener != null) {
//                    mListener.onExpandStateChanged(this, !mCollapsed);
//                }
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });

        clearAnimation();
        startAnimation(animation);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return mAnimating;
    }

    @Override
    protected void onFinishInflate() {
        findViews();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        //如果展开
        if (mCollapsed)
            mHeight = getMeasuredHeight();
    }

    public void setOnExpandStateChangeListener(@Nullable OnExpandStateChangeListener listener) {
        mListener = listener;
    }


    private void init(Context context, AttributeSet attrs) {
        mContext = context;
        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.ExpandableView);
        mAnimationDuration = typedArray.getInt(R.styleable.ExpandableView_animDuration, DEFAULT_ANIM_DURATION);
        mAnimAlphaStart = typedArray.getFloat(R.styleable.ExpandableView_animAlphaStart, DEFAULT_ANIM_ALPHA_START);
        mExpandDrawable = typedArray.getDrawable(R.styleable.ExpandableView_expandDrawable);
        mCollapseDrawable = typedArray.getDrawable(R.styleable.ExpandableView_collapseDrawable);
        mCollapsed = typedArray.getBoolean(R.styleable.ExpandableView_viewTitleShow, DEFAULT_SHOW);

        if (mExpandDrawable == null) {
            mExpandDrawable = getDrawable(getContext(), R.drawable.ic_expand_more_black_16dp);
        }
        if (mCollapseDrawable == null) {
            mCollapseDrawable = getDrawable(getContext(), R.drawable.ic_expand_less_black_16dp);
        }
        setOrientation(LinearLayout.VERTICAL);
        RelativeLayout title = new RelativeLayout(context);
        int color = typedArray.getColor(R.styleable.ExpandableView_viewTitleBacColor, Color.WHITE);
        title.setOnClickListener(this);
        title.setBackgroundColor(color);
        title.setGravity(Gravity.CENTER_VERTICAL);
        String title_text = typedArray.getString(R.styleable.ExpandableView_viewTitle);
        float title_size = typedArray.getDimension(R.styleable.ExpandableView_viewTitleSize,
                DisplayUtil.sp2px(mContext, 10));
        color = typedArray.getColor(R.styleable.ExpandableView_viewTitleColor, Color.BLACK);
        TextView tv = new TextView(mContext);
        tv.setTextSize(title_size);
        tv.setTextColor(color);
        tv.setText(title_text);
        int padLeft = DisplayUtil.dip2px(mContext, 20);
        int padTop = DisplayUtil.dip2px(mContext, 10);
        tv.setPadding(padLeft, padTop, 0, padTop);
        title.addView(tv);

        mButton = new ImageButton(mContext);
        mButton.setImageDrawable(mCollapsed ? mExpandDrawable : mCollapseDrawable);
        int h = mExpandDrawable.getMinimumHeight();
        MarginLayoutParams mp = new MarginLayoutParams(h, h);  //item的宽高
        mp.setMargins(0, 0, padLeft, 0);//分别是margin_top那四个属性
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(mp);
        params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        params.addRule(RelativeLayout.CENTER_VERTICAL);
        mButton.setLayoutParams(params);
        title.addView(mButton);
        View line = new View(mContext);
        color = typedArray.getColor(R.styleable.ExpandableView_viewTitleLineColor, Color.GRAY);
        line.setBackgroundColor(color);
        h = DisplayUtil.dip2px(mContext, 1);
        MarginLayoutParams mpLine = new MarginLayoutParams(LayoutParams.MATCH_PARENT, h);  //item的宽高
        title.measure(0, 0);
        h = title.getMeasuredHeight() - h;
        mpLine.setMargins(0, h, 0, 0);//分别是margin_top那四个属性
        RelativeLayout.LayoutParams mpLineparams = new RelativeLayout.LayoutParams(mpLine);
        line.setLayoutParams(mpLineparams);
        title.addView(line);
        mMinHeight = title.getMeasuredHeight();
        addView(title);
        typedArray.recycle();
    }


    private void findViews() {

    }

    private static boolean isPostLolipop() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP;
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private static Drawable getDrawable(@NonNull Context context, @DrawableRes int resId) {
        Resources resources = context.getResources();
        if (isPostLolipop()) {
            return resources.getDrawable(resId, context.getTheme());
        } else {
            return resources.getDrawable(resId);
        }
    }


    class ExpandCollapseAnimation extends Animation {
        private final View mTargetView;
        private final int mStartHeight;
        private final int mEndHeight;

        public ExpandCollapseAnimation(View view, int startHeight, int endHeight) {
            mTargetView = view;
            mStartHeight = startHeight;
            mEndHeight = endHeight;
            setDuration(mAnimationDuration);
        }

        @Override
        protected void applyTransformation(float interpolatedTime, Transformation t) {
            final int newHeight = (int) ((mEndHeight - mStartHeight) * interpolatedTime + mStartHeight);
            setMinimumHeight(newHeight);
            mTargetView.getLayoutParams().height = newHeight;
            mTargetView.requestLayout();
        }

        @Override
        public void initialize(int width, int height, int parentWidth, int parentHeight) {
            super.initialize(width, height, parentWidth, parentHeight);
        }

        @Override
        public boolean willChangeBounds() {
            return true;
        }
    }

    public interface OnExpandStateChangeListener {
        /**
         * Called when the expand/collapse animation has been finished
         *
         * @param textView   - TextView being expanded/collapsed
         * @param isExpanded - true if the TextView has been expanded
         */
        void onExpandStateChanged(LinearLayout textView, boolean isExpanded);
    }
}