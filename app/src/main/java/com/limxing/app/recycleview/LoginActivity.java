package com.limxing.app.recycleview;

import android.Manifest;
import android.os.Handler;
import android.provider.Settings;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

//import com.alibaba.mobileim.IYWLoginService;
//import com.alibaba.mobileim.WXAPI;
//import com.alibaba.mobileim.YWAPI;
//import com.alibaba.mobileim.YWIMKit;
//import com.alibaba.mobileim.YWLoginParam;
//import com.alibaba.mobileim.channel.event.IWxCallback;
//import com.alibaba.mobileim.channel.util.WxLog;
import com.limxing.app.BaseActivity;
import com.limxing.app.R;
import com.limxing.app.view.DemoView;
import com.limxing.library.utils.ToastUtils;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by limxing on 16/1/23.
 */
public class LoginActivity extends BaseActivity {


    private DemoView view1;
    private Timer time;

    @Override
    protected void onDestroy() {
        super.onDestroy();
        time.cancel();
        time=null;
        view1 = null;
    }

    @Override
    protected void initView() {
        setContentView(R.layout.login);
        RecyclerView recycleview = (RecyclerView) findViewById(R.id.recycleview);
        recycleview.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.HORIZONTAL));
        recycleview.setAdapter(new LoginAdapter());
        view1 = (DemoView) findViewById(R.id.view);
//        boolean isOpen = Settings.Secure.getInt(getContentResolver(), Settings.Secure.ALLOW_MOCK_LOCATION, 0) != 0;
//        ToastUtils.showLong(this,isOpen+"打开里吗?");
    }

    @Override
    protected void init() {


    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        return super.onTouchEvent(event);
    }

    public void Permission(View view) {
        checkPermission(new CheckPermListener() {
            @Override
            public void superPermission() {
                ToastUtils.showLong(LoginActivity.this, "相机可用");
            }
        }, "需要获取相机权限", Manifest.permission.CAMERA);
    }


    int i = 0;

    public void hah(View view) {
        final View v = View.inflate(LoginActivity.this, R.layout.title, null);
        final TextView tv = (TextView) v.findViewById(R.id.tt);
        view1.setText(v);
        time = new Timer();
        time.schedule(new TimerTask() {
            @Override
            public void run() {
                tv.setText("" + i++);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        view1.postInvalidate();
                        if (i==5){
                            view1.removeText();
                            time.cancel();
                            i=0;
                        }
                    }
                });
            }
        }, 0, 1000);

    }


}
