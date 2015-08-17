package com.pyxx.chinesetourism.activity.more;

import org.json.JSONObject;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import com.pyxx.chinesetourism.BaseActivity;
import com.pyxx.chinesetourism.R;
import com.pyxx.chinesetourism.bean.UserBase;
import com.pyxx.chinesetourism.bean.UserBean;
import com.pyxx.chinesetourism.custom.CustomAlertDialog;
import com.pyxx.chinesetourism.custom.CustomToast;
import com.pyxx.chinesetourism.myhttp.AsyncHttpResponseHandler;
import com.pyxx.chinesetourism.myhttp.HttpClientUtils;
import com.pyxx.chinesetourism.myhttp.HttpParams;
import com.pyxx.chinesetourism.view.HeaderClickListener;
import com.pyxx.chinesetourism.view.HeaderView;

/**
 * 登录界面
 * 
 * @author wll
 */
public class LoginTestActivity extends BaseActivity implements OnClickListener,
		HeaderClickListener {

	HeaderView headerView;
	EditText userEditText, pwdEditText;
	TextView forgetTextView, registTextView;
	Button loginButton;

	UserBean userBean;
	public static final int RECEIVED_USER_INFO = 0X1234;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);

		setContentView(R.layout.main_login_layout);

		initView();

	}

	private void initView() {
		headerView = (HeaderView) findViewById(R.id.header_view);
		headerView.setTitle(R.string.login);
		headerView.setHeaderClickListener(this);

		userEditText = (EditText) findViewById(R.id.login_username);
		pwdEditText = (EditText) findViewById(R.id.login_password);
		forgetTextView = (TextView) findViewById(R.id.login_forget);
		forgetTextView.setOnClickListener(this);
		registTextView = (TextView) findViewById(R.id.login_register);
		registTextView.setOnClickListener(this);
		loginButton = (Button) findViewById(R.id.login_btn);
		loginButton.setOnClickListener(this);

	}

	@Override
	public void onHeaderClick(View v, int which) {
		switch (v.getId()) {
		case R.id.header_back_btn:
			LoginTestActivity.this.finish();
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
		case R.id.login_forget:// 密码找回
			intent = new Intent();
			intent.setClass(this, MainForgetPwdActivity.class);
			break;
		case R.id.login_register:// 注册帐号
			intent = new Intent();
			intent.setClass(this, MainRegisterActivity.class);
			break;
		case R.id.login_btn:
			if (isFullCompleted()) {
				ConfirmLogin();
			}
			break;
		}
		if (intent != null) {
			startActivity(intent);
		}
	}

	/**
	 * 确认登录
	 */
	public void ConfirmLogin() {
		String name = userEditText.getText().toString().trim();
		String password = pwdEditText.getText().toString().trim();
		String path = "http://api.tcshenghuo.org:9999/Zglygg/in/";
		String url = "login!account?";
		HttpParams params = new HttpParams();
		params.put("user.phone", name);
		params.put("user.passWord", password);
		HttpClientUtils.getInstance().post(path, url, params,
				new AsyncHttpResponseHandler() {
					@Override
					public void onSuccess(JSONObject jsonObject) {
						System.out.println("登录返回:" + jsonObject);
						Message message = new Message();
						message.what = RECEIVED_USER_INFO;
						message.obj = jsonObject;
						handler.sendMessage(message);
					}
				});

	}

	Handler handler = new Handler(Looper.myLooper()) {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case RECEIVED_USER_INFO:
				JSONObject jsonObject = (JSONObject) msg.obj;
				userBean = new UserBean();
				if (jsonObject.optInt("code", 0) == 1) {
					JSONObject object = jsonObject.optJSONObject("userInfo");
					if (object != null) {
						UserBean userBean = new UserBean();
						userBean.id = object.optInt("id", -1);
						userBean.name = object.optString("name", "");
						userBean.nick = object.optString("nick", "");
						userBean.passWord = object.optString("passWord", "");
						userBean.sex = object.optInt("sex", -1);
						userBean.birthday = object.optString("birthday", "");
						userBean.address = object.optString("address", "");
						userBean.phone = object.optString("phone", "");
						userBean.email = object.optString("email", "");
						userBean.avatar = object.optString("avatar", "");
						userBean.age = object.optInt("age", -1);

						if (LoginTestActivity.this != null) {
							UserBase.setUserBase(LoginTestActivity.this,
									userBean);
							CustomToast.makeText(LoginTestActivity.this,
									R.string.login_success,
									CustomToast.LENGTH_SHORT).show();
						}
						LoginTestActivity.this.finish();
					}
				} else {
					if (LoginTestActivity.this != null) {
						// 登录失败
						CustomAlertDialog.Builder builder = new CustomAlertDialog.Builder(
								LoginTestActivity.this);
						builder.setTitle(R.string.login_failure);
						builder.setMessage(jsonObject.optString("msg", ""));
						builder.setPositiveButton(R.string.sure, null);
						builder.create().show();
					}
				}
				break;
			}
		};
	};

	// 检查用户名和密码是否填写完全
	public boolean isFullCompleted() {
		boolean flg = false;
		String message = null;
		final EditText view;
		if (userEditText.getText().toString() == null
				|| userEditText.getText().toString().equals("")) {
			message = getString(R.string.user_hint);
			view = userEditText;
		} else if (pwdEditText.getText().toString() == null
				|| pwdEditText.getText().toString().equals("")) {
			message = getString(R.string.pwd_hint);
			view = pwdEditText;
		} else {
			flg = true;
			view = userEditText;
		}
		if (flg == false) {
			showErrorDialog(view, message);
		}
		return flg;
	}

	// 显示错误信息的对话框
	public void showErrorDialog(final View view, String message) {
		AlertDialog.Builder builder = new CustomAlertDialog.Builder(this);
		builder.setMessage(message);
		builder.setTitle(R.string.login_notify);
		builder.setPositiveButton(R.string.sure,
				new android.content.DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.cancel();
						view.setFocusable(true);
						view.setFocusableInTouchMode(true);
						view.requestFocus();
					}
				});
		builder.create().show();
	}

}
