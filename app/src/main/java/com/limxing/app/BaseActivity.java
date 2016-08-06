package com.limxing.app;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.view.WindowManager;

import com.limxing.library.NoTitleBar.StatusBarCompat;
import com.limxing.library.NoTitleBar.SystemBarTintManager;
import com.limxing.library.Permission.EasyPermissions;

import java.util.List;

/**
 * Created by limxing on 15/12/1.
 */
public abstract class BaseActivity extends AppCompatActivity  implements
        EasyPermissions.PermissionCallbacks {
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
        overridePendingTransition(com.limxing.library.R.anim.push_left_in, com.limxing.library.R.anim.push_left_out);
    }

    // 切换界面动画关闭一个Activiyty
    protected void finishActivity() {
        finish();
        overridePendingTransition(com.limxing.library.R.anim.push_right_in, com.limxing.library.R.anim.push_right_out);
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

    /**
     * 检查权限
     * @param listener 全县坚挺
     * @param resString 全县提示
     * @param mPerms 全县内容
     */
    public void checkPermission(CheckPermListener listener, String resString, String... mPerms) {
        mListener = listener;
        if (EasyPermissions.hasPermissions(this, mPerms)) {
            if (mListener != null)
                mListener.superPermission();
        } else {
            EasyPermissions.requestPermissions(this, resString,
                    RC_PERM, mPerms);
        }
    }

    /**
     * 用户权限处理,
     * 如果全部获取, 则直接过.
     * 如果权限缺失, 则提示Dialog.
     *
     * @param requestCode  请求码
     * @param permissions  权限
     * @param grantResults 结果
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    //同意了全部权限
    @Override
    public void onPermissionsAllGranted() {
        if (mListener != null)
        mListener.superPermission();//同意了全部权限的回调
    }

    //权限被拒绝
    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {

        EasyPermissions.checkDeniedPermissionsNeverAskAgain(this,
                getString(R.string.perm_tip),
                R.string.setting, R.string.cancel, null, perms);
    }

    //同意了某些权限可能不是全部
    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {

    }
}
