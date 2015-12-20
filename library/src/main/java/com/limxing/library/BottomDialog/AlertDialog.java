package com.limxing.library.BottomDialog;


import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;

import com.limxing.library.R;

/**
 * Created by limxing on 15/12/20.
 */
public abstract class AlertDialog  {

    private PopupWindow pop;
    private final View view;
    private  Context context;

    public AlertDialog(Context context,View view){
        this.context=context;
        this.view=view;


    }

    public void show(){
        View bview=View.inflate(context, R.layout.lmbottomselecter,null);

        pop= new PopupWindow(bview,
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);

        pop.setAnimationStyle(R.style.myBottom);
        pop.setBackgroundDrawable(new BitmapDrawable());
        View view1=new View(context);
        view1.setBackgroundColor(context.getResources().getColor(R.color.lm_bottomdialog));
        final PopupWindow pop1=new PopupWindow(view1,
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, true);
        pop1.setBackgroundDrawable(new BitmapDrawable());
        pop1.showAtLocation(view, Gravity.CENTER, 0, 0);
        pop.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                pop1.dismiss();
                closed();
            }
        });


        pop.showAtLocation(view1, Gravity.BOTTOM, 0, 0);

//        pop.showAsDropDown(view);
    }

    public abstract void closed();

}
