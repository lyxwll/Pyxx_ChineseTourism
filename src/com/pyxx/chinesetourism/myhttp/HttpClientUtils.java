package com.pyxx.chinesetourism.myhttp;

public class HttpClientUtils {
	private static AsyncHttpClient asyncHttpClient;
	
	public static AsyncHttpClient getInstance() {
		asyncHttpClient = AsyncHttpClient.getInstance();
		return asyncHttpClient;
	}

}
