package com.limxing.library.BottomDialog;


import android.annotation.TargetApi;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Interpolator;
import android.view.animation.TranslateAnimation;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.limxing.library.R;
import com.limxing.library.utils.DisplayUtil;
import com.limxing.library.utils.LogUtils;

/**
 * @author limxing
 *         这是一个底部弹出的对话选择框
 */


public class BottomSelect {


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
                                   final BottomSelect.OnClickListener listener, DialogInterface.OnCancelListener cancelListener) {
//        String cancel = context.getString(R.string.app_cancel);

        final Dialog dlg = new Dialog(context, R.style.MMTheme_DataSheet);
//        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//        View layout = inflater.inflate(R.layout.bottomselecter, null);
//        LinearLayout lm_top = (LinearLayout) layout.findViewById(R.id.lm_top);
//        layout.findViewById(R.id.bottom_cancle).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                dlg.dismiss();
//            }
//        });
        LinearLayout lm_top=new LinearLayout(context);
        lm_top.setOrientation(LinearLayout.VERTICAL);
        if (describtion != null) {
            TextView view = new TextView(context);
            view.setText(describtion);
            view.setTextSize(DisplayUtil.px2sp(context, 30));
            view.setGravity(Gravity.CENTER);
            view.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.button_selector_for_bottomselect));
            view.setClickable(false);
            int pad = DisplayUtil.dip2px(context, 15);
            view.setPadding(pad, pad, pad, pad);
            lm_top.addView(view, 0);
        }
        int height = DisplayUtil.dip2px(context, 60);
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
            //设置字体位粗体
//            TextPaint tp = view.getPaint();
//            tp.setFakeBoldText(true);
            view.setGravity(Gravity.CENTER);
            view.setHeight(height);
            view.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.button_selector_for_bottomselect));
            view.setTextColor(context.getResources().getColor(R.color.holo_blue_light));
            lm_top.addView(view);
        }
//        final int cFullFillWidth = 10000;
//        layout.setMinimumWidth(cFullFillWidth);

        dlg.setCanceledOnTouchOutside(true);
        if (cancelListener != null) {

            dlg.setOnCancelListener(cancelListener);
        }
        dlg.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {

            }
        });
        dlg.setContentView(lm_top);
        // set a large value put it in bottom
        Window w=dlg.getWindow();
        WindowManager.LayoutParams lp = w.getAttributes();
        lp.dimAmount=0.4f;
//        lp.x = 0;
//        final int cMakeBottom = -1000;
//        lp.y = cMakeBottom;
        lp.gravity = Gravity.BOTTOM;
//        dlg.onWindowAttributesChanged(lp);
        lp.width = DisplayUtil.getScreenWith(context); //设置宽度
        w.setAttributes(lp);
//        w.addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        dlg.show();
//        int screenheight=DisplayUtil.getScreenHeight(context);
//
//        TranslateAnimation ta=new TranslateAnimation(0,0,600,100);
//
//        ta.setDuration(300);
//        layout.startAnimation(ta);
        return dlg;
    }

    public interface OnClickListener {
        void onClick(int which);
    }

}
