package com.leefeng.app;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.view.WindowManager;

import me.leefeng.library.NoTitleBar.StatusBarCompat;

import java.util.List;

/**
 * Created by limxing on 15/12/1.
 */
public abstract class BaseActivity extends AppCompatActivity   {
    protected static final int RC_PERM = 123;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        SystemBarTintManager.initSystemBar(this);
        StatusBarCompat.translucentStatusBar(this);
        initView();
        init();
    }

    /**
     * 初始化界面
     */
    protected abstract void initView();

    /**
     * 初始化数据
     */
    protected abstract void init();

    // 切换界面动画开启一个Activiyty
    protected void startActivityWithAnim(Intent intent) {
        startActivity(intent);
        overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
    }

    // 切换界面动画关闭一个Activiyty
    protected void finishActivity() {
        finish();
        overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
    }
    //屏幕全屏
    protected  void fullScreen(){
        requestWindowFeature(Window.FEATURE_NO_TITLE);//隐藏标题栏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);//隐藏状态栏
    }
    /**
     * 权限回调接口
     */
    private CheckPermListener mListener;

    public interface CheckPermListener {
        //权限通过后的回调方法
        void superPermission();
    }


}
