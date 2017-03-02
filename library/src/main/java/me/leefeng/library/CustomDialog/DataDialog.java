package me.leefeng.library.CustomDialog;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.TextView;

import me.leefeng.library.LoopView.LoopView;
import me.leefeng.library.LoopView.OnItemSelectedListener;
import com.leefeng.library.R;
import me.leefeng.library.utils.DisplayUtil;

import java.util.List;

/**
 * Created by limxing on 16/5/16.
 *
 * 还是学到了alertview中的方法
 */
public class DataDialog implements OnItemSelectedListener, View.OnClickListener, DialogInterface.OnDismissListener {
    private  TextView confirmText;
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
        confirmText = new TextView(context);
        confirmText.setText("确定");
        confirmText.setTextColor(Color.parseColor("#7bbfea"));
        confirmText.setTextSize(18);
        confirmText.setOnClickListener(this);
        RelativeLayout.LayoutParams lp1 = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lp1.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
//        lp1.addRule(RelativeLayout.CENTER_HORIZONTAL, RelativeLayout.TRUE);
        lp1.setMargins(0, 15, 35, 0);
        relativeLayout.addView(confirmText, lp1);

    }


    public void setViewItems(List<String> y, List<String> m, List<String> d) {
        yearView.setItems(y);
        monthView.setItems(m);
        dayView.setItems(d);
        yearView.measure(0, 0);
        monthView.measure(0, 0);
        dayView.measure(0, 0);
        confirmText.measure(0, 0);
        int textH = confirmText.getMeasuredHeight() + 30;

        View view = new View(context);
        view.setBackgroundColor(Color.parseColor("#eeeeee"));
        RelativeLayout.LayoutParams lp1 = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 2);
//        lp1.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
//        lp1.addRule(RelativeLayout.CENTER_HORIZONTAL, RelativeLayout.TRUE);
        lp1.setMargins(0, textH, 0, 0);
        relativeLayout.addView(view, lp1);
        textH = textH + 32;

        int yearW = yearView.getMeasuredWidth();
        int monthW = monthView.getMeasuredWidth();
        int dayW = dayView.getMeasuredWidth();
        int padleft = DisplayUtil.getScreenWith(context) / 2 - (yearW + monthW + dayW + 100) / 2;

        ViewGroup.MarginLayoutParams mp = new ViewGroup.MarginLayoutParams(yearW, ViewGroup.LayoutParams.WRAP_CONTENT);  //item的宽高
        mp.setMargins(padleft, textH, 0, 30);//分别是margin_top那四个属性
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(mp);
        yearView.setLayoutParams(params);
        relativeLayout.addView(yearView);

        padleft = padleft + yearW + 50;
        mp = new ViewGroup.MarginLayoutParams(monthW, ViewGroup.LayoutParams.WRAP_CONTENT);  //item的宽高
        mp.setMargins(padleft, textH, 0, 30);//分别是margin_top那四个属性
        params = new RelativeLayout.LayoutParams(mp);
        monthView.setLayoutParams(params);
        relativeLayout.addView(monthView);

        padleft = padleft + monthW + 50;
        mp = new ViewGroup.MarginLayoutParams(dayW, ViewGroup.LayoutParams.WRAP_CONTENT);  //item的宽高
        mp.setMargins(padleft, textH, 0, 30);//分别是margin_top那四个属性
        params = new RelativeLayout.LayoutParams(mp);
        dayView.setLayoutParams(params);
        relativeLayout.addView(dayView);


    }

    public void show() {

        dialog = new Dialog(context, R.style.MMTheme_DataSheet);
        dialog.setOnDismissListener(this);
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
    public void onItemSelected(LoopView view) {
        String year = yearView.getItems().get(yearView.getSelectedItem());
        String month = monthView.getItems().get(monthView.getSelectedItem());
        String day = "";
        if (dayView.getItems() != null) {
            day = dayView.getItems().get(dayView.getSelectedItem());
        }
        if (dataDialogListener != null) {
            dataDialogListener.onItemSelected(year, month, day);
        }

    }

    @Override
    public void onClick(View view) {
        onItemSelected(null);
        dialog.dismiss();
    }

    @Override
    public void onDismiss(DialogInterface dialogInterface) {
        context=null;
        dataDialogListener=null;
        dialog=null;

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
