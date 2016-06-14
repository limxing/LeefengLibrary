package com.limxing.app.activity;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

//import com.alibaba.mobileim.IYWLoginService;
//import com.alibaba.mobileim.WXAPI;
//import com.alibaba.mobileim.YWAPI;
//import com.alibaba.mobileim.YWIMKit;
//import com.alibaba.mobileim.YWLoginParam;
//import com.alibaba.mobileim.channel.event.IWxCallback;
//import com.alibaba.mobileim.channel.util.WxLog;
import com.limxing.app.BaseActivity;
import com.limxing.app.R;
import com.limxing.app.application.MyApplication;
import com.limxing.library.utils.LogUtils;
import com.limxing.library.utils.SharedPreferencesUtil;
import com.limxing.library.utils.ToastUtils;

/**
 * Created by limxing on 16/1/23.
 */
public class LoginActivity extends BaseActivity {
    private EditText login_name;

    private String mUserId;


    @Override
    protected void initView() {
        setContentView(R.layout.login);
        login_name=(EditText)findViewById(R.id.login_name);

       findViewById(R.id.login_login).setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               if(login_name.getText().toString().isEmpty()){
                   ToastUtils.showLong(LoginActivity.this,"请输入手机号码");
               }else{
//                   IYWLoginService loginService = MyApplication.mIMKit.getLoginService();
//                   String username="llf"+login_name.getText().toString().trim();
//                   YWLoginParam loginParam = YWLoginParam.createLoginParam(username
//                           , username);
//                   loginService.login(loginParam, new IWxCallback() {
//
//                       @Override
//                       public void onSuccess(Object... arg0) {
////                          if( WXAPI.getInstance().getLoginUserId().isEmpty()){
////                              ToastUtils.showLong(LoginActivity.this,"登录失败");
////                              return;
////                           }
//                           SharedPreferencesUtil.saveStringData(LoginActivity.this,"username","llf"+login_name.getText().toString().trim());
//                           LogUtils.i("login success:" + arg0.toString());
//                           Intent intent =new Intent(LoginActivity.this,AllConversationActivity.class);
//                           startActivityWithAnim(intent);
//                           finish();
//                       }
//
//                       @Override
//                       public void onProgress(int arg0) {
//                           // TODO Auto-generated method stub
//                       }
//
//                       @Override
//                       public void onError(int errCode, String description) {
//                           //如果登录失败，errCode为错误码,description是错误的具体描述信息
//                           LogUtils.i("login onError:"+errCode+"=="+description);
//                           ToastUtils.showLong(LoginActivity.this,description);
//                       }
//                   });
               }
           }
       });
    }

    @Override
    protected void init() {


    }


}
