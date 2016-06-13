常见Android开发控件
===================================
Android开发中遇到了一些特殊需求，在此罗列自己收集总结到的控件或者方法！
-------------------------------------------------------------------------
使用方法是:把library作为库引入

####一、沉浸式状态栏
  1、布局文件中的title控件中添加android:clipToPadding="true" android:fitsSystemWindows="true"(顶部是图片的话就不需要)       
  2、在布局文件中的title种添加paddingTop="paddingTop",SDK19以上padding20dp,19一下为0dp (设不设置还是看采用那种方式)
      android:fitsSystemWindows="true"
      android:clipToPadding="true"
      在标题中加入以上两句替换paddingTop=20dp,这样高度还是正常高度,


  2、在onCreat中调用：SystemBarTintManager.initSystemBar(this,R.color.colorAccent);
  
  4,内容最好用scrollView 否则editText会把所有顶出去

####二、右滑退出
  1、需要右滑退出的activity集成SwipeBackActivity,默认右滑退出;      
  2、可以设置setDragEdge(TOP,...)的方法,设置其他方向退出

  3、(不能用,哈哈)推荐使用布局文件最外层的包裹为SwipeBackLayout,然后在activity中声明初始化这个:
     SwipeBackLayout swipeBackLayout = (SwipeBackLayout) findViewById(R.id.swipeBackLayout);
            swipeBackLayout.setDragEdge(SwipeBackLayout.DragEdge.LEFT);
本人修复(在原始框架中修改):修正了右滑退出时,只能在最侧右滑,其他地方不响应,解决与listView的冲突
####三、工具类方法介绍
  FileUtils文件相关操作类:
     writeFile(InputStream is, String path, boolean recreate) 把流写入文件;       
     writeFile(byte[] content, String path, boolean append)把字符数组写入文件;       
     writeFile(String content, String path, boolean append)把字符串写入文件;        
     writeProperties(String filePath, String key, String value, String comment)把键值对写入文件;        
     copy(String src, String des, boolean delete)复制文件或更改名称      
  LogUtils日志打印工具类       
  StringUtils字符串操作类     
  UIUtils关于UI界面的操作类
  ToastUtils是吐丝的单例操作        
  DisplayUtil是dppxsp之间的相互转换工具类
  Encryptutil是MD5和base64位加密(依赖文件Base64)
   
####四、下拉刷新上拉加载  
   PullToRefresh包:
    
######1,布局文件
    
        <com.limxing.library.PullToRefresh.SwipeRefreshLayout
         android:id="@+id/main_fresh"
         android:layout_width="match_parent"
         android:layout_height="match_parent"
         android:layout_below="@+id/main_title"
         swiperefresh:srlAnimationStyle="rotate"
         swiperefresh:srlTextSize="16sp" >
        
         <ListView
         android:id="@+id/main_listview"
         android:layout_width="match_parent"
         android:layout_height="match_parent"
         android:background="#ffffff"
         android:dividerHeight="1dp" >
         </ListView>
         </com.limxing.library.PullToRefresh.SwipeRefreshLayout>
######2,在activity中获取到两个控件,SwipeRefreshLayout控件需要设置监听:     
        
         main_fresh.setOnRefreshListener(this);
         main_fresh.setOnLoadListener(this);
         在监听方法中实现响应的操作      
######3,调用方法刷新加载完成恢复:        
        
         1,在请求网络后结果中发送message
            handler.sendEmptyMessageDelayed(what,time=2000);    
         2,在handler 处理中调用  
            main_fresh.setRefreshing(false);   main_refresh.setLoading(false);

####五、进栈出栈左右滑动效果
    使用方式:继承BaseActivity,startActivityWithAnim(Intent intent)进栈和finishActivity()出栈.

####六、点击实现从底部弹出的对话框
    实现方式是使用继承的Activity的方式,开启式需要使用startAvtivityForResult(int)的方式,
    再实现onActivityResult的方法用于接收来自对话框的点击事件.对话框是一个特殊的activity因此可以自定义使用.

    2、使用popwindow的方式实现:

####七、图片加载工具
    https://github.com/limxing/Android-Universal-Image-Loader
    全局初始化对象
####八、SweetDialog的引入以及使用
    1、创建SweetDialog对象,参数Context和窗口类型;
    2、设置标题内容,按钮的点击事件等
    3、调用show方法展示
    4、调用dismiss关闭窗口
      
####九、底部弹窗窗口(不是很好用,使用十的)
效果图
![image](https://github.com/limxing/app/blob/master/screenshot.png)

    1、创建窗口对象,
        AlertDialog dialog = new AlertDialog(MainActivity.this, findViewById(R.id.swipeBackLayout)) {
            @Override
            public void closed() {

            }

            @Override
            protected void selectionClick(int tag) {
                switch (tag) {
                    case 0:
                      ToastUtils.showLong(MainActivity.this, "第一个");
                    break;
                    case 1:
                     ToastUtils.showLong(MainActivity.this, "第二个");
                    break;
                }
             }
        };



     dialog.setCancleButtonTitle("取消");//设置取消信息
     dialog.setDescription("这是我精心准备的底部弹窗");//设置顶部描述信息
     dialog.setSelections(new String[]{"第一个","第二个","第三个"});//设置选择项,是数组的形式
     dialog.show();//把选择框show出来

    需要在onPause中调用

      @Override
         protected void onPause() {
             if (dialog!=null&&dialog.isShowing()) {
                 dialog.dismiss();
             }
             super.onPause();
         }
####十、底部弹窗窗口(最优化)
调用方法:
     BottomDialog.showAlert(MainActivity.this, "哈哈哈", new String[]{"你好", "你不好"},
         new BottomDialog.OnClickListener() {
         @Override
          public void onClick(int which) {
             ToastUtils.showLong(MainActivity.this, which + "个");
           }
         }, new DialogInterface.OnCancelListener() {
         @Override
         public void onCancel(DialogInterface dialogInterface) {
             ToastUtils.showLong(MainActivity.this,"已关闭");
         }
     });
####十一、仿IOS的加载
         <com.limxing.library.IOSLoading.LoadingView
             android:layout_width="wrap_content"
             android:layout_height="wrap_content" />
####十二、环形的下载加载的progressbar
       

####十三、SVProgressbar,类似IOS上的对话框
         new SVProgressHUD(MainActivity.this).showLmWithStatus("加载中...", SVProgressHUD.SVProgressHUDMaskType.Clear);
####十四、可以拖动的ListView
        DragListView dragListView = (DragListView) findViewById(R.id.other_drag_list);
        mAdapter = new DragListAdapter(this, mData);默认data是String类型可自定义bean,以及Item
        dragListView.setAdapter(mAdapter); 
        \
      
#### 十五:添加弹性ScrollView

#### 十六,添加一个能够点击缩回去的控件 




                 


