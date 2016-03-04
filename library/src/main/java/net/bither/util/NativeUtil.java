/*
 * Copyright 2014 http://Bither.net
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package net.bither.util;
/**
 * 使用说明:
 * NativeUtil.compressBitmap(BitmapFactory.decodeFile(path), 80,path, true);
 * 参数分别是:原文件的bitmap,压缩质量,保存路径,true比false占用空间更小,false是android底层的实现
 */
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.util.Log;

import java.io.File;

public class NativeUtil {
	private static int DEFAULT_QUALITY = 95;

	public static void compressBitmap(Bitmap bit, String fileName,
			boolean optimize) {
		compressBitmap(bit,DEFAULT_QUALITY, fileName, optimize);

	}

	public static void compressBitmap(Bitmap bit,int quality, String fileName,
			boolean optimize) {
		Log.d("native", "compress of native");
		if (bit.getConfig() != Config.ARGB_8888) {
			//创建了一个空的花画板,应该可以指定大小
			Bitmap result = Bitmap.createBitmap(bit.getWidth(), bit.getHeight(),
					Config.ARGB_8888);
			Canvas canvas = new Canvas(result);
			Rect rect = new Rect(0, 0, bit.getWidth(), bit.getHeight());
			canvas.drawBitmap(bit, null, rect, null);
			saveBitmap(result, quality, fileName, optimize);
			result.recycle();
		} else {
			saveBitmap(bit, quality, fileName, optimize);
		}


	}

	/**
	 * 压缩图片质量
	 * @param path 图片路径
	 * @param quality 1~100 质量
	 * @param fileName 保存路径
	 * @param optimize 是否采用更好压缩
	 * @param isDelete 是否删除原文件
	 */
	public static void compressBitmap(String path,int quality, String fileName,
									  boolean optimize,boolean isDelete) {
		Log.d("native", "compress of native");
		Bitmap bit= BitmapFactory.decodeFile(path);
		if (bit.getConfig() != Config.ARGB_8888) {
			//创建了一个空的花画板,应该可以指定大小
			Bitmap result = Bitmap.createBitmap(bit.getWidth(), bit.getHeight(),
					Config.ARGB_8888);
			Canvas canvas = new Canvas(result);
			Rect rect = new Rect(0, 0, bit.getWidth(), bit.getHeight());
			canvas.drawBitmap(bit, null, rect, null);
			saveBitmap(result, quality, fileName, optimize);
			result.recycle();
		} else {
			saveBitmap(bit, quality, fileName, optimize);
		}
		if(isDelete){
			new File(path).delete();
		}


	}

	private static void saveBitmap(Bitmap bit, int quality, String fileName,
			boolean optimize) {
		compressBitmap(bit, bit.getWidth(), bit.getHeight(), quality,
				fileName.getBytes(), optimize);

	}

	private static native String compressBitmap(Bitmap bit, int w, int h,
			int quality, byte[] fileNameBytes, boolean optimize);

	static {
		System.loadLibrary("jpegbither");
		System.loadLibrary("bitherjni");

	}

}
