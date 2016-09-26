package com.limxing.app.recycleview;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.provider.Settings;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.telephony.TelephonyManager;
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
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.limxing.app.BaseActivity;
import com.limxing.app.R;
import com.limxing.app.recycleview.ui.ViewImageActivity;
import com.limxing.app.view.DemoView;
import com.limxing.library.utils.ToastUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by limxing on 16/1/23.
 */
public class LoginActivity extends BaseActivity {


    private DemoView view1;
    private Timer time;
    private LocationClient mLocationClient;

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        time.cancel();
//        time=null;
        view1 = null;
        mLocationClient.stop();
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
    public BDLocationListener myListener = new MyLocationListener();

    @Override
    protected void init() {

//        List<String> data = new ArrayList<>();
//        data.add("http://img03.sogoucdn.com/app/a/100520093/39ccc87f3e85c326-d833987af7322860-4ce19032c3b0f23baadb92fbb834ca57.jpg");
//        data.add("http://img03.sogoucdn.com/app/a/100520093/ff997748674109a3-a39fb229dd0dbda7-694fe393ad45dc0aa9ea5a22823a4a89.jpg");
//        data.add("http://img02.sogoucdn.com/app/a/100520093/398e25e72e0c6d43-69bdc558c3bd67b0-cfe3bbc83b1b97766b1f563b5a2ca7f7.jpg");
//        data.add("http://img03.sogoucdn.com/app/a/100520093/11388287d0e56ad7-53b51a5be5b5a2db-9e20f21c3413f36b211a6543ad164d1f.jpg");
//        data.add("http://img03.sogoucdn.com/app/a/100520093/11388287d0e56ad7-53b51a5be5b5a2db-0d4a965d46d4436ed1c7053eccb6fe70.jpg");
//        data.add("http://img03.sogoucdn.com/app/a/100520093/33707f33b97c03ef-e989d519207501fc-417f0b65c0bd38f89fa860dc6e331204.jpg");
//        data.add("http://img03.sogoucdn.com/app/a/100520093/ea54b1c5225b5b8f-1f7d693ce5c84217-c3bf467271f05ac7fb1b65bcd04075df.jpg");
//        data.add("http://img01.sogoucdn.com/app/a/100520093/ea54b1c5225b5b8f-1f7d693ce5c84217-29186a9893391156126abf6b88edb947.jpg");
//        data.add("http://img04.sogoucdn.com/app/a/100520024/a9fd5fa28fa88b93b3a551d77d7485af");
//        data.add("http://img03.sogoucdn.com/app/a/100520024/5ebc6321d6d250cdc60b60c63d112398");
//        data.add("http://img04.sogoucdn.com/app/a/100520024/da58c325457e35bc35ef5b88ff6e8f93");
//        data.add("http://img04.sogoucdn.com/app/a/100520024/d8cd08e9ad5e594e6072b79b16a79cb9");
//        data.add("http://img03.sogoucdn.com/app/a/100520024/e26644a572d792eb04c758fb7928cf6f");
//        Intent intent = new Intent(this, ViewImageActivity.class);
//        intent.putExtra(ViewImageActivity.IMAGE_NUM, 1);
//        intent.putExtra(ViewImageActivity.IMAGES_DATA_LIST, (Serializable) data);
//        startActivity(intent);
        mLocationClient = new LocationClient(getApplicationContext());     //声明LocationClient类
        mLocationClient.registerLocationListener(myListener);    //注册监听函数
        initLocation();
        mLocationClient.start();

    }


    private void initLocation(){
        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy
        );//可选，默认高精度，设置定位模式，高精度，低功耗，仅设备
        option.setCoorType("bd09ll");//可选，默认gcj02，设置返回的定位结果坐标系
        int span=1000;
        option.setScanSpan(span);//可选，默认0，即仅定位一次，设置发起定位请求的间隔需要大于等于1000ms才是有效的
        option.setIsNeedAddress(true);//可选，设置是否需要地址信息，默认不需要
        option.setOpenGps(true);//可选，默认false,设置是否使用gps
        option.setLocationNotify(true);//可选，默认false，设置是否当gps有效时按照1S1次频率输出GPS结果
        option.setIsNeedLocationDescribe(true);//可选，默认false，设置是否需要位置语义化结果，可以在BDLocation.getLocationDescribe里得到，结果类似于“在北京天安门附近”
        option.setIsNeedLocationPoiList(true);//可选，默认false，设置是否需要POI结果，可以在BDLocation.getPoiList里得到
        option.setIgnoreKillProcess(false);//可选，默认true，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认不杀死
        option.SetIgnoreCacheException(false);//可选，默认false，设置是否收集CRASH信息，默认收集
        option.setEnableSimulateGps(false);//可选，默认false，设置是否需要过滤gps仿真结果，默认需要
        mLocationClient.setLocOption(option);
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
        ImageUtil.getImageFromCamera(this);
        time = new Timer();
//        time.schedule(new TimerTask() {
//            @Override
//            public void run() {
//                tv.setText("" + i++);
//                runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        view1.postInvalidate();
//                        if (i==5){
//                            view1.removeText();
//                            time.cancel();
//                            i=0;
//                        }
//                    }
//                });
//            }
//        }, 0, 1000);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
    }
}
