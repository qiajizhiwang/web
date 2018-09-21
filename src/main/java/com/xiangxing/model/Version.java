package com.xiangxing.model;

public class Version {
    private String version;

    private Integer type;

    private Integer must;

    private String address;

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getMust() {
        return must;
    }

    public void setMust(Integer must) {
        this.must = must;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}