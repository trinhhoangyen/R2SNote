package com.example.r2snote.DTO;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class database {
    DatabaseReference database = FirebaseDatabase.getInstance().getReference();

}
