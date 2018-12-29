package com.xiangxing.model;

public class User {
    private Long id;

    private String name;

    private String password;

    private Integer type;

    private Long schoolId;

    private Integer status;

    private Long headquartersId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Long getSchoolId() {
        return schoolId;
    }

    public void setSchoolId(Long schoolId) {
        this.schoolId = schoolId;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Long getHeadquartersId() {
        return headquartersId;
    }

    public void setHeadquartersId(Long headquartersId) {
        this.headquartersId = headquartersId;
    }
}