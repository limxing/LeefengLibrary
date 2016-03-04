package com.limxing.library.utils;

/**
 * 作者：李利锋
 * 创建日期：2016/2/26 22:57
 * 描述：bitmap处理,图片文件路径缩小后保存,纠正图片的方向
 */

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.Matrix;
import android.media.ExifInterface;


/**
 * A class for helping deal the bitmap,
 * like: get the orientation of the bitmap, compress bitmap etc.
 *
 * @author limxing
 */
public class BitmapHelper {

    /**
     * get the orientation of the bitmap {@link ExifInterface}
     *
     * @param path
     * @return
     */
    public static int getDegress(String path) {
        int degree = 0;
        try {
            ExifInterface exifInterface = new ExifInterface(path);
            int orientation = exifInterface.getAttributeInt(
                    ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_NORMAL);
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    degree = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    degree = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    degree = 270;
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return degree;
    }

    /**
     * rotate the bitmap
     *
     * @param bitmap
     * @param degress
     * @return
     */
    public static Bitmap rotateBitmap(Bitmap bitmap, int degress) {
        if (bitmap != null) {
            Matrix m = new Matrix();
            m.postRotate(degress);
            bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), m, true);
            return bitmap;
        }
        return bitmap;
    }

    /**
     * caculate the bitmap sampleSize
     *
     * @param
     * @return
     */
    public static int caculateInSampleSize(Options options, int rqsW, int rqsH) {
        int height = options.outHeight;
        int width = options.outWidth;
        if (width > height) {
            int temp = width;
            width = height;
            height = temp;
        }
        if (rqsW > rqsH) {
            int rqsT = rqsH;
            rqsH = rqsW;
            rqsW = rqsT;
        }

        int inSampleSize = 1;
        if (rqsW == 0 || rqsH == 0) return 1;
        if (height > rqsH || width > rqsW) {
            int heightRatio = Math.round((float) height / (float) rqsH);
            int widthRatio = Math.round((float) width / (float) rqsW);
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }
        return inSampleSize;
    }

