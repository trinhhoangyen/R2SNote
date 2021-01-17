package com.example.r2snote.ui.slideshow;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.r2snote.R;

public class NoteFragment extends Fragment {

    private NoteViewModel noteViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        noteViewModel =
                new ViewModelProvider(this).get(NoteViewModel.class);
        View root = inflater.inflate(R.layout.fragment_note, container, false);

        Log.e("okla","okla");
        Bundle args = getArguments();
        if(args != null){
            TextView test = root.findViewById(R.id.txt_test);
            String data = args.getString("ms");
            Log.e("data", data);
        }

        //test.setText(args.getString("message"));

        return root;
    }
}