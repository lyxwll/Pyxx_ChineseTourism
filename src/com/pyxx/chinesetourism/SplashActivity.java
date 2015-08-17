package com.pyxx.chinesetourism;

import com.pyxx.chinesetourism.activity.MainContentActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;

/**
 * 启动 动画
 * 
 * @author wll
 */
public class SplashActivity extends BaseActivity {

	@Override
	protected void onCreate(Bundle bundle) {
		super.onCreate(bundle);
		requestWindowFeature(Window.FEATURE_NO_TITLE);

		setContentView(R.layout.splash_layout);

		Handler handler = new Handler();
		handler.postDelayed(new SplashHandler(), 2000);

	}

	class SplashHandler implements Runnable {

		@Override
		public void run() {
			startActivity(new Intent(getApplication(),
					MainContentActivity.class));
			SplashActivity.this.finish();
		}
	}

	@Override
	public void networkStatusChanged(int status) {

	}

}
