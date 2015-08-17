package com.pyxx.chinesetourism.activity.more;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONObject;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.pyxx.chinesetourism.BaseActivity;
import com.pyxx.chinesetourism.R;
import com.pyxx.chinesetourism.custom.CustomAlertDialog;
import com.pyxx.chinesetourism.custom.CustomAlertDialog.Builder;
import com.pyxx.chinesetourism.myhttp.AsyncHttpResponseHandler;
import com.pyxx.chinesetourism.myhttp.HttpClientUtils;
import com.pyxx.chinesetourism.myhttp.HttpParams;

/**
 * 用户反馈
 * 
 * @author wll
 */
public class MoreFeedbackActivity extends BaseActivity implements
		OnClickListener {

	private EditText msgEditText, emailEditText;
	private TextView titleTextView, checkEmailTextView;
	Button backButton;
	private ImageView commitImage;
	private static final int RECEIVE_CODE = 0x123;

	@Override
	protected void onCreate(Bundle bundle) {
		super.onCreate(bundle);
		requestWindowFeature(Window.FEATURE_NO_TITLE);

		setContentView(R.layout.more_feedback);

		initView();

	}

	private void initView() {
		titleTextView = (TextView) findViewById(R.id.header_title);
		titleTextView.setText("用户反馈");
		backButton = (Button) findViewById(R.id.header_back_btn);
		backButton.setOnClickListener(this);
		commitImage = (ImageView) findViewById(R.id.header_commit);
		commitImage.setOnClickListener(this);
		msgEditText = (EditText) findViewById(R.id.suggestion_content);
		emailEditText = (EditText) findViewById(R.id.email_edit);
		checkEmailTextView = (TextView) findViewById(R.id.email_checked);

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.header_back_btn:
			this.finish();
			break;
		case R.id.header_commit:
			if (isEmailChecked()) {
				feedBack();
				msgEditText.getText().clear();
				emailEditText.getText().clear();
			}
			break;
		}
	}

	public void feedBack() {
		String msg = msgEditText.getText().toString().trim();
		String email = emailEditText.getText().toString().trim();
		if (msg != null && !msg.equals("") || email != null
				&& !email.equals("")) {
			String path = "http://api.tcshenghuo.org:9999/Zglygg/in/";
			String url = "general!feedback?";
			HttpParams params = new HttpParams();
			params.put("feedback.content", msg);
			params.put("feedback.email", email);
			HttpClientUtils.getInstance().post(path, url, params,
					new AsyncHttpResponseHandler() {
						@Override
						public void onSuccess(JSONObject jsonObject) {
							System.out.println("用户反馈返回:" + jsonObject);
							Message message = new Message();
							message.obj = jsonObject;
							message.what = RECEIVE_CODE;
							handler.sendMessage(message);
						}
					});
		}
	}

	Handler handler = new Handler(new Handler.Callback() {

		@Override
		public boolean handleMessage(Message msg) {
			switch (msg.what) {
			case RECEIVE_CODE:
				JSONObject object = (JSONObject) msg.obj;
				if (object != null && object.optInt("code", 0) == 1) {
					CustomAlertDialog.Builder builder = new Builder(
							MoreFeedbackActivity.this);
					builder.setTitle(R.string.commit_success);
					builder.setMessage("用户反馈信息成功!");
					builder.setPositiveButton(R.string.sure, null);
					builder.create().show();
				} else {
					CustomAlertDialog.Builder builder = new Builder(
							MoreFeedbackActivity.this);
					builder.setTitle(R.string.commit_failure);
					builder.setMessage("用户反馈信息失败!");
					builder.setPositiveButton(R.string.sure, null);
					builder.create().show();
				}
				break;
			}
			return true;
		}
	});

	// 检查邮箱的格式是否完全
	public boolean isEmailChecked() {
		boolean flg = true;
		if (msgEditText.getText().toString() == null
				|| msgEditText.getText().toString().equals("")) {
			flg = false;
			Toast.makeText(this, "你还未输入内容,请输入后重试!", Toast.LENGTH_SHORT).show();
		}
		if (emailEditText.getText().toString() == null
				|| emailEditText.getText().toString().equals("")) {
			flg = false;
			Toast.makeText(this, "你还未输入邮箱,请输入后重试!", Toast.LENGTH_SHORT).show();
		} else {
			String email = emailEditText.getText().toString().trim();
			String check = "^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";
			Pattern regex = Pattern.compile(check);
			Matcher matcher = regex.matcher(email);
			boolean isMatched = matcher.matches();
			if (isMatched == false) {
				flg = false;
				checkEmailTextView.setText(R.string.register_email_not);
				checkEmailTextView.setVisibility(View.VISIBLE);
			} else {
				checkEmailTextView.setVisibility(View.INVISIBLE);
			}
		}
		return flg;
	}

	@Override
	public void networkStatusChanged(int status) {

	}

}
