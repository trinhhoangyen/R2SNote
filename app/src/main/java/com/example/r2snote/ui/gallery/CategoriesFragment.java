package com.example.r2snote.ui.gallery;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.PopupMenu;
import android.widget.PopupWindow;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.r2snote.R;

public class CategoriesFragment extends Fragment {
    PopupWindow popUp;
    private CategoriesViewModel categoriesViewModel;
    private Button btnShow;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        categoriesViewModel =
                new ViewModelProvider(this).get(CategoriesViewModel.class);
        View root = inflater.inflate(R.layout.fragment_categories, container, false);
        btnShow = (Button) root.findViewById(R.id.btn_show);

        return root;
    }
    private void ShowMenu(){

    }
}