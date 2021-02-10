package com.example.tripreminder;

import androidx.annotation.NonNull;


import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProviders;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.provider.Settings;
import android.content.ComponentName;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tripreminder.fragments.History;
import com.example.tripreminder.fragments.Upcoming;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import com.example.tripreminder.beans.Trips;
import com.example.tripreminder.roomDB.TripsViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutionException;

import static com.example.tripreminder.DialogActivity.notificationIntentKey;

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
    FloatingActionButton addBtn;
    public static ProgressBar progressBar;
    TextView title;
    TextView title1;
    FirebaseAuth firebaseAuth;
    int PREMISSION_ID = 100;
    FusedLocationProviderClient fusedLocationProviderClient;
    public static double latitude = 0.0;
    public static double longitude = 0.0;
    NavigationView menu;
    View header;
    TextView email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O_MR1) {
            setShowWhenLocked(true);
            setTurnScreenOn(true);
        }

        menu = findViewById(R.id.nav_view);
        header = menu.getHeaderView(0);
        email = header.findViewById(R.id.name);
        SharedPreferences writr = getSharedPreferences("UserInfo", Context.MODE_PRIVATE);
        String n = writr.getString("Email","m");
        email.setText(n);

        progressBar = findViewById(R.id.progressBar);
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        getlocation();
        mactivity = MainActivity.this;
         title= (TextView)findViewById(R.id.title);
         title1= (TextView)findViewById(R.id.title1);

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer);


        //imageView = (ImageView) findViewById(R.id.menu);


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
        progressBar.setVisibility(View.VISIBLE);


        addBtn = (FloatingActionButton)findViewById(R.id.addBtn);
        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this, addTripActivity.class);
                startActivityForResult(intent,1);
            }
        });

        imageView = (ImageView)findViewById(R.id.menu);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout.openDrawer(Gravity.LEFT);
            }
        });
     /*   ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolBar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_open);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();*/

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

        if(id ==R.id.logout)
        {
        AlertDialog diaBox = AskOption();
            diaBox.show();


        }

        if(id == R.id.upComing)
        {
            title.setText("UpComing");
            title1.setVisibility(View.VISIBLE);
            getSupportFragmentManager().beginTransaction().replace(
                    R.id.fragmentContainer, new Upcoming(), "UpcomingFragment").commit();
        }
        if(id == R.id.history)
        {
            title.setText("History");
            title1.setVisibility(View.INVISIBLE);
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
        if(id == R.id.map)
        {
            Intent mapIntent = new Intent(MainActivity.this,MapActivity.class);
            startActivity(mapIntent);
            finish();
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
    private void cancelAllAlarm() throws ExecutionException, InterruptedException {
        List<Trips> trips = viewModel.getUpcomingTrips();
        for (Trips t : trips) {
                cancelAlarm(t.getId());
        }
    }
    private void cancelAlarm(int requestCode) {
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent broadcastIntent= new Intent(MainActivity.this,NotificationReceiver.class);
        broadcastIntent.putExtra(notificationIntentKey,"say goodbye to your data");
        PendingIntent pendingBroadcastIntent=PendingIntent.getBroadcast(MainActivity.this,requestCode,
                broadcastIntent,0);
        alarmManager.cancel(pendingBroadcastIntent);
        pendingBroadcastIntent.cancel();


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
            Toast.makeText(MainActivity.this, "" + "Error,Please try again later" , Toast.LENGTH_SHORT).show();
    }

    public void getlocation() {
        getLastLocation();
    }
    private void getLastLocation() {
        //check premission in runtime
        if (checkPremissions()) {
            if (isLocationEnabled()) {

                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                fusedLocationProviderClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
                    @Override
                    public void onComplete(@NonNull Task<Location> task) {
                        Location location = task.getResult();
                        if (location == null)
                            requstNewLocationData();
                        else
                        {
                            latitude = location.getLatitude();
                            longitude = location.getLongitude();
                            //Toast.makeText(MainActivity.this,  latitude+ "," + longitude, Toast.LENGTH_LONG).show();

                        }

                    }
                });
            } else {
                Toast.makeText(this, "Turn on location", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);

            }
        } else {
            requestPremission();
        }
    }

    private void requestPremission() {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION}, PREMISSION_ID);
    }
    private void requstNewLocationData() {
        LocationRequest locationRequest = new LocationRequest();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(0);
        locationRequest.setNumUpdates(1);

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, Looper.myLooper());
    }
    private LocationCallback locationCallback = new LocationCallback()
    {
        @Override
        public void onLocationResult(LocationResult locationResult) {
            super.onLocationResult(locationResult);
            Location lastLocation = locationResult.getLastLocation();
            latitude = lastLocation.getLatitude();
            longitude = lastLocation.getLongitude();
            Log.i("click",latitude+"insideCallBack"+longitude);

           // Toast.makeText(MainActivity.this, latitude+", "+longitude, Toast.LENGTH_LONG).show();
        }
    };


    private boolean isLocationEnabled() {
        LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }

    private boolean checkPremissions() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            return true;
        }
        return false;
    }

    //user say yes to premission
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == PREMISSION_ID)
        {
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                getLastLocation();
        }
    }

    private AlertDialog AskOption() {
          AlertDialog myQuittingDialogBox = new AlertDialog.Builder(MainActivity.this)
                // set message, title, and icon
                .setTitle("Logout")
                .setMessage("You will lose all set alarms")
                .setIcon(R.drawable.ic_baseline_login_24)
                .setPositiveButton("Logout", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        //your deleting code
                        try {
                            cancelAllAlarm();
                        } catch (ExecutionException e) {
                            e.printStackTrace();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        viewModel.deleteAllTrips();
                        FirebaseAuth.getInstance().signOut();
                        startActivity(new Intent(getApplicationContext(), SignIn.class));
                        finish();
                        dialog.dismiss();
                    }

                })
                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        dialog.dismiss();

                    }
                })
                .create();

        return myQuittingDialogBox;
    }

}