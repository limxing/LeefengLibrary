package com.limxing.app;

import android.annotation.TargetApi;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Build;
import android.os.Bundle;

import android.support.v7.app.AppCompatActivity;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.limxing.library.BottomDialog.AlertDialog;
import com.limxing.library.BottomDialog.BottomDialog;
import com.limxing.library.BottomDialog.LMBottomSelecter;
import com.limxing.library.PullToRefresh.SwipeRefreshLayout;

import com.limxing.library.SVProgressHUD.SVProgressHUD;
import com.limxing.library.SweetAlert.SweetAlertDialog;
import com.limxing.library.SwipeBack.SwipeBackActivity;

import com.limxing.library.NoTitleBar.SystemBarTintManager;

import com.limxing.library.SwipeBack.SwipeBackLayout;
import com.limxing.library.utils.EncryptUtil;
import com.limxing.library.utils.LogUtils;
import com.limxing.library.utils.ToastUtils;

import java.math.MathContext;


public class MainActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener, SwipeRefreshLayout.OnLoadListener {


    private SwipeRefreshLayout main_fresh;
    private ListView main_listview;
    private AlertDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SystemBarTintManager.initSystemBar(this, R.color.transparent);

        main_fresh = (com.limxing.library.PullToRefresh.SwipeRefreshLayout) findViewById(R.id.main_fresh);

        SwipeBackLayout swipeBackLayout = (SwipeBackLayout) findViewById(R.id.swipeBackLayout);
        swipeBackLayout.setDragEdge(SwipeBackLayout.DragEdge.LEFT);


        main_listview = (ListView) findViewById(R.id.main_listview);
        main_listview.setAdapter(new BaseAdapter() {
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
                TextView textView = new TextView(MainActivity.this);
                textView.setText("你是谁");
                textView.setHeight(200);
                return textView;
            }
        });
        findViewById(R.id.btn_bottom).setOnClickListener(new View.OnClickListener() {
            @TargetApi(Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View view) {

                new SVProgressHUD(MainActivity.this).showSuccessWithStatus("成功");
//                BottomDialog.showAlert(MainActivity.this, "哈哈哈", new String[]{"你好", "你不好"},
//                        new BottomDialog.OnClickListener() {
//                            @Override
//                            public void onClick(int which) {
//                                ToastUtils.showLong(MainActivity.this, which + "个");
//                            }
//                        }, new DialogInterface.OnCancelListener() {
//                            @Override
//                            public void onCancel(DialogInterface dialogInterface) {
//                                ToastUtils.showLong(MainActivity.this,"已关闭");
//                            }
//                        });

                //错误的提示框
//               new SweetAlertDialog(MainActivity.this,SweetAlertDialog.ERROR_TYPE)
//                       .setTitleText("网络错误")
//                       .setContentText("请重试")
//                       .show();
//                成功的提示框
//                new SweetAlertDialog(MainActivity.this,SweetAlertDialog.SUCCESS_TYPE)
//                        .setTitleText("成功").show();
                //加载对话框
//                SweetAlertDialog dialog = new SweetAlertDialog(MainActivity.this, SweetAlertDialog.PROGRESS_TYPE)
//                        .setTitleText("加载中...");
//                dialog.setCancelable(false);
//                dialog.show();
                //警告对话框,取消和确定
//                new SweetAlertDialog(MainActivity.this,SweetAlertDialog.WARNING_TYPE)
//                        .setTitleText("确定退出吗?")
//                        .setCancelText("取消")
//                        .setConfirmText("确定")
//                        .showCancelButton(true)
//                        .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
//                            @Override
//                            public void onClick(SweetAlertDialog sweetAlertDialog) {
//                                sweetAlertDialog.dismissWithAnimation();
//                            }
//                        })
//                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
//                            @Override
//                            public void onClick(SweetAlertDialog sweetAlertDialog) {
//                                sweetAlertDialog.setTitleText("已删除")
////                                        .setContentText("Your imaginary file has been deleted!")
//                                        .setConfirmText("确定")
//                                        .showCancelButton(false)
//                                        .setCancelClickListener(null)
//                                        .setConfirmClickListener(null)
//                                        .changeAlertType(SweetAlertDialog.SUCCESS_TYPE);
//                            }
//                        }).show();


//                LMBottomSelecter selecter=new LMBottomSelecter(getApplicationContext(),new String[]{},"haha");
//                selecter.show(findViewById(R.id.swipeBackLayout));


//                dialog = new AlertDialog(MainActivity.this, findViewById(R.id.swipeBackLayout)) {
//
//
//                    @Override
//                    public void closed() {
//
//                    }
//
//                    @Override
//                    protected void selectionClick(int tag) {
//                        switch (tag) {
//                            case 0:
//                                ToastUtils.showLong(MainActivity.this, "第一个");
//                                break;
//                            case 1:
//                                ToastUtils.showLong(MainActivity.this, "第二个");
//                                break;
//                        }
//                    }
//                };
//                dialog.setSelections(new String[]{"你好", "你好", "你好"});
//                dialog.setDescription("这是我精心准备的底部弹窗,这是我精心准备的底部弹窗,这是我精心准备的底部弹窗");
//
//                dialog.show();


//                WindowManager.LayoutParams lp = getWindow().getAttributes();
////                getWindow().setBackgroundDrawable();
//                lp.alpha = 0.5f; //0.0-1.0
//                getWindow().setAttributes(lp);


//                Intent intent = new Intent(MainActivity.this, BottomDialog.class);
//                startActivityForResult(intent, 0);
//                AlertDialog dialog= new AlertDialog.Builder(MainActivity.this).setView(R.layout.bottomdialog).create();
//
//                Window window= dialog.getWindow();
//                window.setWindowAnimations(com.limxing.library.R.style.myBottom);
//                window.setGravity(Gravity.BOTTOM);
//
//                dialog.show();
//                WindowManager windowManager = getWindowManager();
//                Display display = windowManager.getDefaultDisplay();
//                WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
//                lp.width = display.getWidth(); //设置宽度
//                window.setAttributes(lp);


            }
        });
        main_fresh.setOnRefreshListener(this);
        main_fresh.setOnLoadListener(this);
        String username = "woaini";

        LogUtils.i("加密MD5.woaini:" + EncryptUtil.MD5Encode(username));

        ImageView loading = (ImageView) findViewById(R.id.loading);
        loading.setImageResource(R.drawable.loading);
        AnimationDrawable animationDrawable = (AnimationDrawable) loading.getDrawable();
        animationDrawable.start();
    }

    @Override
    public void onLoad() {
        LogUtils.i("onLoad");
    }

    @Override
    public void onRefresh() {
        LogUtils.i("onRefresh");
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
    protected void onPause() {
        if (dialog!=null&&dialog.isShowing()) {
            dialog.dismiss();
        }
        super.onPause();
    }
}
