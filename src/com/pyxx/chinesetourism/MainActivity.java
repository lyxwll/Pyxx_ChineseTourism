package com.pyxx.chinesetourism;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
/**
 * 中国旅游观光     主程序入口
 * MainActivity.java
 * @author Administrator
 */
public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
