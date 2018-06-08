package com.xiangxing.model;

public class Exam {
    private Integer id;

    private String subjectId;

    private String rank;

    private Long money;

    private String examTime;

    private String openFlag;

    private byte[] examAddress;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(String subjectId) {
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