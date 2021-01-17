package com.example.r2snote.DTO;

import java.util.Date;

public class Category {
    private String name;
    private Date createDate;
    public Category() {

    }

    public Category(String name, Date createDate) {
        this.name = name;
        this.createDate = createDate;
    }


    public Date getCreateDate() {
        return createDate;
    }

    public String getName() {
        return name;
    }


}
