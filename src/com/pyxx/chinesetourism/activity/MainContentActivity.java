package com.pyxx.chinesetourism.activity;

import java.util.ArrayList;
import java.util.List;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.RadioButton;

import com.pyxx.chinesetourism.BaseActivity;
import com.pyxx.chinesetourism.R;
import com.pyxx.chinesetourism.custom.CustomAlertDialog;
import com.pyxx.chinesetourism.fragment.FiveFragment;
import com.pyxx.chinesetourism.fragment.FourFragment;
import com.pyxx.chinesetourism.fragment.OneFragment;
import com.pyxx.chinesetourism.fragment.SixFragment;
import com.pyxx.chinesetourism.fragment.ThreeFragment;
import com.pyxx.chinesetourism.fragment.TwoFragment;

/**
 * 主界面
 * 
 * @author wll
 */
public class MainContentActivity extends BaseActivity implements
		OnClickListener {

	RadioButton button1, button2, button3, button4, button5, button6;
	// 声明集合保存图片按钮控件
	private List<RadioButton> buttons = new ArrayList<RadioButton>();
	private FragmentManager fgManager;

	@Override
	protected void onCreate(Bundle bundle) {
		super.onCreate(bundle);
		requestWindowFeature(Window.FEATURE_NO_TITLE);

		setContentView(R.layout.main_content_layout);

		initView();

		fgManager = getSupportFragmentManager();
		fgManager.beginTransaction()
				.replace(R.id.main_content, new OneFragment()).commit();

	}

	private void initView() {

		button1 = (RadioButton) findViewById(R.id.tuijian_bottom);
		button2 = (RadioButton) findViewById(R.id.jiudian_bottom);
		button3 = (RadioButton) findViewById(R.id.tourism_bottom);
		button4 = (RadioButton) findViewById(R.id.nvxingshe_bottom);
		button5 = (RadioButton) findViewById(R.id.national_bottom);
		button6 = (RadioButton) findViewById(R.id.nvyouzixun_bottom);
		buttons.add(button1);
		buttons.add(button2);
		buttons.add(button3);
		buttons.add(button4);
		buttons.add(button5);
		buttons.add(button6);
		button1.setOnClickListener(this);
		button2.setOnClickListener(this);
		button3.setOnClickListener(this);
		button4.setOnClickListener(this);
		button5.setOnClickListener(this);
		button6.setOnClickListener(this);

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tuijian_bottom:
			fgManager.beginTransaction()
					.replace(R.id.main_content, new OneFragment()).commit();
			break;
		case R.id.jiudian_bottom:
			fgManager.beginTransaction()
					.replace(R.id.main_content, new TwoFragment()).commit();
			break;
		case R.id.tourism_bottom:
			fgManager.beginTransaction()
					.replace(R.id.main_content, new ThreeFragment()).commit();
			break;
		case R.id.nvxingshe_bottom:
			fgManager.beginTransaction()
					.replace(R.id.main_content, new FourFragment()).commit();
			break;
		case R.id.national_bottom:
			fgManager.beginTransaction()
					.replace(R.id.main_content, new FiveFragment()).commit();
			break;
		case R.id.nvyouzixun_bottom:
			fgManager.beginTransaction()
					.replace(R.id.main_content, new SixFragment()).commit();
			break;
		}
	}

	/**
	 * 再按一次退出程序
	 */

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {

			new CustomAlertDialog.Builder(this)
					.setTitle(R.string.exit)
					.setIcon(R.drawable.icon_launch)
					.setMessage(getString(R.string.exit_message))
					.setPositiveButton(R.string.done,
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									MainContentActivity.this.finish();
								}
							})
					.setNegativeButton(R.string.cancel,
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface arg0,
										int arg1) {
								}
							}).show();
			return false;
		}
		return super.onKeyDown(keyCode, event);
	}

	/*
	 * private long exitTime = 0;
	 * 
	 * @Override public boolean dispatchKeyEvent(KeyEvent event) { if
	 * (event.getKeyCode() == KeyEvent.KEYCODE_BACK && event.getAction() ==
	 * KeyEvent.ACTION_DOWN) {
	 * 
	 * if ((System.currentTimeMillis() - exitTime) > 2000) {
	 * Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show(); exitTime =
	 * System.currentTimeMillis(); } else { // 完全退出程序； System.exit(0);
	 * android.os.Process.killProcess(android.os.Process.myPid()); finish(); }
	 * return true; } return super.dispatchKeyEvent(event); }
	 */

	// 建议在APP整体退出之前调用MapApi的destroy()函数，不要在每个activity的OnDestroy中调用，
	// 避免MapApi重复创建初始化，提高效率
	@Override
	protected void onDestroy() {
		System.exit(0);
		super.onDestroy();
	}

	@Override
	public void networkStatusChanged(int status) {

	}

}
