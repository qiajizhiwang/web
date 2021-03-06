package com.xiangxing.vo.api;

public class ApiResponse {

	public ApiResponse() {
		// TODO Auto-generated constructor stub
	}

	public ApiResponse(int status, String memo) {
		this.status = status;
		this.memo = memo;
	}

	public static ApiResponse getErrorResponse(String memo) {
		return new ApiResponse(-1, memo);
	}

	public static ApiResponse getTokenErrorResponse() {
		return new ApiResponse(2, "token无效，请重新登录");
	}
	
	public static ApiResponse getDenyErrorResponse() {
		return new ApiResponse(3, "无权限访问");
	}

	private int status = 1;

	private String memo;

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getMemo() {
		return memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

}
