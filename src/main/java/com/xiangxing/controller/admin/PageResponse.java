package com.xiangxing.controller.admin;

import java.util.List;

public class PageResponse<T> {

	private long total = 0;
	private List<T> rows;

	public PageResponse() {
	}

	public PageResponse(long total, List<T> rows) {
		this.total = total;
		this.rows = rows;
	}

	public List<T> getRows() {
		return rows;
	}

	public void setRows(List<T> rows) {
		this.rows = rows;
	}

	public long getTotal() {
		return total;
	}

	public void setTotal(long total) {
		this.total = total;
	}

}
