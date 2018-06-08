package com.xiangxing.vo;

import java.util.List;
import java.util.Set;
import java.util.TreeSet;

public class MenuVo implements Comparable<MenuVo>{

	private Long id;

	private String name;

	private String url;

	private int no;
	
	private Set<MenuVo> children = new TreeSet<>();

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public Set<MenuVo> getChildren() {
		return children;
	}

	public void setChildren(Set<MenuVo> children) {
		this.children = children;
	}
	
	

	

	public int getNo() {
		return no;
	}

	public void setNo(int no) {
		this.no = no;
	}

	@Override
	public int compareTo(MenuVo o) {
		// TODO Auto-generated method stub
		return this.no < o.no?-1:1;
	}

}
