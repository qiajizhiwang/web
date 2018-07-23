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

}
