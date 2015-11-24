# app
Android开发中遇到了一些特殊需求，在此罗列自己收集总结到的控件或者方法，在此分享！

一、沉浸式状态栏
  1、布局文件中的title控件中添加android:clipToPadding="true" android:fitsSystemWindows="true"
  2、在布局文件中的title种添加paddingTop="paddingTop",SDK19以上padding20dp,19一下为0dp
  2、在onCreat中调用：SystemBarTintManager.initSystemBar(this,R.color.colorAccent);

二、右滑退出
  1、需要右滑退出的activity集成SwipeBackActivity,默认右滑退出;
  2、可以设置setDragEdge(TOP,...)的方法,设置其他方向退出




