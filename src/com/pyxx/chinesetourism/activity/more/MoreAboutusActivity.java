package com.pyxx.chinesetourism.activity.more;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import com.pyxx.chinesetourism.BaseActivity;
import com.pyxx.chinesetourism.R;
import com.pyxx.chinesetourism.view.HeaderClickListener;
import com.pyxx.chinesetourism.view.HeaderView;

/**
 * 关于应用信息
 * 
 * @author wll
 */
public class MoreAboutusActivity extends BaseActivity implements
		HeaderClickListener {

	HeaderView headerView;
	TextView versionId;

	@Override
	protected void onCreate(Bundle bundle) {
		super.onCreate(bundle);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.more_aboutus);

		initView();
	}

	private void initView() {
		headerView = (HeaderView) findViewById(R.id.more_about_header);
		headerView.setTitle(R.string.more_aboutus);
		headerView.setHeaderClickListener(this);
		versionId = (TextView) findViewById(R.id.version_text);
		versionId.setText("V " + getVersion(this));

	}

	/**
	 * 获取版本号
	 * 
	 * @return
	 */
	public static String getVersion(Context context) {
		PackageManager packageManager = context.getPackageManager();
		try {
			PackageInfo packInfo = packageManager.getPackageInfo(
					context.getPackageName(), 0);
			return packInfo.versionName;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		return "1.0.0";
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
