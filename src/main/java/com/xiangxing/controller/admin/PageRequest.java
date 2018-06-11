package com.xiangxing.controller.admin;

public class PageRequest {

	private int page = 1;
	private int rows = 20;

	public int getIndex() {
		return (page - 1) * rows;
	}

	public int getPage() {
		return page;
	}

	public void setPage(int page) {
		this.page = page;
	}

	public int getRows() {
		return rows;
	}

	public void setRows(int rows) {
		this.rows = rows;
	}

}
