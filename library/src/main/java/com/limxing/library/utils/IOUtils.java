package com.limxing.library.utils;

import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.PushbackInputStream;

/**
 * IO流的操作类
 */
public class IOUtils {
	/** 关闭流 */
	public static boolean close(Closeable io) {
		if (io != null) {
			try {
				io.close();
			} catch (IOException e) {
				LogUtils.e(e);
			}
		}
		return true;
	}
	/**
	 * 将输入流转换成字符串
	 *
	 * @param is
	 *            从网络获取的输入流
	 * @return
	 */
	public static String streamToString(InputStream is) {
		try {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			byte[] buffer = new byte[1024];
			int len = 0;
			while ((len = is.read(buffer)) != -1) {
				baos.write(buffer, 0, len);
			}
			baos.close();
			is.close();
			byte[] byteArray = baos.toByteArray();
			return new String(byteArray);
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * 读流转为成字符串
	 * @param in
	 * @return
	 * @throws IOException
     */
	public static String readLine(PushbackInputStream in) throws IOException {
		char[] buf = new char[128];
		int room = buf.length;
		int offset = 0;

		while (true) {
			int c;
			switch (c = in.read()) {
				case 13:
					int c2 = in.read();
					if (c2 != 10 && c2 != -1) {
						in.unread(c2);
					}
				case -1:
				case 10:
					if (c == -1 && offset == 0) {
						return null;
					}

					return String.copyValueOf(buf, 0, offset);
			}

			--room;
			if (room < 0) {
				char[] lineBuffer = buf;
				buf = new char[offset + 128];
				room = buf.length - offset - 1;
				System.arraycopy(lineBuffer, 0, buf, 0, offset);
			}

			buf[offset++] = (char) c;
		}
	}
}
