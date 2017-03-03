package com.leefeng.app;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;

import java.util.List;

import me.leefeng.library.NoTitleBar.SystemBarTintManager;

import me.leefeng.library.Permission.EasyPermissions;
import me.leefeng.library.Swipeback.SwipeBackHelper;
import me.leefeng.library.utils.EncryptUtil;
import me.leefeng.library.utils.LogUtils;
import me.leefeng.library.utils.PhoneInfo;
import me.leefeng.library.utils.ToastUtils;
import me.leefeng.publicc.alertview.OnConfirmeListener;
import me.leefeng.publicc.alertview.OnItemClickListener;


public class MainActivity extends AppCompatActivity implements OnItemClickListener, OnConfirmeListener
        , EasyPermissions.PermissionCallbacks{


    private static final int NUM = 1001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SystemBarTintManager.initSystemBar(this);
        SwipeBackHelper.onCreate(this);
        SwipeBackHelper.getCurrentPage(this)
                .setSwipeBackEnable(true)
                .setSwipeSensitivity(0.5f)
                .setSwipeRelateEnable(true)
                .setSwipeRelateOffset(300);
        setContentView(R.layout.activity_main);
//        main_xlistview.set
//        MyThreadPool.excuteCachedTask(new Runnable() {
//            @Override
//            public void run() {
//                Net.requestNet(10,"http://p.gdown.baidu.com/f29f27f966ac7b50f4fdedd65c5b1d9e34715de9b6a3b98f" +
//                        "6db8560d94582cf487c0bbd4357af9680fc8c9f7a498a0e0f0d61293f65d316b88b279bfc257ba3" +
//                        "83592a64162d4f72352923f1145ddb1e458224fc809bcdc412cea2d229f513ef2fcbc374db7f570" +
//                        "a613319dfc26719a8acba2e70005a5fdf589a871b7a567c52829da093e4130e0fe4fda4521ff185" +
//                        "ca67c88cd52a523629642a9987241523be4acf92e0fdec5c6bfe87a8fba48f4c346dd4c3969deca" +
//                        "53e3f8c382969586bae42093102d7e6c0d3e9c2392277acd05d36b15ca32db8522cd8635deb88fd" +
//                        "01893aa47d9a457f8c30d1774366012a2f9de1607e9ae8f050802777bf2a32bf98d0092d257acee" +
//                        "faf81edaf3df116071027b9200de49dc6471fe");//下载蜻蜓
//            }
//        });

        SystemBarTintManager.initSystemBar(this, R.color.transparent);
        PhoneInfo.show(MainActivity.this);
//设置滑动返回
//        SwipeBackLayout swipeBackLayout = (SwipeBackLayout) findViewById(R.id.swipeBackLayout);
//        swipeBackLayout.setDragEdge(SwipeBackLayout.DragEdge.LEFT);


        String username = "woaini";

        LogUtils.i("加密MD5.woaini:" + EncryptUtil.MD5Encode(username));

//        ImageView loading = (ImageView) findViewById(R.id.loading);
//        loading.setImageResource(R.drawable.loading);
//        AnimationDrawable animationDrawable = (AnimationDrawable) loading.getDrawable();
//        animationDrawable.start();


        mTotalProgress = 100;
        mCurrentProgress = 0;
        new Thread(new ProgressRunable()).start();


        EasyPermissions.requestPermissions(this, "NEED SMS PLZ", NUM,
                Manifest.permission.CAMERA,Manifest.permission.WRITE_EXTERNAL_STORAGE);
    }


    private int mTotalProgress;
    private int mCurrentProgress;

    /**
     * alertview的点击事件
     * @param o
     * @param position
     */
    @Override
    public void onItemClick(Object o, int position) {
        ToastUtils.showShort(this,position+"");
    }

    @Override
    public void result(String s) {
        ToastUtils.showShort(MainActivity.this,s);
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        // EasyPermissions handles the request result.
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }
    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {

    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {

    }

    class ProgressRunable implements Runnable {

        @Override
        public void run() {
            while (mCurrentProgress < mTotalProgress) {
                mCurrentProgress += 1;
                try {
                    Thread.sleep(100);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (resultCode) {
            case 1:
                LogUtils.i(data.getStringExtra("name"));
                break;
        }

    }
    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        SwipeBackHelper.onPostCreate(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SwipeBackHelper.onDestroy(this);
        //ViewServer.get(this).removeWindow(this);
    }
}
