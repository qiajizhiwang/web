package com.xiangxing.vo.api;

public class LoginRequest {

	private String phone;

	private String password;

	/* 1 老师 2学生 */
	private int type;

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

}
