package com.pyxx.chinesetourism.activity.more;

import java.util.ArrayList;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.pyxx.chinesetourism.BaseActivity;
import com.pyxx.chinesetourism.R;
import com.pyxx.chinesetourism.view.HeaderClickListener;
import com.pyxx.chinesetourism.view.HeaderView;

/**
 * 忘记密码界面 MainRegisterActivity.java
 * 
 * @author Administrator
 */
public class MainForgetPwdActivity extends BaseActivity implements
		OnClickListener, HeaderClickListener {

	HeaderView headerView;
	EditText teleEditText, codeEditText;
	TextView checkTextView;
	Button getcodeButton, nextButton;
	ArrayList<Fragment> list = new ArrayList<Fragment>();

	@Override
	protected void onCreate(Bundle bundle) {
		super.onCreate(bundle);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.main_register_layout);

		initView();

	}

	private void initView() {
		headerView = (HeaderView) findViewById(R.id.register_header);
		headerView.setTitle(R.string.register_title);
		headerView.setHeaderClickListener(this);

		getcodeButton.setOnClickListener(this);
		nextButton.setOnClickListener(this);

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

		}
	}

	public void Checked() {

	}

}
