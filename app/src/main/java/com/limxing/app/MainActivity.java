package com.limxing.app;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;

import com.limxing.library.PullToRefresh.SwipeRefreshLayout;
import com.limxing.library.SwipeBack.SwipeBackActivity;

import com.limxing.library.NoTitleBar.SystemBarTintManager;
import com.limxing.library.utils.LogUtils;


public class MainActivity extends SwipeBackActivity implements SwipeRefreshLayout.OnRefreshListener,SwipeRefreshLayout.OnLoadListener{


    private SwipeRefreshLayout main_fresh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SystemBarTintManager.initSystemBar(this, R.color.transparent);
        main_fresh=(com.limxing.library.PullToRefresh.SwipeRefreshLayout)findViewById(R.id.main_fresh);
        main_fresh.setOnRefreshListener(this);
        main_fresh.setOnLoadListener(this);
    }

    @Override
    public void onLoad() {
        LogUtils.i("onLoad");
    }

    @Override
    public void onRefresh() {
        LogUtils.i("onRefresh");
    }
}
