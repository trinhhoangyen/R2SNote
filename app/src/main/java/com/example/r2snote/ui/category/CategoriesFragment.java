package com.example.r2snote.ui.category;

import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.fragment.app.Fragment;

import com.example.r2snote.DTO.Category;
import com.example.r2snote.MainActivity;
import com.example.r2snote.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;

public class CategoriesFragment extends Fragment {
    private MainActivity mainActivity = (MainActivity) getActivity();

    DatabaseReference database = FirebaseDatabase.getInstance().getReference();
    ArrayList<Category> listCategory;
    CategoryListViewAdapter categoryListViewAdapter;
    ListView listViewCategory;
    private Button btnPopupCategory;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_list, container, false);

        listViewCategory = (ListView) root.findViewById(R.id.listItem);
        btnPopupCategory = (Button) root.findViewById(R.id.btnShowPopup);
        getList();

        btnPopupCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPopup(view);
            }
        });

        return root;
    }

    public void showPopup(View view){
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
                        check(name, view);
                        popupWindow.dismiss();
                    }
                    else {
                        Toast.makeText(getActivity(), "Add was not successful", Toast.LENGTH_SHORT).show();
                    }
                }
            });
    }

    public void getList(){
        listCategory  = new ArrayList<>();
        database.child("categories").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot ds : dataSnapshot.getChildren()) {
                    Category item = ds.getValue(Category.class);
                    listCategory.add(item);
                }
            if (listCategory.size() > 0){
                categoryListViewAdapter = new CategoryListViewAdapter(listCategory);
                listViewCategory.setAdapter(categoryListViewAdapter);
            }
            }

        @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getActivity(), error.toString(), Toast.LENGTH_SHORT).show();

            }
        });
    }

    public void add(String name){
        try {
            Date createDate = new Date();
            Category item = new Category(name,createDate);
                UUID uuid = UUID.randomUUID();
                database.child("categories").child(uuid.toString()).setValue(item);
                listCategory.clear();
                Toast.makeText(getActivity(), "Add successfully", Toast.LENGTH_SHORT).show();
        }
        catch (Exception err){
            Toast.makeText(getActivity(), "Add was not successful: " +err.toString(), Toast.LENGTH_SHORT).show();
        }
    }

    public void check(String name, View v){
        database.child("categories").addValueEventListener(new ValueEventListener() {
            Boolean result = new Boolean(true);
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot ds : dataSnapshot.getChildren()) {
                    Category item = ds.getValue(Category.class);
                    if(item.getName().equals(name)) {
                        result = false;
                    }
                }
                if (result){
                    add(name);
                }
                else {
                    Toast.makeText(getActivity(), "Add was not successful!", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onCancelled(DatabaseError error) {
                Toast.makeText(getActivity(), error.toString(), Toast.LENGTH_SHORT).show();
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

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LinearLayoutCompat view;
        if (convertView == null) {
            view = (LinearLayoutCompat) View.inflate(parent.getContext(), R.layout.activity_list_item, null);
        } else view = (LinearLayoutCompat) convertView;

        Category item = (Category) getItem(position);

        ((TextView) view.findViewById(R.id.txtNameCate)).setText(("Name: " +  item.getName()));
        String cd = item.getCreateDate().getDate() + "/" + (item.getCreateDate().getMonth()+1) + "/"
                + (item.getCreateDate().getYear() + 1900);
        ((TextView) view.findViewById(R.id.txtCreateDateCate)).setText(String.format("Create date: %s", cd));
        return view;
    }
}