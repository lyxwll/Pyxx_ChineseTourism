package com.pyxx.chinesetourism.activity.more;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONObject;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

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
 * 注册界面
 * 
 * @author wll
 */
public class MainRegisterActivity extends BaseActivity implements
		OnClickListener, HeaderClickListener, OnFocusChangeListener {

	private HeaderView headerView;
	private EditText nameEdit, emailEdit, pwdEdit, repeatEdit;
	private TextView checkName, checkEmail, checkPwd, checkRepeat;
	private Button confirmBtn;

	private static final int RECEIVE_CODE = 0x1234;

	@Override
	protected void onCreate(Bundle bundle) {
		super.onCreate(bundle);
		requestWindowFeature(Window.FEATURE_NO_TITLE);

		setContentView(R.layout.main_register_layout);

		initView();

	}

	private void initView() {
		headerView = (HeaderView) findViewById(R.id.register_header);
		headerView.setHeaderClickListener(this);
		headerView.setTitle(R.string.register_title);

		nameEdit = (EditText) findViewById(R.id.regist_name);
		emailEdit = (EditText) findViewById(R.id.regist_email);
		pwdEdit = (EditText) findViewById(R.id.regist_pwd);
		repeatEdit = (EditText) findViewById(R.id.regist_repeat_pwd);

		checkName = (TextView) findViewById(R.id.check_name);
		checkEmail = (TextView) findViewById(R.id.check_email);
		checkPwd = (TextView) findViewById(R.id.check_pwd);
		checkRepeat = (TextView) findViewById(R.id.check_repeat);

		confirmBtn = (Button) findViewById(R.id.regist_complete_btn);
		confirmBtn.setOnClickListener(this);

		nameEdit.setOnFocusChangeListener(this);
		emailEdit.setOnFocusChangeListener(this);
		pwdEdit.setOnFocusChangeListener(this);
		repeatEdit.setOnFocusChangeListener(this);

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
	public void onFocusChange(View v, boolean hasFocus) {
		switch (v.getId()) {
		case R.id.regist_name:
			if (hasFocus == false) {
				// 检查输入的用户名
				checkNameInput(nameEdit);
			}
			break;
		case R.id.regist_email:
			if (hasFocus == false) {
				checkEmailInput(emailEdit);
			}
			break;
		case R.id.regist_pwd:// 检查密码
			if (hasFocus == false) {
				checkPasswordInput(pwdEdit);
			}
			break;
		case R.id.regist_repeat_pwd:// 检测重复密码
			if (hasFocus == false) {
				checkRepeatPasswordInput();
			}
			break;
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.regist_complete_btn:
			confirmBtn.requestFocus();
			confirmBtn.requestFocusFromTouch();
			if (isAllChecked()) {
				ConfirmRegist();
			} else {
				if (MainRegisterActivity.this != null) {
					// 检测未通过
					CustomToast
							.makeText(MainRegisterActivity.this,
									R.string.register_check_failure,
									Toast.LENGTH_SHORT).show();
				}
			}
			break;
		}
	}

	/**
	 * 帐号密码注册方式
	 */
	public void ConfirmRegist() {
		String path = "http://api.tcshenghuo.org:9999/Zglygg/in/";
		String url = "register!phone?";
		HttpParams params = new HttpParams();
		params.put("user.phone", emailEdit.getText().toString().trim());
		params.put("user.nick", nameEdit.getText().toString().trim());
		params.put("user.passWord", pwdEdit.getText().toString().trim());
		HttpClientUtils.getInstance().post(path, url, params,
				new AsyncHttpResponseHandler() {
					@Override
					public void onSuccess(JSONObject jsonObject) {
						System.out.println("注册帐号返回:" + jsonObject);
						Message message = new Message();
						message.what = RECEIVE_CODE;
						message.obj = jsonObject;
						handler.sendMessage(message);
					}
				});
	}

	Handler handler = new Handler(Looper.myLooper()) {

		public void handleMessage(Message msg) {
			switch (msg.what) {
			case RECEIVE_CODE:
				JSONObject jsonObject = (JSONObject) msg.obj;
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
						if (MainRegisterActivity.this != null) {
							UserBase.setUserBase(MainRegisterActivity.this,
									userBean);
							CustomToast.makeText(MainRegisterActivity.this,
									R.string.register_success,
									CustomToast.LENGTH_SHORT).show();
						}
						MainRegisterActivity.this.finish();
					}
				} else {
					if (MainRegisterActivity.this != null) {
						// 注册失败
						CustomAlertDialog.Builder builder = new CustomAlertDialog.Builder(
								MainRegisterActivity.this);
						builder.setTitle(R.string.register_failure);
						builder.setMessage(jsonObject.optString("msg", ""));
						builder.setPositiveButton(R.string.sure, null);
						builder.create().show();
					}
				}
				break;
			}
		}
	};

	// 检查用户名
	public void checkNameInput(EditText editText) {
		String value = editText.getText().toString().trim();
		if (value != null && value.length() >= 6 && value.length() <= 12) {
			// 隐藏提示栏
			checkName.setVisibility(View.INVISIBLE);
		} else {
			checkName.setText(R.string.register_name_length);
			// 显示提示栏
			checkName.setVisibility(View.VISIBLE);
		}
	}

	// 检查邮箱是否正确
	public void checkEmailInput(EditText editText) {
		String email = editText.getText().toString().trim();
		String check = "^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";
		Pattern regex = Pattern.compile(check);
		Matcher matcher = regex.matcher(email);
		boolean isMatched = matcher.matches();
		if (isMatched == false) {
			checkEmail.setText(R.string.register_email_not);
			checkEmail.setVisibility(View.VISIBLE);
		} else {
			checkEmail.setVisibility(View.INVISIBLE);
		}
	}

	// 密码的检查
	public void checkPasswordInput(EditText editText) {
		String value = editText.getText().toString().trim();
		if (value != null && value.length() >= 6 && value.length() <= 12) {
			// 隐藏提示栏
			checkPwd.setVisibility(View.INVISIBLE);
		} else {
			checkPwd.setText(R.string.register_passowrd_length);
			// 显示提示栏
			checkPwd.setVisibility(View.VISIBLE);
		}
	}

	// 重复密码的检查
	public void checkRepeatPasswordInput() {
		// 拿到密码的控件的值然后转换为字符串 然后去除前后空格
		String password = pwdEdit.getText().toString().trim();
		String repeatPassword = repeatEdit.getText().toString().trim();
		// 两次密码比较
		if (password.equals(repeatPassword)) {
			// 隐藏提示栏
			checkRepeat.setVisibility(View.INVISIBLE);
		} else {
			checkRepeat.setText(R.string.register_password_notsame);
			checkRepeat.setVisibility(View.VISIBLE);
		}
	}

	// 检查注册的内容是否完全
	public boolean isAllChecked() {
		boolean flg = true;
		if (nameEdit.getText().toString() == null
				|| nameEdit.getText().toString().equals("")
				|| nameEdit.getText().length() < 6
				|| nameEdit.getText().length() > 16) {
			flg = false;
		}
		if (pwdEdit.getText().toString() == null
				|| pwdEdit.getText().toString().equals("")
				|| pwdEdit.getText().length() < 6
				|| pwdEdit.getText().length() > 16) {
			flg = false;
		}
		if (repeatEdit.getText().toString() == null
				|| repeatEdit.getText().toString().equals("")
				|| repeatEdit.getText().length() < 6
				|| repeatEdit.getText().length() > 16) {
			flg = false;
		}
		if (emailEdit.getText().toString() == null
				|| emailEdit.getText().toString().equals("")) {
			flg = false;
		} else {
			String email = emailEdit.getText().toString().trim();
			String check = "^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";
			Pattern regex = Pattern.compile(check);
			Matcher matcher = regex.matcher(email);
			boolean isMatched = matcher.matches();
			if (isMatched == false) {
				flg = false;
				checkEmail.setText(R.string.register_email_not);
				checkEmail.setVisibility(View.VISIBLE);
			}
		}
		return flg;
	}

}
