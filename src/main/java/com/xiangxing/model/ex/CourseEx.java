package com.xiangxing.model.ex;

import com.xiangxing.model.Course;

public class CourseEx extends Course {
	private String teacherName;
	private Long schoolId;
	private String schoolName;
	private String showCurriculumTime;
	private String showFinishTime;
	private Integer period;
	private Integer signPeriod;

	public String getShowFinishTime() {
		return showFinishTime;
	}

	public void setShowFinishTime(String showFinishTime) {
		this.showFinishTime = showFinishTime;
	}

	public Long getSchoolId() {
		return schoolId;
	}

	public void setSchoolId(Long schoolId) {
		this.schoolId = schoolId;
	}

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

	public Integer getPeriod() {
		return period;
	}

	public void setPeriod(Integer period) {
		this.period = period;
	}

	public Integer getSignPeriod() {
		return signPeriod;
	}

	public void setSignPeriod(Integer signPeriod) {
		this.signPeriod = signPeriod;
	}

}
