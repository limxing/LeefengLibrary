package com.limxing.app.activity;

import android.content.Intent;

import com.alibaba.mobileim.IYWLoginService;
import com.alibaba.mobileim.YWLoginParam;
import com.alibaba.mobileim.channel.event.IWxCallback;
import com.limxing.app.BaseActivity;
import com.limxing.app.R;
import com.limxing.app.application.MyApplication;
import com.limxing.library.utils.LogUtils;
import com.limxing.library.utils.SharedPreferencesUtil;
import com.limxing.library.utils.ToastUtils;

/**
 * Created by limxing on 16/1/25.
 */
public class WelcomeActivity extends BaseActivity {
    @Override
    protected void initView() {
        setContentView(R.layout.welcome);

    }

    @Override
    protected void init() {
        new Thread() {
            @Override
            public void run() {
                try {
                    sleep(3000);
                    String username = SharedPreferencesUtil.getStringData(WelcomeActivity.this, "username", "");
                    if (username.isEmpty()) {
                        Intent intent = new Intent(WelcomeActivity.this, LoginActivity.class);
                        startActivityWithAnim(intent);
                        finish();
                    } else {
                        IYWLoginService loginService = MyApplication.mIMKit.getLoginService();
                        YWLoginParam loginParam = YWLoginParam.createLoginParam(username
                                , username);
                        loginService.login(loginParam, new IWxCallback() {

                            @Override
                            public void onSuccess(Object... arg0) {
                                Intent intent =new Intent(WelcomeActivity.this,AllConversationActivity.class);
                                startActivityWithAnim(intent);
                                finish();
                            }

                            @Override
                            public void onProgress(int arg0) {
                                // TODO Auto-generated method stub
                            }

                            @Override
                            public void onError(int errCode, String description) {
                                Intent intent = new Intent(WelcomeActivity.this, LoginActivity.class);
                                startActivityWithAnim(intent);
                                finish();
                            }
                        });
                    }


                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }.start();

    }
}
