package note.aboutnet;

import com.limxing.library.utils.FileUtils;
import com.limxing.library.utils.LogUtils;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by limxing on 16/3/10.
 * 网络请求的笔记
 * 1.项目用到json作为参数上传,get请求时需要把特殊字符转码
 * string.replace("\"", "%22").replace("{", "%7B").replace("}", "%7D")
 * 2.使用HttpURLConnection请求网络下载文件等操作
 *
 */
public class Net {
    public static void requestNet(long fromlen,String path) {

        HttpURLConnection conn = null;
        URL url = null;
        try {
            url = new URL(path);
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setConnectTimeout(5000);
//            conn.setRequestProperty("User-Agent","NetFox");
//            conn.setRequestProperty("RANGE","bytes="+fromlen);//从哪开始下载
            long fileLength = conn.getContentLength();
            LogUtils.i("总大小:" + fileLength);
            if (conn.getResponseCode() == 200) {//是否请求成功,页面是200
                InputStream s = conn.getInputStream();//获取页面上的流

//                readStreamContinueToFile(s, fromlen, fileLength);//继续下载


                s.close();
                conn.disconnect();
                LogUtils.i("下载完毕");
            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * 把流转换成字符串
     *
     * @param is
     * @return
     */
    public static String readStream(InputStream is) {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int len = -1;
            while ((len = is.read(buffer)) != -1) {
                baos.write(buffer, 0, len);
            }
            is.close();

            return new String(baos.toByteArray());
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

    }

    /**
     * 解析流写成文件
     *
     * @param is
     */
    public static void readStreamToFile(InputStream is, long fileLength) {
        try {

            String url = FileUtils.getCacheDir() + "蜻蜓FM.apk";
            FileUtils.creatFile(url);
            FileOutputStream fileOutputStream = new FileOutputStream(url);
            int curlen = 0;
            byte[] buffer = new byte[1024];
            int len = -1;
            while ((len = is.read(buffer)) != -1) {
                curlen += len;
                LogUtils.i(curlen + "/" + fileLength);//
                fileOutputStream.write(buffer, 0, len);
            }

            fileOutputStream.close();


        } catch (IOException e) {
            e.printStackTrace();

        }
    }

    /**
     * 解析流继续写成文件
     *
     * @param is
     */
    public static void readStreamContinueToFile(InputStream is,long fromLen, long fileLength) {
        try {
            String url = FileUtils.getCacheDir() + "蜻蜓FM.apk";
            FileUtils.creatFile(url);
            RandomAccessFile randomAccessFile=new RandomAccessFile(url,"rw");
            randomAccessFile.seek(8421071);

            int curlen = 0;
            byte[] buffer = new byte[1024];
            int len = -1;
            while ((len = is.read(buffer)) != -1) {
                curlen += len;
//                if(curlen>fileLength) {
                    LogUtils.i(curlen + "/" + fileLength);//
                    randomAccessFile.write(buffer, 0, len);
//                }
            }
            randomAccessFile.close();


        } catch (IOException e) {
            e.printStackTrace();

        }
    }
}
