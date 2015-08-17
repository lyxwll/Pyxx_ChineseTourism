package com.pyxx.chinesetourism.activity.more;

import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import com.pyxx.chinesetourism.BaseActivity;
import com.pyxx.chinesetourism.R;
import com.pyxx.chinesetourism.view.HeaderClickListener;
import com.pyxx.chinesetourism.view.HeaderView;

public class MoreMyCollectActivity extends BaseActivity implements HeaderClickListener{

	HeaderView headerView;
	TextView wineshopText,attractionText,travelText;
	
	@Override
	protected void onCreate(Bundle bundle) {
		super.onCreate(bundle);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.more_mycollect);
		
		initView();
	}
	
	private void initView() {
		headerView = (HeaderView) findViewById(R.id.more_collect_header);
		headerView.setTitle(R.string.more_mycollect);
		headerView.setHeaderClickListener(this);
		
		
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

}
