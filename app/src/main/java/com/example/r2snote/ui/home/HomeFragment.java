package com.example.r2snote.ui.home;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.anychart.AnyChart;
import com.anychart.AnyChartView;
import com.anychart.chart.common.dataentry.DataEntry;
import com.anychart.chart.common.dataentry.ValueDataEntry;
import com.anychart.charts.Pie;
import com.example.r2snote.R;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    AnyChartView anyChartView;

    String[] months = {"Jan", "Feb", "Mar"};
    int[] earnings = {500,800,2000};

    private HomeViewModel homeViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);

        Log.d("huy", "aloooooooooooooooooooooooooooooooooo");
        anyChartView = (AnyChartView) root.findViewById(R.id.any_chart_view);

        setupPieChart();

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
}