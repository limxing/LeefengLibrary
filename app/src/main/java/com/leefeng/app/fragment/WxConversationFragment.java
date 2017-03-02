//package com.limxing.app.fragment;
//
///**
// * Created by limxing on 16/1/23.
// */
////
//// Source code recreated from a .class file by IntelliJ IDEA
//// (powered by Fernflower decompiler)
////
//
//import android.annotation.TargetApi;
//import android.app.Activity;
//import android.app.AlertDialog;
//import android.content.Context;
//import android.content.DialogInterface;
//import android.content.Intent;
//import android.os.Build;
//import android.os.Bundle;
//import android.os.Handler;
//import android.os.Looper;
//import android.text.Editable;
//import android.text.TextUtils;
//import android.text.TextWatcher;
//import android.view.LayoutInflater;
//import android.view.MotionEvent;
//import android.view.View;
//import android.view.ViewGroup;
//import android.view.inputmethod.InputMethodManager;
//import android.widget.AbsListView;
//import android.widget.AdapterView;
//import android.widget.Button;
//import android.widget.EditText;
//import android.widget.Filter;
//import android.widget.ListView;
//
//import com.alibaba.mobileim.WXAPI;
//import com.alibaba.mobileim.YWAPI;
//import com.alibaba.mobileim.YWAccountType;
//import com.alibaba.mobileim.YWChannel;
//import com.alibaba.mobileim.aop.AdviceObjectInitUtil;
//import com.alibaba.mobileim.aop.AspectConversationFragment;
//import com.alibaba.mobileim.aop.BaseAdvice;
//import com.alibaba.mobileim.aop.PointCutEnum;
//import com.alibaba.mobileim.channel.event.IWxCallback;
//import com.alibaba.mobileim.channel.util.WxLog;
//import com.alibaba.mobileim.contact.IYWContact;
//import com.alibaba.mobileim.conversation.IYWConversationListener;
////import com.alibaba.mobileim.conversation.YWConversation;
//import com.alibaba.mobileim.conversation.YWConversation;
//import com.alibaba.mobileim.conversation.YWConversationType;
//import com.alibaba.mobileim.conversation.YWMessage;
//import com.alibaba.mobileim.conversation.YWP2PConversationBody;
//import com.alibaba.mobileim.conversation.YWTribeConversationBody;
//import com.alibaba.mobileim.fundamental.widget.WxAlertDialog;
//import com.alibaba.mobileim.fundamental.widget.refreshlist.PullToRefreshBase;
//import com.alibaba.mobileim.fundamental.widget.refreshlist.PullToRefreshListView;
//import com.alibaba.mobileim.gingko.model.contact.ISearchable;
//import com.alibaba.mobileim.gingko.presenter.contact.YWContactManagerImpl;
//import com.alibaba.mobileim.kit.chat.adapter.SearchFilter;
//import com.alibaba.mobileim.kit.common.IMPushNotificationHandler;
//import com.alibaba.mobileim.kit.common.IMUtility;
//import com.alibaba.mobileim.kit.common.StateTitleHelper;
//import com.alibaba.mobileim.kit.conversation.ConversationAdapter;
//import com.alibaba.mobileim.lib.model.message.Message;
//import com.alibaba.mobileim.lib.presenter.account.Account;
//import com.alibaba.mobileim.lib.presenter.contact.IContactListListener;
//import com.alibaba.mobileim.lib.presenter.conversation.Conversation;
//import com.alibaba.mobileim.lib.presenter.conversation.ConversationManager;
//import com.alibaba.mobileim.lib.presenter.conversation.TribeConversation;
//import com.alibaba.mobileim.lib.presenter.conversation.YWConversationManagerImpl;
//import com.alibaba.mobileim.ui.WxChattingActvity;
//import com.alibaba.mobileim.utility.IMPrefsTools;
//import com.alibaba.mobileim.utility.IMUtil;
//import com.alibaba.mobileim.utility.LeakCanaryHandler;
//import com.alibaba.mobileim.utility.ResourceLoader;
//import com.alibaba.wxlib.thread.WXThreadPoolMgr;
//import com.alibaba.wxlib.track.Tracker;
//import com.limxing.app.activity.ChattingActivity;
//
//import java.util.ArrayList;
//import java.util.Iterator;
//import java.util.List;
//
//public abstract class WxConversationFragment extends AspectConversationFragment implements AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener, AbsListView.OnScrollListener, IContactListListener {
//    private static final String TAG = WxConversationFragment.class.getSimpleName();
//    protected static final long POST_DELAYED_TIME = 300L;
//    private ListView mMessageListView;
//    private ConversationAdapter adapter;
//    private final Handler handler = new Handler();
//    private int max_visible_item_count = 0;
//    private StateTitleHelper titleHelper;
//    private List<YWConversation> mConversations;
//    private Activity mContext;
//    private String mUserId;
//    private View mProgress;
//    private int mAccountType;
//    private String mCallerFlag;
//    private YWConversationManagerImpl conversationManager;
//    private BaseAdvice baseAdvice;
//    private BaseAdvice baseAdviceUI;
//    private BaseAdvice baseAdviceOperation;
//    private View view;
//    private boolean onViewCreatedDid;
//    private View mHeadView;
//    private Handler mUIHandler;
//    private boolean isAtEnable;
//    private Account account;
//    private boolean mIsPullListviewInited;
//    private IYWConversationListener mCvsListListener;
//    private PullToRefreshListView mPullToRefreshListView;
//    private Runnable cancelRefresh;
//    private WxConversationFragment.SearcherStateManager mSearcherStateManager;
//    private ListView mSearchListView;
//    private View mSearchConversationLayout;
//    private View mSearchLayout;
//    private ConversationAdapter mSearchAdapter;
//    private List<YWConversation> mSearchList;
//    private EditText mSearchText;
//    private SearchFilter mFilter;
//    private Button mCancelBtn;
//    private long mConversationListTimeStamp;
//
//    public WxConversationFragment() {
//        this.baseAdvice = AdviceObjectInitUtil.initAdvice(PointCutEnum.CONVERSATION_FRAGMENT_POINTCUT, this);
//        this.baseAdviceUI = AdviceObjectInitUtil.initAdvice(PointCutEnum.CONVERSATION_FRAGMENT_UI_POINTCUT, this);
//        this.baseAdviceOperation = AdviceObjectInitUtil.initAdvice(PointCutEnum.CONVERSATION_FRAGMENT_OPERATION_POINTCUT, this);
//        this.mUIHandler = new Handler(Looper.getMainLooper());
//        this.mIsPullListviewInited = false;
//        this.mCvsListListener = new IYWConversationListener() {
//            public void onItemUpdated() {
//                WxConversationFragment.this.updateConversationListUI();
//                WxConversationFragment.this.updateSearchAdapterIfNecessary();
//                WxConversationFragment.this.refreshAdapter();
//                int totalCount = WxConversationFragment.this.getTotalUnreadCount();
//                if(totalCount == 0) {
//                    IMPushNotificationHandler.getInstance().cancelNotification();
//                }
//
//            }
//        };
//        this.cancelRefresh = new Runnable() {
//            public void run() {
//                if(WxConversationFragment.this.mPullToRefreshListView != null) {
//                    WxConversationFragment.this.mPullToRefreshListView.onRefreshComplete(false, false, ResourceLoader.getIdByName(WxConversationFragment.this.mContext, "string", "aliwx_sync_failed"));
//                }
//
//            }
//        };
//        this.mSearchList = new ArrayList();
//    }
//
//    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        WxLog.d(TAG, "onCreateView");
//        if(this.onViewCreatedDid && this.view != null) {
//            ViewGroup bundle1 = (ViewGroup)this.view.getParent();
//            WxLog.i(TAG, "parent = " + bundle1);
//            if(bundle1 != null) {
//                bundle1.removeView(this.view);
//            }
//
//            return this.view;
//        } else {
//            this.onViewCreatedDid = false;
//            this.mContext = this.getActivity();
//            this.view = View.inflate(this.mContext, ResourceLoader.getIdByName(this.mContext, "layout", "aliwx_message"), null);
//            this.mProgress = this.view.findViewById(ResourceLoader.getIdByName(this.mContext, "id", "progress"));
//            this.mProgress.setVisibility(View.VISIBLE);
//            this.mAccountType = 0;
//            Bundle bundle = this.getArguments();
//            if(bundle != null) {
//                this.mAccountType = bundle.getInt(YWAccountType.class.getSimpleName(), 0);
//            }
//
//            if(this.mAccountType != 0) {
////                WXAPI.getInstance().changeTopAccountType(YWAccountType.valueOf(this.mAccountType));
//            } else if(this.getAccountType() != null) {
//                this.mAccountType = this.getAccountType().getValue();
//                WXAPI.getInstance().changeTopAccountType(this.getAccountType());
//            }
//
//            this.mUserId = WXAPI.getInstance().getLoginUserId();
//            this.isAtEnable = YWAPI.getYWSDKGlobalConfig().enableTheTribeAtRelatedCharacteristic();
//            WxLog.d(TAG, "onCreateView end");
//            return this.view;
//        }
//    }
//
//    public void onActivityCreated(Bundle savedInstanceState) {
//        super.onActivityCreated(savedInstanceState);
//        this.onActivityCreated_(savedInstanceState, this);
//        if(!this.onViewCreatedDid || TextUtils.isEmpty(WXAPI.getInstance().getLoginUserId())) {
//            WXThreadPoolMgr.getInstance().doAsyncRun(new Runnable() {
//                public void run() {
//                    WxConversationFragment.this.checkLoginState();
//                }
//            });
//            this.handler.postDelayed(new Runnable() {
//                public void run() {
//                    WxConversationFragment.this.mProgress.setVisibility(View.GONE);
//                }
//            }, 5000L);
//        }
//
//        this.initTitle();
//        this.initPullToRefreshView();
//    }
//
//    private void checkLoginState() {
//        for(int i = 0; i < 30; ++i) {
//            this.mUserId = WXAPI.getInstance().getLoginUserId();
//            if(!TextUtils.isEmpty(this.mUserId)) {
//                WxLog.i(TAG, "mUserId = " + this.mUserId);
//                this.handler.post(new Runnable() {
//                    public void run() {
//                        if(!WxConversationFragment.this.isFinished()) {
//                            WxConversationFragment.this.mProgress.setVisibility(View.GONE);
//                            WxConversationFragment.this.init();
//                            WxConversationFragment.this.initMsgListView();
//                            WxConversationFragment.this.viewOnResume();
//                            WxConversationFragment.this.onViewCreatedDid = true;
//                        }
//                    }
//                });
//                break;
//            }
//
//            WxLog.i(TAG, "user not login, i = " + i);
//
//            try {
//                Thread.sleep(1000L);
//            } catch (InterruptedException var3) {
//                var3.printStackTrace();
//            }
//        }
//
//    }
//
//    private int getTotalUnreadCount() {
//        int totalCount = 0;
//        YWConversation yWConversation;
//        if(this.mConversations != null) {
//            for(Iterator i$ = this.mConversations.iterator(); i$.hasNext(); totalCount += yWConversation.getUnreadCount()) {
//                yWConversation = (YWConversation)i$.next();
//            }
//        }
//
//        return totalCount;
//    }
//
//    protected void init() {
//        if(IMUtility.isThirdAppActivity(this.mContext)) {
//            this.mContext.getWindow().setWindowAnimations(0);
//        }
//
//        this.initStateManagers();
//        this.initAdapters();
//        this.initView();
//        this.getAtMsgList(this.mConversations);
//        if(this.mAccountType != YWAccountType.open.getValue()) {
//            if(!WXAPI.getInstance().shouldReloadRecent()) {
//                this.handler.post(new Runnable() {
//                    public void run() {
//                        if(WxConversationFragment.this.titleHelper != null) {
//                            WxConversationFragment.this.titleHelper.showReceiverLayout(true);
//                        }
//
//                    }
//                });
//                this.handler.postDelayed(new Runnable() {
//                    public void run() {
//                        if(WxConversationFragment.this.titleHelper != null) {
//                            WxConversationFragment.this.titleHelper.showReceiverLayout(false);
//                        }
//
//                    }
//                }, 5000L);
//            } else {
//                this.handler.post(new Runnable() {
//                    public void run() {
//                        if(WxConversationFragment.this.titleHelper != null) {
//                            WxConversationFragment.this.titleHelper.showReceiverLayout(true);
//                        }
//
//                    }
//                });
//                this.handler.postDelayed(new Runnable() {
//                    public void run() {
//                        if(WxConversationFragment.this.titleHelper != null) {
//                            WxConversationFragment.this.titleHelper.showReceiverLayout(false);
//                        }
//
//                    }
//                }, 5000L);
//            }
//        }
//
//        this.checkUnreadAtMsgAndUpdateUI();
//        if(WXAPI.getInstance().checkLoadRecentMessages() && this.mAccountType != YWAccountType.open.getValue() && this.titleHelper != null) {
//            this.titleHelper.showReceiverLayout(false);
//        }
//
//        WXAPI.getInstance().getConversationManager().removeConversationListener(this.mCvsListListener);
//        WXAPI.getInstance().getConversationManager().addConversationListener(this.mCvsListListener);
//    }
//
//    private synchronized void initPullToRefreshView() {
//        if(!this.mIsPullListviewInited) {
//            this.mIsPullListviewInited = true;
//            this.mPullToRefreshListView = (PullToRefreshListView)this.getView().findViewById(ResourceLoader.getIdByName(this.mContext, "id", "message_list"));
//            this.mPullToRefreshListView.setVisibility(View.VISIBLE);
//            this.mMessageListView = (ListView)this.mPullToRefreshListView.getRefreshableView();
//            if(this.getCustomEmptyViewInConversationUI(this.getActivity()) != null) {
//                this.mMessageListView.setEmptyView(this.getCustomEmptyViewInConversationUI(this.getActivity()));
//            }
//
//            this.mPullToRefreshListView.setMode(PullToRefreshBase.Mode.PULL_DOWN_TO_REFRESH);
//            this.mPullToRefreshListView.setPullToRefreshEnabled(this.getPullToRefreshEnabled());
//            this.mPullToRefreshListView.setShowIndicator(false);
//            this.mPullToRefreshListView.setDisableScrollingWhileRefreshing(false);
//            this.mPullToRefreshListView.setRefreshingLabel(this.getResources().getString(ResourceLoader.getIdByName(this.mContext, "string", "aliwx_pull_to_refresh_contact_pull_label")));
//            this.mPullToRefreshListView.setPullLabel(this.getResources().getString(ResourceLoader.getIdByName(this.mContext, "string", "aliwx_pull_to_refresh_contact_pull_label")));
//            this.mPullToRefreshListView.setReleaseLabel(this.getResources().getString(ResourceLoader.getIdByName(this.mContext, "string", "aliwx_pull_to_refresh_contact_release_label")));
//            this.mPullToRefreshListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener() {
//                public void onRefresh() {
//                    String loginUserId = WXAPI.getInstance().getLoginUserId();
//
//                    if (TextUtils.isEmpty(loginUserId)) {
//                        WxConversationFragment.this.handler.postDelayed(WxConversationFragment.this.cancelRefresh, 3000L);
//                    } else {
//                        WxConversationFragment.this.handler.postDelayed(new Runnable() {
//                            public void run() {
//                                if (WXAPI.getInstance().getConversationManager() != null) {
//                                    WXAPI.getInstance().getConversationManager().syncRecentConversations(WxConversationFragment.this.new PresenterResult(false));
//                                }
//
//                                WxConversationFragment.this.handler.postDelayed(WxConversationFragment.this.cancelRefresh, 3000L);
//                            }
//                        }, 300L);
//                    }
//                }
//            });
//            this.advInit(this.getActivity(), this.mMessageListView);
//        }
//    }
//
//    private void resetTitleBar() {
//        String title = "";
//
//        try {
//            title = this.getString(ResourceLoader.getIdByName(this.mContext, "string", "aliwx_title_back"));
//        } catch (Exception var3) {
//            title = this.getString(ResourceLoader.getIdByName(this.mContext, "string", "aliwx_back"));
//        }
//
//        if(this.titleHelper != null) {
//            this.titleHelper.setLeftButtonListener(title, new View.OnClickListener() {
//                public void onClick(View v) {
//                    WxConversationFragment.this.mContext.finish();
//                    WxConversationFragment.this.mContext.overridePendingTransition(0, 0);
//                }
//            });
//        }
//
//    }
//
//    private void initTitle() {
//        this.titleHelper = new StateTitleHelper(this, this.getView().findViewById(ResourceLoader.getIdByName(this.mContext, "id", "title_self_state")), this.mContext, true);
//        int resId = ResourceLoader.getIdByName("string", "aliwx_conversation_title");
//        if(resId != 0) {
//            String shadow = YWChannel.getResources().getString(resId);
//            if(!TextUtils.isEmpty(shadow)) {
//                this.titleHelper.setName(shadow);
//            }
//        }
//
//        View shadow1;
//        if(this.needHideTitleView(this)) {
//            shadow1 = this.view.findViewById(ResourceLoader.getIdByName(this.mContext, "id", "title_bar_shadow_view"));
//            shadow1.setVisibility(View.GONE);
//        }
//
//        if(this.needHideTitleView(this)) {
//            shadow1 = this.view.findViewById(ResourceLoader.getIdByName(this.mContext, "id", "title_bar_shadow_view"));
//            shadow1.setVisibility(View.GONE);
//        }
//
//    }
//
//    private void initMsgListView() {
//        if(this.mMessageListView != null) {
//            this.mMessageListView.setAdapter(this.adapter);
//            this.mMessageListView.setOnItemClickListener(this);
//            this.mMessageListView.setOnItemLongClickListener(this);
//            this.mMessageListView.setOnScrollListener(this);
//            View view = this.getView().findViewById(ResourceLoader.getIdByName(this.mContext, "id", "title_self_state"));
//            if(view != null) {
//                view.setOnClickListener(new View.OnClickListener() {
//                    @TargetApi(11)
//                    public void onClick(View v) {
//                        if(WxConversationFragment.this.mMessageListView != null && WxConversationFragment.this.mMessageListView.getAdapter() != null && WxConversationFragment.this.mMessageListView != null && WxConversationFragment.this.mMessageListView.getAdapter() != null) {
//                            WxConversationFragment.this.mMessageListView.setSelection(0);
//                        }
//
//                    }
//                });
//            }
//        }
//
//    }
//
//    protected void messgeListItemLongClick(YWConversation c, int position) {
//        if(c != null && c.getUnreadCount() > 0) {
//            IMPushNotificationHandler.getInstance().cancelNotification();
//        }
//
//        if(c != null) {
//            ;
//        }
//
//        WXAPI.getInstance().getConversationManager().deleteConversation(c);
//    }
//
//    public void onItemClick(AdapterView<?> parent, View view, int position1, long id) {
//        Tracker.startSession("openChattingActivity");
//        Object start = Tracker.beginStep("openChattingActivity", "clickConversationItem");
//        List localConversations = this.mConversations;
//        int position = 0;
//        if(parent == this.mMessageListView) {
//            position = position1 - this.mMessageListView.getHeaderViewsCount();
//            localConversations = this.mConversations;
//        } else if(parent == this.mSearchListView) {
//            position = position1 - this.mSearchListView.getHeaderViewsCount();
//            localConversations = this.mSearchList;
//        }
//
//        if(position >= 0 && position < localConversations.size()) {
//            YWConversation conversation = (YWConversation)localConversations.get(position);
//            if(conversation != null) {
//                if(this.onItemClick(this, conversation)) {
//                    return;
//                }
//
//                int conversationType = conversation.getConversationType().getValue();
//                Intent intent = new Intent(this.mContext, ChattingActivity.class);
//                intent.putExtra("caller", this.mCallerFlag);
//                if(this.getConversationItemClickIntent(this, conversation) != null) {
//                    intent = this.getConversationItemClickIntent(this, conversation);
//                }
//
//                intent.putExtra(YWAccountType.class.getSimpleName(), this.mAccountType);
//                if(conversation.getConversationType() != YWConversationType.P2P && conversation.getConversationType() != YWConversationType.SHOP) {
//                    if(conversation.getConversationType() == YWConversationType.Tribe) {
//                        long tribeId = ((YWTribeConversationBody)conversation.getConversationBody()).getTribe().getTribeId();
//                        if(conversation.hasUnreadAtMsg()) {
//                            intent.putExtra("atMsgId", conversation.getLatestUnreadAtMsg());
//                        }
//
//                        intent.putExtra("extraTribeId", tribeId);
//                        intent.putExtra("conversationType", conversationType);
//                        this.startActivity(intent);
//                    } else if(conversation.getConversationType() == YWConversationType.Custom) {
//                        this.onCustomConversationItemClick(conversation);
//                    }
//                } else {
//                    intent.putExtra("conversationId", conversation.getConversationId());
//                    intent.putExtra("conversationType", conversationType);
//                    intent.addFlags(67108864);
//                    this.startActivityWithAnim(intent);
//                }
//
//                Tracker.endStep(start, 0, "openChattingActivity");
//            }
//        }
//
//    }
//
//    public void onDestroy() {
//        super.onDestroy();
//        this.onDestory_(this);
//        if(this.adapter != null) {
//            ((YWContactManagerImpl)WXAPI.getInstance().getContactService()).removeProfileUpdateListener(this.adapter);
//            this.adapter.recycle();
//        }
//
//        this.desotryAdv();
//        if(this.titleHelper != null) {
//            this.titleHelper.recycle();
//        }
//
//        WxLog.i(TAG, "ondestroy");
//        WXAPI.getInstance().getConversationManager().removeConversationListener(this.mCvsListListener);
//        LeakCanaryHandler.getInstance().watch(this);
//    }
//
//    public void onResume() {
//        super.onResume();
//        this.checkUnreadAtMsgAndUpdateUI();
//        this.onResume_(this);
//        this.isAtEnable = YWAPI.getYWSDKGlobalConfig().enableTheTribeAtRelatedCharacteristic();
//        this.advPlay();
//        Bundle bundle = this.getArguments();
//        String currentUserId;
//        if(bundle != null) {
//            currentUserId = bundle.getString("receiverId");
//            if(!TextUtils.isEmpty(currentUserId)) {
//                WXAPI.switchAccount(currentUserId);
//            }
//
//            this.mCallerFlag = bundle.getString("caller");
//        }
//
//        currentUserId = WXAPI.getInstance().getLoginUserId();
//        if(!TextUtils.isEmpty(this.mUserId) && this.mUserId.equals(currentUserId)) {
//            this.mProgress.setVisibility(View.GONE);
//            WxLog.d(TAG + "@kiked off", " WXAPI.getInstance(): " + WXAPI.getInstance().toString());
//            WxLog.d(TAG + "@kiked off", " WXAPI.getInstance().getLoginUserId(): " + WXAPI.getInstance().getLoginUserId().toString());
//            WxLog.d(TAG + "@kiked off", "   WXAPI.getInstance().getConversationManager(): " + WXAPI.getInstance().getConversationManager().toString());
//            WxLog.d(TAG + "@kiked off", "  WXAPI.getInstance().getConversationManager().getConversationList(): " + WXAPI.getInstance().getConversationManager().getConversationList().toString());
//            if(this.mConversations != null) {
//                WxLog.d(TAG + "@kiked off", " mConversations: " + this.mConversations.toString());
//                WxLog.d(TAG + "@kiked off", "  mConversations.hashCode(): " + Integer.toHexString(this.mConversations.hashCode()));
//                WxLog.d(TAG + "@kiked off", "  WXAPI.getInstance().getConversationManager().getConversationList().hashCode(): " + Integer.toHexString(WXAPI.getInstance().getConversationManager().getConversationList().hashCode()));
//            }
//
//            if(this.adapter != null && this.adapter.getList().hashCode() != WXAPI.getInstance().getConversationManager().getConversationList().hashCode()) {
//                this.mConversations = WXAPI.getInstance().getConversationManager().getConversationList();
//                this.updateAdapter();
//            }
//
//            this.updateSearchAdapterIfNecessary();
//            if(WXAPI.getInstance().checkLoadRecentMessages() && this.titleHelper != null && this.mAccountType != YWAccountType.open.getValue()) {
//                this.titleHelper.showReceiverLayout(false);
//            }
//
//            this.viewOnResume();
//        } else {
//            this.mUserId = WXAPI.getInstance().getLoginUserId();
//            if(!TextUtils.isEmpty(this.mUserId)) {
//                this.mProgress.setVisibility(View.GONE);
//                this.init();
//                this.initMsgListView();
//                if(WXAPI.getInstance().checkLoadRecentMessages() && this.titleHelper != null && this.mAccountType != YWAccountType.open.getValue()) {
//                    this.titleHelper.showReceiverLayout(false);
//                }
//
//                this.viewOnResume();
//            } else {
//                WxLog.i(TAG, "user not login");
//            }
//        }
//
//    }
//
//    private void updateAdapter() {
//        if(this.adapter != null) {
//            this.adapter.setList(this.mConversations);
//        }
//
//    }
//
//    private void viewOnResume() {
//        this.checkUnreadAtMsgAndUpdateUI();
//        WXAPI.getInstance().getConversationManager().removeConversationListener(this.mCvsListListener);
//        WXAPI.getInstance().getConversationManager().addConversationListener(this.mCvsListListener);
//        this.refreshAdapter();
//        if(this.titleHelper != null) {
//            this.titleHelper.helperOnrefresh();
//            this.resetTitleBar();
//        }
//
//        this.advPlay();
//    }
//
//    public void syncRecentConversations() {
//        WXAPI.getInstance().getConversationManager().getRecentConversations(20, true, true, new WxConversationFragment.PresenterResult(false));
//    }
//
//    public void onPause() {
//        super.onPause();
//        WXAPI.getInstance().getConversationManager().removeConversationListener(this.mCvsListListener);
//    }
//
//    private void refreshAdapter() {
//        if(this.mConversations != null && this.mConversations.size() > 0) {
//            this.advHidden();
//        } else {
//            this.advShow();
//        }
//
//        if(this.adapter != null) {
//            if(IMUtil.isMainThread()) {
//                this.adapter.notifyDataSetChangedWithAsyncLoad();
//            } else {
//                this.handler.post(new Runnable() {
//                    public void run() {
//                        WxConversationFragment.this.adapter.notifyDataSetChangedWithAsyncLoad();
//                    }
//                });
//            }
//        }
//
//    }
//
//    public boolean onItemLongClick(AdapterView<?> parent, View view, int positionItem, long id) {
//        if(this.mPullToRefreshListView.isRefreshing()) {
//            return true;
//        } else {
//            List tempLocalConversations = this.mConversations;
//            int tempPosition = 0;
//            if(parent == this.mMessageListView) {
//                tempPosition = positionItem - this.mMessageListView.getHeaderViewsCount();
//                tempLocalConversations = this.mConversations;
//            } else if(parent == this.mSearchListView) {
//                tempPosition = positionItem - this.mSearchListView.getHeaderViewsCount();
//                tempLocalConversations = this.mSearchList;
//            }
//
//            if(tempPosition < tempLocalConversations.size() && tempPosition >= 0) {
//                final YWConversation c = (YWConversation)tempLocalConversations.get(tempPosition);
//                if(c != null) {
//                    String title = null;
//                    if(c.getConversationType() == YWConversationType.Custom) {
//                        title = this.getCustomConversationName(c);
//                    } else if(c.getConversationType() == YWConversationType.P2P) {
//                        IYWContact menulist = ((YWP2PConversationBody)c.getConversationBody()).getContact();
//                        String items = menulist.getUserId();
//                        title = items;
//                        IYWContact alertDialog = IMUtility.getContactProfileInfo(items, menulist.getAppKey());
//                        if(alertDialog != null && !TextUtils.isEmpty(alertDialog.getShowName())) {
//                            title = alertDialog.getShowName();
//                        } else {
//                            alertDialog = WXAPI.getInstance().getWXIMContact(items);
//                            if(alertDialog != null) {
//                                title = alertDialog.getShowName();
//                            }
//                        }
//                    }
//
//                    ArrayList menulist1 = new ArrayList();
//                    if(this.onConversationItemLongClick(this, c)) {
//                        return true;
//                    }
//
//                    if(c.getConversationType() == YWConversationType.Custom) {
//                        List items1 = this.getCustomConversationLongClickMenuList(c);
//                        if(items1 != null) {
//                            menulist1.addAll(items1);
//                        }
//                    }
//
//                    if(c.isTop()) {
//                        menulist1.add(this.getString(ResourceLoader.getIdByName(this.mContext, "string", "aliwx_conversation_cancel_top")));
//                    } else {
//                        menulist1.add(this.getString(ResourceLoader.getIdByName(this.mContext, "string", "aliwx_conversation_top")));
//                    }
//
//                    menulist1.add(this.getString(ResourceLoader.getIdByName(this.mContext, "string", "aliwx_conversation_del")));
//                    final String[] items2 = new String[menulist1.size()];
//                    menulist1.toArray(items2);
//                    final int finalTempPosition = tempPosition;
//                    final List finalTempLocalConversations = tempLocalConversations;
//                    AlertDialog alertDialog1 = (new WxAlertDialog.Builder(this.mContext)).setTitle(title).setItems(items2, new android.content.DialogInterface.OnClickListener() {
//                        public void onClick(DialogInterface dialog, int which) {
//                            if(TextUtils.equals(items2[which], WxConversationFragment.this.getString(ResourceLoader.getIdByName(WxConversationFragment.this.mContext, "string", "aliwx_conversation_del")))) {
//                                if(finalTempPosition < finalTempLocalConversations.size()) {
//                                    WxConversationFragment.this.messgeListItemLongClick(c, finalTempPosition);
//                                }
//                            } else if(TextUtils.equals(items2[which], WxConversationFragment.this.getString(ResourceLoader.getIdByName(WxConversationFragment.this.mContext, "string", "aliwx_conversation_top")))) {
//                                WXAPI.getInstance().getConversationManager().setTopConversation(c);
//                            } else if(TextUtils.equals(items2[which], WxConversationFragment.this.getString(ResourceLoader.getIdByName(WxConversationFragment.this.mContext, "string", "aliwx_conversation_cancel_top")))) {
//                                WXAPI.getInstance().getConversationManager().removeTopConversation(c);
//                            }
//
//                            if(c.getConversationType() == YWConversationType.Custom) {
//                                WxConversationFragment.this.onCustomConversationItemLongClick(WxConversationFragment.this, c, items2[which]);
//                            }
//
//                        }
//                    }).setNegativeButton(this.getResources().getString(ResourceLoader.getIdByName(this.mContext, "string", "aliwx_cancel")), new android.content.DialogInterface.OnClickListener() {
//                        public void onClick(DialogInterface dialog, int id) {
//                            dialog.cancel();
//                        }
//                    }).create();
//                    if(!alertDialog1.isShowing()) {
//                        alertDialog1.show();
//                    }
//                }
//            }
//
//            return true;
//        }
//    }
//
//    private void cancelOtherTopConversation(List<YWConversation> conversations) {
//        for(int i = 0; i < conversations.size(); ++i) {
//            YWConversation conversation = (YWConversation)conversations.get(i);
//            if(conversation.isTop()) {
//                WXAPI.getInstance().getConversationManager().removeTopConversation(conversation);
//            }
//        }
//
//    }
//
//    public void onScrollStateChanged(AbsListView view, int scrollState) {
//        if(scrollState == 0 && this.adapter != null) {
//            this.adapter.loadAsyncTask();
//        }
//
//    }
//
//    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
//        this.max_visible_item_count = visibleItemCount > this.max_visible_item_count?visibleItemCount:this.max_visible_item_count;
//        if(this.adapter != null) {
//            this.adapter.setMax_visible_item_count(this.max_visible_item_count);
//            this.adapter.loadAsyncTask();
//        }
//
//    }
//
//    public boolean onBackPressed() {
//        return false;
//    }
//
//    public void onShow() {
//    }
//
//    public abstract YWAccountType getAccountType();
//
//    private void getAtMsgList(List<YWConversation> conversations) {
//        if(conversations != null && conversations.size() != 0) {
//            if(this.isAtEnable) {
//                Iterator i$ = conversations.iterator();
//
//                while(true) {
//                    YWConversation conversation;
//                    do {
//                        do {
//                            if(!i$.hasNext()) {
//                                return;
//                            }
//
//                            conversation = (YWConversation)i$.next();
//                            if(this.account == null) {
//                                this.account = ((Conversation)conversation).mWxAccount;
//                            }
//                        } while(this.account == null);
//                    } while(!this.account.isFirstLogin());
//
//                    WxLog.d(TAG, "----------------------执行网络请求查询@消息-----------------------");
//                    if(conversation.getConversationType() == YWConversationType.Tribe || conversation.getConversationType() == YWConversationType.HJTribe) {
//                        conversation.getMessageLoader().loadAtMessages(20, new WxConversationFragment.SyncAtMessageCallback(conversation));
//                    }
//
//                    this.account.setFirstLogin(false);
//                }
//            }
//        }
//    }
//
//    private void checkUnreadAtMsgAndUpdateUI() {
//        if(this.isAtEnable && this.mConversations != null) {
//            Iterator i$ = this.mConversations.iterator();
//
//            while(i$.hasNext()) {
//                YWConversation conversation = (YWConversation)i$.next();
//                if(conversation != null && conversation.getConversationType() == YWConversationType.Tribe) {
//                    if(this.conversationManager == null) {
//                        this.conversationManager = (YWConversationManagerImpl)WXAPI.getInstance().getConversationManager();
//                    }
//
//                    this.conversationManager.checkHasUnreadAtMsgs(this.getActivity(), conversation, new WxConversationFragment.CheckAtMsgInDBCallback(conversation));
//                }
//            }
//        }
//
//    }
//
//    private void updateConversationListUI() {
//        if(this.isAtEnable && this.mConversations != null) {
//            Iterator i$ = this.mConversations.iterator();
//
//            while(i$.hasNext()) {
//                YWConversation conversation = (YWConversation)i$.next();
//                if(conversation.getConversationType() == YWConversationType.Tribe && conversation.hasUnreadAtMsg() && conversation.getLatestUnreadAtMsg() != null) {
//                    this.adapter.notifyDataSetChanged();
//                }
//            }
//        }
//
//    }
//
//    private void refreshAdapterWithDelay(long delay) {
//        if(this.handler != null) {
//            this.handler.postDelayed(new Runnable() {
//                public void run() {
//                    WxConversationFragment.this.refreshAdapter();
//                }
//            }, delay);
//        }
//
//    }
//
//    public void onDeleteContact(String[] ids) {
//    }
//
//    public void onChange(int type) {
//        this.refreshAdapter();
//    }
//
//    public void initStateManagers() {
//        if(this.enableSearchConversations(this)) {
//            this.mSearcherStateManager = new WxConversationFragment.SearcherStateManager(this);
//        }
//
//        this.conversationManager = (YWConversationManagerImpl)WXAPI.getInstance().getConversationManager();
//    }
//
//    public void initAdapters() {
//        this.mConversations = WXAPI.getInstance().getConversationManager().getConversationList();
//        this.adapter = new ConversationAdapter(this, this.mContext, this.mConversations);
//        ((YWContactManagerImpl)WXAPI.getInstance().getContactService()).addProfileUpdateListener(this.adapter);
//        if(this.enableSearchConversations(this)) {
//            if(this.mConversations != null) {
//                this.mSearchList = new ArrayList(this.mConversations);
//            } else {
//                this.mSearchList = new ArrayList();
//            }
//
//            this.mSearchAdapter = new ConversationAdapter(this, this.mContext, this.mSearchList);
////            this.mFilter = new SearchFilter(this.mSearchList);
//            this.updateSearchAdapterIfNecessary();
//        }
//
//    }
//
//    private void updateSearchAdapterIfNecessary() {
//        if(this.enableSearchConversations(this) && this.mSearchAdapter != null && this.mSearcherStateManager != null && this.mConversationListTimeStamp < ConversationManager.mConversationListTimeStamp) {
//            this.mConversationListTimeStamp = ConversationManager.mConversationListTimeStamp;
//            this.mSearcherStateManager.updateSearchAdapter();
//        }
//
//    }
//
//    public void initView() {
//        this.initPullToRefreshView();
//        if(this.enableSearchConversations(this)) {
//            this.initSearchLayout();
//            this.mSearcherStateManager.initSearchListView();
//        }
//
//    }
//
//    private void initSearchLayout() {
//        if(this.mHeadView == null) {
//            this.mHeadView = this.mContext.getLayoutInflater().inflate(ResourceLoader.getIdByName(this.mContext, "layout", "aliwx_contacts_header_layout"), (ViewGroup)null);
//            this.mMessageListView.addHeaderView(this.mHeadView);
//        }
//
//        this.mSearchLayout = this.mHeadView.findViewById(ResourceLoader.getIdByName(this.mContext, "id", "aliwx_search_layout"));
//        this.mSearchLayout.setOnClickListener(this.mSearcherStateManager);
//    }
//
//    public class SearcherStateManager implements View.OnClickListener {
//        private WxConversationFragment context;
//
//        public SearcherStateManager(WxConversationFragment context) {
//            this.context = context;
//        }
//
//        private void initSearchListView() {
//            WxConversationFragment.this.mSearchText = (EditText)WxConversationFragment.this.view.findViewById(ResourceLoader.getIdByName(WxConversationFragment.this.mContext, "id", "aliwx_search_key"));
//            WxConversationFragment.this.mSearchText.addTextChangedListener(new WxConversationFragment.SearcherStateManager.SearchTextChangeWatcher());
//            WxConversationFragment.this.mCancelBtn = (Button)WxConversationFragment.this.view.findViewById(ResourceLoader.getIdByName(WxConversationFragment.this.mContext, "id", "aliwx_cancel_search"));
//            WxConversationFragment.this.mCancelBtn.setVisibility(View.VISIBLE);
//            WxConversationFragment.this.mCancelBtn.setOnClickListener(this);
//            WxConversationFragment.this.mSearchConversationLayout = WxConversationFragment.this.view.findViewById(ResourceLoader.getIdByName(WxConversationFragment.this.mContext, "id", "aliwx_search_contacts_layout"));
//            WxConversationFragment.this.mSearchConversationLayout.setOnClickListener(this);
//            WxConversationFragment.this.mSearchListView = (ListView)WxConversationFragment.this.view.findViewById(ResourceLoader.getIdByName(WxConversationFragment.this.mContext, "id", "aliwx_search_contacts_listview"));
//            WxConversationFragment.this.mSearchListView.setAdapter(WxConversationFragment.this.mSearchAdapter);
//            if(Build.VERSION.SDK_INT >= 9) {
//                WxConversationFragment.this.mSearchListView.setOverScrollMode(2);
//            }
//
//            WxConversationFragment.this.mSearchListView.setOnScrollListener(this.context);
//            WxConversationFragment.this.mSearchListView.setOnItemClickListener(this.context);
//            WxConversationFragment.this.mSearchListView.setOnItemLongClickListener(this.context);
//            WxConversationFragment.this.mSearchListView.setOnTouchListener(new View.OnTouchListener() {
//                public boolean onTouch(View v, MotionEvent event) {
//                    SearcherStateManager.this.hideKeyBoard();
//                    String text = WxConversationFragment.this.mSearchText.getText().toString();
//                    if(TextUtils.isEmpty(text)) {
//                        SearcherStateManager.this.hideSearch();
//                        return true;
//                    } else {
//                        return false;
//                    }
//                }
//            });
//        }
//
//        private void updateSearchAdapter() {
//            if(WxConversationFragment.this.mSearchAdapter != null && WxConversationFragment.this.mFilter != null) {
//                WxConversationFragment.this.mSearchList = new ArrayList(WxConversationFragment.this.mConversations);
//                ArrayList localConversations = new ArrayList(WxConversationFragment.this.mConversations);
//                WxConversationFragment.this.mSearchAdapter.setList(WxConversationFragment.this.mSearchList);
//                WxConversationFragment.this.mSearchAdapter.notifyDataSetChanged();
////                WxConversationFragment.this.mFilter.setFilterList(WxConversationFragment.this.mSearchList);
//                WxConversationFragment.this.mFilter.clear();
//                WxConversationFragment.this.mFilter.addSearchList(localConversations);
//            }
//
//        }
//
//        private void hideSearch() {
//            WxConversationFragment.this.mSearchConversationLayout.setVisibility(View.GONE);
//            WxConversationFragment.this.mSearchLayout.setVisibility(View.VISIBLE);
//            this.hideKeyBoard();
//        }
//
//        private void searchFriends() {
//            if(WxConversationFragment.this.mSearchText != null) {
//                String searchWord = WxConversationFragment.this.mSearchText.getText().toString();
//                WxConversationFragment.this.mFilter.filter(searchWord, new Filter.FilterListener() {
//                    public void onFilterComplete(int count) {
//                        WxConversationFragment.this.mSearchAdapter.notifyDataSetChangedWithAsyncLoad();
//                    }
//                });
//            }
//
//        }
//
//        private void showKeyBoard() {
//            View view = WxConversationFragment.this.mContext.getCurrentFocus();
//            if(view != null) {
////                Activity var10000 = WxConversationFragment.this.mContext;
////                ((InputMethodManager)var10000.getSystemService(Context.INPUT_METHOD_SERVICE)).toggleSoftInput(0,InputMethodManager.HIDE_NOT_ALWAYS);
//            }
//
//        }
//
//        protected void hideKeyBoard() {
//            View view = WxConversationFragment.this.mContext.getCurrentFocus();
//            if(view != null) {
//                Activity var10000 = WxConversationFragment.this.mContext;
////                WxConversationFragment.this.mContext;
//                ((InputMethodManager)var10000.getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(view.getWindowToken(), 0);
////                ((InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(view.getWindowToken(),0);
//            }
//
//        }
//
//        public void onClick(View v) {
//            int i = v.getId();
//            if(i == ResourceLoader.getIdByName(WxConversationFragment.this.mContext, "id", "aliwx_search_layout")) {
//                WxConversationFragment.this.mSearchConversationLayout.setVisibility(View.VISIBLE);
//                WxConversationFragment.this.mSearchText.setText("");
//                WxConversationFragment.this.mSearchText.requestFocus();
//                WxConversationFragment.this.mSearchConversationLayout.invalidate();
//                WxConversationFragment.this.mSearchAdapter.notifyDataSetChanged();
//                WxConversationFragment.this.mSearchLayout.setVisibility(View.GONE);
//                this.showKeyBoard();
//            } else if(i == ResourceLoader.getIdByName(WxConversationFragment.this.mContext, "id", "aliwx_cancel_search")) {
//                this.hideSearch();
//            }
//
//        }
//
//        private class SearchTextChangeWatcher implements TextWatcher {
//            private SearchTextChangeWatcher() {
//            }
//
//            public void afterTextChanged(Editable s) {
//                String text = s.toString();
//                if(TextUtils.isEmpty(text)) {
//                    WxConversationFragment.this.mSearchConversationLayout.setBackgroundColor(WxConversationFragment.this.getResources().getColor(ResourceLoader.getIdByName(WxConversationFragment.this.mContext, "color", "aliwx_halftransparent")));
//                } else {
//                    WxConversationFragment.this.mSearchConversationLayout.setBackgroundColor(WxConversationFragment.this.getResources().getColor(ResourceLoader.getIdByName(WxConversationFragment.this.mContext, "color", "aliwx_common_bg_color")));
//                }
//
//                SearcherStateManager.this.searchFriends();
//            }
//
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//            }
//
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//            }
//        }
//    }
//
//    class CheckAtMsgInDBCallback implements IWxCallback {
//        private YWConversation conversation;
//
//        public CheckAtMsgInDBCallback(YWConversation conversation) {
//            this.conversation = conversation;
//        }
//
//        public void onError(int code, String info) {
//            ((TribeConversation)this.conversation).setHasUnreadAtMsg(false);
//            ((TribeConversation)this.conversation).setLatestUnreadAtMsgId(-1L);
//            ((TribeConversation)this.conversation).setLatestUnreadAtMessage((YWMessage)null);
//        }
//
//        public void onProgress(int progress) {
//        }
//
//        public void onSuccess(Object... result) {
//            if(result != null && result.length == 1) {
//                Message message = (Message)result[0];
//                if(message != null) {
//                    ((TribeConversation)this.conversation).setHasUnreadAtMsg(true);
//                    ((TribeConversation)this.conversation).setLatestUnreadAtMsgId(message.getMsgId());
//                    ((TribeConversation)this.conversation).setLatestUnreadAtMessage(message);
//                    WxConversationFragment.this.mUIHandler.post(new Runnable() {
//                        public void run() {
//                            WxConversationFragment.this.adapter.notifyDataSetChanged();
//                        }
//                    });
//                    WxLog.d(WxConversationFragment.TAG, "更新adapter");
//                } else {
//                    ((TribeConversation)this.conversation).setHasUnreadAtMsg(false);
//                    ((TribeConversation)this.conversation).setLatestUnreadAtMsgId(-1L);
//                    ((TribeConversation)this.conversation).setLatestUnreadAtMessage((YWMessage)null);
//                    WxConversationFragment.this.mUIHandler.post(new Runnable() {
//                        public void run() {
//                            WxConversationFragment.this.adapter.notifyDataSetChanged();
//                        }
//                    });
//                }
//            }
//
//        }
//    }
//
//    class SyncAtMessageCallback implements IWxCallback {
//        private YWConversation conversation;
//
//        public SyncAtMessageCallback(YWConversation conversation) {
//            this.conversation = conversation;
//        }
//
//        public void onSuccess(Object... result) {
//            WxLog.d(WxConversationFragment.TAG, "result:" + result[0] + "  result.length:" + result.length);
//            if(WxConversationFragment.this.conversationManager == null) {
//                WxConversationFragment.this.conversationManager = (YWConversationManagerImpl)WXAPI.getInstance().getConversationManager();
//            }
//
//            WxConversationFragment.this.conversationManager.checkHasUnreadAtMsgs(WxConversationFragment.this.getActivity(), this.conversation, WxConversationFragment.this.new CheckAtMsgInDBCallback(this.conversation));
//        }
//
//        public void onError(int code, String info) {
//        }
//
//        public void onProgress(int progress) {
//        }
//    }
//
//    private class PresenterResult implements IWxCallback {
//        private boolean mAuto;
//
//        public PresenterResult(boolean isAuto) {
//            this.mAuto = isAuto;
//        }
//
//        public void onSuccess(Object... result) {
//            if(!this.mAuto) {
//                WxConversationFragment.this.handler.removeCallbacks(WxConversationFragment.this.cancelRefresh);
//            }
//
//            WxConversationFragment.this.handler.post(new Runnable() {
//                public void run() {
//                    WxConversationFragment.this.checkUnreadAtMsgAndUpdateUI();
//                }
//            });
//            WxConversationFragment.this.handler.postDelayed(new Runnable() {
//                public void run() {
//                    WxConversationFragment.this.refreshAdapter();
//                    if(WxConversationFragment.this.mPullToRefreshListView != null) {
//                        WxConversationFragment.this.mPullToRefreshListView.onRefreshComplete(false, true, ResourceLoader.getIdByName(WxConversationFragment.this.mContext, "string", "aliwx_sync_success"));
//                    }
//
//                    IMPrefsTools.setLongPrefs(WxConversationFragment.this.getActivity(), "lastAutoSyncRecentContact" + WXAPI.getInstance().getLongLoginUserId(), System.currentTimeMillis());
//                    IMPrefsTools.setBooleanPrefs(WxConversationFragment.this.getActivity(), "contacts_sync_state", true);
//                    WxLog.d(WxConversationFragment.TAG, "下拉刷新成功：@" + WXAPI.getInstance().getLongLoginUserId() + "@" + System.currentTimeMillis());
//                }
//            }, 300L);
//        }
//
//        public void onError(int code, String info) {
//            if(WxConversationFragment.this.handler != null && !this.mAuto) {
//                WxConversationFragment.this.handler.removeCallbacks(WxConversationFragment.this.cancelRefresh);
//            }
//
//            IMPrefsTools.setBooleanPrefs(WxConversationFragment.this.getActivity(), "contacts_sync_state", true);
//            WxLog.d(WxConversationFragment.TAG, "下拉刷新失败：@" + WXAPI.getInstance().getLongLoginUserId() + "@" + System.currentTimeMillis());
//            if(!this.mAuto) {
//                WxConversationFragment.this.handler.post(new Runnable() {
//                    public void run() {
//                        if(WxConversationFragment.this.mPullToRefreshListView != null) {
//                            WxConversationFragment.this.mPullToRefreshListView.onRefreshComplete(false, false, ResourceLoader.getIdByName(WxConversationFragment.this.mContext, "string", "aliwx_sync_failed"));
//                        }
//
//                    }
//                });
//            }
//        }
//
//        public void onProgress(int progress) {
//        }
//    }
//    // 切换界面动画开启一个Activiyty
//    protected void startActivityWithAnim(Intent intent) {
//        startActivity(intent);
//      getActivity().overridePendingTransition(com.limxing.library.R.anim.push_left_in, com.limxing.library.R.anim.push_left_out);
//    }
//
//
//}
