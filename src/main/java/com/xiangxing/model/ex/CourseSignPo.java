package com.xiangxing.model.ex;

import com.xiangxing.model.CourseSign;

public class CourseSignPo extends CourseSign {

	private Long studentId;
	
	private Integer period=2;

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