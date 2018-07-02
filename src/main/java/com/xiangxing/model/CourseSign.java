package com.xiangxing.model;

import java.util.Date;

public class CourseSign {
    private Long id;

    private Long studentCourseId;

    private Date signTime;

    private Long signFlag;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getStudentCourseId() {
        return studentCourseId;
    }

    public void setStudentCourseId(Long studentCourseId) {
        this.studentCourseId = studentCourseId;
    }

    public Date getSignTime() {
        return signTime;
    }

    public void setSignTime(Date signTime) {
        this.signTime = signTime;
    }

    public Long getSignFlag() {
        return signFlag;
    }

    public void setSignFlag(Long signFlag) {
        this.signFlag = signFlag;
    }
}