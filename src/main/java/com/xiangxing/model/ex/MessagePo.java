package com.xiangxing.model.ex;

import com.xiangxing.model.Message;

public class MessagePo extends Message {
	private String name;
	private String showCreateTime;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getShowCreateTime() {
		return showCreateTime;
	}

	public void setShowCreateTime(String showCreateTime) {
		this.showCreateTime = showCreateTime;
	}

}
