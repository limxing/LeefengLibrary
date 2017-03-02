package com.leefeng.app.application;

import android.app.Application;

//import com.alibaba.mobileim.IYWLoginService;
//import com.alibaba.mobileim.IYWPushListener;
//import com.alibaba.mobileim.YWAPI;
//import com.alibaba.mobileim.YWIMCore;
//import com.alibaba.mobileim.YWIMKit;
//import com.alibaba.mobileim.channel.event.IWxCallback;
//import com.alibaba.mobileim.contact.IYWContact;
//import com.alibaba.mobileim.conversation.IYWConversationUnreadChangeListener;
//import com.alibaba.mobileim.conversation.YWConversation;
//import com.alibaba.mobileim.conversation.YWMessage;
//import com.alibaba.mobileim.gingko.model.tribe.YWTribe;
//import com.alibaba.mobileim.login.IYWConnectionListener;
//import com.alibaba.wxlib.util.SysUtil;

//import com.umeng.openim.OpenIMAgent;

/**
 * Created by limxing on 16/1/23.
 */
public class MyApplication extends Application {
//    public static YWIMCore imCore;
//    public static YWIMKit mIMKit;
    private static boolean isLogin = false;

    //    public static YWIMKit mIMKit=null;
    @Override
    public void onCreate() {
        super.onCreate();


//        SysUtil.setApplication(this);
//        if (SysUtil.isTCMSServiceProcess(this)) {
//            return;  //todo 特别注意：此处return是退出onCreate函数，因此不能封装到其他任何方法中!
//        }
//
//        YWAPI.enableSDKLogOutput(true);
//
//
//        final OpenIMAgent im = OpenIMAgent.getInstance(this);
//        im.init();
//
//        mIMKit = YWAPI.getIMKitInstance();
//        imCore = mIMKit.getIMCore();
//        //添加连接状态监听，即登录状态监听
//        imCore.addConnectionListener(new IYWConnectionListener() {
//            @Override
//            public void onDisconnect(final int i, String s) {
//                if (i == -3) {
//
//
//                    ToastUtils.showLong(getApplicationContext(), i + "==" + s);
////                im.init();
//                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
//                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                startActivity(intent);
////                IYWLoginService loginService = imCore.getLoginService();
////                loginService.logout(new IWxCallback() {
////
////                    @Override
////                    public void onSuccess(Object... arg0) {
////                        //登出成功
////
////
////                    }
////
////                    @Override
////                    public void onProgress(int arg0) {
////                        // TODO Auto-generated method stub
////                    }
////
////                    @Override
////                    public void onError(int errCode, String description) {
////                        //登出失败，errCode为错误码,description是错误的具体描述信息
////                    }
////                });
//                    //掉线
//                }
//            }
//
//            @Override
//            public void onReConnecting() {
//                //正在重登
//            }
//
//            @Override
//            public void onReConnected() {
//                ToastUtils.showLong(getApplicationContext(), "登录成功");
//                //重登成功
//            }
//        });
//
//        imCore.getConversationService().addPushListener(new IYWPushListener() {
//            @Override
//            //收到单聊消息时会回调该方法，开发者可以在该方法内更新该会话的未读数
//            public void onPushMessage(IYWContact arg0, YWMessage arg1) {
//                //单聊消息
//                YWConversation conversation = imCore.getConversationService().getConversationByUserId(arg0.getUserId());
//                int unreadCount = conversation.getUnreadCount();
//                //TODO 更新UI上该会话未读数
//            }
//
//            @Override
//            //收到群聊消息时会回调该方法，开发者可以在该方法内更新该会话的未读数
//            public void onPushMessage(YWTribe arg0, YWMessage arg1) {
//                //群消息
//                YWConversation conversation = imCore.getConversationService().getTribeConversation(arg0.getTribeId());
//                int unreadCount = conversation.getUnreadCount();
//                //TODO 更新UI上该会话未读数
//            }
//        });


        //注册消息未读总数监听
//        imCore.getConversationService().addTotalUnreadChangeListener(new IYWConversationUnreadChangeListener() {
//            @Override
//            //当前登录账号的未读消息总数发送变化时会回调该方法，用户可以再该方法中更新UI的未读数
//            public void onUnreadChange() {
//                int totalUnreadCount = imCore.getConversationService().getAllUnreadCount();
//                //TODO 更新UI的未读数
//            }
//        });
    }
}
