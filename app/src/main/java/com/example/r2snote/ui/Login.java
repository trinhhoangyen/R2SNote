package com.example.r2snote.ui;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.r2snote.DTO.User;
import com.example.r2snote.MainActivity;
import com.example.r2snote.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Login extends AppCompatActivity {
    DatabaseReference database = FirebaseDatabase.getInstance().getReference();
    private EditText edtUsername, edtPassword;
    private Button btnLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        edtUsername = (EditText) findViewById(R.id.edtUsername);
        edtPassword = (EditText) findViewById(R.id.edtPassword);
        btnLogin = (Button) findViewById(R.id.btnLogin);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("aaaa", "aa");
                String username , password;
                username = edtUsername.getText().toString();
                password = edtPassword.getText().toString();
                Login(username, password, v);
            }
        });
    }

    public void Login(String user, String pass, View v){
        // Login
        database.child("users").addValueEventListener(new ValueEventListener() {
            Boolean result = new Boolean(false);

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot ds : dataSnapshot.getChildren()) {
                    User u = ds.getValue(User.class);
                    if(u.getUsername().equals(user) && u.getPassword().equals(pass)) {
                        result = true;
                        Toast.makeText(getApplicationContext(), "Đăng nhập thành công!!!", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(v.getContext(), MainActivity.class);
                        intent.putExtra("Username", user);
                        intent.putExtra("Password", pass);
                        startActivity(intent);
                    }
                }
            }
            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
            }
        });
    }
}