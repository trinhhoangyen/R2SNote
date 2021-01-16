package com.example.r2snote;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Menu;

import com.example.r2snote.DTO.Note;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.r2snote.DTO.User;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    // Write a message to the database
    DatabaseReference database = FirebaseDatabase.getInstance().getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getNote();
        DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        Date pd = null;
        try {
            pd = formatter.parse("1/1/2021");
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Date cd = new Date();
        createNote("001", "Football", "Relax", pd,cd);
        createNote("002","Play", "Relax", pd,cd);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
    }

    public void Login(String user, String pass){
        // Login
        database.child("users").addValueEventListener(new ValueEventListener() {
            Boolean result = new Boolean(false);
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                for(DataSnapshot ds : dataSnapshot.getChildren()) {
                    User u = ds.getValue(User.class);
                    if(u.getUsername().equals(user) && u.getPassword().equals(pass)) {
                        result = true;
                        Log.e("-----SUCCESS-----", u.getUsername() + u.getPassword());
                    }
                }
            }
            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
            }
        });
    }
    public void createNote(String id, String name, String cate, Date planDate, Date createDate){
        Note n = new Note( name, cate, planDate, createDate);
        database.child("notes").child(id).setValue(n);
    }
    public void getNote(){
        // Login
        database.child("notes").addValueEventListener(new ValueEventListener() {
            List<Note> list = new ArrayList<>();
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot ds : dataSnapshot.getChildren()) {
                    Note u = ds.getValue(Note.class);
                    list.add(u);
                }
                for (Note n : list
                ) {
                    Log.e(n.getCategory() + " - " + n.getName(), n.getPlanDate().toLocaleString());
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
            }
        });
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }
}