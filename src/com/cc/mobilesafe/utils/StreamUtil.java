package com.cc.mobilesafe.utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class StreamUtil {

	public static String streamToString(InputStream inputStream) {
		// TODO 自动生成的方法存根
		ByteArrayOutputStream out=new ByteArrayOutputStream();
		byte[] buffer=new byte[1024];
		int len=-1;
		try {
			while ((len=inputStream.read(buffer))!=-1) {
				out.write(buffer, 0, len);
			}
			return	out.toString();
		} catch (IOException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}finally {
			try {
				inputStream.close();
				out.close();
			} catch (IOException e) {
				// TODO 自动生成的 catch 块
				e.printStackTrace();
			}

		}
		return null;
	}
	

}
