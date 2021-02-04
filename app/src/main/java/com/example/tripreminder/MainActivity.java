package com.example.tripreminder;

import androidx.annotation.NonNull;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProviders;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.tripreminder.beans.Trips;
import com.example.tripreminder.fragments.History;
import com.example.tripreminder.fragments.Upcoming;
import com.example.tripreminder.roomDB.TripsViewModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    EditText name;
    DatabaseReference databaseReference;
    FirebaseAuth firebaseAuth;
    NavigationView navigationView;
    ImageView imageView;
    DrawerLayout drawerLayout;
    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;
    TripsViewModel viewModel;
    DatabaseReference rootRef;
    FirebaseUser currentUser;
    Toolbar toolBar;
    FloatingActionButton addBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        firebaseAuth=FirebaseAuth.getInstance();
        currentUser=firebaseAuth.getCurrentUser();

        rootRef = FirebaseDatabase.getInstance().getReference("Users");

        viewModel= ViewModelProviders.of(this).get(TripsViewModel.class);
        navigationView = (NavigationView)findViewById(R.id.nav_view);
        drawerLayout = (DrawerLayout)findViewById(R.id.drawer);
        toolBar = (Toolbar) findViewById(R.id.toolbar);


        addBtn = (FloatingActionButton)findViewById(R.id.addBtn);
        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this, addTripActivity.class);
                startActivityForResult(intent,1);
            }
        });

       /* imageView = (ImageView)findViewById(R.id.menu);
        //firebaseAuth = FirebaseAuth.getInstance();
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout.openDrawer(Gravity.LEFT);
            }
        });*/
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolBar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_open);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(
                    R.id.fragmentContainer, new Upcoming(), "UpcomingFragment").commit();
            navigationView.setCheckedItem(R.id.upComing);
        }
        navigationView.bringToFront();
        navigationView.setNavigationItemSelectedListener(this);

    }

    @Override
    public void onBackPressed() {
        if(drawerLayout.isDrawerOpen(GravityCompat.START))
        {
            drawerLayout.closeDrawer(GravityCompat.START);
        }
        else
        {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if(id ==R.id.logout)
        {
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(getApplicationContext(),SignIn.class));
            finish();
        }
        if(id == R.id.upComing)
        {
            //toolBar.setTitle("UpComing Trips");
            getSupportFragmentManager().beginTransaction().replace(
            R.id.fragmentContainer, new Upcoming(), "UpcomingFragment").commit();
        }
        if(id == R.id.history)
        {
            toolBar.setTitle("History");
            getSupportFragmentManager().beginTransaction().replace(
                    R.id.fragmentContainer, new History(), "HistoryFragment").commit();
        }
        if(id == R.id.syncronyize)
        {
            try {
                syncronyize();
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    private void syncronyize() throws ExecutionException, InterruptedException {
        List<Trips> trips= viewModel.getAll();
        Log.i("note","lis"+trips);
        if(currentUser!=null) {
            String currentUserId = currentUser.getUid();
            // rootRef.child(currentUserId).child("Trips").setValue("");
            rootRef.child(currentUserId).child("Trips").setValue(Arrays.asList(trips)).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        Toast.makeText(MainActivity.this, "Backup is done", Toast.LENGTH_SHORT).show();
                    } else {
                        String message = task.getException().getLocalizedMessage();
                        Toast.makeText(MainActivity.this, "" + "Error" + message, Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
        else
            Toast.makeText(MainActivity.this, "" + "Error" , Toast.LENGTH_SHORT).show();
    }
}