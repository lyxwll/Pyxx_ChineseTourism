package com.pyxx.chinesetourism.view;

import com.pyxx.chinesetourism.R;
import com.pyxx.chinesetourism.custom.CustomAlertDialog;
import com.pyxx.chinesetourism.utils.NetworkStatus;
import com.pyxx.chinesetourism.utils.PackageManagerUtils;

import android.annotation.SuppressLint;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewTreeObserver;
import android.view.ViewTreeObserver.OnPreDrawListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

/**
 * 自定义常用顶部菜单
 * 
 * @author wll
 */
public class HeaderView extends LinearLayout implements OnClickListener {
	public static final int BACK_POSITION = 0;
	public static final int OPT_POSITION = 1;
	public static final int TITLE_POSITION = 2;
	public static final int NETWORK_POSITION = 3;

	public static final int SHOW_NETWORK = 0X123;
	public static final int HIDDEN_NETWORK = 0X124;
	View view;
	Button backButton; // 返回按钮
	ImageView moreImage; // 更多选项
	TextView titleView; // 标题
	Spinner spinner; // 下拉列表
	TextView headerNetwork; // 网络提示
	Context mContext;
	HeaderClickListener headerClickListener;

	boolean isMeasured = false;
	// int headerViewHeight; //总控件的高度
	int networkHeight; // 显示网络控件的高度

	LinearLayout.LayoutParams layoutParams; // headerNetwork的布局参数

	public HeaderView(Context context) {
		this(context, null);
	}

	public HeaderView(Context context, AttributeSet attrs) {
		super(context, attrs);
		mContext = context;
		view = LayoutInflater.from(context).inflate(R.layout.ch_header_layout,
				this);
		backButton = (Button) view.findViewById(R.id.header_back_btn);
		moreImage = (ImageView) view.findViewById(R.id.header_more_img);
		titleView = (TextView) view.findViewById(R.id.header_title);
		spinner = (Spinner) view.findViewById(R.id.spinner);
		headerNetwork = (TextView) view.findViewById(R.id.title_network);
		backButton.setOnClickListener(this);
		moreImage.setOnClickListener(this);
		headerNetwork.setOnClickListener(this);
		titleView.setOnClickListener(this);
		/*
		 * ViewTreeObserver observer = view.getViewTreeObserver();
		 * observer.addOnPreDrawListener(new OnPreDrawListener() {
		 * 
		 * @Override public boolean onPreDraw() { headerViewHeight =
		 * view.getMeasuredHeight(); return true; } });
		 */
		ViewTreeObserver observer = headerNetwork.getViewTreeObserver();
		observer.addOnPreDrawListener(new OnPreDrawListener() {
			@Override
			public boolean onPreDraw() {
				if (!isMeasured) {
					isMeasured = true;
					networkHeight = headerNetwork.getMeasuredHeight();
					layoutParams = new LayoutParams(LayoutParams.MATCH_PARENT,
							0);
					headerNetwork.setLayoutParams(layoutParams);
				}
				return true;
			}
		});
	}

	public void setHeaderClickListener(HeaderClickListener headerClickListener) {
		this.headerClickListener = headerClickListener;
	}

	public HeaderClickListener getHeaderClickListener() {
		return headerClickListener;
	}

	public void setTitle(int resId) {
		setTitle(mContext.getResources().getString(resId));
	}

	public void setTitle(String title) {
		titleView.setText(title);
	}

	public void setOptText(int resId) {
		setOptText(mContext.getResources().getString(resId));
	}

	public void setOptText(String optTitle) {
		moreImage.setTag(optTitle);
		moreImage.setVisibility(View.VISIBLE);
	}

	public void setAdapter(SpinnerAdapter adapter) {
		spinner.setAdapter(adapter);
		spinner.setVisibility(View.VISIBLE);
	}

	public void hiddenOpt() {
		moreImage.setVisibility(View.INVISIBLE);
	}

	public void showOpt() {
		moreImage.setVisibility(View.VISIBLE);
	}

