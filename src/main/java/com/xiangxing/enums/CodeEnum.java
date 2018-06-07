package com.xiangxing.enums;

/**
 * @author hongxuewen
 * @version 1.0 Description:参数结果编码
 */

public enum CodeEnum {
	/**
	 * 接口调用成功
	 */
	CODE_10000("10000"),
	/**
	 * 接口调用异常
	 */
	CODE_20000("20000"),
	/**
	 * 接口调用参数异常
	 */
	CODE_30000("30000");

	private String msg;

	private CodeEnum(String msg) {
		this.msg = msg;
	}

	public String getMsg() {
		return this.msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

}
