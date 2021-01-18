package com.example.r2snote.ui.prioity;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.fragment.app.Fragment;

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

import com.example.r2snote.DTO.Priority;
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

public class PriorityFragment extends Fragment {
    private MainActivity mainActivity = (MainActivity) getActivity();

    DatabaseReference database = FirebaseDatabase.getInstance().getReference();
    ArrayList<Priority> listPriority;
    PriorityListViewAdapter priorityListViewAdapter;
    ListView listViewPriority;
    private Button btnShowPopup;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_list, container, false);

        listViewPriority = (ListView) root.findViewById(R.id.listItem);
        btnShowPopup = (Button) root.findViewById(R.id.btnShowPopup);
        getList();

        btnShowPopup.setOnClickListener(new View.OnClickListener() {
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
        listPriority  = new ArrayList<>();
        database.child("priority").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot ds : dataSnapshot.getChildren()) {
                    Priority item = ds.getValue(Priority.class);
                    listPriority.add(item);
                }
                if (listPriority.size() > 0){
                    priorityListViewAdapter = new PriorityListViewAdapter(listPriority);
                    listViewPriority.setAdapter(priorityListViewAdapter);
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
            Priority item = new Priority(name,createDate);
            UUID uuid = UUID.randomUUID();
            database.child("priority").child(uuid.toString()).setValue(item);
            listPriority.clear();
            Toast.makeText(getActivity(), "Add successfully", Toast.LENGTH_SHORT).show();
        }
        catch (Exception err){
            Toast.makeText(getActivity(), "Add was not successful: " + err.toString(), Toast.LENGTH_SHORT).show();
        }
    }

    public void check(String name, View v){
        database.child("priority").addValueEventListener(new ValueEventListener() {
            Boolean result = new Boolean(true);
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot ds : dataSnapshot.getChildren()) {
                    Priority item = ds.getValue(Priority.class);
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


class PriorityListViewAdapter extends BaseAdapter {

    final ArrayList<Priority> listPriority;

    PriorityListViewAdapter(ArrayList<Priority> listPriority) {
        this.listPriority = listPriority;
    }

    @Override
    public int getCount() {
        return listPriority.size();
    }

    @Override
    public Object getItem(int position) {
        return listPriority.get(position);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    public Priority getPriority(int position) {
        return listPriority.get(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LinearLayoutCompat view;
        if (convertView == null) {
            view = (LinearLayoutCompat) View.inflate(parent.getContext(), R.layout.activity_list_item, null);
        } else view = (LinearLayoutCompat) convertView;

        Priority item = (Priority) getItem(position);
        ((TextView) view.findViewById(R.id.txtNameCate)).setText(("Name: " +  item.getName()));
        String cd = item.getCreateDate().getDate() + "/" + (item.getCreateDate().getMonth()+1) + "/"
                + (item.getCreateDate().getYear() + 1900);
        ((TextView) view.findViewById(R.id.txtCreateDateCate)).setText(String.format("Create date: %s", cd));
        return view;
    }
}