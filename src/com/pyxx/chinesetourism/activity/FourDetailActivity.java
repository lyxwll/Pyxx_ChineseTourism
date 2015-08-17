package com.pyxx.chinesetourism.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.pyxx.chinesetourism.BaseActivity;
import com.pyxx.chinesetourism.R;
import com.pyxx.chinesetourism.bean.BookingBean;
import com.pyxx.chinesetourism.myhttp.NetWorkUtils;
import com.pyxx.chinesetourism.myhttp.NetWorkUtils.OnImageDownload;
import com.pyxx.chinesetourism.view.HeaderClickListener;
import com.pyxx.chinesetourism.view.HeaderView;

/**
 * 旅行社 详情
 * 
 * @author wll
 */
public class FourDetailActivity extends BaseActivity implements
		HeaderClickListener, OnClickListener {

	BookingBean bookBean;
	private TextView nameText, priceText, teleText, addressText, content1,
			content2, content3;
	private ImageView logoImage;
	private HeaderView headerView;
	RelativeLayout addressLayout, roadLayout;
	String logo;

	public static final int LOAD_IMAGE = 123;

	@Override
	protected void onCreate(Bundle bundle) {
		super.onCreate(bundle);
		requestWindowFeature(Window.FEATURE_NO_TITLE);

		if (getIntent() != null && getIntent().hasExtra("bookBean")) {
			bookBean = (BookingBean) getIntent().getSerializableExtra(
					"bookBean");
		}
		setContentView(R.layout.one_attraction_details);

		initView();

	}

	private void initView() {

		headerView = (HeaderView) findViewById(R.id.one_head);
		headerView.setHeaderClickListener(this);
		headerView.setTitle("旅行社详情");
		headerView.showBack();

		logoImage = (ImageView) findViewById(R.id.one_ico_img);
		nameText = (TextView) findViewById(R.id.one_detail_name);
		priceText = (TextView) findViewById(R.id.one_price);
		teleText = (TextView) findViewById(R.id.one_detail_tele);
		teleText.setOnClickListener(this);
		addressText = (TextView) findViewById(R.id.one_address);
		content1 = (TextView) findViewById(R.id.detail_building);
		content2 = (TextView) findViewById(R.id.detail_develop);
		content3 = (TextView) findViewById(R.id.detail_turism);

		addressLayout = (RelativeLayout) findViewById(R.id.relativeLayout1);
		roadLayout = (RelativeLayout) findViewById(R.id.relativeLayout2);
		addressLayout.setOnClickListener(this);
		roadLayout.setOnClickListener(this);

		if (bookBean != null) {
			logo = bookBean.logo;
			nameText.setText(bookBean.name);
			priceText.setText(bookBean.productPrice);
			teleText.setText(bookBean.tel);
			addressText.setText(bookBean.address);
			content3.setText(bookBean.productBrief);
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
	public void onHeaderClick(View v, int which) {
		switch (v.getId()) {
		case R.id.header_back_btn:
			this.finish();
			break;
		}
	}

	@Override
	public void networkStatusChanged(int status) {
		headerView.setNetworkStatus(status);
	}

	@Override
	public void onClick(View v) {
		Intent intent = null;
		switch (v.getId()) {
		case R.id.one_detail_tele:
			intent = new Intent();
			intent.setAction("android.intent.action.CALL");
			intent.setData(Uri.parse("tel:" + teleText.getText()));
			break;
		case R.id.relativeLayout1:
			intent = new Intent(this, BaiduMapActivity2.class);
			Bundle bundle = new Bundle();
			bundle.putSerializable("bookBean", bookBean);
			intent.putExtras(bundle);
			break;
		case R.id.relativeLayout2:
			intent = new Intent(this, DetailRouteSearch2.class);
			Bundle mBundle = new Bundle();
			mBundle.putSerializable("bookBean", bookBean);
			intent.putExtras(mBundle);
			break;
		}
		if (intent != null) {
			startActivity(intent);
		}
	}
}
