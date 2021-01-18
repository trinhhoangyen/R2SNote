package com.example.r2snote.ui.home;

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
import java.util.List;

public class HomeFragment extends Fragment {
    private MainActivity mainActivity;
    DatabaseReference database = FirebaseDatabase.getInstance().getReference();
    private User user;

    AnyChartView anyChartView;
    ArrayList<Note> listNote;

    String[] months = {"Jan", "Feb", "Mar"};
    int[] earnings = {500,800,2000};

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        anyChartView = (AnyChartView) root.findViewById(R.id.any_chart_view);
        mainActivity = (MainActivity) getActivity();
        user = mainActivity.getUser();
        setupPieChart();
        getListNote();

        return root;
    }

    public void setupPieChart(){
        Pie pie = AnyChart.pie();

        List<DataEntry> dataEntries = new ArrayList<>();

        for(int i = 0; i < months.length; i++){
            dataEntries.add(new ValueDataEntry(months[i], earnings[i]));
        }

        pie.data(dataEntries);
        anyChartView.setChart(pie);
    }

    public void getListNote(){
        listNote  = new ArrayList<>();
        database.child("notes").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot ds : dataSnapshot.getChildren()) {
                    Note note = ds.getValue(Note.class);
                    note.setId(ds.getKey());
                    if (note != null && !note.getUserId().equals("")) {
                        if (note.getUserId().equals(user.getId())) {
                            listNote.add(note);
                        }
                    }
                }
                for (Note n: listNote ) {
                    Log.e("----------", n.getName());
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Toast.makeText(mainActivity.getApplicationContext(), error.toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}