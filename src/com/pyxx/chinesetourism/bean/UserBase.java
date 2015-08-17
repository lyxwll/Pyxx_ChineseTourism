package com.pyxx.chinesetourism.bean;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class UserBase {

	public static void setUserBase(Context mContext, UserBean userBean) {
		SharedPreferences sharedPreferences = mContext.getSharedPreferences(
				"UserBase", Context.MODE_PRIVATE);
		Editor editor = sharedPreferences.edit();
		editor.putInt("id", userBean.id);
		editor.putString("name", userBean.name);
		editor.putString("passWord", userBean.passWord);
		editor.putString("birthday", userBean.birthday);
		editor.putString("address", userBean.address);
		editor.putString("phone", userBean.phone);
		editor.putString("email", userBean.email);
		editor.putInt("role", userBean.role);
		editor.putInt("sex", userBean.sex);
		editor.putString("avatar", userBean.avatar);
		editor.putString("nick", userBean.nick);
		editor.putString("captcha", userBean.captcha);
		editor.putInt("age", userBean.age);

		editor.commit();
	}

	public static int getUserId(Context mContext) {
		SharedPreferences preferences = mContext.getSharedPreferences(
				"UserBase", Context.MODE_PRIVATE);
		return preferences.getInt("id", -1);
	}

	public static String getUserName(Context mContext) {
		SharedPreferences preferences = mContext.getSharedPreferences(
				"UserBase", Context.MODE_PRIVATE);
		return preferences.getString("name", "");
	}

	public static void setUserPassword(Context mContext, String password) {
		SharedPreferences preferences = mContext.getSharedPreferences(
				"UserBase", Context.MODE_PRIVATE);
		Editor editor = preferences.edit();
		editor.putString("passWord", password);
		editor.commit();
	}

	public static String getUserPassword(Context mContext) {
		SharedPreferences preferences = mContext.getSharedPreferences(
				"UserBase", Context.MODE_PRIVATE);
		return preferences.getString("passWord", "");
	}

	public static int getUserSex(Context mContext) {
		SharedPreferences preferences = mContext.getSharedPreferences(
				"UserBase", Context.MODE_PRIVATE);
		return preferences.getInt("sex", -1);
	}

	public static String getUserBirthday(Context mContext) {
		SharedPreferences preferences = mContext.getSharedPreferences(
				"UserBase", Context.MODE_PRIVATE);
		return preferences.getString("birthday", "");
	}

	public static String getUserAddress(Context mContext) {
		SharedPreferences preferences = mContext.getSharedPreferences(
				"UserBase", Context.MODE_PRIVATE);
		return preferences.getString("address", "");
	}

	public static String getUserPhone(Context mContext) {
		SharedPreferences preferences = mContext.getSharedPreferences(
				"UserBase", Context.MODE_PRIVATE);
		return preferences.getString("phone", "");
	}

	public static String getUserEmail(Context mContext) {
		SharedPreferences preferences = mContext.getSharedPreferences(
				"UserBase", Context.MODE_PRIVATE);
		return preferences.getString("email", "");
	}

	public static int getUserRole(Context mContext) {
		SharedPreferences preferences = mContext.getSharedPreferences(
				"UserBase", Context.MODE_PRIVATE);
		return preferences.getInt("role", 0);
	}

	public static void setUserAvatar(Context mContext, String url) {
		SharedPreferences preferences = mContext.getSharedPreferences(
				"UserBase", Context.MODE_PRIVATE);
		Editor editor = preferences.edit();
		editor.putString("avatar", url);
		editor.commit();
	}

	public static String getUserAvatar(Context mContext) {
		SharedPreferences preferences = mContext.getSharedPreferences(
				"UserBase", Context.MODE_PRIVATE);
		return preferences.getString("avatar", "");
	}

	public static int getSex(Context mContext) {
		SharedPreferences preferences = mContext.getSharedPreferences(
				"UserBase", Context.MODE_PRIVATE);
		return preferences.getInt("sex", 0);
	}

	public static String getUserNick(Context mContext) {
		SharedPreferences preferences = mContext.getSharedPreferences(
				"UserBase", Context.MODE_PRIVATE);
		return preferences.getString("nick", "");
	}

}
