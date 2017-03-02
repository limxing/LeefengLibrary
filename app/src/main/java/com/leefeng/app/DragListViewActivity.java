package com.leefeng.app;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import me.leefeng.library.DragList.DragListAdapter;
import me.leefeng.library.DragList.DragListView;

import java.util.ArrayList;

/**
 * Created by limxing on 16/1/12.
 */
public class DragListViewActivity  extends AppCompatActivity{
    private DragListAdapter mAdapter = null;
    private ArrayList<String> mData = new ArrayList<String>();


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.drag_list_main);

        initView();
    }


    private void initView() {
        initData();//

        DragListView dragListView = (DragListView) findViewById(R.id.other_drag_list);
        mAdapter = new DragListAdapter(this, mData);
        dragListView.setAdapter(mAdapter);
    }

    public void initData() {
        //  ˝æ›Ω·π˚
        mData = new ArrayList<String>();
        for (int i = 0; i < 10; i++) {
            mData.add("limxing的第" + i + "个老婆");
        }
    }
}
