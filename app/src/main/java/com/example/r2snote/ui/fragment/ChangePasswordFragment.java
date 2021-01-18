package com.example.r2snote.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.r2snote.DTO.Note;
import com.example.r2snote.DTO.User;
import com.example.r2snote.MainActivity;
import com.example.r2snote.R;
import com.example.r2snote.ui.Login;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ChangePasswordFragment extends Fragment{
    private MainActivity mainActivity;
    private TextView txt_tittle;
    private Button btnChange;
    private EditText edtCurrent, edtNewPass, edtNewPassAgain;
    private User user;

    DatabaseReference database = FirebaseDatabase.getInstance().getReference();

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_change_password, container, false);
        mainActivity = (MainActivity) getActivity();
        user = mainActivity.getUser();

        edtCurrent = root.findViewById(R.id.edt_current);
        edtNewPass = root.findViewById(R.id.edt_newpass);
        edtNewPassAgain = root.findViewById(R.id.edt_newpassagain);

        btnChange = root.findViewById(R.id.btn_change);
        btnChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String p = edtCurrent.getText().toString();
                String np = edtNewPass.getText().toString();
                String npa = edtNewPassAgain.getText().toString();

                if (!p.equals("") || !np.equals("")) {
                    if (!p.equals(user.getPassword())) {
                        Toast.makeText(mainActivity.getApplicationContext(), "Wrong password!", Toast.LENGTH_SHORT).show();
                    } else {
                        if (!npa.equals(np)) {
                            Toast.makeText(mainActivity.getApplicationContext(), "New password doesn't match!", Toast.LENGTH_SHORT).show();
                        } else {
                            User temp = new User(user.getUsername(), np);
                            changePassword(user.getId(), temp, v);
                        }
                    }
                }
                else {
                    Toast.makeText(mainActivity.getApplicationContext(),
                            "Password change was not successful!", Toast.LENGTH_SHORT).show();
                }
            }
        });
        return root;
    }

    public void changePassword(String id, User u, View v){
        try {
            database.child("users").child(id).setValue(u);
            Toast.makeText(mainActivity.getApplicationContext(), "Change password successfully", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(v.getContext(), Login.class);
            intent.putExtra("Username", u.getUsername());
            startActivity(intent);
        }
        catch (Exception err){
            Toast.makeText(mainActivity.getApplicationContext(), "Password change was not successful: " + err.toString(), Toast.LENGTH_SHORT).show();
        }
    }
}
