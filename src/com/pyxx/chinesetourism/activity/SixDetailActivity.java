package com.pyxx.chinesetourism.activity;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.pyxx.chinesetourism.BaseActivity;
import com.pyxx.chinesetourism.R;
import com.pyxx.chinesetourism.bean.InfoBean;
import com.pyxx.chinesetourism.myhttp.NetWorkUtils;
import com.pyxx.chinesetourism.myhttp.NetWorkUtils.OnImageDownload;
import com.pyxx.chinesetourism.view.HeaderClickListener;
import com.pyxx.chinesetourism.view.HeaderView;

/**
 * 国家地理 详情
 * 
 * @author wll
 */
public class SixDetailActivity extends BaseActivity implements
		HeaderClickListener {

	HeaderView headerView;
	private InfoBean infoBean;
	private ImageView logoImage;
	TextView title, time, source, content, content2;
	String logo;

	public static final int LOAD_IMAGE = 123;

	@Override
	protected void onCreate(Bundle bundle) {
		super.onCreate(bundle);
		if (getIntent() != null && getIntent().hasExtra("infoBean")) {
			infoBean = (InfoBean) getIntent().getSerializableExtra("infoBean");
		}

		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.six_detail_layout);

		initView();

	}

	private void initView() {
		headerView = (HeaderView) findViewById(R.id.six_header);
		headerView.setHeaderClickListener(this);
		headerView.setTitle("旅游资讯详情");
		headerView.showBack();

		title = (TextView) findViewById(R.id.six_detail_name);
		time = (TextView) findViewById(R.id.six_detail_time);
		source = (TextView) findViewById(R.id.six_comefrom);
		content = (TextView) findViewById(R.id.six_content1);
		content2 = (TextView) findViewById(R.id.six_content2);
		logoImage = (ImageView) findViewById(R.id.six_detail_img);

		if (infoBean != null) {
			logo = infoBean.logo;
			title.setText(infoBean.title);
			time.setText(infoBean.time);
			source.setText(infoBean.source);
			content.setText(infoBean.digest);
			content2.setText(infoBean.content);
		}

		new NetWorkUtils.NetWorkThread(logo, new OnImageDownload() {

			@Override
			public void imageDownloaded(Bitmap bitmap) {
				Message message = new Message();
				message.what = LOAD_IMAGE;
				message.obj = bitmap;
				handler.sendMessage(message);
			}
		}).start();

	}

	@SuppressLint("HandlerLeak")
	Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case LOAD_IMAGE:
				Bitmap bitmap = (Bitmap) msg.obj;
				logoImage.setImageBitmap(bitmap);
				break;
			}
		};
	};

	@Override
	public void networkStatusChanged(int status) {
		headerView.setNetworkStatus(status);
	}

	@Override
	public void onHeaderClick(View v, int which) {
		switch (which) {
		case HeaderView.BACK_POSITION:
			this.finish();
			break;
		}
	}

}
