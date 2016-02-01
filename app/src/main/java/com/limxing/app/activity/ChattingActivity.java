package com.limxing.app.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.alibaba.mobileim.channel.util.WxLog;
import com.alibaba.mobileim.conversation.YWConversationType;
import com.alibaba.mobileim.conversation.YWMessage;

import com.alibaba.mobileim.utility.ResourceLoader;
import com.limxing.app.BaseActivity;
import com.limxing.app.R;
import com.limxing.app.fragment.ChattingFragment;
import com.limxing.library.SwipeBack.SwipeBackLayout;

/**
 * Created by limxing on 16/1/25.
 */
public class ChattingActivity extends BaseActivity {
    private TextView limxing_title_name;

    @Override
    protected void initView() {
        setContentView(R.layout.chatting);
        SwipeBackLayout swipeBackLayout = (SwipeBackLayout) findViewById(R.id.swipeBackLayout);
        swipeBackLayout.setDragEdge(SwipeBackLayout.DragEdge.LEFT);
        TextView chat_back = (TextView) findViewById(R.id.chat_back);
        chat_back.setVisibility(View.VISIBLE);
        chat_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finishActivity();
            }
        });
        limxing_title_name=(TextView) findViewById(R.id.limxing_title_name);
        createFragment(getIntent());
    }

    @Override
    protected void init() {

    }
    private void createFragment(Intent intent) {

        int type = intent.getIntExtra("conversationType", -1);
        String conversationId = intent.getStringExtra("conversationId");
        String userId = intent.getStringExtra("extraUserId");
        YWMessage atMsg = (YWMessage)this.getIntent().getSerializableExtra("atMsgId");
        if(TextUtils.isEmpty(conversationId) && TextUtils.isEmpty(userId) && type != YWConversationType.Tribe.getValue() && type != YWConversationType.HJTribe.getValue()) {
            this.finish();
        } else {
            ChattingFragment mCurrentFrontFragment = new ChattingFragment(new ChattingFragment.ChattingInterface() {
                @Override
                public void changeText(String text) {
                    limxing_title_name.setText(text);
                }
            });

            Bundle bundle = intent.getExtras();
            if(bundle == null) {
                bundle = new Bundle();
                bundle.putSerializable("unReadAtMsg", atMsg);
            } else {
                bundle.putSerializable("unReadAtMsg", atMsg);
            }

            mCurrentFrontFragment.setArguments(bundle);
            this.getSupportFragmentManager().beginTransaction().replace(R.id.limxing_chatting_frame, mCurrentFrontFragment).commitAllowingStateLoss();
        }
    }
}
