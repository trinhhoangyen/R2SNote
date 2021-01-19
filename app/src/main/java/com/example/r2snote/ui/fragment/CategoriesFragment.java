package com.example.r2snote.ui.fragment;

import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.appcompat.widget.PopupMenu;
import androidx.fragment.app.Fragment;

import com.example.r2snote.DTO.Category;
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
    DatabaseReference database = FirebaseDatabase.getInstance().getReference();
    ArrayList<Category> list = new ArrayList<>();
    CategoryListViewAdapter listViewAdapter;
    ListView listView;
    private Button btnShowPopup;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_list, container, false);

        listView = (ListView) root.findViewById(R.id.listItem);
        btnShowPopup = (Button) root.findViewById(R.id.btnShowPopup);

//        getList();

        Category c1 = new Category("abc", new Date()),
                c2 = new Category("abc", new Date()),
                c3 = new Category("abc", new Date()); c1.setId("1"); c2.setId("2"); c3.setId("3");
        list.add(c1); list.add(c2); list.add(c3); listViewAdapter = new CategoryListViewAdapter(list);
        listView.setAdapter(listViewAdapter);

        btnShowPopup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPopup(view, new Category(), 0);
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                showPopupMenu(view, listViewAdapter.getItem(i));
            }
        });

        return root;
    }

    private void showPopupMenu(View view, Category category){
        PopupMenu popupMenu = new PopupMenu(view.getContext(), view);
        popupMenu.inflate(R.menu.popup_edit_delete);
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()){
                    case R.id.action_popup_edit:
                        showPopup(view, category,1);
                        return true;
                    case R.id.action_popup_delete:
                        deleteItem(category.getId());
                        return true;
                    default:
                        return false;
                }
            }
        });
        popupMenu.show();
    }

    public void showPopup(View view, Category category, int type){
        View popupView = getLayoutInflater().inflate(R.layout.popup_add, null);
        PopupWindow popupWindow = new PopupWindow(popupView, 800, 300, true);
        popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);

        EditText edtNameAdd = popupView.findViewById(R.id.edtNameAdd);
        Button btnAdd = popupView.findViewById(R.id.btnAdd);

        if (type == 1){
            edtNameAdd.setText(category.getName());
            btnAdd.setText("EDIT");
            btnAdd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String name = edtNameAdd.getText().toString();
                    if (name != null) {
                        Category temp = new Category(category.getName(), category.getCreateDate());
                        checkUpdate(category.getId(), temp, view);
                        popupWindow.dismiss();
                    }
                    else {
                        Toast.makeText(getActivity(), "Edit was not successful", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
        else{
            btnAdd.setText("ADD");
            btnAdd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String name = edtNameAdd.getText().toString();
                    if (name != null) {
                        checkAdd(name, view);
                        popupWindow.dismiss();
                    }
                    else {
                        Toast.makeText(getActivity(), "Add was not successful", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    public void getList(){
        database.child("categories").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot ds : dataSnapshot.getChildren()) {
                    Category item = ds.getValue(Category.class);
                    item.setId(ds.getKey());
                    list.add(item);
                }
                if (list.size() > 0){
                    listViewAdapter = new CategoryListViewAdapter(list);
                    listView.setAdapter(listViewAdapter);
                }
            }

        @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getActivity(), error.toString(), Toast.LENGTH_SHORT).show();

            }
        });
    }

    public void checkAdd(String name, View v){
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
                if (result){ try {
                    Date createDate = new Date();
                    Category item = new Category(name,createDate);
                    UUID uuid = UUID.randomUUID();
                    database.child("categories").child(uuid.toString()).setValue(item);
                    list.clear();
                    Toast.makeText(getActivity(), "Add successfully", Toast.LENGTH_SHORT).show();
                }
                catch (Exception err){
                    Toast.makeText(getActivity(), "Add was not successful: " +err.toString(), Toast.LENGTH_SHORT).show();
                }
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

    public void checkUpdate(String id, Category category, View v){
        database.child("categories").addValueEventListener(new ValueEventListener() {
            Boolean result = new Boolean(true);
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot ds : dataSnapshot.getChildren()) {
                    Category item = ds.getValue(Category.class);
                    if(item.getName().equals(category.getName())) {
                        result = false;
                    }
                }
                if (result){
                    try {
                        database.child("categories").child(id).setValue(category);
                        list.clear();
                        Toast.makeText(getActivity(), "Edit successfully", Toast.LENGTH_SHORT).show();
                    }
                    catch (Exception err){
                        Toast.makeText(getActivity(), "Edit was not successful: " +err.toString(), Toast.LENGTH_SHORT).show();
                    }
                }
                else {
                    Toast.makeText(getActivity(), "Edit was not successful!", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onCancelled(DatabaseError error) {
                Toast.makeText(getActivity(), error.toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void deleteItem(String id){
        try {
            database.child("categories").child(id).removeValue();
            list.clear();
            Toast.makeText(getActivity(), "Delete successfully", Toast.LENGTH_SHORT).show();
        }
        catch (Exception err){
            Toast.makeText(getActivity(), "Delete was not successful: " + err.toString(), Toast.LENGTH_SHORT).show();
        }
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
    public Category getItem(int position) {
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