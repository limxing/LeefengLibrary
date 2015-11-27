# 常见Android开发控件
===================================
Android开发中遇到了一些特殊需求，在此罗列自己收集总结到的控件或者方法，在此分享！
-------------------------------------------------------------------------
使用方法是:把library作为库引入

####一、沉浸式状态栏
  1、布局文件中的title控件中添加android:clipToPadding="true" android:fitsSystemWindows="true"       
  2、在布局文件中的title种添加paddingTop="paddingTop",SDK19以上padding20dp,19一下为0dp      
  2、在onCreat中调用：SystemBarTintManager.initSystemBar(this,R.color.colorAccent);

####二、右滑退出
  1、需要右滑退出的activity集成SwipeBackActivity,默认右滑退出;      
  2、可以设置setDragEdge(TOP,...)的方法,设置其他方向退出
  
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
  




