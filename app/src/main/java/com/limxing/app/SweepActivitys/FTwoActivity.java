package com.limxing.app.SweepActivitys;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.limxing.app.R;
import com.limxing.library.NoTitleBar.SystemBarTintManager;
import com.limxing.library.SwipeBack.SwipeBackActivity;
import com.limxing.library.XListView.XListView;

/**
 * Created by limxing on 16/2/16.
 */
public class FTwoActivity extends SwipeBackActivity implements XListView.IXListViewListener {
    private XListView xlist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_two);
        SystemBarTintManager.initSystemBar(this, R.color.transparent);
        xlist = (XListView) findViewById(R.id.xlist);
        xlist.setPullLoadEnable(true);
        xlist.setXListViewListener(this);
        xlist.setAdapter(new BaseAdapter() {
            @Override
            public int getCount() {
                return 20;
            }

            @Override
            public Object getItem(int i) {
                return i;
            }

            @Override
            public long getItemId(int i) {
                return i;
            }

            @Override
            public View getView(int i, View view, ViewGroup viewGroup) {
                TextView view1 = new TextView(FTwoActivity.this);
                view1.setText("limxing" + i);
                view1.setHeight(100);
                return view1;
            }
        });
    }


    @Override
    public void onRefresh() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                xlist.stopRefresh();
            }
        }, 2000);
    }

    @Override
    public void onLoadMore() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                xlist.stopLoadMore();
            }
        }, 2000);
    }
}
