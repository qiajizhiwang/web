package com.xiangxing.enums;

public enum SignFlagEnum {
	/**
	 * 缺勤
	 */
	qq(0l),
	/**
	 * 签到
	 */
	qd(1l),
	/**
	 * 请假
	 */
	qj(2l),
	/**
	 * 旷课
	 */
	kk(3l);

	private Long msg;

	private SignFlagEnum(Long msg) {
		this.msg = msg;
	}

	public Long getMsg() {
		return this.msg;
	}

	public void setMsg(Long msg) {
		this.msg = msg;
	}

}
