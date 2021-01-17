package com.example.r2snote.ui.slideshow;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

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
import java.util.Date;
import java.util.zip.Inflater;

public class NoteFragment extends Fragment {
    private MainActivity mainActivity;
    private User user;
    DatabaseReference database = FirebaseDatabase.getInstance().getReference();
    private NoteViewModel noteViewModel;
    NoteListViewAdapter noteListViewAdapter;
    ArrayList<Note> listNote = new ArrayList<>();
    ListView listViewNote;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        noteViewModel =
                new ViewModelProvider(this).get(NoteViewModel.class);
        View root = inflater.inflate(R.layout.fragment_note, container, false);
        mainActivity = (MainActivity) getActivity();
        listViewNote = (ListView) root.findViewById(R.id.listViewNote);
        user = mainActivity.getUser();
        getNote(user.getId());


        //        Log.e("----------user: ", user.getId());
//        getNote();
//        DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
//        Date pd = null;
//        try {
//            pd = formatter.parse("1/1/2021");
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
//
//        Date cd = new Date();

        return root;
    }


    public void createNote(String id, String name, String cate, Date planDate, Date createDate){
        Note n = new Note(user.getId(), name, cate, planDate, createDate);
        if(!user.getId().equals("") && !name.equals("") && !cate.equals("")) {
            database.child("notes").child(id).setValue(n);
        }
    }

    public void getNote(String userId){
        database.child("notes").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot ds : dataSnapshot.getChildren()) {
                    Note note = ds.getValue(Note.class);
                    if (note != null && !note.getUserId().equals("")) {
                        if (note.getUserId().equals(user.getId())) {
                            listNote.add(note);
                        }
                    }
                }

                Log.e("-----size in getNote", listNote.size() + "");
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

    public String getNoteId(int position) {
        return listNote.get(position).getUserId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //convertView là View của phần tử ListView, nếu convertView != null nghĩa là
        //View này được sử dụng lại, chỉ việc cập nhật nội dung mới
        //Nếu null cần tạo mới

        LinearLayoutCompat viewNote;
        if (convertView == null) {
            viewNote = (LinearLayoutCompat) View.inflate(parent.getContext(), R.layout.activity_note_item, null);
        } else viewNote = (LinearLayoutCompat) convertView;

        //Bind sữ liệu phần tử vào View
        Note note = (Note) getItem(position);
        ((TextView) viewNote.findViewById(R.id.txtNameNote)).setText(("Name: " +  note.getName()));
        ((TextView) viewNote.findViewById(R.id.txtCategoryNote)).setText(String.format("Category: %s", note.getCategory()));
        long planDate = note.getPlanDate().getTime();
        ((TextView) viewNote.findViewById(R.id.txtPlanDateNote)).setText(String.format("Plan date: %tc", planDate));
        long createDate = note.getCreateDate().getTime();
        ((TextView) viewNote.findViewById(R.id.txtCreateDateNote)).setText(String.format("Create date: %d", createDate));

        return viewNote;
    }
}