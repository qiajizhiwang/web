package com.xiangxing.model.ex;

import com.xiangxing.model.Exam;

public class ExamEx extends Exam {

	private String subjectName;
	private String showEndTime;
	private String showOpenFlag;
	private String showStatus;

	public String getShowStatus() {
		return showStatus;
	}

	public void setShowStatus(String showStatus) {
		this.showStatus = showStatus;
	}

	public String getShowOpenFlag() {
		return showOpenFlag;
	}

	public void setShowOpenFlag(String showOpenFlag) {
		this.showOpenFlag = showOpenFlag;
	}

	public String getShowEndTime() {
		return showEndTime;
	}

	public void setShowEndTime(String showEndTime) {
		this.showEndTime = showEndTime;
	}

	public String getSubjectName() {
		return subjectName;
	}

	public void setSubjectName(String subjectName) {
		this.subjectName = subjectName;
	}

}
