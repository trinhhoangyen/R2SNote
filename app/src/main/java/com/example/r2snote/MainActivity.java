package com.example.r2snote;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.r2snote.DTO.Note;
import com.example.r2snote.ui.Login;
import com.example.r2snote.ui.home.HomeFragment;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private ViewGroup containerView;
    private TextView txt_password;
    private AppBarConfiguration mAppBarConfiguration;
    private String pass;
    // Write a message to the database
    DatabaseReference database = FirebaseDatabase.getInstance().getReference();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
            newInstance();
            Intent intent = getIntent();
            String username = intent.getStringExtra("Username");
            String password = intent.getStringExtra("Password");
            pass = password;

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

            //set user name
            NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
            View headerView = navigationView.getHeaderView(0);
            TextView navUsername = (TextView) headerView.findViewById(R.id.txt_username);
            navUsername.setText(username);

            Toolbar toolbar = findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);
            DrawerLayout drawer = findViewById(R.id.drawer_layout);

            mAppBarConfiguration = new AppBarConfiguration.Builder(
                    R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow, R.id.nav_changepassword)
                    .setDrawerLayout(drawer)
                    .build();
            NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
            NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
            NavigationUI.setupWithNavController(navigationView, navController);

            navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    switch (item.getItemId()) {
                        case R.id.nav_logout:
                            Intent intent = new Intent(MainActivity.this, Login.class);
                            startActivity(intent);
                            finish();
                            break;
                    }
                    return true;
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
                newInstance();
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

    public void newInstance(){
        HomeFragment n = new HomeFragment();
        // Supply index input as an argument.
        Bundle args = new Bundle();
        args.putString("ms", "list note" );
        n.setArguments(args);
    }

    public String getPass(){
        return pass;
    }


}