//    /**
//     * 压缩指定路径的图片，并得到图片对象
//     *
//     * @param path bitmap source path
//     * @return Bitmap {@link Bitmap}
//     */
//    public static Bitmap compressBitmap(String path, int rqsW, int rqsH) {
//        Options options = new Options();
//        options.inJustDecodeBounds = true;
////        options.inSampleSize = 2;
//        BitmapFactory.decodeFile(path, options);
//        options.inSampleSize = caculateInSampleSize(options, rqsW, rqsH);
//        options.inJustDecodeBounds = false;
//        return BitmapFactory.decodeFile(path, options);
//    }

    /**
     * 根据需要压缩到某尺寸压缩指定路径的图片，并得到图片对象
     *
     * @param path
     * @param rqsW
     * @param rqsH
     * @return
     */
    public static Bitmap compressBitmap(String path, int rqsW, int rqsH) {
        Options options = getBitmapOptions(path);
        options.inSampleSize = caculateInSampleSize(options, rqsW, rqsH);
        return BitmapFactory.decodeFile(path, options);
    }

    /**
     * 压缩指定路径图片，并将其保存在缓存目录中，通过isDelSrc判定是否删除源文件，并获取到缓存后的图片路径
     *
     * @param context
     * @param srcPath
     * @param rqsW
     * @param rqsH
     * @param isDelSrc
     * @return
     */
    public static String compressBitmap(Context context, String srcPath, int rqsW, int rqsH, boolean isDelSrc) {
        int degree = getDegress(srcPath);
        Bitmap bitmap = compressBitmap(srcPath, rqsW, rqsH);//根据长宽以及图片的长宽得到缩放图片
        File srcFile = new File(srcPath);
        String desPath = getImageCacheDir(context) + srcFile.getName();
        try {
            if (degree != 0) bitmap = rotateBitmap(bitmap, degree);
            File file = new File(desPath);
            FileOutputStream fos = new FileOutputStream(file);
            bitmap.compress(CompressFormat.JPEG, 80, fos);//80是图片质量
            fos.close();
            if (isDelSrc) srcFile.deleteOnExit();

        } catch (Exception e) {
        }

        bitmap.recycle();
        System.gc();

        return desPath;
    }

    /**
     * 此方法过期，该方法可能造成OutOfMemoryError，使用不含isAdjust参数的方法
     *
     * @param is
     * @param reqsW
     * @param reqsH
     * @param isAdjust
     * @return
     */
    @Deprecated
    public static Bitmap compressBitmap(InputStream is, int reqsW, int reqsH, boolean isAdjust) {
        Bitmap bitmap = BitmapFactory.decodeStream(is);
        return compressBitmap(bitmap, reqsW, reqsH, isAdjust);
    }

    /**
     * 压缩某个输入流中的图片，可以解决网络输入流压缩问题，并得到图片对象
     *
     * @return Bitmap {@link Bitmap}
     */
    public static Bitmap compressBitmap(InputStream is, int reqsW, int reqsH) {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ReadableByteChannel channel = Channels.newChannel(is);
            ByteBuffer buffer = ByteBuffer.allocate(1024);
            while (channel.read(buffer) != -1) {
                buffer.flip();
                while (buffer.hasRemaining()) baos.write(buffer.get());
                buffer.clear();
            }
            byte[] bts = baos.toByteArray();
            Bitmap bitmap = compressBitmap(bts, reqsW, reqsH);
            is.close();
            channel.close();
            baos.close();
            return bitmap;
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 压缩指定byte[]图片，并得到压缩后的图像
     *
     * @param bts
     * @param reqsW
     * @param reqsH
     * @return
     */
    public static Bitmap compressBitmap(byte[] bts, int reqsW, int reqsH) {
        Options options = new Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeByteArray(bts, 0, bts.length, options);
        options.inSampleSize = caculateInSampleSize(options, reqsW, reqsH);
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeByteArray(bts, 0, bts.length, options);
    }

    /**
     * 此方法已过期，该方法可能造成OutOfMemoryError，使用不含isAdjust参数的方法
     *
     * @param bitmap
     * @param reqsW
     * @param reqsH
     * @return
     */
    @Deprecated
    public static Bitmap compressBitmap(Bitmap bitmap, int reqsW, int reqsH, boolean isAdjust) {
        if (bitmap == null || reqsW == 0 || reqsH == 0) return bitmap;
        if (bitmap.getWidth() > reqsW || bitmap.getHeight() > reqsH) {
            float scaleX = new BigDecimal(reqsW).divide(new BigDecimal(bitmap.getWidth()), 4, RoundingMode.DOWN).floatValue();
            float scaleY = new BigDecimal(reqsH).divide(new BigDecimal(bitmap.getHeight()), 4, RoundingMode.DOWN).floatValue();
            if (isAdjust) {
                scaleX = scaleX < scaleY ? scaleX : scaleY;
                scaleY = scaleX;
            }
            Matrix matrix = new Matrix();
            matrix.postScale(scaleX, scaleY);
            bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        }
        return bitmap;
    }

    /**
     * 压缩已存在的图片对象，并返回压缩后的图片
     *
     * @param bitmap
     * @param reqsW
     * @param reqsH
     * @return
     */
    public static Bitmap compressBitmap(Bitmap bitmap, int reqsW, int reqsH) {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(CompressFormat.PNG, 100, baos);
            byte[] bts = baos.toByteArray();
            Bitmap res = compressBitmap(bts, reqsW, reqsH);
            baos.close();
            return res;
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return bitmap;
        }
    }

    /**
     * 此方法过期，该方法可能造成OutOfMemoryError，使用不含isAdjust参数的方法
     * get bitmap form resource dictory, and then compress bitmap according to reqsW and reqsH
     *
     * @param res   {@link Resources}
     * @param resID
     * @param reqsW
     * @param reqsH
     * @return
     */
    @Deprecated
    public static Bitmap compressBitmap(Resources res, int resID, int reqsW, int reqsH, boolean isAdjust) {
        Bitmap bitmap = BitmapFactory.decodeResource(res, resID);
        return compressBitmap(bitmap, reqsW, reqsH, isAdjust);
    }

    /**
     * 压缩资源图片，并返回图片对象
     *
     * @param res   {@link Resources}
     * @param resID
     * @param reqsW
     * @param reqsH
     * @return
     */
    public static Bitmap compressBitmap(Resources res, int resID, int reqsW, int reqsH) {
        Options options = new Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(res, resID, options);
        options.inSampleSize = caculateInSampleSize(options, reqsW, reqsH);
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeResource(res, resID, options);
    }


    /**
     * 基于质量的压缩算法， 此方法未 解决压缩后图像失真问题
     * <br> 可先调用比例压缩适当压缩图片后，再调用此方法可解决上述问题
     *
     * @param maxBytes 压缩后的图像最大大小 单位为byte
     * @return
     */
    public static Bitmap compressBitmap(Bitmap bitmap, long maxBytes) {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(CompressFormat.PNG, 100, baos);
            int options = 90;
            while (baos.toByteArray().length > maxBytes) {
                baos.reset();
                bitmap.compress(CompressFormat.PNG, options, baos);
                options -= 10;
            }
            byte[] bts = baos.toByteArray();
            Bitmap bmp = BitmapFactory.decodeByteArray(bts, 0, bts.length);
            baos.close();
            return bmp;
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;
        }
    }

//  public  static Bitmap compressBitmap(InputStream is, long maxBytes) {
//      try {
//          ByteArrayOutputStream baos = new ByteArrayOutputStream();
//          byte[] bts = new byte[1024];
//          while (is.read(bts) != -1) baos.write(bts, 0, bts.length);
//          is.close();
//          int options = 100;
//          while (baos.toByteArray().length > maxBytes) {
//
//          }
//      } catch (Exception e) {
//          // TODO: handle exception
//      }
//  }

    /**
     * 得到指定路径图片的options
     *
     * @param srcPath
     * @return Options {@link Options}
     */
    public static Options getBitmapOptions(String srcPath) {
        Options options = new Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(srcPath, options);
        options.inJustDecodeBounds = false;
        return options;
    }

    /**
     * 获取图片缓存路径
     *
     * @param context
     * @return
     */
    public static String getImageCacheDir(Context context) {
//        String dir = FileHelper.getCacheDir(context) + "Image" + File.separator;
        String dir = FileUtils.getCacheDir() + "Image" + File.separator;
        File file = new File(dir);
        if (!file.exists()) file.mkdirs();
        return dir;
    }
}

