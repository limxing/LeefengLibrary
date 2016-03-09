package com.limxing.app.SweepActivitys;

import android.inputmethodservice.Keyboard;
import android.inputmethodservice.KeyboardView;
import android.os.Bundle;
import android.os.Handler;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.limxing.app.R;
import com.limxing.library.DatePicker.DatePickerView;
import com.limxing.library.NoTitleBar.SystemBarTintManager;
import com.limxing.library.PullToRefresh.SwipeRefreshLayout;
import com.limxing.library.SwipeBack.SwipeBackActivity;
import com.limxing.library.utils.LogUtils;

/**
 * Created by limxing on 16/2/16.
 */
public class FirstActivity extends SwipeBackActivity implements SwipeRefreshLayout.OnRefreshListener,
        SwipeRefreshLayout.OnLoadListener {
    private ListView main_listview;
    private SwipeRefreshLayout main_fresh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first);
        SystemBarTintManager.initSystemBar(this, R.color.transparent);
        main_fresh = (com.limxing.library.PullToRefresh.SwipeRefreshLayout) findViewById(R.id.main_fresh);
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
                TextView textView = new TextView(FirstActivity.this);
                textView.setText("你是谁");
                textView.setHeight(200);
                return textView;
            }
        });
        main_fresh.setOnRefreshListener(this);
        main_fresh.setOnLoadListener(this);
    }

    public void next(View view) {

        DatePickerView view1 = new DatePickerView(FirstActivity.this, new DatePickerView.DatePickerListener() {
            @Override
            public void dateChange(String string) {
                LogUtils.i(string);

            }

            @Override
            public void finish() {

            }
        });
        view1.show();
//        Intent intent = new Intent(FirstActivity.this, FTwoActivity.class);
//        startActivity(intent);
//        BottomSelect.showAlert(FirstActivity.this, "请选择添加图片方式", new String[]{"相机", "相册"}, new BottomSelect.OnClickListener() {
//            @Override
//            public void onClick(int which) {
//
//            }
//        }, new DialogInterface.OnCancelListener() {
//            @Override
//            public void onCancel(DialogInterface dialogInterface) {
//
//            }
//        });
//        AlertDialog.Builder builder = new AlertDialog.Builder(FirstActivity.this);
//        builder.setItems(new String[]{"第一个", "第二个", "第三个"}, new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialogInterface, int i) {
//
//            }
//        });
//        AlertDialog dialog = builder.create();
//        Window w = dialog.getWindow();
////       WindowManager.LayoutParams lp= w.getAttributes();
//        w.setGravity(Gravity.BOTTOM);
//        w.setWindowAnimations(R.style.myBottom);
//        dialog.show();

    }

    @Override
    public void onLoad() {
        new Handler().postDelayed(new Runnable() {

            public void run() {

                //execute the task
                main_fresh.setLoading(false);

            }

        }, 3000);
    }

    @Override
    public void onRefresh() {
        new Handler().postDelayed(new Runnable() {

            public void run() {

                //execute the task
                main_fresh.setRefreshing(false);

            }

        }, 3000);
    }

}
