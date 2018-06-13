package com.xiangxing.model.ex;

import com.xiangxing.model.Course;

public class CourseEx extends Course {
	private String teacherName;
	private String schoolName;
	private String showCurriculumTime;

	public String getTeacherName() {
		return teacherName;
	}

	public void setTeacherName(String teacherName) {
		this.teacherName = teacherName;
	}

	public String getSchoolName() {
		return schoolName;
	}

	public void setSchoolName(String schoolName) {
		this.schoolName = schoolName;
	}

	public String getShowCurriculumTime() {
		return showCurriculumTime;
	}

	public void setShowCurriculumTime(String showCurriculumTime) {
		this.showCurriculumTime = showCurriculumTime;
	}

}
