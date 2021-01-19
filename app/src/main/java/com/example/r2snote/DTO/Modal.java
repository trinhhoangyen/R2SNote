package com.example.r2snote.DTO;

import com.example.r2snote.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Date;

public class Modal {
    DatabaseReference database = FirebaseDatabase.getInstance().getReference();
    private String name;
    private String id;
    private Date createDate;
    public Modal() {
    }

    public Modal(String name, Date createDate) {
        this.name = name;
        this.createDate = createDate;
    }

    public void setId(String id) {
        this.id = id;
    }
    public Date getCreateDate() {
        return createDate;
    }
    public String getName() {
        return name;
    }
    public String getId() {
        return id;
    }
}

