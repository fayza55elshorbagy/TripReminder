package com.example.tripreminder;

import androidx.annotation.NonNull;


import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProviders;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.location.Address;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.provider.Settings;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import com.example.tripreminder.firebase.FireBaseData;
import com.example.tripreminder.firebase.ReadData;
import com.example.tripreminder.fragments.History;
import com.example.tripreminder.fragments.Upcoming;
import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

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
    public static Dialog dialog;
    NavigationView navigationView;
    ImageView imageView;
    DrawerLayout drawerLayout;
    public static Activity mactivity;

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
        mactivity = MainActivity.this;

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer);

        imageView = (ImageView) findViewById(R.id.menu);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if(!Settings.canDrawOverlays(this)) {
                checkDrawOverAppsPermissionsDialog();
            }
        }
        runBackgroundPermissions();

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
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.logout) {
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(getApplicationContext(), SignIn.class));
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

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    private void errorWarningForNotGivingDrawOverAppsPermissions(){
        new AlertDialog.Builder(this).setTitle("Warning").setCancelable(false).setMessage("Unfortunately the display over other apps permission" +
                " is not granted so the application might not behave properly \nTo enable this permission kindly restart the application" )
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                }).show();
    }

    public void runBackgroundPermissions() {
        if (Build.VERSION.SDK_INT == Build.VERSION_CODES.M) {
            if (Build.BRAND.equalsIgnoreCase("xiaomi")) {
                Intent intent = new Intent();
                intent.setComponent(new ComponentName("com.miui.securitycenter", "com.miui.permcenter.autostart.AutoStartManagementActivity"));
                startActivity(intent);
            } else if (Build.BRAND.equalsIgnoreCase("Honor") || Build.BRAND.equalsIgnoreCase("HUAWEI")) {
                Intent intent = new Intent();
                intent.setComponent(new ComponentName("com.huawei.systemmanager", "com.huawei.systemmanager.optimize.process.ProtectActivity"));
                startActivity(intent);
            }
        }
    }

    public void drawOverAppPermission (){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!Settings.canDrawOverlays(this)) {
                Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                        Uri.parse("package:" + getPackageName()));
                startActivityForResult(intent, 80);
            }
        }
    }

    private void checkDrawOverAppsPermissionsDialog(){
        new AlertDialog.Builder(this).setTitle("Permission request").setCancelable(false).setMessage("Allow Draw Over Apps Permission to be able to use application probably")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        drawOverAppPermission();
                    }
                }).setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                errorWarningForNotGivingDrawOverAppsPermissions();
            }
        }).show();
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