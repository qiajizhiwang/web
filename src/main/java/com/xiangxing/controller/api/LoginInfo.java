package com.xiangxing.controller.api;

public class LoginInfo {

	private long id;
	private int type;

	public LoginInfo() {
		// TODO Auto-generated constructor stub
	}

	public LoginInfo(long id, int type) {
		this.id = id;
		this.type = type;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

}
