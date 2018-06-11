package com.xiangxing.model;

public class Product {
    private Long id;

    private Long studentCourseId;

    private String 作品名称;

    private String productUrl;

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

    public String get作品名称() {
        return 作品名称;
    }

    public void set作品名称(String 作品名称) {
        this.作品名称 = 作品名称;
    }

    public String getProductUrl() {
        return productUrl;
    }

    public void setProductUrl(String productUrl) {
        this.productUrl = productUrl;
    }
}