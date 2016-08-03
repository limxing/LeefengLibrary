package com.limxing.app.recycleview;

import android.provider.Settings;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;

//import com.alibaba.mobileim.IYWLoginService;
//import com.alibaba.mobileim.WXAPI;
//import com.alibaba.mobileim.YWAPI;
//import com.alibaba.mobileim.YWIMKit;
//import com.alibaba.mobileim.YWLoginParam;
//import com.alibaba.mobileim.channel.event.IWxCallback;
//import com.alibaba.mobileim.channel.util.WxLog;
import com.limxing.app.BaseActivity;
import com.limxing.app.R;
import com.limxing.library.utils.ToastUtils;

/**
 * Created by limxing on 16/1/23.
 */
public class LoginActivity extends BaseActivity {


    @Override
    protected void initView() {
        setContentView(R.layout.login);
        RecyclerView recycleview = (RecyclerView) findViewById(R.id.recycleview);
        recycleview.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.HORIZONTAL));
        recycleview.setAdapter(new LoginAdapter());

//        boolean isOpen = Settings.Secure.getInt(getContentResolver(), Settings.Secure.ALLOW_MOCK_LOCATION, 0) != 0;
//        ToastUtils.showLong(this,isOpen+"打开里吗?");
    }

    @Override
    protected void init() {


    }


}
