package com.xiangxing.vo.api;

public class VersionVo extends ApiResponse{
	
	private String version;
	
	private int must;
	private String address;

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public int getMust() {
		return must;
	}

	public void setMust(int must) {
		this.must = must;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

}
