package com.limxing.app.activity;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.limxing.app.R;
import com.limxing.app.fragment.MyFragment;

import com.limxing.library.NoTitleBar.SystemBarTintManager;
import com.limxing.app.view.SlidMenu;
import com.limxing.library.SVProgressHUD.SVProgressHUD;

import java.util.List;

/**
 * Created by limxing on 16/3/27.
 */
public class DemoActivity extends AppCompatActivity {
    private ListView listView;
    private List<Fragment> mList;
    private SlidMenu slidMenu;


    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SystemBarTintManager.initSystemBar(this, R.color.transparent);
        setContentView(R.layout.activity_demo);
         slidMenu=(SlidMenu) findViewById(R.id.slidMenu);


        final MyFragment mf = new MyFragment("第一个", Color.WHITE);

        final FragmentManager fm = getFragmentManager();
        FragmentTransaction fr =fm.beginTransaction();
        fr.add(R.id.container, mf, "mf").commit();

        listView = (ListView) findViewById(R.id.listView);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if(i<5) {
                    MyFragment mf2 = new MyFragment("第二个",Color.BLUE);
                    fm.beginTransaction().replace(R.id.container, mf2, "fg2").commit();
                }
                if(10>i&&i>5){
                    fm.beginTransaction().remove(fm.findFragmentByTag("fg2")).commit();
                }
            }
        });
        listView.setAdapter(new BaseAdapter() {
            @Override
            public int getCount() {
                return 100;
            }

            @Override
            public Object getItem(int i) {
                return 0;
            }

            @Override
            public long getItemId(int i) {
                return i;
            }

            @Override
            public View getView(int i, View view, ViewGroup viewGroup) {
                TextView t = new TextView(DemoActivity.this);
                t.setText("woshimingxing" + i);

                return t;
            }
        });
//        SVProgressHUD svProgressHUD=new SVProgressHUD(this);
//        svProgressHUD.showLoading("dasda", SVProgressHUD.SVProgressHUDMaskType.Clear);

    }
}
