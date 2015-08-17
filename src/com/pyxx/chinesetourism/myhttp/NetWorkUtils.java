package com.pyxx.chinesetourism.myhttp;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;
import java.util.Map;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

/**
 * 图片下载工具
 * 
 * @author wll
 */
public class NetWorkUtils {

	public interface OnImageDownload {
		void imageDownloaded(Bitmap bitmap);
	}

	public static class NetWorkThread extends Thread {
		String path;
		OnImageDownload imageDownload;

		// ImageView imageView;

		public NetWorkThread(String path, OnImageDownload imageDownload) {
			this.path = path;
			this.imageDownload = imageDownload;
		}

		@Override
		public void run() {
			getURL(path, imageDownload);
		}
	}

	/**
	 * 使用URL访问web资源上的图片
	 */
	public static void getURL(String path, OnImageDownload imageDownload) {
		try {
			URL url = new URL(path);
			// 拿到输入流
			InputStream inputStream = url.openStream();
			Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
			// imageDownload.setImageBitmap(bitmap);
			if (imageDownload != null) {
				// 回调图片下载
				imageDownload.imageDownloaded(bitmap);
			}
			inputStream.close();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// *******************************************
	/**
	 * get 请求方式
	 * 
	 * @param url
	 * @param params
	 * @param onHttpComplete
	 */
	public static void get(String url, String params,
			OnHttpComplete onHttpComplete) {
		new HttpThread(url, params, onHttpComplete, 0).start();
	}

	/**
	 * post 请求方式
	 * 
	 * @param url
	 * @param params
	 * @param onHttpComplete
	 */
	public static void post(String url, String params,
			OnHttpComplete onHttpComplete) {
		new HttpThread(url, params, onHttpComplete, 1).start();
	}

	// *******************************************

	public static interface OnHttpComplete {
		void onSuccess(String result);
	}

	public static class HttpThread extends Thread {
		String url;// 地址
		String params;// 参数
		OnHttpComplete onHttpComplete;// 回调实例
		int method;// 请求方法

		public HttpThread(String url, String params,
				OnHttpComplete onHttpComplete, int method) {
			this.url = url;
			this.params = params;
			this.onHttpComplete = onHttpComplete;
			this.method = method;
		}

		@Override
		public void run() {
			String result = "";
			if (method == 0) {
				result = sendGet(url, params);
			} else if (method == 1) {
				result = sendPost(url, params);
			}
			if (onHttpComplete != null) {
				onHttpComplete.onSuccess(result);
			}
		}
	}

	/**
	 * 使用URlConnection发送GET请求
	 */
	public static String sendGet(String url, String params) {
		StringBuilder builder = new StringBuilder();
		try {
			URL realUrl = new URL(url + "?" + params);
			// 获取到URLConnection对象
			URLConnection urlConnection = realUrl.openConnection();
			// 设置通用请求属性
			// 设置客户端接收哪些类型的信息,通配符表示接收所有类型的信息
			urlConnection.setRequestProperty("accept", "*/*");
			// 保持长链接
			urlConnection.setRequestProperty("connection", "Keep-Alive");
			// 设置浏览器代理
			urlConnection.setRequestProperty("user-agent",
					"Mozilla/4.0(compatible;MSIE 6.0;Windows NT5.1;SV1)");
			// 指定客户端接受的字符集
			urlConnection.setRequestProperty("accept-charset", "utf-8;GBK");
			// 建立连接
			urlConnection.connect();
			// 获取所有的响应报头的字段
			Map<String, List<String>> headers = urlConnection.getHeaderFields();
			// 遍历所有响应报头的字段
			for (String key : headers.keySet()) {
				System.out.println(key + "-------------->>" + headers.get(key));
			}
			// 获取到输入流,也就是响应内容
			InputStream inputStream = urlConnection.getInputStream();
			BufferedReader bufferedReader = new BufferedReader(
					new InputStreamReader(inputStream));
			String line = bufferedReader.readLine();
			while (line != null && line.length() > 0) {
				builder.append(line);
				line = bufferedReader.readLine();
			}
			inputStream.close();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return builder.toString();
	}

	// *********************************************************

	// 使用UrlConnection发送post请求
	public static String sendPost(String url, String params) {
		StringBuilder builder = new StringBuilder();
		URL realUrl;
		try {
			realUrl = new URL(url + "?" + params);
			// 获取到URLConnection对象
			URLConnection urlConnection = realUrl.openConnection();
			// 设置通用请求属性
			// 设置客户端接收哪些类型的信息,通配符表示接收所有类型的信息
			// setRequestProperty();设置指定的请求头字段的值。该值将只用于由当前的URLConnection实例。在连接建立之前该方法只能被调用。
			urlConnection.setRequestProperty("accept", "*/*");
			// 保持长链接
			urlConnection.setRequestProperty("connection", "Keep-Alive");
			// 设置浏览器代理
			urlConnection.setRequestProperty("user-agent",
					"Mozilla/4.0(compatible;MSIE 6.0;Windows NT5.1;SV1)");
			// 指定客户端接受的字符集
			urlConnection.setRequestProperty("accept-charset", "utf-8;GBK");
			urlConnection.setDoOutput(true);// 设置使用post方式
			urlConnection.setDoInput(true);// 设置可以使用输入流
			// 建立链接
			urlConnection.connect();
			// 先要使用输出流
			OutputStream outputStream = urlConnection.getOutputStream();
			BufferedWriter bufferedWriter = new BufferedWriter(
					new OutputStreamWriter(outputStream));
			bufferedWriter.write(params);
			bufferedWriter.flush();
			outputStream.close();
			Map<String, List<String>> headers = urlConnection.getHeaderFields();
			// 遍历所有响应报头的字段
			for (String key : headers.keySet()) {
				System.out.println(key + "-------------->>" + headers.get(key));
			}
			// 获取到输入流,也就是响应内容
			InputStream inputStream = urlConnection.getInputStream();
			BufferedReader bufferedReader = new BufferedReader(
					new InputStreamReader(inputStream));
			String line = bufferedReader.readLine();
			while (line != null && line.length() > 0) {
				builder.append(line);
				line = bufferedReader.readLine();
			}
			inputStream.close();
			bufferedReader.close();
			bufferedWriter.close();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return builder.toString();
	}

}
