package com.limxing.library.BottomDialog;


import android.annotation.TargetApi;


import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;

import android.os.Build;

import android.text.TextPaint;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.limxing.library.R;
import com.limxing.library.utils.DisplayUtil;

/**
 * @author limxing
 *         这是一个底部弹出的对话选择框
 */


public class BottomDialog {


    /**
     * 静态的从底部弹出的对话框
     *
     * @param context        上下文
     * @param describtion    描述
     * @param selections     选项内容
     * @param listener       选中监听
     * @param cancelListener 取消的监听
     * @return
     */
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public static Dialog showAlert(Context context, String describtion, String[] selections,
                                   final BottomDialog.OnClickListener listener, DialogInterface.OnCancelListener cancelListener) {
//        String cancel = context.getString(R.string.app_cancel);

        final Dialog dlg = new Dialog(context, R.style.MMTheme_DataSheet);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layout = inflater.inflate(R.layout.lmbottomselecter, null);
        LinearLayout lm_top = (LinearLayout) layout.findViewById(R.id.lm_top);
        layout.findViewById(R.id.bottom_cancle).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dlg.dismiss();
            }
        });
        if (describtion != null) {
            TextView view = new TextView(context);
            view.setText(describtion);
            view.setTextSize(DisplayUtil.px2sp(context, 30));
            view.setGravity(Gravity.CENTER);
            view.setBackground(context.getResources().getDrawable(R.drawable.button_selector_top));
            view.setClickable(false);
            int pad = DisplayUtil.dip2px(context, 15);
            view.setPadding(pad, pad, pad, pad);
            lm_top.addView(view, 0);
        }
        int height = DisplayUtil.dip2px(context, 55);
        for (int i = 0; i < selections.length; i++) {
            TextView view = new TextView(context);
            view.setText(selections[i]);
            view.setClickable(true);
            view.setTag(i);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dlg.dismiss();
                    listener.onClick((int) view.getTag());
                }
            });
            view.setTextSize(DisplayUtil.px2sp(context, context.getResources()
                    .getDimension(R.dimen.bottomdialog_title_size)));
//            TextPaint tp = view.getPaint();
//            tp.setFakeBoldText(true);
            view.setGravity(Gravity.CENTER);
            view.setHeight(height);
            if (describtion == null && i == 0) {
                if (selections.length > 1) {
                    view.setBackground(context.getResources().getDrawable(R.drawable.button_selector_top));
                } else {
                    view.setBackground(context.getResources().getDrawable(R.drawable.button_selector));
                }
                view.setBackground(context.getResources().getDrawable(R.drawable.button_selector_top));

            } else if (i == selections.length - 1) {
                view.setBackground(context.getResources().getDrawable(R.drawable.button_selector_bottom));
            } else {
                view.setBackground(context.getResources().getDrawable(R.drawable.button_selector_middle));
            }
            view.setTextColor(context.getResources().getColor(R.color.holo_blue_light));
            lm_top.addView(view);
        }
//        final int cFullFillWidth = 10000;
//        layout.setMinimumWidth(cFullFillWidth);
        dlg.setCanceledOnTouchOutside(true);
        if (cancelListener != null) {
            dlg.setOnCancelListener(cancelListener);
        }
        dlg.setContentView(layout);
        // set a large value put it in bottom
        Window w = dlg.getWindow();
        WindowManager.LayoutParams lp = w.getAttributes();
        lp.dimAmount=0.4f;
//        lp.x = 0;
//        final int cMakeBottom = -1000;
//        lp.y = cMakeBottom;
        lp.gravity = Gravity.BOTTOM;
//        dlg.onWindowAttributesChanged(lp);
        lp.width = DisplayUtil.getScreenWith(context); //设置宽度
        w.setAttributes(lp);
        dlg.show();
        return dlg;
    }

    public interface OnClickListener {
        void onClick(int which);
    }

}
