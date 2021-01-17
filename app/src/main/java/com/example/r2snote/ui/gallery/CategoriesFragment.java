package com.example.r2snote.ui.gallery;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
<<<<<<< HEAD
import android.widget.Button;
import android.widget.PopupMenu;
import android.widget.PopupWindow;
=======
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
>>>>>>> a14565c1f04b28a1a88ad54cfd5fed252ca3d144

import androidx.annotation.NonNull;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.fragment.app.Fragment;

import com.example.r2snote.DTO.Category;
import com.example.r2snote.DTO.User;
import com.example.r2snote.MainActivity;
import com.example.r2snote.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class CategoriesFragment extends Fragment {
<<<<<<< HEAD
    PopupWindow popUp;
    private CategoriesViewModel categoriesViewModel;
    private Button btnShow;
=======
    private MainActivity mainActivity = (MainActivity) getActivity();
    private User user;

    DatabaseReference database = FirebaseDatabase.getInstance().getReference();
    ArrayList<Category> listCategory;
    CategoryListViewAdapter categoryListViewAdapter;
    ListView listViewCategory;
>>>>>>> a14565c1f04b28a1a88ad54cfd5fed252ca3d144

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_categories, container, false);
        btnShow = (Button) root.findViewById(R.id.btn_show);

        mainActivity = (MainActivity) getActivity();
        user = mainActivity.getUser();

        listViewCategory = (ListView) root.findViewById(R.id.listViewCategory);

        getListCategory();

        return root;
    }
<<<<<<< HEAD
    private void ShowMenu(){

=======

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
>>>>>>> a14565c1f04b28a1a88ad54cfd5fed252ca3d144
    }
}