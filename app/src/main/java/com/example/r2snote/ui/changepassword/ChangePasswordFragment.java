package com.example.r2snote.ui.changepassword;

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

import com.example.r2snote.DTO.User;
import com.example.r2snote.MainActivity;
import com.example.r2snote.R;
import com.example.r2snote.ui.Login;
import com.example.r2snote.ui.changepassword.ChangePasswordViewModel;

public class ChangePasswordFragment extends Fragment{
    private ChangePasswordViewModel changePasswordViewModel;
    private MainActivity mainActivity;
    private TextView txt_tittle;
    private Button btnChange;
    private EditText edtCurrent, edtNewPass, edtNewPassAgain;
    private User user;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        changePasswordViewModel =
                new ViewModelProvider(this).get(ChangePasswordViewModel.class);
        View root = inflater.inflate(R.layout.fragment_change_password, container, false);
        mainActivity = (MainActivity) getActivity();
        txt_tittle = root.findViewById(R.id.txt_tittle);
        user = mainActivity.getUser();

        btnChange = root.findViewById(R.id.btn_change);
        btnChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edtCurrent = root.findViewById(R.id.edt_current);
                edtNewPass = root.findViewById(R.id.edt_newpass);
                edtNewPassAgain = root.findViewById(R.id.edt_newpassagain);
                String p = edtCurrent.getText().toString();
                String np = edtNewPass.getText().toString();
                String npa = edtNewPassAgain.getText().toString();
                if(!p.equals(user.getPassword())){
                    Toast.makeText(mainActivity.getApplicationContext(), "Mật khẩu cũ không đúng !", Toast.LENGTH_SHORT).show();
                }
                else {
                    if(!npa.equals(np) || !np.equals(npa) || np.equals("") || npa.equals("")){
                        Toast.makeText(mainActivity.getApplicationContext(), "Mật khẩu mới không khớp !", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        Toast.makeText(mainActivity.getApplicationContext(), "Đổi mật khẩu thành công !", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
        return root;
    }
}
