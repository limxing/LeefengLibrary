package com.limxing.library.CustomDialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;

import com.limxing.library.LoopView.LoopView;
import com.limxing.library.LoopView.OnItemSelectedListener;
import com.limxing.library.R;
import com.limxing.library.utils.DisplayUtil;

import java.util.List;

/**
 * Created by limxing on 16/5/16.
 */
public class DataDialog implements OnItemSelectedListener {
    private RelativeLayout relativeLayout;
    private DataDialogListener dataDialogListener;
    private Context context;
    private LoopView yearView;
    private LoopView monthView;
    private LoopView dayView;
    private Dialog dialog;


    public DataDialog(Context context) {
        this.context = context;
        relativeLayout = new RelativeLayout(context);


//        RelativeLayout.LayoutParams linearParams =  (RelativeLayout.LayoutParams)relativeLayout.getLayoutParams();
//        linearParams.height = middleHeight;
//        relativeLayout.setLayoutParams(linearParams);

        relativeLayout.setBackgroundColor(Color.WHITE);
        yearView = new LoopView(context);
        monthView = new LoopView(context);
        dayView = new LoopView(context);
        yearView.setListener(this);
        monthView.setListener(this);
        dayView.setListener(this);

    }


    public void setViewItems(List<String> y, List<String> m, List<String> d) {
        yearView.setItems(y);
        monthView.setItems(m);
        dayView.setItems(d);
        yearView.measure(0, 0);
        monthView.measure(0, 0);
        dayView.measure(0, 0);
        int yearW = yearView.getMeasuredWidth();
        int monthW = monthView.getMeasuredWidth();
        int dayW = dayView.getMeasuredWidth();
        int padleft = DisplayUtil.getScreenWith(context) / 2 - (yearW + monthW + dayW + 100) / 2;

        ViewGroup.MarginLayoutParams mp = new ViewGroup.MarginLayoutParams(yearW, ViewGroup.LayoutParams.WRAP_CONTENT);  //item的宽高
        mp.setMargins(padleft, 0, 0, 0);//分别是margin_top那四个属性
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(mp);
        yearView.setLayoutParams(params);
        relativeLayout.addView(yearView);

        padleft = padleft + yearW + 50;
        mp = new ViewGroup.MarginLayoutParams(monthW, ViewGroup.LayoutParams.WRAP_CONTENT);  //item的宽高
        mp.setMargins(padleft, 0, 0, 0);//分别是margin_top那四个属性
        params = new RelativeLayout.LayoutParams(mp);
        monthView.setLayoutParams(params);
        relativeLayout.addView(monthView);

        padleft = padleft + monthW + 50;
        mp = new ViewGroup.MarginLayoutParams(dayW, ViewGroup.LayoutParams.WRAP_CONTENT);  //item的宽高
        mp.setMargins(padleft, 0, 0, 0);//分别是margin_top那四个属性
        params = new RelativeLayout.LayoutParams(mp);
        dayView.setLayoutParams(params);
        relativeLayout.addView(dayView);


    }

    public void show() {
        dialog = new Dialog(context, R.style.MMTheme_DataSheet);
        dialog.setCanceledOnTouchOutside(true);
        dialog.setContentView(relativeLayout);
        Window w = dialog.getWindow();
        WindowManager.LayoutParams lp = w.getAttributes();
        lp.dimAmount = 0.4f;
        lp.gravity = Gravity.BOTTOM;
        lp.width = DisplayUtil.getScreenWith(context); //设置宽度
        w.setAttributes(lp);
        dialog.show();
    }

    @Override
    public void onItemSelected(int view) {
        String year = yearView.getItems().get(yearView.getSelectedItem());
        String month = monthView.getItems().get(monthView.getSelectedItem());
        String day = dayView.getItems().get(dayView.getSelectedItem());
        if (dataDialogListener != null) {
            dataDialogListener.onItemSelected(year, month, day);
        }

    }

    public interface DataDialogListener {
        void onItemSelected(String year, String month, String day);
    }

    public LoopView getYearView() {
        return yearView;
    }

    public LoopView getMonthView() {
        return monthView;
    }

    public LoopView getDayView() {
        return dayView;
    }

    public void setDataDialogListener(DataDialogListener dataDialogListener) {
        this.dataDialogListener = dataDialogListener;
    }
}
