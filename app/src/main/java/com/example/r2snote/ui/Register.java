package com.example.r2snote.ui;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import com.example.r2snote.DTO.Note;
import com.example.r2snote.DTO.User;
import com.example.r2snote.MainActivity;
import com.example.r2snote.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Date;
import java.util.UUID;

public class Register extends AppCompatActivity {
    DatabaseReference database = FirebaseDatabase.getInstance().getReference();
    private TextView txtLogin;
    private Button btnRegister;
    private EditText edtNewUsername, edtNewPassword, edtConfirmPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        txtLogin = (TextView) findViewById(R.id.txtLogin);
        btnRegister = (Button) findViewById(R.id.btnRegister);
        edtNewUsername = (EditText) findViewById(R.id.edtNewUsername);
        edtNewPassword = (EditText) findViewById(R.id.edtNewPassword);
        edtConfirmPassword = (EditText) findViewById(R.id.edtConfirmPassword);

        txtLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), Login.class);
                startActivity(intent);
            }
        });
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = edtNewUsername.getText().toString(),
                        password= edtNewPassword.getText().toString(),
                        confirmPassword= edtConfirmPassword.getText().toString();

                if (username.equals("") || password.equals("") || !password.equals(confirmPassword)){
                    Toast.makeText(getApplicationContext(), "Register was not successful!", Toast.LENGTH_SHORT).show();
                }
                else {
                    CheckUser(username, password, view);
                }
            }
        });

    }

    public void Register(String user, String pass, View v){
        try {
            User u = new User(user, pass);
                UUID uuid = UUID.randomUUID();
                database.child("users").child(uuid.toString()).setValue(u);
                Toast.makeText(getApplication(), "Register successfully", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(v.getContext(), Login.class);
                intent.putExtra("Username", u.getUsername());
                intent.putExtra("Password", u.getPassword());
                intent.putExtra("Id", u.getId());
                startActivity(intent);
        }
        catch (Exception err){
            Toast.makeText(getApplication(), "Register was not successful: " + err.toString(), Toast.LENGTH_SHORT).show();
        }
    }
    public void CheckUser(String user, String pass, View v){
        database.child("users").addValueEventListener(new ValueEventListener() {
            Boolean result = new Boolean(true);
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot ds : dataSnapshot.getChildren()) {
                    User u = ds.getValue(User.class);
                    if(u.getUsername().equals(user)) {
                        result = false;
                    }
                }
                if (result){
                    Register(user, pass, v);
                }
                else {
                    Toast.makeText(getApplicationContext(), "Register was not successful!", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onCancelled(DatabaseError error) {
                Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void sendDataToFragment(String username, String pass){
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
    }
}