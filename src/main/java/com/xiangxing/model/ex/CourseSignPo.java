package com.xiangxing.model.ex;

import com.xiangxing.model.CourseSign;

public class CourseSignPo extends CourseSign {

	private Long studentId;
	
	private Integer period=2;
	private String courseName;
	private String teacherName;
	private String schoolTime;
	private String showSignTime;
	
	public String getSchoolTime() {
		return schoolTime;
	}

	public void setSchoolTime(String schoolTime) {
		this.schoolTime = schoolTime;
	}

	public String getShowSignTime() {
		return showSignTime;
	}

	public void setShowSignTime(String showSignTime) {
		this.showSignTime = showSignTime;
	}

	public String getCourseName() {
		return courseName;
	}

	public void setCourseName(String courseName) {
		this.courseName = courseName;
	}

	public String getTeacherName() {
		return teacherName;
	}

	public void setTeacherName(String teacherName) {
		this.teacherName = teacherName;
	}

	public Integer getPeriod() {
		return period;
	}

	public void setPeriod(Integer period) {
		this.period = period;
	}

	public Long getStudentId() {
		return studentId;
	}

	public void setStudentId(Long studentId) {
		this.studentId = studentId;
	}

}