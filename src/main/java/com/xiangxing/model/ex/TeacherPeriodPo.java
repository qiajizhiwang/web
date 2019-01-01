package com.xiangxing.model.ex;

import com.xiangxing.model.TeacherPeriod;

public class TeacherPeriodPo extends TeacherPeriod {

	private int weekPeriod;// 周课时
	private int thisMonthPeriod;// 本月
	private int lastMonthPeriod;// 上月
	private int surplusPeriod;// 剩余
	private String teacherName;
	private String schoolName;

	public String getSchoolName() {
		return schoolName;
	}

	public void setSchoolName(String schoolName) {
		this.schoolName = schoolName;
	}

	public int getSurplusPeriod() {
		return surplusPeriod;
	}

	public void setSurplusPeriod(int surplusPeriod) {
		this.surplusPeriod = surplusPeriod;
	}

	public int getWeekPeriod() {
		return weekPeriod;
	}

	public void setWeekPeriod(int weekPeriod) {
		this.weekPeriod = weekPeriod;
	}

	public int getThisMonthPeriod() {
		return thisMonthPeriod;
	}

	public void setThisMonthPeriod(int thisMonthPeriod) {
		this.thisMonthPeriod = thisMonthPeriod;
	}

	public int getLastMonthPeriod() {
		return lastMonthPeriod;
	}

	public void setLastMonthPeriod(int lastMonthPeriod) {
		this.lastMonthPeriod = lastMonthPeriod;
	}

	public String getTeacherName() {
		return teacherName;
	}

	public void setTeacherName(String teacherName) {
		this.teacherName = teacherName;
	}

}
