package com.limxing.app.fragment;

/**
 * Created by limxing on 16/1/25.
 */

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.PowerManager;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.AbsListView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.mobileim.WXAPI;
import com.alibaba.mobileim.YWAPI;
import com.alibaba.mobileim.YWAccountType;
import com.alibaba.mobileim.YWChannel;
import com.alibaba.mobileim.aop.AdviceObjectInitUtil;
import com.alibaba.mobileim.aop.AspectChattingFragment;
import com.alibaba.mobileim.aop.BaseAdvice;
import com.alibaba.mobileim.aop.PointCutEnum;
import com.alibaba.mobileim.callback.SyncContextForMsgCallback;
import com.alibaba.mobileim.channel.IMChannel;
import com.alibaba.mobileim.channel.constant.WXConstant;
import com.alibaba.mobileim.channel.constant.WXType;
import com.alibaba.mobileim.channel.event.IWxCallback;
import com.alibaba.mobileim.channel.message.IMsg;
import com.alibaba.mobileim.channel.util.AccountUtils;
import com.alibaba.mobileim.channel.util.TBSWrapper;
import com.alibaba.mobileim.channel.util.WxLog;
import com.alibaba.mobileim.contact.IYWContact;
import com.alibaba.mobileim.contact.IYWContactHeadClickCallback;
import com.alibaba.mobileim.contact.IYWContactProfileCallback;
import com.alibaba.mobileim.contact.IYWContactService;
import com.alibaba.mobileim.contact.IYWCrossContactProfileCallback;
import com.alibaba.mobileim.contact.YWAppContactImpl;
import com.alibaba.mobileim.conversation.IYWMessageListener;
import com.alibaba.mobileim.conversation.YWConversation;
import com.alibaba.mobileim.conversation.YWConversationManager;
import com.alibaba.mobileim.conversation.YWConversationType;
import com.alibaba.mobileim.conversation.YWFileMessageBody;
import com.alibaba.mobileim.conversation.YWImageMessageBody;
import com.alibaba.mobileim.conversation.YWMessage;
import com.alibaba.mobileim.conversation.YWMessageChannel;
import com.alibaba.mobileim.conversation.YWMessageType;
import com.alibaba.mobileim.conversation.YWP2PConversationBody;
import com.alibaba.mobileim.fundamental.widget.WxAlertDialog;
import com.alibaba.mobileim.fundamental.widget.refreshlist.PullToRefreshBase;
import com.alibaba.mobileim.fundamental.widget.refreshlist.PullToRefreshListView;
import com.alibaba.mobileim.gingko.model.contact.ComparableContact;
import com.alibaba.mobileim.gingko.model.tribe.WxTribe;
import com.alibaba.mobileim.gingko.model.tribe.YWTribeMember;
import com.alibaba.mobileim.gingko.presenter.contact.IContactProfileUpdateListener;
import com.alibaba.mobileim.gingko.presenter.contact.YWContactManagerImpl;
import com.alibaba.mobileim.gingko.presenter.tribe.ITribeManager;
import com.alibaba.mobileim.kit.CloudPwdSettingHintActivity;
import com.alibaba.mobileim.kit.chat.ChattingDetailAdapter;
import com.alibaba.mobileim.kit.chat.EnlargeChattingTextActivity;
import com.alibaba.mobileim.kit.chat.MessageContentOnTouchListener;
import com.alibaba.mobileim.kit.chat.MsgOnTouchListener;
import com.alibaba.mobileim.kit.chat.presenter.ICustomViewChangeListener;
import com.alibaba.mobileim.kit.chat.presenter.NormalChattingDetailPresenter;
import com.alibaba.mobileim.kit.chat.view.IListView;
import com.alibaba.mobileim.kit.chat.view.INormalChattingDetailView;
import com.alibaba.mobileim.kit.chat.widget.ChattingReplayBar;
import com.alibaba.mobileim.kit.common.CheckCodeListener;
import com.alibaba.mobileim.kit.common.IMUtility;
import com.alibaba.mobileim.kit.imageviewer.ImageViewerListener;
import com.alibaba.mobileim.lib.model.conversation.ConversationModel;
import com.alibaba.mobileim.lib.model.message.Message;
import com.alibaba.mobileim.lib.presenter.contact.cache.TribeCache;
import com.alibaba.mobileim.lib.presenter.conversation.Conversation;
import com.alibaba.mobileim.lib.presenter.conversation.HJTribeConversation;
import com.alibaba.mobileim.lib.presenter.conversation.TribeConversation;
import com.alibaba.mobileim.lib.presenter.message.IMessagePresenter;
import com.alibaba.mobileim.lib.presenter.message.MessageList;
import com.alibaba.mobileim.tribe.IYWTribeService;
import com.alibaba.mobileim.tribe.YWTribeManager;
import com.alibaba.mobileim.tribe.YWTribeManagerImpl;
import com.alibaba.mobileim.ui.WxConversationActivity;
import com.alibaba.mobileim.ui.atmessage.AtMsgListActivity;
import com.alibaba.mobileim.ui.atmessage.SendAtMessageDetailActivity;
import com.alibaba.mobileim.ui.chat.ChattingHandlerManager;
import com.alibaba.mobileim.ui.chat.widget.IChattingReply;
import com.alibaba.mobileim.utility.AccountInfoTools;
import com.alibaba.mobileim.utility.IMNotificationUtils;
import com.alibaba.mobileim.utility.IMUtil;
import com.alibaba.mobileim.utility.LeakCanaryHandler;
import com.alibaba.mobileim.utility.ResourceLoader;
import com.alibaba.wxlib.track.Tracker;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class ChattingFragment extends AspectChattingFragment implements AbsListView.OnScrollListener, View.OnTouchListener, View.OnFocusChangeListener, IChattingReply, INormalChattingDetailView {
    private static final String TAG = ChattingFragment.class.getSimpleName();
    private static final String PACKAGE_NAME = "com.alibaba.mobileim";
    private static final String CLASS_NAME = "com.alibaba.mobileim.ui.chat.SlidingMenuActivity";
    public static final String EXTRA_LAUNCH_CLASS_NAME = "launch_class_name";
    public static final String EXTRA_REAL_BACK = "realBack";
    public static final int RECORD_IMAGE_FAKE_REFRESH_INTERVAL = 150;
    public static final String EXTRA_SHARE_ITEMURL = "share_itemurl";
    public static final int AUTH_CHECK_CODE = 6;
    private static final int POST_DELAYED_TIME = 200;
    public static final int minVelocityX = 800;
    public static final String SEND_AT_MSG = "sendAtMsg";
    public static final String SEND_AT_MSG_UNREADLIST = "sendAtMsgUnreadList";
    public static final String SEND_IMAGE_MSG = "SendImageMsg";
    private View mAddButton;
    private View mUnreadView;
    public ChattingInterface chatinterface;
    LinearLayout mCustomLayout;
    View mCustomView;
    private ListView listView;
    private PullToRefreshListView mPullRefreshListView;
    private TextView title;
    private List<YWMessage> list;
    private ChattingDetailAdapter adapter;
    private Handler handler = new Handler();
    private int maxVisibleItemCount = 0;
    private ChattingReplayBar chattingReplyBar;
    private NormalChattingDetailPresenter presenter;
    private ProgressDialog progress;
    private Context mContext;
    private int mCvsType;
    private int mUnreadAtMsgIndex = 0;
    private List<YWMessage> unreadAtMsgList;
    private BaseAdvice baseAdvice;
    private BaseAdvice baseAdviceUI;
    private BaseAdvice baseAdviceOperation;
    private String conversationId;
    private String targetId;
    private String appKey;
    private long tribeId;
    private View view;
    private int accountType;
    private float scale;
    private RelativeLayout mEnterChattingRoomLayout;
    private RelativeLayout mWholeBack;
    private boolean mIsRunning;
    private View atImage;
    private ImageView atUnDisposeView;
    private YWMessage unReadAtMsg;
    private YWMessage atMsg;
    private boolean fragmentFirstCreated;
    private boolean isNeedReviseVisiblePosition;
    private Handler mUIHandler;
    private boolean isAtEnable;
    private int mFirstUnreadMsgPosition;
    private TextView gotoNewMsgsTopTextView;
    private TextView gotoChatListBottomTextView;
    private IContactProfileUpdateListener mContactProfileUpdateListener;
    private List<YWMessage> messagesListCopy;
    private boolean isPullUpToLoad;
    private int mFooterViewHeight;
    private boolean mIsInitCalled;
    private long tid;
    private String cvsId;
    private YWTribeManager tribeManager;
    private ITribeManager iTribeManager;
    private TribeCache tribeCache;
    private HashMap<String, String> tribeMembers;
    private ChattingFragment.LoadUnreadAtMessageTask loadUnreadAtMessageTask;
    private Bundle saveState;
    private IYWContactService contactService;
    private boolean isMsgListEmpty;
    private boolean isFirstItemChanged;
    private View.OnClickListener msgResendClickListener;
    private View.OnClickListener msgRegetClickListener;
    private MessageContentOnTouchListener contentTouchListener;
    private MsgOnTouchListener leftOrRightViewTouchListener;
    private View.OnLongClickListener headOnLongClickListener;
    private View.OnClickListener headOnClickListener;
    private View.OnLongClickListener contentLongClickListener;
    ICustomViewChangeListener customListener;
    private IYWMessageListener mYWMessageListener;
    private IMessagePresenter.IMessageListener mMessageListener;
    private View.OnClickListener contentClickListener;
    private View.OnClickListener unReadCountClickListener;
    private int previousItemHeight;
    private Dialog mPwdDialog;

    public ChattingFragment(ChattingInterface chatinterface) {
        this.chatinterface=chatinterface;
        this.baseAdvice = AdviceObjectInitUtil.initAdvice(PointCutEnum.CHATTING_FRAGMENT_POINTCUT, this);
        this.baseAdviceUI = AdviceObjectInitUtil.initAdvice(PointCutEnum.CHATTING_FRAGMENT_UI_POINTCUT, this);
        this.baseAdviceOperation = AdviceObjectInitUtil.initAdvice(PointCutEnum.CHATTING_FRAGMENT_OPERATION_POINTCUT, this);
        this.fragmentFirstCreated = true;
        this.mUIHandler = new Handler(Looper.getMainLooper());
        this.isPullUpToLoad = false;
        this.tribeMembers = new HashMap();
        this.isMsgListEmpty = false;
        this.isFirstItemChanged = true;
        this.msgResendClickListener = new View.OnClickListener() {
            public void onClick(View v) {
                if(v.getTag() instanceof YWMessage) {
                    ChattingFragment.this.presenter.resendMsg((YWMessage)v.getTag());
                }

            }
        };
        this.msgRegetClickListener = new View.OnClickListener() {
            public void onClick(View v) {
                if(v.getTag() instanceof YWMessage) {
                    ChattingFragment.this.presenter.regetMsg((YWMessage)v.getTag());
                }

            }
        };
        this.contentTouchListener = new MessageContentOnTouchListener(this.mContext, new MessageContentOnTouchListener.OnGestureAndDoubleTapListenerImpl() {
            public boolean onDoubleTap(View v, MotionEvent e) {
                if(v instanceof TextView) {
                    Intent intent = new Intent(ChattingFragment.this.mContext, EnlargeChattingTextActivity.class);
                    intent.putExtra("enhancedChattingText", ((TextView)v).getText().toString());
                    ChattingFragment.this.mContext.startActivity(intent);
                    return true;
                } else {
                    return false;
                }
            }
        });
        this.leftOrRightViewTouchListener = new MsgOnTouchListener(this.mContext, new com.alibaba.mobileim.kit.chat.MsgOnTouchListener.OnGestureAndDoubleTapListenerImpl() {
            public boolean onDoubleTap(View v, MotionEvent e) {
                if(v instanceof RelativeLayout) {
                    Object tag = v.getTag(2130837504);
                    if(tag instanceof YWMessage) {
                        YWMessage msg = (YWMessage)tag;
                        if(msg.getSubType() == 0 || msg.getSubType() == 66) {
                            Object tag2 = v.getTag(2130837505);
                            if(tag2 instanceof Integer) {
                                Intent intent = new Intent(ChattingFragment.this.mContext, EnlargeChattingTextActivity.class);
                                Integer leftOrRight = (Integer)tag2;
                                TextView rightText;
                                if(leftOrRight.intValue() == 0) {
                                    rightText = (TextView)v.findViewById(ResourceLoader.getIdByName("id", "left_text"));
                                    if(rightText != null) {
                                        intent.putExtra("enhancedChattingText", rightText.getText().toString());
                                    }
                                } else {
                                    rightText = (TextView)v.findViewById(ResourceLoader.getIdByName("id", "right_text"));
                                    if(rightText != null) {
                                        intent.putExtra("enhancedChattingText", rightText.getText().toString());
                                    }
                                }

                                ChattingFragment.this.mContext.startActivity(intent);
                            }

                            return true;
                        }
                    }
                }

                return false;
            }
        });
        this.headOnLongClickListener = new View.OnLongClickListener() {
            public boolean onLongClick(View v) {
                if(!YWAPI.getYWSDKGlobalConfig().enableTheTribeAtRelatedCharacteristic()) {
                    return false;
                } else if(ChattingFragment.this.mCvsType != YWConversationType.Tribe.getValue()) {
                    return true;
                } else {
                    Integer position = (Integer)v.getTag(2131361898);
                    YWMessage message = (YWMessage)ChattingFragment.this.list.get(position.intValue());
                    if(WXAPI.getInstance().getLoginUserId().equals(message.getAuthorUserId())) {
                        return true;
                    } else {
                        HashMap atMemberMap = new HashMap();
                        String longUserId = message.getAuthorId();
                        String shortUserId = message.getAuthorUserId();
                        if(ChattingFragment.this.contactService == null) {
                            ChattingFragment.this.contactService = WXAPI.getInstance().getContactService();
                        }

                        IYWContact contact = null;
                        if(ChattingFragment.this.contactService != null) {
                            contact = ChattingFragment.this.contactService.getContactProfileInfo(shortUserId, message.getAuthorAppkey());
                        }

                        if(ChattingFragment.this.tribeMembers != null) {
                            if(IMChannel.getAppId() == 2) {
                                atMemberMap.put(shortUserId, message.getAuthorId());
                            } else if(IMChannel.getAppId() == WXConstant.APPID.APPID_OPENIM) {
                                if(contact != null && shortUserId.equals(ChattingFragment.this.tribeMembers.get(longUserId))) {
                                    atMemberMap.put(longUserId, contact.getShowName());
                                } else {
                                    atMemberMap.put(longUserId, ChattingFragment.this.tribeMembers.get(longUserId));
                                }
                            }
                        } else if(IMChannel.getAppId() == 2) {
                            atMemberMap.put(shortUserId, message.getAuthorId());
                        } else if(IMChannel.getAppId() == WXConstant.APPID.APPID_OPENIM) {
                            if(contact != null) {
                                atMemberMap.put(longUserId, contact.getShowName());
                            } else {
                                atMemberMap.put(longUserId, message.getAuthorUserId());
                            }
                        }

                        ChattingFragment.this.chattingReplyBar.handleAtMembers(atMemberMap, false);
                        return true;
                    }
                }
            }
        };
        this.headOnClickListener = new View.OnClickListener() {
            public void onClick(View v) {
                IYWContactHeadClickCallback headClickCallback = WXAPI.getInstance().getContactHeadClickCallback();
                if(headClickCallback != null) {
                    String profileCallback1 = (String)v.getTag(2131361918);
                    if(TextUtils.isEmpty(profileCallback1)) {
                        profileCallback1 = (String)v.getTag(YWChannel.getIdByName("id", "head"));
                    }

                    Intent crossProfileCallback2 = headClickCallback.onShowProfileActivity(profileCallback1, (String)v.getTag(2131361912));
                    if(crossProfileCallback2 != null) {
                        ChattingFragment.this.startActivity(crossProfileCallback2);
                    }

                } else {
                    IYWContactProfileCallback profileCallback = WXAPI.getInstance().getContactProfileCallback();
                    if(profileCallback != null) {
                        String crossProfileCallback = (String)v.getTag(2131361918);
                        if(TextUtils.isEmpty(crossProfileCallback)) {
                            crossProfileCallback = (String)v.getTag(YWChannel.getIdByName("id", "head"));
                        }

                        Intent userId = profileCallback.onShowProfileActivity(crossProfileCallback);
                        if(userId != null) {
                            ChattingFragment.this.startActivity(userId);
                        }
                    } else {
                        IYWCrossContactProfileCallback crossProfileCallback1 = WXAPI.getInstance().getCrossContactProfileCallback();
                        if(crossProfileCallback1 != null) {
                            String userId1 = (String)v.getTag(2131361918);
                            if(TextUtils.isEmpty(userId1)) {
                                userId1 = (String)v.getTag(YWChannel.getIdByName("id", "head"));
                            }

                            Intent intent = crossProfileCallback1.onShowProfileActivity(userId1, (String)v.getTag(2131361912));
                            if(intent != null) {
                                ChattingFragment.this.startActivity(intent);
                            }
                        }
                    }

                }
            }
        };
        this.contentLongClickListener = new View.OnLongClickListener() {
            public boolean onLongClick(View view) {
                int position = ((Integer)view.getTag()).intValue();
                if(position >= 0 && position < ChattingFragment.this.list.size()) {
                    if(IMChannel.DEBUG.booleanValue()) {
                        YWMessage ywMessage = (YWMessage)ChattingFragment.this.list.get(position);
                        if((ywMessage != null && ywMessage.getSubType() == 1 || ywMessage.getSubType() == 4) && ywMessage != null) {
                            YWImageMessageBody messageBody = (YWImageMessageBody)ywMessage.getMessageBody();
                            if(messageBody != null) {
                                WxLog.d(ChattingFragment.TAG + "@OriginalPic", "image position: " + position);
                                WxLog.d(ChattingFragment.TAG + "@OriginalPic", "image 默认图片地址: " + messageBody.getOriContent());
                                WxLog.d(ChattingFragment.TAG + "@OriginalPic", "image 缩略图地址: " + messageBody.getContent());
                            }
                        }
                    }

                    if(!ChattingFragment.this.onMessageLongClick(ChattingFragment.this, (YWMessage)ChattingFragment.this.list.get(position))) {
                        ChattingFragment.this.presenter.onItemLongClick(position, view);
                    }

                    ChattingFragment.this.contentTouchListener.setIsLongClick(true);
                    return true;
                } else {
                    return false;
                }
            }
        };
        this.customListener = new ICustomViewChangeListener() {
            public void onCustomViewShow(View customView) {
                if(customView != null) {
                    ViewGroup parent = (ViewGroup)customView.getParent();
                    if(parent != null && parent.getChildCount() > 0) {
                        parent.removeView(customView);
                    }

                    ChattingFragment.this.mCustomLayout.setVisibility(View.VISIBLE);
                    ChattingFragment.this.mCustomLayout.addView(customView);
                }

            }

            public void onCustomViewHide() {
                ChattingFragment.this.mCustomLayout.setVisibility(View.GONE);
            }
        };
        this.mYWMessageListener = new IYWMessageListener() {
            public void onItemUpdated() {
            }

            public void onItemComing() {
                int size = ChattingFragment.this.list.size();
                if(size > 0) {
                    YWMessage message = (YWMessage)ChattingFragment.this.list.get(size - 1);
                    if(message instanceof IMsg) {
                        if(message.getSubType() == WXType.WXTribeMsgType.updateMemberNick.getValue()) {
                            if(ChattingFragment.this.tribeMembers != null) {
                                ChattingFragment.this.tribeMembers.put(message.getAuthorId(), ChattingFragment.this.tribeCache.getTribeNickCache().get(ChattingFragment.this.tid + message.getAuthorId()));
                                ChattingFragment.this.list.remove(size - 1);
                                ChattingFragment.this.adapter.notifyDataSetChanged();
                            }
                        } else if(message.getSubType() == -1 && message instanceof Message) {
                            Message msg = (Message)message;
                            if(msg.getTribeSysMsgType() == 3) {
                                WxTribe tribe = (WxTribe)ChattingFragment.this.tribeManager.getTribe(ChattingFragment.this.tribeId);
                                tribe.setTribeName(msg.getTribeInfo());
                                ChattingFragment.this.initTitle();
                            }
                        }
                    }
                }

            }

            public void onInputStatus(byte status) {
            }
        };
        this.mMessageListener = new IMessagePresenter.IMessageListener() {
            public boolean onItemChanged() {
                if(ChattingFragment.this.presenter.isLastVisible()) {
                    if(ChattingFragment.this.presenter.getConversation().getConversationType() == YWConversationType.Tribe) {
                        if(ChattingFragment.this.unReadAtMsg == null && !ChattingFragment.this.isFirstItemChanged) {
                            ChattingFragment.this.isFirstItemChanged = false;
                            ChattingFragment.this.mUIHandler.post(new Runnable() {
                                public void run() {
                                    ChattingFragment.this.scrollToBottom();
                                }
                            });
                        }
                    } else {
                        ChattingFragment.this.mUIHandler.post(new Runnable() {
                            public void run() {
                                ChattingFragment.this.scrollToBottom();
                            }
                        });
                    }
                }

                return false;
            }

            public boolean onItemUpdated() {
                ChattingFragment.this.mUIHandler.post(new Runnable() {
                    public void run() {
                        ChattingFragment.this.scrollToBottom();
                    }
                });
                return false;
            }

            public boolean onItemComing() {
                return false;
            }
        };
        this.contentClickListener = new View.OnClickListener() {
            public void onClick(View view) {
                int position = ((Integer)view.getTag()).intValue();
                if(position >= 0 && position < ChattingFragment.this.list.size()) {
                    if(!ChattingFragment.this.onMessageClick(ChattingFragment.this, (YWMessage)ChattingFragment.this.list.get(position))) {
                        ChattingFragment.this.presenter.onItemClick(position, view);
                    }

                }
            }
        };
        this.unReadCountClickListener = new View.OnClickListener() {
            public void onClick(View v) {
                if(v.getTag() instanceof YWMessage) {
                    YWMessage message = (YWMessage)v.getTag();
                    Intent intent = new Intent(ChattingFragment.this.getActivity(), SendAtMessageDetailActivity.class);
                    intent.putExtra("YWMessage", message);
                    intent.putExtra("tribeId", ChattingFragment.this.tribeId);
                    ChattingFragment.this.startActivity(intent);
                }

            }
        };
        this.previousItemHeight = 0;
    }

    public void onCreate(Bundle savedInstanceState) {
        Object start = Tracker.beginStep("openChattingActivity", "onCreate");
        super.onCreate(savedInstanceState);
        this.setHasOptionsMenu(true);
        this.initData();
        this.getTribeMembersWithNick();
        Tracker.endStep(start, 0, "openChattingActivity");
        WxLog.i(TAG, "onCreate");
    }

    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        this.restoreStateFromSharedPreference();
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if(this.view != null) {
            ViewGroup start1 = (ViewGroup)this.view.getParent();
            WxLog.i(TAG, "parent = " + start1);
            if(start1 != null) {
                start1.removeView(this.view);
            }

            return this.view;
        } else {
            this.addWhiteList();
            Object start = Tracker.beginStep("openChattingActivity", "onCreateView");
            this.mContext = this.getActivity();
            this.view = inflater.inflate(ResourceLoader.getIdByName(this.mContext, "layout", "aliwx_chatting_detail"), container, false);
            this.init();
            this.unReadAtMsg = (Message)this.getArguments().getSerializable("unReadAtMsg");
            Tracker.endStep(start, 0, "openChattingActivity");
            this.isAtEnable = YWAPI.getYWSDKGlobalConfig().enableTheTribeAtRelatedCharacteristic();
            this.sendImageMsgByIntent(this.getActivity().getIntent());
            WxLog.i(TAG, "onCreateView");
            return this.view;
        }
    }

    @TargetApi(23)
    private void addWhiteList() {
        if(Build.VERSION.SDK_INT >= 23) {
            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this.getActivity());
            int ignoreBatteryOpt = sharedPreferences.getInt("IgnoreBatteryOpt", -1);
            if(ignoreBatteryOpt < 0) {
                try {
                    Intent e = new Intent();
                    FragmentActivity context = this.getActivity();
                    String packageName = context.getPackageName();
                    PowerManager pm = (PowerManager)context.getSystemService(Activity.POWER_SERVICE);
                    if(!pm.isIgnoringBatteryOptimizations(packageName)) {
                        e.setAction("android.settings.REQUEST_IGNORE_BATTERY_OPTIMIZATIONS");
                        e.setData(Uri.parse("package:" + packageName));
                        this.startActivity(e);
                    }

                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putInt("IgnoreBatteryOpt", 1);
                    editor.commit();
                } catch (Exception var8) {
                    if(IMChannel.DEBUG.booleanValue()) {
                        var8.printStackTrace();
                    }
                }
            }

        }
    }

    private void initData() {
        this.mIsInitCalled = true;
        WxLog.i(TAG, "init, mIsInitCalled = " + this.mIsInitCalled);
        this.mCvsType = this.getArguments().getInt("conversationType", YWConversationType.P2P.getValue());
        this.cvsId = this.getArguments().getString("conversationId");
        this.targetId = this.getArguments().getString("extraUserId");
        if(TextUtils.isEmpty(this.targetId)) {
            this.targetId = AccountUtils.getShortUserID(this.cvsId);
        }

        this.appKey = this.getArguments().getString("extraAppKey");
        if(TextUtils.isEmpty(this.appKey)) {
            this.appKey = YWChannel.getInstance().getAppKey();
        }

        if(TextUtils.isEmpty(this.cvsId) && this.targetId != null) {
            this.cvsId = this.getArguments().getString("extraAppKey") + this.targetId;
        }

        this.tid = this.getArguments().getLong("extraTribeId");
    }

    private void init() {
        if(this.mCvsType != YWConversationType.Tribe.getValue() && this.mCvsType != YWConversationType.HJTribe.getValue()) {
            if(TextUtils.isEmpty(this.conversationId) || !this.conversationId.equals(this.cvsId)) {
                this.conversationId = this.cvsId;
                this.init(this.view);
            }
        } else if(this.tribeId == 0L || this.tribeId != this.tid) {
            this.tribeId = this.tid;
            this.conversationId = "tribe" + this.tid;
            this.init(this.view);
        }

        this.initContactProfileUpdateListener();
        ((YWContactManagerImpl)WXAPI.getInstance().getContactService()).addProfileUpdateListener(this.mContactProfileUpdateListener);
    }

    protected void init(View view) {
        if(this.mCvsType == YWConversationType.P2P.getValue()) {
            this.createPage("WangXin_Chat");
        } else if(this.mCvsType == YWConversationType.Tribe.getValue()) {
            this.createPage("WangXin_MultiChat");
        } else if(this.mCvsType == YWConversationType.HJTribe.getValue()) {
            this.createPage("Page_QFW_ChatRoom");
        }

        String argumentConversationId = this.getArguments().getString("conversationId");
        if(!TextUtils.isEmpty(argumentConversationId)) {
            this.conversationId = argumentConversationId;
        }

        String targetId = this.getArguments().getString("extraUserId");
        if(TextUtils.isEmpty(this.conversationId) && targetId != null) {
            this.conversationId = this.getArguments().getString("extraAppKey") + targetId;
        }

        this.presenter = new NormalChattingDetailPresenter(this.getActivity(), this.getArguments(), view, this);
        String caller = this.getArguments().getString("caller");
        if(!TextUtils.isEmpty(caller) && caller.equals("tae_caller_flag")) {
            this.presenter.setShowGoodsBuyButton(false);
        }

        this.accountType = this.getActivity().getIntent().getIntExtra(YWAccountType.class.getSimpleName(), 0);
        this.mCustomLayout = (LinearLayout)view.findViewById(ResourceLoader.getIdByName(this.mContext, "id", "custom_view"));
        this.scale = this.getDisplayMetrics().density;
        if(this.presenter.initView()) {
            this.chattingReplyBar = new ChattingReplayBar(this.getActivity(), this, view, this, this.conversationId, this.presenter);
            this.chattingReplyBar.initReplyBar(this.needRoundChattingImage(), this.dip2px(this.scale, this.getRoundRadiusDps()));
            this.gotoChatListBottomTextView = (TextView)view.findViewById(ResourceLoader.getIdByName(this.mContext, "id", "goto_chat_list_bottom_tv"));
            if(this.gotoChatListBottomTextView != null) {
                this.gotoChatListBottomTextView.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        ChattingFragment.this.restoreMessageList();
                        ChattingFragment.this.scrollToBottom();
                    }
                });
            }

            this.presenter.getConversation().getMessageLoader().addMessageListener(this.mYWMessageListener);
            if(this.presenter.getConversation() instanceof Conversation) {
                MessageList messageList = ((Conversation)this.presenter.getConversation()).getMessageList();
                messageList.addListener(this.mMessageListener);
            }

            if(this.presenter.initView()) {
                super.initFragment(this, this.presenter.getConversation());
                this.mEnterChattingRoomLayout = (RelativeLayout)view.findViewById(ResourceLoader.getIdByName(this.mContext, "id", "enter_chatting_room_layout"));
                if(this.needHideEnterChattingRoom(this.presenter.getConversation(), this.getActivity().getIntent())) {
                    this.mEnterChattingRoomLayout.setVisibility(View.GONE);
                } else {
                    this.mEnterChattingRoomLayout.setVisibility(View.VISIBLE);
                }

                this.mEnterChattingRoomLayout.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        ChattingFragment.this.onEnterChattingRoomClick(ChattingFragment.this, v, ChattingFragment.this.presenter.getConversation(), ChattingFragment.this.getActivity().getIntent());
                    }
                });
                this.initTitle();
                this.getArguments().putBoolean("async_render_fragment", true);
                this.onShow();
                if(this.listView != null && this.unReadAtMsg == null) {
                    this.scrollToBottom();
                }

                this.onStart(this, this.getActivity().getIntent(), this.presenter);
            }
        }
    }

    public int dip2px(float scale, float dipValue) {
        return (int)(dipValue * scale + 0.5F);
    }

    private void initTitle() {
        ViewGroup titleLayout = (ViewGroup)this.view.findViewById(ResourceLoader.getIdByName(this.mContext, "id", "title_layout"));
        View shadow = this.view.findViewById(ResourceLoader.getIdByName(this.mContext, "id", "title_bar_shadow_view"));
        if(this.needHideTitleView(this, this.presenter.getConversation())) {
            titleLayout.setVisibility(View.GONE);
            if(shadow != null) {
                shadow.setVisibility(View.GONE);
            }
        }

        View advancedTitleView = this.getCustomAdvancedTitleView(this.presenter.getConversation(), this.getActivity().getIntent());
        View customTitleView = this.getCustomTitleView(this.presenter.getConversation());
        int mBackButton1;
        RelativeLayout.LayoutParams e1;
        if(advancedTitleView != null && titleLayout != null) {
            titleLayout.removeAllViews();
            mBackButton1 = (int)this.mContext.getResources().getDimension(ResourceLoader.getIdByName(this.mContext, "dimen", "aliwx_title_bar_height"));
            e1 = new RelativeLayout.LayoutParams(-1, mBackButton1);
            titleLayout.addView(advancedTitleView, e1);
            this.title = (TextView)advancedTitleView.findViewById(ResourceLoader.getIdByName(this.mContext, "id", "chat_title"));
            this.atImage = this.view.findViewById(ResourceLoader.getIdByName(this.getActivity(), "id", "aliwx_at_content"));
            this.atUnDisposeView = (ImageView)this.view.findViewById(ResourceLoader.getIdByName(this.getActivity(), "id", "aliwx_at_msg_unread"));
        } else if(customTitleView != null && titleLayout != null) {
            titleLayout.removeAllViews();
            if(customTitleView.getLayoutParams() == null) {
                mBackButton1 = (int)this.mContext.getResources().getDimension(ResourceLoader.getIdByName(this.mContext, "dimen", "aliwx_title_bar_height"));
                e1 = new RelativeLayout.LayoutParams(-1, mBackButton1);
                titleLayout.addView(customTitleView, e1);
            } else {
                titleLayout.addView(customTitleView);
            }

            this.title = (TextView)customTitleView.findViewById(ResourceLoader.getIdByName(this.mContext, "id", "title"));
            this.atImage = this.view.findViewById(ResourceLoader.getIdByName(this.getActivity(), "id", "aliwx_at_content"));
            this.atUnDisposeView = (ImageView)this.view.findViewById(ResourceLoader.getIdByName(this.getActivity(), "id", "aliwx_at_msg_unread"));
            if(shadow != null) {
                shadow.setVisibility(View.GONE);
            }
        } else {
            TextView mBackButton = (TextView)this.view.findViewById(ResourceLoader.getIdByName(this.mContext, "id", "chat_back"));

            try {
                String e = this.getString(ResourceLoader.getIdByName(this.mContext, "string", "aliwx_title_back"));
                mBackButton.setText(e);
            } catch (Exception var7) {
                ;
            }

            mBackButton.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    if(ChattingFragment.this.getActivity() != null && !ChattingFragment.this.getActivity().isFinishing()) {
                        ChattingFragment.this.presenter.turnBack();
                        ChattingFragment.this.getActivity().finish();
                    }

                }
            });
            mBackButton.setVisibility(View.VISIBLE);
            this.mUnreadView = this.view.findViewById(ResourceLoader.getIdByName(this.mContext, "id", "aliwx_title_unread"));
            this.mAddButton = this.view.findViewById(ResourceLoader.getIdByName(this.mContext, "id", "aliwx_title_button"));
            this.mAddButton.setVisibility(View.GONE);
            this.mUnreadView.setVisibility(View.GONE);
            this.mAddButton.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    ChattingFragment.this.hideKeyBoard();
                    Intent intent = new Intent(ChattingFragment.this.getActivity(), WxConversationActivity.class);
                    intent.setFlags(67108864);
                    intent.putExtra(YWAccountType.class.getSimpleName(), ChattingFragment.this.accountType);
                    intent.putExtra("caller", "tae_caller_flag");
                    ChattingFragment.this.startActivity(intent);
                    if(ChattingFragment.this.getActivity() != null && !ChattingFragment.this.getActivity().isFinishing()) {
                        ChattingFragment.this.presenter.turnBack();
                        ChattingFragment.this.getActivity().finish();
                    }

                }
            });
            this.title = (TextView)this.view.findViewById(ResourceLoader.getIdByName(this.mContext, "id", "chat_title"));
            this.initAtView(this.view);
        }

    }

    private void initContactProfileUpdateListener() {
        this.mContactProfileUpdateListener = new IContactProfileUpdateListener() {
            public void onProfileUpdate(String userid, String appkey) {
                WxLog.d(ChattingFragment.TAG, "onProfileUpdate, targetId = " + ChattingFragment.this.targetId + " userid=" + userid);
                if(!TextUtils.isEmpty(ChattingFragment.this.targetId) && TextUtils.equals(ChattingFragment.this.targetId, userid)) {
                    IYWContact ywContact = IMUtility.getContactProfileInfo(ChattingFragment.this.targetId, ChattingFragment.this.appKey);
                    if(ChattingFragment.this.presenter != null && ywContact != null) {
                        WxLog.d(ChattingFragment.TAG, "targetId = " + ywContact.getUserId() + "appKey = " + ChattingFragment.this.appKey + ", nick = " + ywContact.getShowName());
                        YWConversation conversation = ChattingFragment.this.presenter.getConversation();
                        if(conversation != null && (conversation.getConversationType() == YWConversationType.P2P || conversation.getConversationType() == YWConversationType.SHOP)) {
                            YWP2PConversationBody conversationBody = (YWP2PConversationBody)conversation.getConversationBody();
                            IYWContact myContact = conversationBody.getContact();
                            if(myContact instanceof YWAppContactImpl) {
                                YWAppContactImpl appContact = (YWAppContactImpl)myContact;
                                appContact.setShowName(ywContact.getShowName());
                                ChattingFragment.this.mUIHandler.post(new Runnable() {
                                    public void run() {
                                        ChattingFragment.this.initTitle();
                                        ChattingFragment.this.presenter.setRoomChattingTitle();
                                    }
                                });
                            }
                        }
                    }

                }
            }

            public void onProfileUpdate() {
            }
        };
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        WxLog.i(TAG, "onActivityResult, requestCode = " + requestCode + ", resultCode = " + resultCode);
        if(resultCode == -1) {
            if(requestCode == 6) {
                this.presenter.resendCheckcodeMsgs();
            } else if(this.chattingReplyBar != null) {
                this.chattingReplyBar.onActivityResult(requestCode, resultCode, data);
            }
        } else if(resultCode == 0 && requestCode == 6) {
            this.presenter.resetCheckcodeFlag();
        }

    }

    public void onPause() {
        super.onPause();
        this.mIsRunning = false;
        if(this.chattingReplyBar != null) {
            this.chattingReplyBar.onPause();
        }

        if(this.adapter != null) {
            this.adapter.stopAudio();
        }

        if(this.presenter != null) {
            this.presenter.onPause();
        }

        ((YWContactManagerImpl)WXAPI.getInstance().getContactService()).removeProfileUpdateListener(this.mContactProfileUpdateListener);
    }

    public void onDestroy() {
        super.onDestroy();
        super.onFragmentDestory();
        if(this.presenter != null && this.presenter.getConversation() != null && this.presenter.getConversation().getMessageLoader() != null) {
            this.presenter.getConversation().getMessageLoader().removeMessageListener(this.mYWMessageListener);
            if(this.presenter.getConversation() instanceof Conversation) {
                MessageList messageList = ((Conversation)this.presenter.getConversation()).getMessageList();
                messageList.removeListener(this.mMessageListener);
            }

            this.presenter.onDestroy();
        }

        if(this.adapter != null) {
            ((YWContactManagerImpl)WXAPI.getInstance().getContactService()).removeProfileUpdateListener(this.adapter);
            this.adapter.recycle();
        }

        if(this.chattingReplyBar != null) {
            this.chattingReplyBar.recycle();
        }

        ChattingHandlerManager.getInstance().unRegister();
        WXAPI.getInstance().removeCustomViewListener(this.customListener);
        LeakCanaryHandler.getInstance().watch(this);
        if(this.unreadAtMsgList != null && this.unreadAtMsgList.size() == 0 && this.presenter.getConversation() instanceof TribeConversation) {
            ((TribeConversation)this.presenter.getConversation()).setHasUnreadAtMsg(false);
            ((TribeConversation)this.presenter.getConversation()).setLatestUnreadAtMessage((YWMessage)null);
            ((TribeConversation)this.presenter.getConversation()).setLatestUnreadAtMsgId(-1L);
        }

        this.restoreMessageList();
        if(this.tribeMembers != null) {
            this.tribeMembers.clear();
            this.tribeMembers = null;
        }

        WxLog.i(TAG, "onDestroy");
    }

    public void handleSendMessageWhenOpenChatting() {
        String mMessageToSendWhenOpenChatting = this.messageToSendWhenOpenChatting(this, this.presenter.getConversation());
        if(!TextUtils.isEmpty(mMessageToSendWhenOpenChatting)) {
            YWMessage msg = YWMessageChannel.createTextMessage(mMessageToSendWhenOpenChatting);
            this.sendMessage(msg);
        }

    }

    public void handleSendYWMessageWhenOpenChatting() {
        if(this.presenter.getIsConversationFirstCreated()) {
            YWMessage message = this.ywMessageToSendWhenOpenChatting(this, this.presenter.getConversation());
            if(message != null) {
                this.sendMessage(message);
            }
        }

    }

    public void asyncCheckAndShowRecentTakenPhoto() {
    }

    public void onResume() {
        Object start = Tracker.beginStep("openChattingActivity", "onResume@" + TAG);
        super.onResume();
        Bundle bundle = this.getArguments();
        if(bundle != null) {
            String start2 = bundle.getString("receiverId");
            if(!TextUtils.isEmpty(start2)) {
                WXAPI.switchAccount(start2);
            }
        }

        if(!this.mIsInitCalled) {
            WxLog.i(TAG, "onResume, mIsInitCalled = " + this.mIsInitCalled);
            this.init();
        }

        this.mIsInitCalled = false;
        this.addCustomView();
        Object start21 = Tracker.beginStep("openChattingActivity", "adapterNotifyDataSetChanged@" + TAG);
        if(this.adapter != null) {
            this.adapter.notifyDataSetChangedWithAsyncLoad();
        }

        Tracker.endStep(start21, 0, "openChattingActivity");
        Object start3 = Tracker.beginStep("openChattingActivity", "presenter.onResume@" + TAG);
        if(this.presenter != null) {
            this.presenter.onResume();
        }

        Tracker.endStep(start3, 0, "openChattingActivity");
        boolean isAsync = this.getArguments().getBoolean("async_render_fragment", false);
        if(!isAsync) {
            Object intent = Tracker.beginStep("openChattingActivity", "onShow@" + TAG);
            this.onShow();
            this.getArguments().putBoolean("async_render_fragment", true);
            Tracker.endStep(intent, 0, "openChattingActivity");
        }

        this.mIsRunning = true;
        Intent intent1 = this.getActivity().getIntent();
        if(intent1 != null) {
            this.atMsg = (YWMessage)intent1.getSerializableExtra("atMsg");
            if(this.atMsg != null) {
                this.selectAtMsg(this.atMsg);
                if(!this.atMsg.isAtMsgHasRead()) {
                    this.sendAtAckForMsg(this.atMsg);
                }

                this.checkScrollPositionAndUnreadMsgCount();
                intent1.removeExtra("atMsg");
            }
        }

        if(this.presenter.getConversation() instanceof TribeConversation && (this.loadUnreadAtMessageTask == null || !this.loadUnreadAtMessageTask.isRunning)) {
            this.loadUnreadAtMessageTask = new ChattingFragment.LoadUnreadAtMessageTask(this.presenter.getConversation());
            this.loadUnreadAtMessageTask.execute(new Void[0]);
        }

        Tracker.endStep(start, 0, "openChattingActivity");
        Tracker.endSession("openChattingActivity", 0);
        WxLog.i(TAG, "onResume");
    }

    private void addCustomView() {
        Object start1 = Tracker.beginStep("openChattingActivity", "getCustomView@" + TAG);
        WXAPI.getInstance().addCustomViewListener(this.customListener);
        this.mCustomView = WXAPI.getInstance().getCustomView();
        if(this.mCustomLayout != null && this.mCustomView != null) {
            ViewGroup view = (ViewGroup)this.mCustomView.getParent();
            if(view != null && view.getChildCount() > 0) {
                view.removeView(this.mCustomView);
            }

            this.mCustomLayout.setVisibility(View.VISIBLE);
            this.mCustomLayout.addView(this.mCustomView);
        }

        if(this.isUseChattingCustomViewAdvice(this, this.getActivity().getIntent())) {
            View view1 = this.getChattingFragmentCustomViewAdvice(this, this.getActivity().getIntent());
            if(view1 != null) {
                this.mCustomLayout.removeAllViews();
                this.mCustomLayout.setVisibility(View.VISIBLE);
                this.mCustomLayout.addView(view1);
            } else {
                this.mCustomLayout.removeAllViews();
            }
        }

        if(this.fragmentFirstCreated) {
            this.fragmentFirstCreated = false;
            this.handleSendMessageWhenOpenChatting();
            this.handleSendYWMessageWhenOpenChatting();
        }

        Tracker.endStep(start1, 0, "openChattingActivity");
    }

    private void showGotoNewMsgsTopTextView() {
        if(this.presenter != null) {
            int unreadMsgCount = this.presenter.getUnreadMsgCountWhenInit();
            this.mFirstUnreadMsgPosition = this.listView.getCount() - 1 - unreadMsgCount;
            if(this.mFirstUnreadMsgPosition < 0) {
                this.mFirstUnreadMsgPosition = 0;
            }

            int firstVisiblePosition = this.listView.getFirstVisiblePosition();
            if(this.mFirstUnreadMsgPosition < firstVisiblePosition) {
                ;
            }
        }
    }

    private void hideGotoNewMsgsTopTextView() {
        if(this.gotoNewMsgsTopTextView != null) {
            this.gotoNewMsgsTopTextView.setVisibility(View.INVISIBLE);
            this.gotoNewMsgsTopTextView = null;
        }

    }

    private void checkScrollPositionAndUnreadMsgCount() {
        if(this.listView != null) {
            int lastPosition = this.listView.getCount() - 1;
            int lastVisiblePosition = this.listView.getLastVisiblePosition();
            if(lastPosition == lastVisiblePosition) {
                if(this.list != null && this.list.size() > 0 && this.messagesListCopy != null && this.messagesListCopy.size() > 0) {
                    if(!((YWMessage)this.list.get(this.list.size() - 1)).equals(this.messagesListCopy.get(this.messagesListCopy.size() - 1)) && this.gotoChatListBottomTextView != null && this.gotoChatListBottomTextView.getVisibility() != 0) {
                        this.gotoChatListBottomTextView.setVisibility(View.VISIBLE);
                        this.mPullRefreshListView.setMode(PullToRefreshBase.Mode.BOTH);
                    }
                } else {
                    this.handleOnScrollToBottom();
                }
            } else if(lastPosition - lastVisiblePosition <= 3) {
                if(this.list != null && this.list.size() > 0 && this.messagesListCopy != null && this.messagesListCopy.size() > 0) {
                    if(!((YWMessage)this.list.get(this.list.size() - 1)).equals(this.messagesListCopy.get(this.messagesListCopy.size() - 1)) && this.gotoChatListBottomTextView != null && this.gotoChatListBottomTextView.getVisibility() != 0) {
                        this.gotoChatListBottomTextView.setVisibility(View.VISIBLE);
                    }
                } else if(this.gotoChatListBottomTextView != null) {
                    this.gotoChatListBottomTextView.setVisibility(View.INVISIBLE);
                }
            } else {
                int unreadCount = 0;
                YWConversation conversation = this.presenter.getConversation();
                if(conversation != null) {
                    unreadCount = conversation.getUnreadCount();
                }

                if(this.getActivity() != null) {
                    if(unreadCount == 0 && this.gotoChatListBottomTextView != null) {
                        if(this.gotoChatListBottomTextView != null) {
                            this.gotoChatListBottomTextView.setCompoundDrawablesWithIntrinsicBounds(ResourceLoader.getDrawableIdByName("aliwx_goto_chat_list_bottom_icon"), 0, 0, 0);
                            this.gotoChatListBottomTextView.setText(this.getResources().getString(ResourceLoader.getStringIdByName("aliwx_goto_chat_list_bottom")));
                            this.gotoChatListBottomTextView.setBackgroundDrawable(this.getResources().getDrawable(ResourceLoader.getDrawableIdByName("aliwx_goto_chat_list_bottom_bg")));
                        }
                    } else if(this.gotoChatListBottomTextView != null) {
                        this.gotoChatListBottomTextView.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                        String unreadStr = String.format(this.getResources().getString(ResourceLoader.getStringIdByName("aliwx_goto_chat_list_bottom_with_unread_msg")), new Object[]{Integer.valueOf(unreadCount)});
                        this.gotoChatListBottomTextView.setText(unreadStr);
                        this.gotoChatListBottomTextView.setBackgroundDrawable(this.getResources().getDrawable(ResourceLoader.getDrawableIdByName("aliwx_unread_goto_chat_list_bottom_bg")));
                    }
                }

                if(this.gotoChatListBottomTextView != null) {
                    this.gotoChatListBottomTextView.setVisibility(View.VISIBLE);
                }
            }

        }
    }

    public void onScrollStateChanged(AbsListView view, int scrollState) {
        if(scrollState == 0) {
            if(this.adapter != null) {
                this.adapter.loadAsyncTask();
            }

            if(this.unreadAtMsgList != null && this.unreadAtMsgList.size() > 0) {
                YWConversation conversation = this.presenter.getConversation();
                if(conversation instanceof TribeConversation) {
                    this.sendAtMsgAckForVisibleItems(this.list, (TribeConversation)conversation);
                }
            }

            this.checkScrollPositionAndUnreadMsgCount();
        }

    }

    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        boolean isLastVisible = firstVisibleItem + visibleItemCount >= totalItemCount - 1;
        if(isLastVisible) {
            if(this.list != null && this.list.size() > 0 && this.messagesListCopy != null && this.messagesListCopy.size() > 0 && this.list.contains(this.messagesListCopy.get(this.messagesListCopy.size() - 1))) {
                if(this.mPullRefreshListView.getMode() == PullToRefreshBase.Mode.BOTH) {
                    this.mPullRefreshListView.setMode(PullToRefreshBase.Mode.PULL_DOWN_TO_REFRESH);
                    this.handleOnScrollToBottom();
                    if(this.presenter != null) {
                        this.presenter.setLastVisible(isLastVisible);
                    }
                }
            } else if(this.messagesListCopy != null && this.mPullRefreshListView.getMode() != PullToRefreshBase.Mode.BOTH) {
                this.mPullRefreshListView.setMode(PullToRefreshBase.Mode.BOTH);
            }
        }

        if(this.messagesListCopy == null && this.presenter != null) {
            this.presenter.setLastVisible(isLastVisible);
        }

        this.maxVisibleItemCount = visibleItemCount > this.maxVisibleItemCount?visibleItemCount:this.maxVisibleItemCount;
        if(this.adapter != null) {
            this.adapter.setMaxVisibleItemCount(this.maxVisibleItemCount);
        }

        if(this.gotoNewMsgsTopTextView != null && this.mFirstUnreadMsgPosition >= firstVisibleItem) {
            this.hideGotoNewMsgsTopTextView();
        }

    }

    public void onStop() {
        if(this.mCustomLayout != null && this.mCustomView != null) {
            this.mCustomLayout.removeView(this.mCustomView);
            WXAPI.getInstance().removeCustomView();
        }

        if(this.loadUnreadAtMessageTask != null && this.loadUnreadAtMessageTask.isRunning) {
            this.loadUnreadAtMessageTask.cancel(true);
        }

        super.onStop();
    }

    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        this.saveStateToSharedPreference();
    }

    public void onDestroyView() {
        super.onDestroyView();
        this.saveStateToSharedPreference();
    }

    public void onStart() {
        super.onStart();
    }

    public boolean onBackPressed() {
        if(this.chattingReplyBar.hideReplyBar()) {
            return true;
        } else {
            boolean k = super.onBackPressed(this);
            if(k) {
                return true;
            } else {
                this.presenter.turnBack();
                return false;
            }
        }
    }

    public boolean onTouch(View v, MotionEvent event) {
        this.adapter.setIsScrolled(true);
        this.chattingReplyBar.hideKeyBoard();
        this.chattingReplyBar.hideWindow();
        this.chattingReplyBar.stopInputStatus();
        this.onChattingTouchEvent(this, this.presenter.getConversation(), event);
        return false;
    }

    public void onFocusChange(View v, boolean hasFocus) {
        if(hasFocus) {
            this.chattingReplyBar.hideWindow();
        }

    }

    public void sendMessage(YWMessage msg) {
        if(this.mCvsType == YWConversationType.HJTribe.getValue()) {
            TBSWrapper.commitTBSEvent("Page_QFW_ChatRoom", "QFW_Click_Send", new String[0]);
        }

        this.beforeSendMessage(this.presenter.getConversation(), msg);
        if(this.isBlackContact() && msg instanceof Message) {
            ((Message)msg).setFrom("local");
        }

        this.presenter.sendMsg(msg);
        this.afterSendMessage(this.presenter.getConversation(), msg);
        this.showBlackContactTips();
        if(this.adapter != null) {
            this.adapter.notifyDataSetChanged();
        }

        this.scrollToBottom();
        WxLog.d(TAG, "replyMessage" + msg.getMessageBody().getContent());
    }

    private void showBlackContactTips() {
        String tips = this.getTipsForSendingMsgToBlackContact(this, this.presenter.getConversation());
        if(tips != null) {
            if("".equals(tips)) {
                tips = "黑名单用户，对方将接收不到消息";
            }

            if(this.isBlackContact()) {
                this.presenter.sendMsg(YWMessageChannel.createLocalSystemMessage(tips));
            }

        }
    }

    private boolean isBlackContact() {
        IYWContactService contactService = WXAPI.getInstance().getContactService();
        return contactService != null && contactService.isBlackContact(this.targetId, this.appKey);
    }

    public void onReplyBarClick() {
        this.scrollToBottom();
    }

    private void handleOnScrollToBottom() {
        if(this.gotoChatListBottomTextView != null) {
            this.gotoChatListBottomTextView.setVisibility(View.INVISIBLE);
        }

        if(this.presenter != null) {
            YWConversation ywConversation = this.presenter.getConversation();
            if(ywConversation != null) {
                int unReadCount = ywConversation.getUnreadCount();
                if(unReadCount > 0 && IMUtility.atApplication(YWChannel.getApplication())) {
                    WXAPI.getInstance().getConversationManager().markReaded(ywConversation);
                    this.presenter.setLastVisible(true);
                }
            }
        }

    }

    private void scrollToBottom() {
        if(this.listView != null && this.listView.getAdapter() != null) {
            this.listView.post(new Runnable() {
                public void run() {
                    ChattingFragment.this.listView.setSelection(ChattingFragment.this.listView.getCount() - 1);
                    ChattingFragment.this.listView.postDelayed(new Runnable() {
                        public void run() {
                            ChattingFragment.this.listView.setSelection(ChattingFragment.this.listView.getCount() - 1);
                        }
                    }, 150L);
                }
            });
        }

        this.handleOnScrollToBottom();
    }

    public void onPrepareMsg(int type) {
        this.presenter.onPrepareMsg(type);
    }

    public void stopPrepareMsg(int type) {
        this.presenter.stopPrepareMsg(type);
    }

    public void onSelectPeople() {
        this.presenter.onSelectPeople();
    }

    public void setConversationName(String name) {
        if(!(this.presenter.getConversation() instanceof HJTribeConversation)) {
            if(this.title != null) {
                this.title.setText(name);
                chatinterface.changeText(name);
            } else {
                this.title = (TextView)this.view.findViewById(ResourceLoader.getIdByName(this.mContext, "id", "chat_title"));
                if(this.title != null) {
                    this.title.setText(name);
                    chatinterface.changeText(name);
                }
            }

        }
    }

    public void cancelAnimation() {
        this.getActivity().getWindow().setWindowAnimations(0);
    }

    public void playAudio(YWMessage msg, View view, int position) {
        if(!"mounted".equals(Environment.getExternalStorageState())) {
            Toast.makeText(this.mContext, ResourceLoader.getIdByName(this.mContext, "string", "aliwx_insert_sdcard"), Toast.LENGTH_SHORT).show();
        } else {
            if(((YWFileMessageBody)msg.getMessageBody()).getDownloadState() == YWMessageType.DownloadState.success && this.adapter != null) {
                this.adapter.playAudio(msg, view, position);
            }

        }
    }

    private int getListItemHeight(int Position) {
        int height = 0;
        if(this.listView.getAdapter() != null && Position >= 0 && Position < this.listView.getAdapter().getCount() + this.listView.getHeaderViewsCount()) {
            View view = this.listView.getAdapter().getView(Position, (View)null, (ViewGroup)null);
            android.widget.AbsListView.LayoutParams p = (android.widget.AbsListView.LayoutParams)view.getLayoutParams();
            if(p == null) {
                p = new android.widget.AbsListView.LayoutParams(-1, -2, 0);
                view.setLayoutParams(p);
            }

            int childWidthSpec = ViewGroup.getChildMeasureSpec(0, p.width, -2);
            int lpHeight = p.height;
            int childHeightSpec;
            if(lpHeight > 0) {
                childHeightSpec = View.MeasureSpec.makeMeasureSpec(lpHeight, View.MeasureSpec.EXACTLY );
            } else {
                childHeightSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
            }

            view.measure(childWidthSpec, childHeightSpec);
            height = view.getMeasuredHeight();
        }

        return height;
    }

    public void hidKeyBoard() {
        this.chattingReplyBar.hideKeyBoard();
    }

    public void onShow() {
        if(this.getActivity() != null && !this.getActivity().isFinishing()) {
            this.isAtEnable = YWAPI.getYWSDKGlobalConfig().enableTheTribeAtRelatedCharacteristic();
            if(this.mPullRefreshListView == null) {
                ViewStub stub = (ViewStub)this.view.findViewById(ResourceLoader.getIdByName(this.mContext, "id", "chat_list_stub"));
                this.mPullRefreshListView = (PullToRefreshListView)stub.inflate();
                this.mPullRefreshListView.setMode(PullToRefreshBase.Mode.PULL_DOWN_TO_REFRESH);
                this.mPullRefreshListView.setShowIndicator(true);
                this.mPullRefreshListView.setDisableScrollingWhileRefreshing(false);
                this.mPullRefreshListView.setRefreshingLabel(this.getResources().getString(ResourceLoader.getStringIdByName("aliwx_pull_to_refresh_refreshing_label")));
                this.listView = (ListView)this.mPullRefreshListView.getRefreshableView();
                this.listView.setOnTouchListener(this);
                this.mPullRefreshListView.setDisableLoadingImage(true);
                this.mFooterViewHeight = this.mPullRefreshListView.getHeaderHeight();
                if(this.getChattingBackgroundResId() != 0) {
                    if(this.mWholeBack == null) {
                        this.mWholeBack = (RelativeLayout)this.view.findViewById(ResourceLoader.getIdByName(this.mContext, "id", "whole_back"));
                    }

                    this.mWholeBack.setBackgroundColor(0);
                    View presenterListView = this.getActivity().findViewById(ResourceLoader.getIdByName(this.mContext, "id", "wx_chat_framelayout"));
                    if(presenterListView != null) {
                        presenterListView.setBackgroundColor(0);
                    }

                    this.mPullRefreshListView.setBackgroundColor(0);
                    this.getActivity().getWindow().getDecorView().setBackgroundResource(this.getChattingBackgroundResId());
                } else if(Build.VERSION.SDK_INT >= 16) {
                    if(this.mWholeBack == null) {
                        this.mWholeBack = (RelativeLayout)this.view.findViewById(ResourceLoader.getIdByName(this.mContext, "id", "whole_back"));
                    }

                    this.getActivity().getWindow().getDecorView().setBackground(this.mWholeBack.getBackground());
                }

                this.mPullRefreshListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2() {
                    public void onPullDownToRefresh() {
                        ChattingFragment.this.handler.postDelayed(new Runnable() {
                            public void run() {
                                if(ChattingFragment.this.adapter != null) {
                                    ChattingFragment.this.adapter.stopAudio();
                                }

                                if(ChattingFragment.this.list == null || ChattingFragment.this.list.size() == 0) {
                                    ChattingFragment.this.isMsgListEmpty = true;
                                }

                                ChattingFragment.this.presenter.loadMoreMsg();
                                if(ChattingFragment.this.list != null && ChattingFragment.this.list.size() == 0) {
                                    YWConversation conversation = ChattingFragment.this.presenter.getConversation();
                                    if(conversation instanceof TribeConversation) {
                                        YWConversationManager conversationManager = WXAPI.getInstance().getConversationManager();
                                        ArrayList conversationList = new ArrayList();
                                        conversationList.add(conversation);
                                        conversationManager.getAtMsgList(ChattingFragment.this.mContext, conversationList, 20, (IWxCallback)null);
                                    }
                                }

                            }
                        }, 200L);
                    }

                    public void onPullUpToRefresh() {
                        YWMessage lastMsg = null;
                        if(ChattingFragment.this.list != null && ChattingFragment.this.list.size() > 0) {
                            lastMsg = (YWMessage)ChattingFragment.this.list.get(ChattingFragment.this.list.size() - 1);
                        }

                        if(lastMsg == null) {
                            ChattingFragment.this.mPullRefreshListView.onRefreshComplete(false, false);
                        } else {
                            ChattingFragment.this.isPullUpToLoad = true;
                            ChattingFragment.this.loadMsgContext(lastMsg, 10, 1);
                        }
                    }
                });
                IListView presenterListView1 = new IListView() {
                    public void onLoadMsg() {
                        if(ChattingFragment.this.progress == null && ChattingFragment.this.getActivity() != null && !ChattingFragment.this.getActivity().isFinishing() && !ChattingFragment.this.isDetached()) {
                            ChattingFragment.this.progress = ProgressDialog.show(ChattingFragment.this.getActivity(), (CharSequence)null, ChattingFragment.this.getResources().getString(ResourceLoader.getIdByName(ChattingFragment.this.mContext, "string", "aliwx_loading")), false, true);
                        }

                    }

                    public void finishLoadMsg() {
                        if(ChattingFragment.this.progress != null && ChattingFragment.this.progress.isShowing() && ChattingFragment.this.getActivity() != null && !ChattingFragment.this.getActivity().isFinishing()) {
                            try {
                                ChattingFragment.this.progress.dismiss();
                            } catch (IllegalArgumentException var4) {
                                var4.printStackTrace();
                            }

                            ChattingFragment.this.isAtEnable = YWAPI.getYWSDKGlobalConfig().enableTheTribeAtRelatedCharacteristic();
                            if(ChattingFragment.this.unReadAtMsg != null) {
                                ChattingFragment.this.sendAtAckForMsg(ChattingFragment.this.unReadAtMsg);
                                ChattingFragment.this.scrollToAtMsgPosition(ChattingFragment.this.list, ChattingFragment.this.unReadAtMsg);
                                if(ChattingFragment.this.unreadAtMsgList != null && ChattingFragment.this.unreadAtMsgList.size() > 0 && ChattingFragment.this.presenter.getConversation() instanceof TribeConversation) {
                                    ChattingFragment.this.sendAtMsgAckForVisibleItems(ChattingFragment.this.list, (TribeConversation)ChattingFragment.this.presenter.getConversation());
                                }
                            }

                            ChattingFragment.this.mUIHandler.post(new Runnable() {
                                public void run() {
                                    ChattingFragment.this.showGotoNewMsgsTopTextView();
                                }
                            });
                        }

                        if(ChattingFragment.this.isMsgListEmpty && ChattingFragment.this.list != null && ChattingFragment.this.list.size() > 0) {
                            ChattingFragment.this.isMsgListEmpty = false;
                            YWConversation conversation = ChattingFragment.this.presenter.getConversation();
                            if(conversation != null && conversation instanceof Conversation) {
                                ConversationModel model = ((Conversation)conversation).getConversationModel();
                                if(model != null) {
                                    YWMessage latestMessage = (YWMessage)ChattingFragment.this.list.get(ChattingFragment.this.list.size() - 1);
                                    if(latestMessage != null) {
                                        model.setLastestMessage(latestMessage);
                                        model.setLatestAuthorId(latestMessage.getAuthorId());
                                        model.setLatestAuthorName(latestMessage.getAuthorUserName());
                                        model.setContent(IMUtil.getContent(latestMessage, ((Conversation) conversation).mWxAccount.getSid(), model.getConversationType()));
                                        ((Conversation)conversation).updateToDB();
                                    }
                                }
                            }
                        }

                        ChattingFragment.this.mPullRefreshListView.onRefreshComplete(false, true);
                    }

                    public void scollListToPosition(int position) {
                        ChattingFragment.this.scrollToBottom();
                    }

                    public void onInvisibleItemComing() {
                        if(ChattingFragment.this.messagesListCopy != null && ChattingFragment.this.list != null && ChattingFragment.this.list.size() > 0) {
                            ChattingFragment.this.messagesListCopy.add(ChattingFragment.this.list.remove(ChattingFragment.this.list.size() - 1));
                        }

                        ChattingFragment.this.mUIHandler.post(new Runnable() {
                            public void run() {
                                ChattingFragment.this.checkScrollPositionAndUnreadMsgCount();
                            }
                        });
                    }

                    public void hideCloudView() {
                        ChattingFragment.this.mPullRefreshListView.setStartRefreshingOver();
                        ChattingFragment.this.mPullRefreshListView.onRefreshComplete(false, true);
                    }

                    public void showCloudView() {
                        ChattingFragment.this.mPullRefreshListView.setStartRefreshing();
                    }

                    public void setListToPosition(int position) {
                        if(ChattingFragment.this.listView != null && ChattingFragment.this.listView.getAdapter() != null) {
                            int difference = ChattingFragment.this.previousItemHeight - ChattingFragment.this.getListItemHeight(position);
                            ChattingFragment.this.listView.setSelectionFromTop(position, ChattingFragment.this.mPullRefreshListView.getHeaderHeight() + difference);
                        }

                    }

                    public void onloadMoreMsg() {
                        ChattingFragment.this.previousItemHeight = ChattingFragment.this.getListItemHeight(ChattingFragment.this.listView.getHeaderViewsCount());
                    }

                    public void setListAutoScroll(boolean isAutoScroll) {
                        ChattingFragment.this.mPullRefreshListView.setNeedAutoSetSelection(isAutoScroll);
                    }

                    public void onNoMoreMsg() {
                        if(ChattingFragment.this.mPullRefreshListView != null) {
                            ChattingFragment.this.mPullRefreshListView.setDisableRefresh(true);
                            ChattingFragment.this.mPullRefreshListView.onRefreshComplete(false, true);
                        }

                    }
                };
                this.list = this.presenter.loadInfo(presenterListView1);
                WxLog.d(TAG, "unReadAtMsg:" + this.unReadAtMsg + " msgId:" + (this.unReadAtMsg != null?Long.valueOf(this.unReadAtMsg.getMsgId()):null));
                if(this.unReadAtMsg == null) {
                    this.unReadAtMsg = (Message)this.getArguments().getSerializable("unReadAtMsg");
                }

                this.addUnreadAtMsgToMsgList(this.list, this.unReadAtMsg);
                this.adapter = new ChattingDetailAdapter(this, this.getActivity(), this.list, this.listView, this.headOnClickListener, this.headOnLongClickListener, this.leftOrRightViewTouchListener, this.contentTouchListener, this.msgResendClickListener, this.msgRegetClickListener, this.contentClickListener, this.contentLongClickListener, this.unReadCountClickListener, this.presenter, this.getArguments().getString("caller"), this.presenter.getConversation(), this.needRoundChattingImage(), (float)this.dip2px(this.scale, this.getRoundRadiusDps()), this.tribeId);
                this.listView.setAdapter(this.adapter);
                this.listView.setOnScrollListener(this);
                this.adapter.notifyDataSetChangedWithAsyncLoad();
                ((YWContactManagerImpl)WXAPI.getInstance().getContactService()).addProfileUpdateListener(this.adapter);
                if(this.unReadAtMsg != null) {
                    this.scrollToAtMsgPosition(this.list, this.unReadAtMsg);
                }

                final String goodsId = this.getArguments().getString("itemid");
                this.handler.post(new Runnable() {
                    public void run() {
                        if(!TextUtils.isEmpty(goodsId)) {
                            ChattingFragment.this.presenter.setTradeFocusMsg(goodsId, 9);
                        }

                    }
                });
                this.presenter.setAdapter(this.adapter);
                this.setBackToListTop(this.listView, this.view, ResourceLoader.getIdByName(this.mContext, "id", "title"));
                if(this.unReadAtMsg == null) {
                    this.scrollToBottom();
                }
            }

        }
    }

    public void onNeedAuthCheck(long msgId, String cvsId, String sessionId) {
        if(this.getActivity() instanceof ImageViewerListener) {
            CheckCodeListener checkCodeListener = (CheckCodeListener)this.getActivity();
            checkCodeListener.showCheckCodeFragment(6, sessionId);
        }

    }

    public void setUnReadCount(int count) {
    }

    public void showPsdDialog() {
        if(this.mPwdDialog == null) {
            LayoutInflater inflater = (LayoutInflater)this.getActivity().getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            View layout = inflater.inflate(ResourceLoader.getIdByName(this.mContext, "layout", "aliwx_cloud_chat_pwd_dialog"), (ViewGroup)null);
            final EditText passwordText = (EditText)layout.findViewById(ResourceLoader.getIdByName(this.mContext, "id", "password_text"));
            layout.findViewById(ResourceLoader.getIdByName(this.mContext, "id", "password_image")).setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    Intent intent = new Intent(ChattingFragment.this.getActivity(), CloudPwdSettingHintActivity.class);
                    ChattingFragment.this.startActivity(intent);
                }
            });
            this.mPwdDialog = (new WxAlertDialog.Builder(this.getActivity())).setTitle(ResourceLoader.getIdByName(this.mContext, "string", "aliwx_setting_hint")).setView(layout).setMessage(ResourceLoader.getIdByName(this.mContext, "string", "aliwx_cloud_chat_pwd_hint")).setPositiveButton(ResourceLoader.getIdByName(this.mContext, "string", "aliwx_confirm"), new android.content.DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    String password = passwordText.getText().toString();
                    passwordText.setText("");
                    ChattingFragment.this.presenter.setSyncPassword(password, false);
                }
            }).setNegativeButton(ResourceLoader.getIdByName(this.mContext, "string", "aliwx_do_not_prompt_any_more"), new android.content.DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    ChattingFragment.this.mPullRefreshListView.onRefreshComplete(false, false);
                    ChattingFragment.this.presenter.setSyncPassword((String)null, true);
                    ChattingFragment.this.presenter.setSyncCloudState(false, true);
                    IMNotificationUtils.showToast(ResourceLoader.getIdByName(ChattingFragment.this.mContext, "string", "aliwx_cloud_msg_mention"), ChattingFragment.this.getActivity());
                }
            }).create();
        }

        if(this.mPwdDialog != null) {
            this.mPwdDialog.show();
        }

    }

    public void refresh() {
        this.presenter.scollListToPosition();
        this.adapter.notifyDataSetChanged();
    }

    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        int aliwx_clear_chatting_msg = ResourceLoader.getIdByName(this.mContext, "string", "aliwx_clear_chatting_msg");
        int menu_del_msg = ResourceLoader.getDrawableIdByName("aliwx_menu_del_msg");
        if(aliwx_clear_chatting_msg != 0 && menu_del_msg != 0) {
            menu.add(0, aliwx_clear_chatting_msg, 0, aliwx_clear_chatting_msg).setIcon(menu_del_msg);
        }

        super.onCreateOptionsMenu(menu, inflater);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        int aliwx_clear_chatting_msg = ResourceLoader.getIdByName(this.mContext, "string", "aliwx_clear_chatting_msg");
        if(item.getItemId() == aliwx_clear_chatting_msg) {
            this.presenter.initClearMsgDialog();
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    public float getScale() {
        return this.scale;
    }

    private void initAtView(View view) {
        if(this.isAtEnable) {
            if(this.mCvsType == YWConversationType.Tribe.getValue()) {
                ViewGroup titleLayout = (ViewGroup)view.findViewById(ResourceLoader.getIdByName(this.mContext, "id", "title_layout"));
                if(titleLayout != null) {
                    View titleView = titleLayout.getChildAt(0);
                    if(titleView != null) {
                        this.atImage = titleView.findViewById(ResourceLoader.getIdByName(this.getActivity(), "id", "aliwx_at_content"));
                        this.atUnDisposeView = (ImageView)titleView.findViewById(ResourceLoader.getIdByName(this.getActivity(), "id", "aliwx_at_msg_unread"));
                    }
                } else {
                    this.atImage = view.findViewById(ResourceLoader.getIdByName(this.getActivity(), "id", "aliwx_at_content"));
                    this.atUnDisposeView = (ImageView)view.findViewById(ResourceLoader.getIdByName(this.getActivity(), "id", "aliwx_at_msg_unread"));
                }

                if(this.atImage != null) {
                    this.atImage.setVisibility(View.VISIBLE);
                    this.atImage.setOnClickListener(new View.OnClickListener() {
                        public void onClick(View view) {
                            Intent intent = new Intent(ChattingFragment.this.getActivity(), AtMsgListActivity.class);
                            Bundle bundle = new Bundle();
                            bundle.putString("conversationId", ChattingFragment.this.conversationId);
                            bundle.putLong("extraTribeId", ChattingFragment.this.tribeId);
                            YWConversation conversation = ChattingFragment.this.presenter.getConversation();
                            String userId = null;
                            if(conversation instanceof TribeConversation) {
                                userId = ((TribeConversation)conversation).mWxAccount.getLid();
                            }

                            bundle.putString("extraUserId", userId);
                            intent.putExtra("bundle", bundle);
                            ChattingFragment.this.startActivity(intent);
                        }
                    });
                }

                if(this.atUnDisposeView != null && this.atUnDisposeView.getVisibility() != View.VISIBLE) {
                    this.atUnDisposeView.setVisibility(View.VISIBLE);
                }
            } else {
                if(this.atImage != null && this.atImage.getVisibility() == View.VISIBLE) {
                    this.atImage.setVisibility(View.INVISIBLE);
                }

                if(this.atUnDisposeView != null && this.atUnDisposeView.getVisibility() == View.VISIBLE) {
                    this.atUnDisposeView.setVisibility(View.INVISIBLE);
                }
            }

        }
    }

    private void addUnreadAtMsgToMsgList(List<YWMessage> msgList, YWMessage message) {
        if(this.isAtEnable) {
            if(msgList == null || msgList.size() > 0) {
                if(message != null && !msgList.contains(message)) {
                    if(this.messagesListCopy == null) {
                        this.messagesListCopy = new ArrayList();
                    } else {
                        this.messagesListCopy.clear();
                    }

                    this.messagesListCopy.addAll(this.list);
                    this.list.clear();
                    this.mPullRefreshListView.setMode(PullToRefreshBase.Mode.BOTH);
                    this.loadMsgContext(message, 20, 2);
                    this.sendAtAckForMsg(message);
                } else if(message != null) {
                    this.sendAtAckForMsg(message);
                }

            }
        }
    }

    private void selectAtMsg(YWMessage message) {
        if(this.isAtEnable) {
            if(message != null && !this.list.contains(message)) {
                if(this.messagesListCopy == null) {
                    this.messagesListCopy = new ArrayList();
                } else {
                    this.messagesListCopy.clear();
                }

                this.messagesListCopy.addAll(this.list);
                this.list.clear();
                this.mPullRefreshListView.setMode(PullToRefreshBase.Mode.BOTH);
                this.loadMsgContext(message, 20, 2);
            } else {
                this.scrollToAtMsgPosition(this.list, message);
            }

        }
    }

    private void loadMsgContext(final YWMessage message, int count, int rangeFlag) {
        if(this.isAtEnable) {
            final int index = this.listView.getFirstVisiblePosition();
            View v = this.listView.getChildAt(0);
            final int top = v == null?0:v.getTop();
            this.presenter.loadContextForMsg(message, count, rangeFlag, new SyncContextForMsgCallback(this.presenter.getConversation(), new IWxCallback() {
                public void onSuccess(Object... result) {
                    if(ChattingFragment.this.mPullRefreshListView != null && ChattingFragment.this.mPullRefreshListView.isRefreshing()) {
                        ChattingFragment.this.mPullRefreshListView.onRefreshComplete(false, true);
                    }

                    if(result != null && result[0] != null) {
                        List msgs = (List)result[0];
                        if(!ChattingFragment.this.isPullUpToLoad) {
                            ChattingFragment.this.list.addAll(msgs);
                            ChattingFragment.this.adapter.notifyDataSetChangedWithAsyncLoad();
                            ChattingFragment.this.scrollToAtMsgPosition(ChattingFragment.this.list, message);
                        } else {
                            if(msgs.size() == 0) {
                                ChattingFragment.this.mUIHandler.post(new Runnable() {
                                    public void run() {
                                        IMNotificationUtils.showToast(ResourceLoader.getStringIdByName("aliwx_no_more_at_msg_context"), ChattingFragment.this.getActivity());
                                    }
                                });
                                return;
                            }

                            ChattingFragment.this.list.addAll(msgs);
                            ChattingFragment.this.adapter.notifyDataSetChanged();
                            ChattingFragment.this.isPullUpToLoad = false;
                            WxLog.d(ChattingFragment.TAG, "mFooterViewHeight:" + ChattingFragment.this.mFooterViewHeight);
                            ChattingFragment.this.listView.post(new Runnable() {
                                public void run() {
                                    ChattingFragment.this.listView.setSelectionFromTop(index, top - ChattingFragment.this.mFooterViewHeight);
                                }
                            });
                        }
                    }

                }

                public void onError(int code, String info) {
                    if(ChattingFragment.this.mPullRefreshListView != null && ChattingFragment.this.mPullRefreshListView.isRefreshing()) {
                        ChattingFragment.this.mPullRefreshListView.onRefreshComplete(false, true);
                    }

                    if(ChattingFragment.this.list != null && !ChattingFragment.this.list.contains(message)) {
                        ChattingFragment.this.list.add(message);
                    }

                    ChattingFragment.this.adapter.notifyDataSetChangedWithAsyncLoad();
                }

                public void onProgress(int progress) {
                }
            }));
        }
    }

    private List<YWMessage> getUnreadAtMsgList() {
        if(this.unreadAtMsgList == null) {
            this.unreadAtMsgList = new ArrayList();
        }

        for(int i = 0; i < this.list.size(); ++i) {
            YWMessage message = (YWMessage)this.list.get(i);
            if(!message.isAtMsgHasRead()) {
                this.unreadAtMsgList.add(message);
            }
        }

        return this.unreadAtMsgList;
    }

    private void sendAtMsgAckForVisibleItems(final List<YWMessage> msgList, final TribeConversation conversation) {
        if(this.isAtEnable) {
            if(this.listView != null) {
                int lastVisibleItemPosition = this.listView.getLastVisiblePosition();
                if(lastVisibleItemPosition == -1) {
                    this.isNeedReviseVisiblePosition = true;
                }

                this.listView.post(new Runnable() {
                    public void run() {
                        int firstVisibleItemPosition = ChattingFragment.this.listView.getFirstVisiblePosition();
                        int lastVisibleItemPosition = ChattingFragment.this.listView.getLastVisiblePosition();
                        int visibleItemCount = lastVisibleItemPosition - firstVisibleItemPosition;
                        if(ChattingFragment.this.isNeedReviseVisiblePosition) {
                            lastVisibleItemPosition = ChattingFragment.this.list.indexOf(ChattingFragment.this.unReadAtMsg);
                            if(lastVisibleItemPosition == -1) {
                                lastVisibleItemPosition = ChattingFragment.this.list.size() - 1;
                            }

                            firstVisibleItemPosition = lastVisibleItemPosition - visibleItemCount;
                            ChattingFragment.this.isNeedReviseVisiblePosition = false;
                        }

                        if(ChattingFragment.this.listView.getChildAt(firstVisibleItemPosition) != null && ChattingFragment.this.listView.getChildAt(firstVisibleItemPosition).getTop() < 0) {
                            ++firstVisibleItemPosition;
                        }

                        if(lastVisibleItemPosition == -1) {
                            lastVisibleItemPosition = msgList.size() - 1;
                        }

                        final ArrayList messages = new ArrayList();

                        for(int i = firstVisibleItemPosition; i <= lastVisibleItemPosition && i < msgList.size() && i >= 0; ++i) {
                            if(ChattingFragment.this.unreadAtMsgList.contains(msgList.get(i))) {
                                messages.add(msgList.get(i));
                            }
                        }

                        conversation.sendAtMsgReadAckBatch(messages, new IWxCallback() {
                            public void onSuccess(Object... result) {
                                if(ChattingFragment.this.unreadAtMsgList != null) {
                                    ChattingFragment.this.unreadAtMsgList.removeAll(messages);
                                }

                                if(ChattingFragment.this.unreadAtMsgList == null || ChattingFragment.this.unreadAtMsgList.size() == 0) {
                                    conversation.setLatestUnreadAtMessage((YWMessage)null);
                                    conversation.setHasUnreadAtMsg(false);
                                    if(ChattingFragment.this.atUnDisposeView != null) {
                                        ChattingFragment.this.mUIHandler.postDelayed(new Runnable() {
                                            public void run() {
                                                ChattingFragment.this.atUnDisposeView.setVisibility(View.GONE);
                                            }
                                        }, 50L);
                                    }
                                }

                            }

                            public void onError(int code, String info) {
                            }

                            public void onProgress(int progress) {
                            }
                        });
                    }
                });
            }
        }
    }

    private void sendAtAckForMsg(final YWMessage message) {
        if(this.isAtEnable) {
            if(this.presenter.getConversation() != null && this.presenter.getConversation() instanceof TribeConversation) {
                final TribeConversation conversation = (TribeConversation)this.presenter.getConversation();
                conversation.sendAtMsgReadAck(message, new IWxCallback() {
                    public void onSuccess(Object... result) {
                        if(message.equals(ChattingFragment.this.unReadAtMsg)) {
                            ChattingFragment.this.unReadAtMsg = null;
                        }

                        List list = conversation.getUnreadAtMsgInConversation(conversation.mWxAccount.getLid());
                        if(list == null || list.size() == 0) {
                            conversation.setHasUnreadAtMsg(false);
                            conversation.setLatestUnreadAtMessage((YWMessage)null);
                            if(ChattingFragment.this.atUnDisposeView != null) {
                                ChattingFragment.this.mUIHandler.postDelayed(new Runnable() {
                                    public void run() {
                                        ChattingFragment.this.atUnDisposeView.setVisibility(View.GONE);
                                    }
                                }, 0L);
                            }
                        }

                    }

                    public void onError(int code, String info) {
                    }

                    public void onProgress(int progress) {
                    }
                });
            }

        }
    }

    private void scrollToAtMsgPosition(List<YWMessage> messageList, YWMessage message) {
        if(this.isAtEnable) {
            final int index = messageList.indexOf(message);
            if(index >= 0) {
                int top = 0;
                if(index > 0) {
                    int finalTop = this.listView.getFirstVisiblePosition();
                    if(this.listView.getChildAt(finalTop) != null) {
                        top = this.listView.getChildAt(finalTop).getTop();
                    }
                }

                final int finalTop1 = top;
                this.listView.post(new Runnable() {
                    public void run() {
                        ChattingFragment.this.listView.setSelectionFromTop(index, finalTop1);
                    }
                });
            }
        }
    }

    private void addLoadMoreMsgsToOriginalList() {
        if(this.messagesListCopy != null && this.messagesListCopy.size() > 0) {
            HashSet set = new HashSet(this.list);
            set.addAll(this.messagesListCopy);
            this.list.clear();
            this.list.addAll(set);
            Collections.sort(this.list, new Comparator() {
                @Override
                public int compare(Object o, Object t1) {
                    return ((YWMessage)o).getMsgId() - ((YWMessage)t1).getMsgId() > 0L ? 1 : -1;
                }

//                @Override
//                public int compare(YWMessage lhs, YWMessage rhs) {
//                    return lhs.getMsgId() - rhs.getMsgId() > 0L ? 1 : -1;
//                }
            });
            this.messagesListCopy.clear();
            this.adapter.notifyDataSetChanged();
        }

    }

    private void restoreMessageList() {
        if(this.messagesListCopy != null && this.messagesListCopy.size() != 0) {
            if(this.list != null && this.list.size() > 0) {
                if(this.list.contains(this.messagesListCopy.get(0))) {
                    return;
                }

                this.list.clear();
                this.list.addAll(this.messagesListCopy);
                this.adapter.notifyDataSetChangedWithAsyncLoad();
            }

        }
    }

    private boolean sendAtMsgByIntent(Intent intent) {
        try {
            Serializable e = intent.getSerializableExtra("sendAtMsgUnreadList");
            ArrayList atMemberList = (ArrayList)e;
            HashMap atMemberMap = new HashMap();
            Iterator i$ = atMemberList.iterator();

            while(true) {
                while(true) {
                    while(i$.hasNext()) {
                        YWTribeMember tribeMember = (YWTribeMember)i$.next();
                        String shortUserId = tribeMember.getUserId();
                        String appKey = tribeMember.getAppKey();
                        String longId = AccountInfoTools.getPrefix(appKey) + shortUserId;
                        if(this.contactService == null) {
                            this.contactService = WXAPI.getInstance().getContactService();
                        }

                        IYWContact contact = null;
                        if(this.contactService != null) {
                            contact = this.contactService.getContactProfileInfo(shortUserId, appKey);
                        }

                        if(this.tribeMembers != null && !TextUtils.isEmpty((CharSequence)this.tribeMembers.get(longId))) {
                            if(IMChannel.getAppId() == 2) {
                                atMemberMap.put(shortUserId, longId);
                            } else if(IMChannel.getAppId() == WXConstant.APPID.APPID_OPENIM) {
                                if(contact != null && shortUserId.equals(this.tribeMembers.get(longId))) {
                                    atMemberMap.put(longId, contact.getShowName());
                                } else {
                                    atMemberMap.put(longId, this.tribeMembers.get(longId));
                                }
                            }
                        } else if(IMChannel.getAppId() == 2) {
                            atMemberMap.put(shortUserId, longId);
                        } else if(IMChannel.getAppId() == WXConstant.APPID.APPID_OPENIM) {
                            if(contact != null) {
                                atMemberMap.put(longId, contact.getShowName());
                            } else {
                                atMemberMap.put(longId, shortUserId);
                            }
                        }
                    }

                    if(this.chattingReplyBar != null) {
                        this.chattingReplyBar.handleAtMsg("", atMemberMap);
                    }

                    return true;
                }
            }
        } catch (Exception var11) {
            return false;
        }
    }

    private boolean sendImageMsgByIntent(Intent intent) {
        String image = intent.getStringExtra("SendImageMsg");
        if(image == null) {
            return false;
        } else {
            if(this.chattingReplyBar != null) {
                this.chattingReplyBar.sendImageMsg(image);
            }

            return true;
        }
    }

    public boolean onNewIntent(Intent intent) {
        return this.sendAtMsgByIntent(intent);
    }

    private void getTribeMembersFromLocal() {
        IYWTribeService tribeService = WXAPI.getInstance().getTribeService();
        tribeService.getMembers(new IWxCallback() {
            private List<ComparableContact> contactList = new ArrayList();

            public void onSuccess(Object... result) {
                ArrayList members = (ArrayList)result[0];
                if(members != null && members.size() > 1) {
                    ChattingFragment.this.onGetMembersSuccess(members);
                } else {
                    ChattingFragment.this.getTribeMembersFromServer();
                }

            }

            public void onError(int code, String info) {
                WxLog.d(ChattingFragment.TAG + "@tribe", "getLocalMembers FAIL! TRIBEID = " + ChattingFragment.this.tribeId);
                ChattingFragment.this.getTribeMembersFromServer();
            }

            public void onProgress(int progress) {
            }
        }, this.tid);
    }

    public void getTribeMembersFromServer() {
        IYWTribeService tribeService = WXAPI.getInstance().getTribeService();
        tribeService.getMembersFromServer(new IWxCallback() {
            public void onSuccess(Object... result) {
                ArrayList members = (ArrayList)result[0];
                ChattingFragment.this.onGetMembersSuccess(members);
            }

            public void onError(int code, String info) {
                WxLog.d(ChattingFragment.TAG + "@tribe", "getMembersFromServer FAIL! TRIBEID = " + ChattingFragment.this.tid);
            }

            public void onProgress(int progress) {
            }
        }, this.tid);
    }

    private void onGetMembersSuccess(ArrayList<YWTribeMember> members) {
        if(this.tribeMembers == null) {
            this.tribeMembers = new HashMap(members.size());
        } else {
            this.tribeMembers.clear();
        }

        Iterator i$ = members.iterator();

        while(i$.hasNext()) {
            YWTribeMember member = (YWTribeMember)i$.next();
            this.tribeMembers.put(this.getLongUserId(member.getAppKey(), member.getUserId()), member.getShowName());
            this.tribeCache.getTribeNickCache().put(this.tribeId + AccountInfoTools.getPrefix(member.getAppKey()) + member.getUserId(), member.getTribeNick());
        }

    }

    private String getLongUserId(String appkey, String shortUserId) {
        return AccountInfoTools.getPrefix(appkey) + shortUserId;
    }

    public HashMap<String, String> getTribeMembers() {
        return this.tribeMembers;
    }

    protected void onSaveState() {
        if(this.chattingReplyBar != null) {
            this.chattingReplyBar.onSaveInstanceState();
        }

    }

    protected void onRestoreState() {
        if(this.chattingReplyBar != null) {
            this.chattingReplyBar.onRestoreState();
        }

    }

    private void saveStateToSharedPreference() {
        if(this.getView() != null) {
            this.onSaveState();
        }

    }

    private void restoreStateFromSharedPreference() {
        this.onRestoreState();
    }

    public boolean isRunning() {
        return this.mIsRunning;
    }

    private void getTribeMembersWithNick() {
        if(this.mCvsType == YWConversationType.Tribe.getValue() || this.mCvsType == YWConversationType.HJTribe.getValue()) {
            this.tribeManager = WXAPI.getInstance().getTribeService();
            if(this.tribeManager != null && this.tribeManager instanceof YWTribeManagerImpl) {
                this.iTribeManager = ((YWTribeManagerImpl)this.tribeManager).getTribeManager();
                if(this.iTribeManager != null) {
                    this.tribeCache = this.iTribeManager.getTribeCache();
                    if(this.tribeCache != null) {
                        Map map = this.tribeCache.getTribeNickCache();
                        int tidLen = String.valueOf(this.tid).length();
                        if(map != null) {
                            Iterator i$ = map.entrySet().iterator();

                            while(i$.hasNext()) {
                                Map.Entry entry = (Map.Entry)i$.next();
                                String key = ((String)entry.getKey()).substring(tidLen);
                                this.tribeMembers.put(key, (String) entry.getValue());
                            }
                        }
                    }
                }
            }

            this.getTribeMembersFromLocal();
        }

    }

    class LoadUnreadAtMessageTask extends AsyncTask<Void, Void, Void> {
        YWConversation conversation;
        public volatile boolean isRunning;

        public LoadUnreadAtMessageTask(YWConversation conversation) {
            this.conversation = conversation;
        }

        protected Void doInBackground(Void... params) {
            this.isRunning = true;
            YWConversation conversation = ChattingFragment.this.presenter.getConversation();
            if(conversation instanceof TribeConversation) {
                TribeConversation tribeConversation = (TribeConversation)conversation;
                ChattingFragment.this.unreadAtMsgList = conversation.getUnreadAtMsgInConversation(tribeConversation.mWxAccount.getLid());
            }

            return null;
        }

        protected void onPostExecute(Void v) {
            this.isRunning = false;
            if(this.conversation instanceof TribeConversation && ChattingFragment.this.unreadAtMsgList != null && ChattingFragment.this.unreadAtMsgList.size() > 0) {
                ChattingFragment.this.sendAtMsgAckForVisibleItems(ChattingFragment.this.list, (TribeConversation)this.conversation);
            }

            if(ChattingFragment.this.unreadAtMsgList != null) {
                if(ChattingFragment.this.unreadAtMsgList.size() > 0) {
                    if(ChattingFragment.this.atUnDisposeView != null) {
                        ChattingFragment.this.atUnDisposeView.setVisibility(View.VISIBLE);
                    }
                } else if(ChattingFragment.this.atUnDisposeView != null) {
                    ChattingFragment.this.atUnDisposeView.setVisibility(View.GONE);
                }
            }

        }

        protected void onCancelled() {
            super.onCancelled();
            this.isRunning = false;
        }
    }

    static class RangeFlag {
        public static final int ABOVE = 0;
        public static final int BELOW = 1;
        public static final int BOTH = 2;

        RangeFlag() {
        }
    }

    /**
     * 修改标题的接口
     */
   public interface ChattingInterface{
        void changeText(String text);
    }
}

