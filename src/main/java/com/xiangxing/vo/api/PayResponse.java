package com.xiangxing.vo.api;

public class PayResponse extends ApiResponse {

	private Object orderInfo;
	
	private String orderNo;
	
	public String getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}

	public Object getOrderInfo() {
		return orderInfo;
	}

	public void setOrderInfo(Object orderInfo) {
		this.orderInfo = orderInfo;
	}

}
