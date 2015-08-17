package com.pyxx.chinesetourism.bean;

import java.io.Serializable;

public class UserBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public int id;
	public String address;
	public int age;
	public String avatar;// 头像
	public String birthday;
	public String captcha;// 验证码
	public String email;
	public String nick;// 昵称
	public String openId;
	public String passWord;
	public String phone;
	public int role;// 角色: 1管理员，2普通用户
	public int sex;// 性别 0：男 1:女
	public String name;// 用户名

}
