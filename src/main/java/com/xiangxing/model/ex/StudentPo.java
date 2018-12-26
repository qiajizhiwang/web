package com.xiangxing.model.ex;

import com.xiangxing.model.Student;

public class StudentPo extends Student {

	private String schoolName;
	
	private String showBirthday;
	
	private String courseName;
	
	private Integer allCount;
	private Integer sendCount;
	private Integer usedCount;
	private Integer remainCount;

	public String getShowBirthday() {
		return showBirthday;
	}

	public void setShowBirthday(String showBirthday) {
		this.showBirthday = showBirthday;
	}

	public String getSchoolName() {
		return schoolName;
	}

	public void setSchoolName(String schoolName) {
		this.schoolName = schoolName;
	}

	public String getCourseName() {
		return courseName;
	}

	public void setCourseName(String courseName) {
		this.courseName = courseName;
	}

	public Integer getAllCount() {
		return allCount;
	}

	public void setAllCount(Integer allCount) {
		this.allCount = allCount;
	}

	public Integer getSendCount() {
		return sendCount;
	}

	public void setSendCount(Integer sendCount) {
		this.sendCount = sendCount;
	}

	public Integer getUsedCount() {
		return usedCount;
	}

	public void setUsedCount(Integer usedCount) {
		this.usedCount = usedCount;
	}

	public Integer getRemainCount() {
		return remainCount;
	}

	public void setRemainCount(Integer remainCount) {
		this.remainCount = remainCount;
	}
	
	

}
