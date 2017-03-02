package com.leefeng.app.activity;

import android.view.View;
import android.widget.Button;

//import com.alibaba.mobileim.IYWLoginService;
//import com.alibaba.mobileim.YWLoginParam;
//import com.alibaba.mobileim.channel.event.IWxCallback;
import com.leefeng.app.BaseActivity;
import com.leefeng.app.R;
import me.leefeng.library.LoopView.LoopView;
import me.leefeng.publicc.alertview.AlertView;
import me.leefeng.publicc.alertview.OnConfirmeListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by limxing on 16/1/25.
 */
public class WelcomeActivity extends BaseActivity {
    @Override
    protected void initView() {
        setContentView(R.layout.welcome);
        List<String> list=new ArrayList<>();
        for (int i=0;i<20;i++)
            list.add(20+i+"年");
        LoopView loopView =(LoopView) findViewById(R.id.loopview);
        LoopView loopView2=(LoopView) findViewById(R.id.loopview2);
        loopView.setItems(list);
        loopView2.setItems(list);
    }

    @Override
    protected void init() {


    }
   public void dianji(final View view){
        new AlertView("选择日期", this, 2000, 2016,null, new OnConfirmeListener() {
            @Override
            public void result(String s) {
                ((Button)view).setText(s);
            }
        }).show();
    }
}
