package com.example.r2snote.DTO;

import java.util.Date;

public class Note {
    private String name, category;


    private Date planDate, createDate;
    public Note() {

    }

    public Note(String name, String category, Date planDate, Date createDate) {
        this.name = name;
        this.category = category;
        this.planDate = planDate;
        this.createDate = createDate;
    }

    public Date getPlanDate() {
        return planDate;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public String getName() {
        return name;
    }

    public String getCategory() {
        return category;
    }

}
