package com.example.r2snote.DTO;

import java.util.Date;

public class Status {
    private String name;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    private String id;
    private Date createDate;
    public Status() {

    }

    public Status(String name, Date createDate) {
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
