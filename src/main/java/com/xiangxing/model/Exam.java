package com.xiangxing.model;

public class Exam {
    private Long id;

    private Long schoolId;

    private Long subjectId;

    private String rank;

    private Long money;

    private String examTime;

    private String examAddress;

    private Long openFlag;

    private Long status;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getSchoolId() {
        return schoolId;
    }

    public void setSchoolId(Long schoolId) {
        this.schoolId = schoolId;
    }

    public Long getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(Long subjectId) {
        this.subjectId = subjectId;
    }

    public String getRank() {
        return rank;
    }

    public void setRank(String rank) {
        this.rank = rank;
    }

    public Long getMoney() {
        return money;
    }

    public void setMoney(Long money) {
        this.money = money;
    }

    public String getExamTime() {
        return examTime;
    }

    public void setExamTime(String examTime) {
        this.examTime = examTime;
    }

    public String getExamAddress() {
        return examAddress;
    }

    public void setExamAddress(String examAddress) {
        this.examAddress = examAddress;
    }

    public Long getOpenFlag() {
        return openFlag;
    }

    public void setOpenFlag(Long openFlag) {
        this.openFlag = openFlag;
    }

    public Long getStatus() {
        return status;
    }

    public void setStatus(Long status) {
        this.status = status;
    }
}