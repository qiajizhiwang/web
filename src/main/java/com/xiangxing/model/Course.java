package com.xiangxing.model;

import java.util.Date;

public class Course {
    private byte[] id;

    private String teaTeacherId;

    private Date curriculumTime;

    public byte[] getId() {
        return id;
    }

    public void setId(byte[] id) {
        this.id = id;
    }

    public String getTeaTeacherId() {
        return teaTeacherId;
    }

    public void setTeaTeacherId(String teaTeacherId) {
        this.teaTeacherId = teaTeacherId;
    }

    public Date getCurriculumTime() {
        return curriculumTime;
    }

    public void setCurriculumTime(Date curriculumTime) {
        this.curriculumTime = curriculumTime;
    }
}