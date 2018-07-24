package com.xiangxing.model.ex;

import com.xiangxing.model.Student;

public class StudentPo extends Student {

	private String schoolName;
	
	private String showBirthday;

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

}
