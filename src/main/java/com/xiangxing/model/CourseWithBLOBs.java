package com.xiangxing.model;

public class CourseWithBLOBs extends Course {
    private byte[] name;

    private byte[] teacherId;

    private byte[] schoolTime;

    private byte[] imageUrl;

    private byte[] comment;

    public byte[] getName() {
        return name;
    }

    public void setName(byte[] name) {
        this.name = name;
    }

    public byte[] getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(byte[] teacherId) {
        this.teacherId = teacherId;
    }

    public byte[] getSchoolTime() {
        return schoolTime;
    }

    public void setSchoolTime(byte[] schoolTime) {
        this.schoolTime = schoolTime;
    }

    public byte[] getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(byte[] imageUrl) {
        this.imageUrl = imageUrl;
    }

    public byte[] getComment() {
        return comment;
    }

    public void setComment(byte[] comment) {
        this.comment = comment;
    }
}