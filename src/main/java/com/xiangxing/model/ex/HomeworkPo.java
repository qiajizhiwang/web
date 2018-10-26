package com.xiangxing.model.ex;

import java.util.Date;

public class HomeworkPo {

	private Long id;

	private String name;

	private Long studentId;

	private Long homeworkId;

	private int status;

	private String courseName;

	private long courseId;

	private Date publishDate;

	private String teacherName;

	private int studentNumber;

	private int undoneNumber;

	private int unreviewedNumber;

	private String path;

	private String jobPath;

	private Integer layout;

	private Integer color;

	private Integer subject;

	private String remark;
	
	private String firstName;

	private String secondName;

	private String thirdName;

	public Integer getLayout() {
		return layout;
	}

	public void setLayout(Integer layout) {
		this.layout = layout;
	}

	public Integer getColor() {
		return color;
	}

	public void setColor(Integer color) {
		this.color = color;
	}

	public Integer getSubject() {
		return subject;
	}

	public void setSubject(Integer subject) {
		this.subject = subject;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public int getStudentNumber() {
		return studentNumber;
	}

	public void setStudentNumber(int studentNumber) {
		this.studentNumber = studentNumber;
	}

	public int getUndoneNumber() {
		return undoneNumber;
	}

	public void setUndoneNumber(int undoneNumber) {
		this.undoneNumber = undoneNumber;
	}

	public int getUnreviewedNumber() {
		return unreviewedNumber;
	}

	public void setUnreviewedNumber(int unreviewedNumber) {
		this.unreviewedNumber = unreviewedNumber;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Long getStudentId() {
		return studentId;
	}

	public void setStudentId(Long studentId) {
		this.studentId = studentId;
	}

	public Long getHomeworkId() {
		return homeworkId;
	}

	public void setHomeworkId(Long homeworkId) {
		this.homeworkId = homeworkId;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getCourseName() {
		return courseName;
	}

	public void setCourseName(String courseName) {
		this.courseName = courseName;
	}

	public long getCourseId() {
		return courseId;
	}

	public void setCourseId(long courseId) {
		this.courseId = courseId;
	}

	public Date getPublishDate() {
		return publishDate;
	}

	public void setPublishDate(Date publishDate) {
		this.publishDate = publishDate;
	}

	public String getTeacherName() {
		return teacherName;
	}

	public void setTeacherName(String teacherName) {
		this.teacherName = teacherName;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getJobPath() {
		return jobPath;
	}

	public void setJobPath(String jobPath) {
		this.jobPath = jobPath;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getSecondName() {
		return secondName;
	}

	public void setSecondName(String secondName) {
		this.secondName = secondName;
	}

	public String getThirdName() {
		return thirdName;
	}

	public void setThirdName(String thirdName) {
		this.thirdName = thirdName;
	}

}
