package com.example.r2snote.ui.fragment;

import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.PopupMenu;
import androidx.fragment.app.Fragment;

import com.example.r2snote.DTO.ListViewAdapter;
import com.example.r2snote.DTO.Modal;
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
    ArrayList<Modal> list = new ArrayList<>();
    ListViewAdapter listViewAdapter;
    ListView listView;
    Button btnShowPopup;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_list, container, false);

        listView = root.findViewById(R.id.listItem);
        btnShowPopup = root.findViewById(R.id.btnShowPopup);

        getList();

        btnShowPopup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPopup(view, new Modal(), 0);
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

    private void showPopupMenu(View view, Modal category){
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

    public void showPopup(View view, Modal category, int type){
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
                        Modal temp = new Modal(name, category.getCreateDate());
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
                    Modal item = ds.getValue(Modal.class);
                    item.setId(ds.getKey());
                    list.add(item);
                }
                if (list.size() > 0){
                    listViewAdapter = new ListViewAdapter(list);
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
                    Modal item = ds.getValue(Modal.class);
                    if(item.getName().equals(name)) {
                        result = false;
                    }
                }
                if (result){ try {
                    Date createDate = new Date();
                    Modal item = new Modal(name,createDate);
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

    public void checkUpdate(String id, Modal modal, View v){
        database.child("categories").addValueEventListener(new ValueEventListener() {
            Boolean result = new Boolean(true);
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot ds : dataSnapshot.getChildren()) {
                    Modal item = ds.getValue(Modal.class);
                    if(item.getName().equals(modal.getName())) {
                        result = false;
                    }
                }
                if (result){
                    try {
                        database.child("categories").child(id).setValue(modal);
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