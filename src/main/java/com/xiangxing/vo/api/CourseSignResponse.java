package com.xiangxing.vo.api;

import java.util.List;

import com.xiangxing.model.ex.CourseSignPo;

public class CourseSignResponse extends ApiResponse {

	private List<CourseSignPo> courseSignPos;

	public List<CourseSignPo> getCourseSignPos() {
		return courseSignPos;
	}

	public void setCourseSignPos(List<CourseSignPo> courseSignPos) {
		this.courseSignPos = courseSignPos;
	}

}
