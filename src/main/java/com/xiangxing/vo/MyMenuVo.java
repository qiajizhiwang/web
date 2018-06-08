package com.xiangxing.vo;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class MyMenuVo implements Comparable<MyMenuVo>{

	private Long id;

	private String text;

	private boolean checked = true;
	
	private Map attributes = new HashMap();

	@JsonIgnore
	private int no;
	
	private Set<MyMenuVo> children = new TreeSet<>();

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	

	public Set<MyMenuVo> getChildren() {
		return children;
	}

	public void setChildren(Set<MyMenuVo> children) {
		this.children = children;
	}
	
	

	

	public int getNo() {
		return no;
	}

	public void setNo(int no) {
		this.no = no;
	}

	@Override
	public int compareTo(MyMenuVo o) {
		// TODO Auto-generated method stub
		return this.no < o.no?-1:1;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public boolean isChecked() {
		return checked;
	}

	public void setChecked(boolean checked) {
		this.checked = checked;
	}

	public Map getAttributes() {
		return attributes;
	}

	public void setAttributes(Map attributes) {
		this.attributes = attributes;
	}

}