	public void showSpinner() {
		spinner.setVisibility(View.VISIBLE);
	}

	public void hiddenSpinner() {
		spinner.setVisibility(View.GONE);
	}

	public void hiddenBack() {
		backButton.setVisibility(View.INVISIBLE);
	}

	public void showBack() {
		backButton.setVisibility(View.VISIBLE);
	}

	public Button getBackButton() {
		return backButton;
	}

	public ImageView getmoreImage() {
		return moreImage;
	}

	public Spinner getSpinner() {
		return spinner;
	}

	public TextView getTitle() {
		return titleView;
	}

	public TextView getNetworTextView() {
		return headerNetwork;
	}

	@Override
	public void onClick(View v) {
		View view = backButton;
		int position = 0;
		switch (v.getId()) {
		case R.id.header_back_btn:
			position = BACK_POSITION;
			view = backButton;
			break;
		case R.id.header_title:
			position = TITLE_POSITION;
			view = titleView;
			break;
		case R.id.header_more_img:
			position = OPT_POSITION;
			view = moreImage;
			break;
		case R.id.title_network:
			openNetwork();
			break;
		}
		if (headerClickListener != null) {
			headerClickListener.onHeaderClick(view, position);
		}
	}

	public void openNetwork() {
		Intent intent = null;
		// 判断手机系统的版本 即API大于10 就是3.0或以上版本
		if (android.os.Build.VERSION.SDK_INT > 10) {
			intent = new Intent(
					android.provider.Settings.ACTION_WIRELESS_SETTINGS);
		} else {
			intent = new Intent();
			ComponentName component = new ComponentName("com.android.settings",
					"com.android.settings.WirelessSettings");
			intent.setComponent(component);
			intent.setAction("android.intent.action.VIEW");
		}
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		if (PackageManagerUtils.isIntentAvailable(mContext, intent)) {
			mContext.startActivity(intent);
		} else {
			CustomAlertDialog.Builder builder = new CustomAlertDialog.Builder(
					mContext);
			builder.setTitle(R.string.settings_not_exists);
			builder.setPositiveButton(R.string.setting_ok,
					new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {

						}
					});
			builder.create().show();
		}
	}

	@SuppressLint("HandlerLeak")
	Handler handler = new Handler(Looper.myLooper()) {

		public void handleMessage(Message msg) {
			switch (msg.what) {
			case SHOW_NETWORK:
				LinearLayout.LayoutParams layoutParams = (LayoutParams) headerNetwork
						.getLayoutParams();
				if (layoutParams.height < networkHeight) {
					layoutParams.height += 1;
					// System.out.println("show network height=" +
					// layoutParams.height);
					headerNetwork.setLayoutParams(layoutParams);
				}
				break;
			case HIDDEN_NETWORK:
				LinearLayout.LayoutParams layoutParams1 = (LayoutParams) headerNetwork
						.getLayoutParams();
				if (layoutParams1.height > 0) {
					layoutParams1.height -= 1;
					// System.out.println("hidden network height=" +
					// layoutParams1.height);
					headerNetwork.setLayoutParams(layoutParams1);
				}
				break;
			}
		}

	};

	class ShowThread extends Thread {
		@Override
		public void run() {
			for (int i = 0; i <= networkHeight; i++) {
				handler.sendEmptyMessage(SHOW_NETWORK);
				try {
					Thread.sleep(50);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}

	class HiddenThread extends Thread {
		@Override
		public void run() {
			for (int i = networkHeight; i > 0; i--) {
				handler.sendEmptyMessage(HIDDEN_NETWORK);
				try {
					Thread.sleep(50);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * 设置网络状态
	 * 
	 * @param status
	 */
	public void setNetworkStatus(int status) {
		switch (status) {
		case NetworkStatus.NETWORK_AVAILABLE:
			new HiddenThread().start();
			break;
		case NetworkStatus.NETWORK_UNAVAILABLE:
			headerNetwork.setText(R.string.network_unreachable);
			new ShowThread().start();
			break;
		}
	}
}
