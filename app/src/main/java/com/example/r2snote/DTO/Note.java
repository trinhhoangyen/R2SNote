package com.example.r2snote.DTO;

import java.util.Date;

public class Note {

    private String id,name,category, userId;
    private Date planDate = new Date(), createDate = new Date();
    public Note() {

    }

    public Note(String userId, String name, String category, Date planDate, Date createDate) {
        this.name = name;
        this.userId = userId;
        this.category = category;
        this.planDate = planDate;
        this.createDate = createDate;
    }


    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getUserId() {
        return userId;
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
