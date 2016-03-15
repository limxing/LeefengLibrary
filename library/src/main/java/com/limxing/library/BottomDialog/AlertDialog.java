package com.limxing.library.BottomDialog;


import android.annotation.TargetApi;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;

import android.text.TextPaint;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.limxing.library.R;
import com.limxing.library.utils.DisplayUtil;


import java.util.ArrayList;
import static com.limxing.library.R.*;


/**
 * Created by limxing on 15/12/20.
 */
public  class AlertDialog implements View.OnClickListener {

    private final View bview;
    private final TextView bottom_cancle;
    private final LinearLayout lm_top;
    private final int height;
    private PopupWindow pop;
    private final View view;
    private  Context context;
    private boolean flag=true;
    private String[] selections;
    private int textColor;
    private int cancleTextColor;
    private ClickListener listener;

    /**
     *
     * @param context 上下文
     * @param view 展示在那个view上
     */
    public AlertDialog(Context context,View view){
        this.context=context;
        this.view=view;
        cancleTextColor=textColor=context.getResources().getColor(color.holo_blue_light);
         bview=View.inflate(context, layout.lmbottomselecter,null);
        bottom_cancle=(TextView)bview.findViewById(id.bottom_cancle);
        lm_top=(LinearLayout)bview.findViewById(id.lm_top);
        bottom_cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pop.dismiss();
            }
        });
        bottom_cancle.setTextSize(DisplayUtil.px2sp(context, 50));
        bottom_cancle.setTextColor(cancleTextColor);
       height= DisplayUtil.dip2px(context, 50);

    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public void show(){
        for (int i=0;i<selections.length;i++){
            TextView view= new TextView(context);
            view.setText(selections[i]);
            view.setClickable(true);
            view.setTag(i);
            view.setOnClickListener(this);
            view.setTextSize(DisplayUtil.px2sp(context, 50));
            TextPaint tp = view.getPaint();
            tp.setFakeBoldText(true);
            view.setGravity(Gravity.CENTER);
            view.setHeight(height);
            if(flag){
                view.setBackground(context.getResources().getDrawable(drawable.button_selector_top));
            }else if(i==selections.length-1){
                view.setBackground(context.getResources().getDrawable(drawable.button_selector_bottom));
            }else{
                view.setBackground(context.getResources().getDrawable(drawable.button_selector_middle));
            }
            view.setTextColor(textColor);
            lm_top.addView(view);
        }

        pop= new PopupWindow(bview,
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
        pop.setAnimationStyle(style.myBottom);
//        pop.setBackgroundDrawable(new BitmapDrawable());
        View view1=new View(context);
        view1.setBackgroundColor(context.getResources().getColor(color.lm_bottomdialog));
        final PopupWindow pop1=new PopupWindow(view1,
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, true);
        pop1.setAnimationStyle(style.myBottomBac);
        pop1.showAtLocation(view, Gravity.TOP, 0, 0);
        pop.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                pop1.dismiss();

            }
        });

        pop.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        pop.showAtLocation(view1, Gravity.BOTTOM, 0, 0);

    }
    public void setClickListener(ClickListener listener){
        this.listener=listener;
    }

    public void setCancleButtonTitle(String cancle){
        bottom_cancle.setText(cancle);
    }
    public void setSelections(String[] selections){
        this.selections=selections;

    }

    /**
     * 其中的tag就是辨别哪一个按钮的点击事件的
     * @param view
     */
    @Override
    public  void onClick(View view){
        pop.dismiss();
        if(listener!=null) {
            listener.selectionClick((int) view.getTag());
        }
    }
    public abstract class  ClickListener{
        public abstract void selectionClick(int i);
    }


    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public void setDescription(String describtion){
        TextView view=new TextView(context);
        view.setText(describtion);
        view.setTextSize(DisplayUtil.px2sp(context, 30));
        view.setGravity(Gravity.CENTER);
        view.setBackground(context.getResources().getDrawable(drawable.button_selector_top));
        view.setClickable(false);
        int pad=DisplayUtil.dip2px(context, 15);
        view.setPadding(pad, pad, pad, pad);
        flag=false;
        lm_top.addView(view, 0);
    }


    public void dismiss() {
        pop.dismiss();
    }

    public boolean isShowing(){
        return pop.isShowing();
    }
    public void setTextColor(int color){

        this.textColor=color;
    }
    public void setCancleTextColor(int color){
        this.cancleTextColor=color;
    }


}
