package me.leefeng.library.promptview;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import me.leefeng.library.R;

/**
 * Created by limxing on 2017/5/7.
 */

public class PromptView {

    private Context context;
    private ViewGroup decorView;

    public PromptView(Activity context) {
        this.context=context;
//        LayoutInflater layoutInflater = LayoutInflater.from(context);
        decorView = (ViewGroup) context.getWindow().getDecorView().findViewById(android.R.id.content);
    }

    public void showLoading(String msg) {

        LoadView loadView=  new  LoadView(context);
        loadView.setText(msg);

        decorView.addView(loadView);

//        rootView = (ViewGroup) layoutInflater.inflate(R.layout.layout_alertview, decorView, false);
//        rootView.setLayoutParams(new FrameLayout.LayoutParams(
//                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT
//        ));

    }


}
