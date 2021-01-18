package com.example.r2snote.ui.category;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.fragment.app.Fragment;

import com.example.r2snote.DTO.Category;
import com.example.r2snote.DTO.Note;
import com.example.r2snote.DTO.Priority;
import com.example.r2snote.DTO.Status;
import com.example.r2snote.DTO.User;
import com.example.r2snote.MainActivity;
import com.example.r2snote.R;
import com.example.r2snote.ui.Login;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

public class CategoriesFragment extends Fragment {
    private MainActivity mainActivity = (MainActivity) getActivity();
    private User user;

    DatabaseReference database = FirebaseDatabase.getInstance().getReference();
    ArrayList<Category> listCategory;
    CategoryListViewAdapter categoryListViewAdapter;
    ListView listViewCategory;
    private Button btnPopupCategory;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_categories, container, false);

        mainActivity = (MainActivity) getActivity();
        user = mainActivity.getUser();

        listViewCategory = (ListView) root.findViewById(R.id.listViewCategory);
        btnPopupCategory = (Button) root.findViewById(R.id.btnPopupCategory);
        getListCategory();

//        CheckCategory("", view);
        btnPopupCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPopupNote(view);
            }
        });
        return root;
    }

    public void showPopupNote(View view){
        View popupView = getLayoutInflater().inflate(R.layout.popup_add, null);
        PopupWindow popupWindow = new PopupWindow(popupView, 800, 300, true);
        popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);

        EditText edtNameAdd = (EditText) popupView.findViewById(R.id.edtNameAdd);
        Button btnAdd = popupView.findViewById(R.id.btnAdd);

        btnAdd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String name = edtNameAdd.getText().toString();
                    if (name != null) {
                        CheckCategory(name, view);
                        popupWindow.dismiss();
                    }
                    else {
                        Toast.makeText(mainActivity.getApplicationContext(), "Add was not successful", Toast.LENGTH_SHORT).show();
                    }
                }
            });
    }

    public void getListCategory(){
        listCategory  = new ArrayList<Category>();
        database.child("categories").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot ds : dataSnapshot.getChildren()) {
                    Category category = ds.getValue(Category.class);
                    listCategory.add(category);
                }
            if (listCategory.size() > 0){
                categoryListViewAdapter = new CategoryListViewAdapter(listCategory);
                listViewCategory.setAdapter(categoryListViewAdapter);
            }
            }

        @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(mainActivity.getApplicationContext(), error.toString(), Toast.LENGTH_SHORT).show();

            }
        });
    }

    public void addCategory(String name){
        try {
            Date createDate = new Date();
            Category category = new Category(name,createDate);
                UUID uuid = UUID.randomUUID();
                database.child("categories").child(uuid.toString()).setValue(category);
                listCategory.clear();
                Toast.makeText(mainActivity.getApplicationContext(), "Add successfully", Toast.LENGTH_SHORT).show();
        }
        catch (Exception err){
            Toast.makeText(mainActivity.getApplicationContext(), "Add was not successful: " +err.toString(), Toast.LENGTH_SHORT).show();
        }
    }

    public void CheckCategory(String name, View v){
        database.child("categories").addValueEventListener(new ValueEventListener() {
            Boolean result = new Boolean(true);
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot ds : dataSnapshot.getChildren()) {
                    Category category = ds.getValue(Category.class);
                    if(category.getName().equals(name)) {
                        result = false;
                    }
                }
                if (result){
                    addCategory(name);
                }
                else {
                    Toast.makeText(mainActivity.getApplicationContext(), "Add was not successful!", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onCancelled(DatabaseError error) {
                Toast.makeText(mainActivity.getApplicationContext(), error.toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }

}

class CategoryListViewAdapter extends BaseAdapter {

    final ArrayList<Category> listCate;

    CategoryListViewAdapter(ArrayList<Category> listCate) {
        this.listCate = listCate;
    }

    @Override
    public int getCount() {
        return listCate.size();
    }

    @Override
    public Object getItem(int position) {
        return listCate.get(position);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    public Category getCate(int position) {
        return listCate.get(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LinearLayoutCompat viewCategory;
        if (convertView == null) {
            viewCategory = (LinearLayoutCompat) View.inflate(parent.getContext(), R.layout.activity_category_item, null);
        } else viewCategory = (LinearLayoutCompat) convertView;

        Category cate = (Category) getItem(position);

        ((TextView) viewCategory.findViewById(R.id.txtNameCate)).setText(("Name: " +  cate.getName()));
        String cd = cate.getCreateDate().getDate() + "/" + (cate.getCreateDate().getMonth()+1) + "/"
                + (cate.getCreateDate().getYear() + 1900);
        ((TextView) viewCategory.findViewById(R.id.txtCreateDateCate)).setText(String.format("Create date: %s", cd));
        return viewCategory;
    }
}