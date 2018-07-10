package com.xiangxing.model.ex;

import com.xiangxing.model.EntryForm;

public class EntryFormPo extends EntryForm {
	private String entryTime;
	private String subjectName;
	private String rank;
	private String studentName;
	private String payFlag;
	private String payTime;
	private Long entryFormId;
	private Long entryFee;

	public Long getEntryFormId() {
		return entryFormId;
	}

	public void setEntryFormId(Long entryFormId) {
		this.entryFormId = entryFormId;
	}

	public Long getEntryFee() {
		return entryFee;
	}

	public void setEntryFee(Long entryFee) {
		this.entryFee = entryFee;
	}

	public String getEntryTime() {
		return entryTime;
	}

	public void setEntryTime(String entryTime) {
		this.entryTime = entryTime;
	}

	public String getSubjectName() {
		return subjectName;
	}

	public void setSubjectName(String subjectName) {
		this.subjectName = subjectName;
	}

	public String getRank() {
		return rank;
	}

	public void setRank(String rank) {
		this.rank = rank;
	}

	public String getStudentName() {
		return studentName;
	}

	public void setStudentName(String studentName) {
		this.studentName = studentName;
	}

	public String getPayFlag() {
		return payFlag;
	}

	public void setPayFlag(String payFlag) {
		this.payFlag = payFlag;
	}

	public String getPayTime() {
		return payTime;
	}

	public void setPayTime(String payTime) {
		this.payTime = payTime;
	}

}