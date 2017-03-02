package com.leefeng.app.recycleview.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.leefeng.app.R;
import com.leefeng.app.recycleview.util.Util;

import java.util.List;



/**
 * Created by linlongxin on 2016/1/22.
 */
public class ViewImageActivity extends AppCompatActivity {

    public static final String IMAGES_DATA_LIST = "DATA_LIST";
    public static final String IMAGE_NUM = "IMAGE_NUM";

    private Toolbar toolbar;
    private ViewPager viewPager;
    private TextView number;
    private List<String> data;
    private int position;
    private int dataLength = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_image);

        Util.init(this);
        setTitle("返回");

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        viewPager = (ViewPager) findViewById(R.id.view_pager);
        number = (TextView) findViewById(R.id.number);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        data = (List<String>) getIntent().getSerializableExtra(IMAGES_DATA_LIST);//能够传递对象
        position = getIntent().getIntExtra(IMAGE_NUM, -1);
        dataLength = data.size();

        viewPager.setAdapter(new ImageAdapter(data,this));


        viewPager.setCurrentItem(position);
        number.setText(position + 1 + "/" + dataLength);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                number.setText(viewPager.getCurrentItem() + 1 + "/" + dataLength);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_look_image, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        } else if (item.getItemId() == R.id.save) {
            Util.downloadImage(data.get(viewPager.getCurrentItem()));
        }
        return true;
    }


}
