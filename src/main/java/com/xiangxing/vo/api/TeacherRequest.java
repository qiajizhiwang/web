package com.xiangxing.vo.api;

import com.xiangxing.controller.admin.PageRequest;

public class TeacherRequest extends PageRequest {
	private Long courseId;
	private Long studentId;

	public Long getStudentId() {
		return studentId;
	}

	public void setStudentId(Long studentId) {
		this.studentId = studentId;
	}

	public Long getCourseId() {
		return courseId;
	}

	public void setCourseId(Long courseId) {
		this.courseId = courseId;
	}

}
