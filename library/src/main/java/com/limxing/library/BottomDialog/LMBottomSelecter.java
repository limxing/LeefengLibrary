package com.limxing.library.BottomDialog;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.widget.PopupWindow;

import com.limxing.library.R;

import java.util.List;

/**
 * Created by limxing on 15/12/5.
 */
public class LMBottomSelecter {
    private Context mContext;
    private String[] redList;
    private String[] mList;
    private String cancle;

    public LMBottomSelecter(Context context, String[] list) {
        this.mContext = context;
        this.mList = list;
    }

    public LMBottomSelecter(Context context, String[] list, String cancle) {
        this.mContext = context;
        this.mList = list;
        this.cancle = cancle;
    }

    public LMBottomSelecter(Context context, String[] list, String cancle, String[] redItem) {
        this.mContext = context;
        this.mList = list;
        this.cancle = cancle;
        this.redList = redItem;
    }

    public void show(View view) {
        PopupWindow pw = new PopupWindow(mContext);
        View bottomView=View.inflate(mContext, R.layout.lmbottomselecter,null);
        //设置SelectPicPopupWindow的View
        pw.setContentView(bottomView);
        //设置SelectPicPopupWindow弹出窗体的宽
        pw.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        //设置SelectPicPopupWindow弹出窗体的高
        pw.setHeight(ViewGroup.LayoutParams.MATCH_PARENT);
        //设置SelectPicPopupWindow弹出窗体可点击
        pw.setFocusable(true);
        //设置SelectPicPopupWindow弹出窗体动画效果
        pw.setAnimationStyle(R.style.myBottom);


        pw.showAtLocation(view, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
        pw.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {

            }
        });
        //实例化一个ColorDrawable颜色为半透明
//        ColorDrawable dw = new ColorDrawable(0xb0000000);
//        设置SelectPicPopupWindow弹出窗体的背景
//        ColorDrawable dw = new ColorDrawable(-00000);
//        pw.setBackgroundDrawable(dw);


    }
}
