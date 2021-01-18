package com.example.r2snote.DTO;

import java.util.Date;

public class Note {

    private String id,name,category, priority, status, userId;
    private Date planDate = new Date(), createDate = new Date();
    public Note() {

    }

    public Note(String userId, String name, String category,String status,String priority, Date planDate, Date createDate) {
        this.name = name;
        this.userId = userId;
        this.category = category;
        this.status = status;
        this.priority = priority;
        this.planDate = planDate;
        this.createDate = createDate;
    }

    public void setId(String id) {
        this.id = id;
    }
    public String getId() {
        return id;
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
    public String getPriority() {
        return priority;
    }
    public String getStatus() {
        return status;
    }
    public String getCategory() {
        return category;
    }

}
