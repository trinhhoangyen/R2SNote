package com.example.r2snote.DTO;

import java.util.Date;

public class Priority {
    private String name;
    private Date createDate;
    public Priority() {

    }

    public Priority(String name, Date createDate) {
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
