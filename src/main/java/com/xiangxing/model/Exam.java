package com.xiangxing.model;

public class Exam {
    private Long id;

    private Long subjectId;

    private String rank;

    private Long money;

    private String examTime;

    private String openFlag;

    private byte[] examAddress;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public String getOpenFlag() {
        return openFlag;
    }

    public void setOpenFlag(String openFlag) {
        this.openFlag = openFlag;
    }

    public byte[] getExamAddress() {
        return examAddress;
    }

    public void setExamAddress(byte[] examAddress) {
        this.examAddress = examAddress;
    }
}