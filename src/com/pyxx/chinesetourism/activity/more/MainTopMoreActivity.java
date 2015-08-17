package com.pyxx.chinesetourism.activity.more;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.pyxx.chinesetourism.BaseActivity;
import com.pyxx.chinesetourism.R;
import com.pyxx.chinesetourism.crop.CropImageMainAvtivity;
import com.pyxx.chinesetourism.crop.CropImagePath;
import com.pyxx.chinesetourism.view.CircleImageView;
import com.pyxx.chinesetourism.view.DampView;
import com.pyxx.chinesetourism.view.HeaderClickListener;
import com.pyxx.chinesetourism.view.HeaderView;

/**
 * Head栏更多more菜单项
 * 
 * @author wll
 */
public class MainTopMoreActivity extends BaseActivity implements
		OnClickListener, HeaderClickListener {

	HeaderView headerView;
	CircleImageView circleImage;

	TextView nameTextView;
	Button loginButton, unLoginBtn;
	RelativeLayout collectLayout, aboutLayout, feedbackLayout, pushLayout,
			cacheLayout;
	Button customerBtn;

	@Override
	protected void onCreate(Bundle bundle) {
		super.onCreate(bundle);
		requestWindowFeature(Window.FEATURE_NO_TITLE);

		setContentView(R.layout.main_top_more);

		initView();
	}

	private void initView() {
		headerView = (HeaderView) findViewById(R.id.more_header);
		headerView.setTitle(R.string.more);
		headerView.setHeaderClickListener(this);

		ImageView headImg = (ImageView) findViewById(R.id.head_img);
		DampView dampView = (DampView) findViewById(R.id.more_dampview);
		dampView.setImageView(headImg);

		circleImage = (CircleImageView) findViewById(R.id.user_img);
		nameTextView = (TextView) findViewById(R.id.more_user_name);
		loginButton = (Button) findViewById(R.id.login_or_no);
		loginButton.setOnClickListener(this);
		collectLayout = (RelativeLayout) findViewById(R.id.mycollect_layout);
		collectLayout.setOnClickListener(this);
		aboutLayout = (RelativeLayout) findViewById(R.id.aboutus_layout);
		aboutLayout.setOnClickListener(this);
		feedbackLayout = (RelativeLayout) findViewById(R.id.feedback_layout);
		feedbackLayout.setOnClickListener(this);
		pushLayout = (RelativeLayout) findViewById(R.id.pushmsg_layout);
		pushLayout.setOnClickListener(this);
		cacheLayout = (RelativeLayout) findViewById(R.id.clearcache_layout);
		cacheLayout.setOnClickListener(this);
		customerBtn = (Button) findViewById(R.id.customer_btn);
		customerBtn.setOnClickListener(this);

		unLoginBtn = (Button) findViewById(R.id.unLogin_btn);
		unLoginBtn.setOnClickListener(this);

		if (isLogin()) {
			loginButton.setText("登录");
		} else {
			loginButton.setText("注销");
			nameTextView.setText("");
		}

	}

	public boolean isLogin() {
		return true;
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
		Intent intent = null;
		switch (v.getId()) {
		case R.id.user_img:
			if (getApplication() != null) {
				intent = new Intent();
				// 跳转至CropImageMainAvtivity界面
				intent.setClass(this, CropImageMainAvtivity.class);
				intent.putExtra(CropImagePath.CROP_IMAGE_WIDTH, 160);
				intent.putExtra(CropImagePath.CROP_IMAGE_HEIGHT, 160);
				startActivityForResult(intent, CropImagePath.START_CROP_IMAGE);
			}
			break;
		case R.id.login_or_no:// 登录
			intent = new Intent();
			intent.setClass(this, LoginTestActivity.class);
			startActivity(intent);
			break;
		case R.id.mycollect_layout:
			if (!isLogin()) {
				intent = new Intent(this, MoreMyCollectActivity.class);
				startActivity(intent);
			} else {
				Toast.makeText(this, "请先登录", Toast.LENGTH_SHORT).show();
			}
			break;
		case R.id.feedback_layout:
			intent = new Intent(this, MoreFeedbackActivity.class);
			startActivity(intent);
			break;
		case R.id.aboutus_layout:
			intent = new Intent(this, MoreAboutusActivity.class);
			startActivity(intent);
			break;
		case R.id.customer_btn:
			intent = new Intent();
			intent.setAction("android.intent.action.CALL");
			intent.setData(Uri.parse("tel:4000-666-058"));
			startActivity(intent);
			break;
		case R.id.unLogin_btn:

			break;
		}
	}

}
