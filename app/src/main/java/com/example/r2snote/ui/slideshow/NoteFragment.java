package com.example.r2snote.ui.slideshow;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
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
import androidx.appcompat.widget.PopupMenu;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.r2snote.DTO.Category;
import com.example.r2snote.DTO.Note;
import com.example.r2snote.DTO.User;
import com.example.r2snote.MainActivity;
import com.example.r2snote.R;
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

public class NoteFragment extends Fragment {
    private MainActivity mainActivity;
    private User user;

    DatabaseReference database = FirebaseDatabase.getInstance().getReference();
    ArrayList<Note> listNote;
    ArrayList<Category> listCategory;

    NoteListViewAdapter noteListViewAdapter;
    ListView listViewNote;

    private Button btnShowPopup;

    String TAG = "Note";
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_note, container, false);

        mainActivity = (MainActivity) getActivity();
        user = mainActivity.getUser();

        listViewNote = (ListView) root.findViewById(R.id.listViewNote);

        btnShowPopup = (Button) root.findViewById(R.id.btnShowPopup);
        getListNote(user.getId());
        getListCategory();

        btnShowPopup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPopupNote(view, new Note(), 0);
            }
        });
        listViewNote.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Log.e(TAG, noteListViewAdapter.getNote(i).getId());
                showPopupMenu(view, i, noteListViewAdapter.getNote(i));
            }
            private void showPopupMenu(View view, int position, Note note){
                PopupMenu popupMenu = new PopupMenu(view.getContext(), view);
                popupMenu.inflate(R.menu.popup_edit_delete);
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()){
                            case R.id.action_popup_edit:
                                showPopupNote(view, note,1);
                                return true;
                            case R.id.action_popup_delete:
                                deleteNote(note.getId());
                                return true;
                            default:
                                return false;
                        }
                    }
                });
                popupMenu.show();
            }
        });

        return root;
    }

    public void showPopupNote(View view, Note note, int type){
        View popupView = getLayoutInflater().inflate(R.layout.popup_note, null);
        PopupWindow popupWindow = new PopupWindow(popupView, 800, 800, true);
        popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);

        EditText etdNewNoteName = (EditText) popupView.findViewById(R.id.etdNewNoteName);
        etdNewNoteName.setText(note.getName());

        Spinner spnCategory = popupView.findViewById(R.id.spnCategpry);

        ArrayList<String> items = new ArrayList<>();
        int positionSpnCategory = 0;
        for (Category c : listCategory){
            items.add(c.getName());
            if (c.getName().equals(note.getCategory())){
                positionSpnCategory = items.size()-1;
            }
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_dropdown_item, items);
        spnCategory.setAdapter(adapter);
        spnCategory.setSelection(positionSpnCategory);

        EditText edtPlanDate = popupView.findViewById(R.id.edtPlanDate);
        edtPlanDate.setInputType(InputType.TYPE_NULL);
            String pd = note.getPlanDate().getDate() + "/" + (note.getPlanDate().getMonth()+1) + "/"
                    + (note.getPlanDate().getYear() + 1900);
            edtPlanDate.setText(pd);
        edtPlanDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar cldr = Calendar.getInstance();
                int day = cldr.get(Calendar.DAY_OF_MONTH);
                int month = cldr.get(Calendar.MONTH);
                int year = cldr.get(Calendar.YEAR);
                DatePickerDialog picker =  new DatePickerDialog(getActivity(),
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                edtPlanDate.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                            }
                        }, year, month, day);
                picker.show();
            }
        });


        TextView txtTypePopupNote = popupView.findViewById(R.id.txtTypePopupNote);
        Button btnActivity = popupView.findViewById(R.id.btnActivity);
        if (type == 1) {
            txtTypePopupNote.setText("Edit note");
            btnActivity.setText("EDIT NOTE");
            btnActivity.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {
                    String name = etdNewNoteName.getText().toString();
                    String cate = spnCategory.getSelectedItem().toString();
                    Date cd = new Date();
                    Date pd = null;

                    try {
                        pd = new SimpleDateFormat("dd/MM/yyyy").parse(edtPlanDate.getText().toString());
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                    if (cate != null && name != null && pd != null) {
                        Note temp = new Note(note.getUserId(), name, cate, pd, cd);
                        temp.setId(note.getId());
                        updateNote(temp);
                        Toast.makeText(mainActivity.getApplicationContext(), "Edit successfully", Toast.LENGTH_SHORT).show();
                        popupWindow.dismiss();
                    }
                    else {
                        Toast.makeText(mainActivity.getApplicationContext(), "Edit failure", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
        else {
            btnActivity.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String name = etdNewNoteName.getText().toString();
                    String cate = spnCategory.getSelectedItem().toString();
                    Date cd = new Date();
                    Date pd = null;

                    try {
                        pd = new SimpleDateFormat("dd/MM/yyyy").parse(edtPlanDate.getText().toString());
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                    if (cate != null && name != null && pd != null) {
                        addNote(name, cate.toString(), pd, cd );
                        Toast.makeText(mainActivity.getApplicationContext(), "Add successfully", Toast.LENGTH_SHORT).show();
                        popupWindow.dismiss();
                    }
                    else {
                        Toast.makeText(mainActivity.getApplicationContext(), "Add failure", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    public void addNote(String name, String cate, Date planDate, Date createDate){
        try {
            Note n = new Note(user.getId(), name, cate, planDate, createDate);
            if(!user.getId().equals("") && !name.equals("") && !cate.equals("")) {
                UUID uuid = UUID.randomUUID();
                database.child("notes").child(uuid.toString()).setValue(n);
                listNote.clear();
                Toast.makeText(mainActivity.getApplicationContext(), "Add successfully", Toast.LENGTH_SHORT).show();
            }
        }
        catch (Exception err){
            Toast.makeText(mainActivity.getApplicationContext(), "Add failure: " +err.toString(), Toast.LENGTH_SHORT).show();
        }
    }

    public void deleteNote(String id){
        try {
            database.child("notes").child(id).removeValue();
            listNote.clear();
            Toast.makeText(mainActivity.getApplicationContext(), "Delete successfully", Toast.LENGTH_SHORT).show();
        }
        catch (Exception err){
            Toast.makeText(mainActivity.getApplicationContext(), "Delete failure: " + err.toString(), Toast.LENGTH_SHORT).show();
        }
    }

    public void updateNote(Note note){
        try {
            database.child("notes").child(note.getId()).setValue(note);
            Log.e(TAG, note.getId());
            listNote.clear();
            Toast.makeText(mainActivity.getApplicationContext(), "Edit successfully", Toast.LENGTH_SHORT).show();
        }
        catch (Exception err){
            Toast.makeText(mainActivity.getApplicationContext(), "Edit failure: " +err.toString(), Toast.LENGTH_SHORT).show();
        }
    }

    public void getListNote(String userId){
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

                if (listNote.size() > 0){
                    noteListViewAdapter = new NoteListViewAdapter(listNote);
                    listViewNote.setAdapter(noteListViewAdapter);
                }
            }


            @Override
            public void onCancelled(DatabaseError error) {
                Toast.makeText(mainActivity.getApplicationContext(), error.toString(), Toast.LENGTH_SHORT).show();
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
                }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(mainActivity.getApplicationContext(), error.toString(), Toast.LENGTH_SHORT).show();

            }
        });
    }
}

class NoteListViewAdapter extends BaseAdapter {

    final ArrayList<Note> listNote;

    NoteListViewAdapter(ArrayList<Note> listNote) {
        this.listNote = listNote;
    }

    @Override
    public int getCount() {
        return listNote.size();
    }

    @Override
    public Object getItem(int position) {
        return listNote.get(position);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    public Note getNote(int position) {
//        Note note = new Note()
        return listNote.get(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LinearLayoutCompat viewNote;
        if (convertView == null) {
            viewNote = (LinearLayoutCompat) View.inflate(parent.getContext(), R.layout.activity_note_item, null);
        } else viewNote = (LinearLayoutCompat) convertView;

        Note note = (Note) getItem(position);
        ((TextView) viewNote.findViewById(R.id.txtNameNote)).setText(("Name: " +  note.getName()));
        ((TextView) viewNote.findViewById(R.id.txtCategoryNote)).setText(String.format("Category: %s", note.getCategory()));
        String planDate = note.getPlanDate().getDate() + "/" + (note.getPlanDate().getMonth()+1) + "/"
                + (note.getPlanDate().getYear() + 1900);
        ((TextView) viewNote.findViewById(R.id.txtPlanDateNote)).setText(String.format("Plan date: %s", planDate));
        String createDate = note.getCreateDate().getDate() + "/" + (note.getCreateDate().getMonth()+1)
                + "/" + (note.getCreateDate().getYear() + 1900) + " " + note.getCreateDate().getHours()
                + ":" + note.getCreateDate().getMinutes() + ":" + note.getCreateDate().getSeconds();
        ((TextView) viewNote.findViewById(R.id.txtCreateDateNote)).setText(String.format("Create date: %s", createDate));

        return viewNote;
    }
}