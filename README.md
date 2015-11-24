# app
Android开发中遇到了一些特殊需求，在此罗列自己收集到的控件或者方法，在此分享！

一、沉浸式状态栏
  1、布局文件中添加android:clipToPadding="true" android:fitsSystemWindows="true"
  2、在onCreat中调用：SystemBarTintManager.initSystemBar(this,R.color.colorAccent);

