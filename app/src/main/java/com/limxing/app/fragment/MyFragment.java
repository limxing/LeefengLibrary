package com.limxing.app.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.limxing.library.utils.LogUtils;

/**
 * Created by limxing on 16/3/28.
 */
public class MyFragment extends Fragment {
    private final int color;
    private String text;
    public MyFragment(String s,int color){
        super();
        this.text=s;
        this.color=color;
        LogUtils.i("MyFragment==MyFragment");

    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LogUtils.i("MyFragment==onCreate");
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        LogUtils.i("MyFragment==onAttach.context");
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        LogUtils.i("MyFragment==onAttach.activity");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        LogUtils.i("MyFragment==onCreateView");
        TextView t=new TextView(getActivity());

        t.setBackgroundColor(color);
        t.setText(text);
        return t;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        LogUtils.i("MyFragment==onActivityCreated");

    }

    @Override
    public void onStart() {
        super.onStart();
        LogUtils.i("MyFragment==onStart");
    }

    @Override
    public void onResume() {
        super.onResume();
        LogUtils.i("MyFragment==onResume");
    }


    @Override
    public void onPause() {
        super.onPause();
        LogUtils.i("MyFragment==onPause");
    }

    @Override
    public void onStop() {
        super.onStop();
        LogUtils.i("MyFragment==onStop");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        LogUtils.i("MyFragment==onDestroyView");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        LogUtils.i("MyFragment==onDestroy");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        LogUtils.i("MyFragment==onDetach");
    }

}
