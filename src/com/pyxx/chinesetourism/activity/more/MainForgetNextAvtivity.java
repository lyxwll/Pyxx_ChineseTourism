package com.pyxx.chinesetourism.activity.more;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;

import com.pyxx.chinesetourism.BaseActivity;
import com.pyxx.chinesetourism.R;
import com.pyxx.chinesetourism.activity.MainContentActivity;
import com.pyxx.chinesetourism.view.HeaderClickListener;
import com.pyxx.chinesetourism.view.HeaderView;

public class MainForgetNextAvtivity extends BaseActivity implements
		HeaderClickListener, OnClickListener {

	HeaderView headerView;
	Button completeButton;
	EditText passwrodEditText,repeatEditText;
	

	@Override
	protected void onCreate(Bundle bundle) {
		super.onCreate(bundle);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.main_forgetpwd_layout);

		initView();

	}

	private void initView() {
		headerView = (HeaderView) findViewById(R.id.forget_next_header);
		headerView.setHeaderClickListener(this);
		headerView.setTitle(R.string.forget_title);
		completeButton = (Button) findViewById(R.id.forget_complete_btn);
		completeButton.setOnClickListener(this);
		
	}

	@Override
	public void networkStatusChanged(int status) {
		headerView.setNetworkStatus(status);
	}
	
	@Override
	public void onHeaderClick(View v, int which) {
		switch (v.getId()) {
		case R.id.header_back_btn:
			this.finish();
			break;
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.forget_complete_btn:
			Intent intent = new Intent();
			intent.setClass(this, MainContentActivity.class);
			startActivity(intent);
			break;
		}
	}


}
