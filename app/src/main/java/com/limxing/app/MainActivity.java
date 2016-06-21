package com.limxing.app;

import android.annotation.TargetApi;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.limxing.app.activity.CameraActivity;
import com.limxing.app.bean.Student;
import com.limxing.library.CirculProgressBar.TasksCompletedView;
import com.limxing.library.CustomDialog.DataDialog;
import com.limxing.library.PullToRefresh.SwipeRefreshLayout;

import com.limxing.library.NoTitleBar.SystemBarTintManager;

import com.limxing.library.XListView.XListView;
import com.limxing.library.utils.EncryptUtil;
import com.limxing.library.utils.LogUtils;
import com.limxing.library.utils.PhoneInfo;
import com.limxing.library.utils.ToastUtils;
import com.limxing.publicc.alertview.AlertView;
import com.limxing.publicc.alertview.OnConfirmeListener;
import com.limxing.publicc.alertview.OnItemClickListener;

import java.util.ArrayList;
import java.util.List;



public class MainActivity extends AppCompatActivity implements OnItemClickListener, OnConfirmeListener {

    private SwipeRefreshLayout main_fresh;
    private XListView main_xlistview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        main_xlistview=(XListView)findViewById(R.id.main_xlistview);
        main_xlistview.setAdapter(new BaseAdapter() {
            @Override
            public int getCount() {
                return 0;
            }

            @Override
            public Object getItem(int i) {
                return null;
            }

            @Override
            public long getItemId(int i) {
                return 0;
            }

            @Override
            public View getView(int i, View view, ViewGroup viewGroup) {
                return null;
            }
        });
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


        findViewById(R.id.btn_bottom).setOnClickListener(new View.OnClickListener() {
            @TargetApi(Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View view) {


     //                new AlertView("上传",null,"取消",null,new String[]{"拍照","从相册选择"},MainActivity.this,AlertView.Style.ActionSheet,MainActivity.this).show();
     //                new AlertView("标题", null, "取消", new String[]{"高亮按钮1"}, new String[]{"其他按钮1", "其他按钮2", "其他按钮3"}, MainActivity.this, AlertView.Style.ActionSheet, MainActivity.this).setCancelable(true).show();
     //                new AlertView("标题", "内容", null, new String[]{"确定"}, null, MainActivity.this, AlertView.Style.Alert, MainActivity.this).show();

     //                年的选择
     //                List<String> list = new ArrayList<String>();
     //                for (int i = 0; i < 20; i++) {
     //                    list.add(2000 + i + "年");
     //                }
     //                new AlertView("请选择日期",MainActivity.this,1990,2100,null,MainActivity.this).show();
     //                new AlertView("选择日期",MainActivity.this,list,list,null,MainActivity.this).setCancelable(true).show();
     //                DataDialog dataDialog = new DataDialog(MainActivity.this);
     //                dataDialog.setViewItems(list, list, null);
     //                dataDialog.setDataDialogListener(new DataDialog.DataDialogListener() {
     //                    @Override
     //                    public void onItemSelected(String year, String month, String day) {
     //                        LogUtils.i(year + month + day);
     //                    }
     //                });
     //                dataDialog.show();

     //                自定义Viewpager
     //                Intent intent = new Intent(MainActivity.this, FirstActivity.class);
     //                startActivity(intent);


     //弹出加载中仿IOS的框
     //                new SVProgressHUD(MainActivity.this).showLmWithStatus("加载中...", SVProgressHUD.SVProgressHUDMaskType.Clear);
                     ;
              


//弹出底部选择框
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


//                SweetAlertDialog的用法
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

//自定义的底部选择框(已弃用)
//                LMBottomSelecter selecter=new LMBottomSelecter(getApplicationContext(),new String[]{},"haha");
//                selecter.show(findViewById(R.id.swipeBackLayout));

//自定义的底部选择框(已弃用)
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

//自定义的底部选择框(已弃用)
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
        String username = "woaini";

        LogUtils.i("加密MD5.woaini:" + EncryptUtil.MD5Encode(username));

//        ImageView loading = (ImageView) findViewById(R.id.loading);
//        loading.setImageResource(R.drawable.loading);
//        AnimationDrawable animationDrawable = (AnimationDrawable) loading.getDrawable();
//        animationDrawable.start();


        mTasksView = (TasksCompletedView) findViewById(R.id.tasks_view);
        mTotalProgress = 100;
        mCurrentProgress = 0;
        new Thread(new ProgressRunable()).start();

        findViewById(R.id.btn_drag).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, CameraActivity.class);
                startActivity(intent);
            }
        });
    }

    private TasksCompletedView mTasksView;

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

    class ProgressRunable implements Runnable {

        @Override
        public void run() {
            while (mCurrentProgress < mTotalProgress) {
                mCurrentProgress += 1;
                mTasksView.setProgress(mCurrentProgress);
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

}
