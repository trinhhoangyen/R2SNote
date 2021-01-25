package com.example.r2snote.ui;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.anychart.AnyChart;
import com.anychart.AnyChartView;
import com.anychart.chart.common.dataentry.DataEntry;
import com.anychart.chart.common.dataentry.ValueDataEntry;
import com.anychart.charts.Pie;

import com.example.r2snote.DTO.Modal;
import com.example.r2snote.DTO.Note;
import com.example.r2snote.DTO.User;
import com.example.r2snote.MainActivity;
import com.example.r2snote.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class test extends Fragment {
    private MainActivity mainActivity;
    DatabaseReference database = FirebaseDatabase.getInstance().getReference();
    private User user;

    AnyChartView anyChartView;
    ArrayList<Note> listNote;
    ArrayList<ItemChart> listStatus;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        anyChartView = root.findViewById(R.id.any_chart_view);
        mainActivity = (MainActivity) getActivity();
        user = mainActivity.getUser();

        getListStatus();
        getListNote();
        count();
        setupPieChart();

        return root;
    }

    public void setupPieChart(){
        Pie pie = AnyChart.pie();

        List<DataEntry> dataEntries = new ArrayList<>();

        for(ItemChart itemChart : listStatus){
            dataEntries.add(new ValueDataEntry(itemChart.getName(), itemChart.getCount()));
        }

        pie.data(dataEntries);
        anyChartView.setChart(pie);
    }

    public void count(){
        for (ItemChart itemChart: listStatus){
            for (Note note: listNote)
                if (note.getStatus().equals(itemChart.getId())) {
                    itemChart.setCount(itemChart.getCount() + 1);
                    Log.e("---------", itemChart.getCount() + "");
                }
        }
    }

    public void getListNote(){
        listNote  = new ArrayList<>();
        database.child("notes").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot ds : dataSnapshot.getChildren()) {
                    Note note = ds.getValue(Note.class);
                    if(note.getUserId().equals(user.getId())){
                        note.setId(ds.getKey());
                        listNote.add(note);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Toast.makeText(mainActivity.getApplicationContext(), error.toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void getListStatus(){
        listStatus = new ArrayList<>();
        database.child("status").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot ds : dataSnapshot.getChildren()) {
                    Modal modal = ds.getValue(Modal.class);
                    listStatus.add(new ItemChart(ds.getKey(), modal.getName(), 0));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(mainActivity.getApplicationContext(), error.toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}

class ItemChart{
    String name, id;
    int count;

    public ItemChart(String id,String name, int count){
        this.id = id;
        this.name = name;
        this.count = count;
    }

    public String getName() {
        return name;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getId() {
        return id;
    }
}