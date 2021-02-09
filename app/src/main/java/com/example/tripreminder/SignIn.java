package com.example.tripreminder;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProviders;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.tripreminder.beans.Trips;
import com.example.tripreminder.firebase.ReadData;
import com.example.tripreminder.roomDB.TripsViewModel;
import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutionException;

import static com.example.tripreminder.DialogActivity.notificationIntentKey;

public class SignIn extends AppCompatActivity {
    FirebaseAuth firebaseAuth;
    private static int AUTH_REC = 12;
    FirebaseAuth.AuthStateListener listener;
    List<AuthUI.IdpConfig> providers;
    ArrayList<Trips> arr;
    public static Handler fireBaseReadHandler;
    public static Thread readFireBaseThread;
    private TripsViewModel viewModel;
    FirebaseUser firebaseUser;
    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        viewModel= ViewModelProviders.of(this).get(TripsViewModel.class);
        arr = new ArrayList<>();
        readFireBaseThread = new Thread(new ReadData());
        fireBaseReadHandler = new Handler(){
            @Override
            public void handleMessage(@NonNull Message msg) {
                super.handleMessage(msg);
                arr = (ArrayList<Trips>) msg.obj;
                if(arr.size() == 0){
                    Toast.makeText(SignIn.this, "You don't have data", Toast.LENGTH_SHORT).show();
                }else {
                    Log.i("click","Trip : "+arr);
                    viewModel.insertAll(arr);
                    MainActivity.progressBar_up.setVisibility(View.GONE);
                    try {
                        cancelAllAlarm();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                   // startActivity(new Intent(getApplicationContext(),MainActivity.class));
                    finish();
                    // System.out.println("the first note of first element :  " + TotalUserData.get(1).getNotes().get(2) + "");
                }
            }
        };
        init();
    }

    private void init() {
        providers = Arrays.asList(
                new AuthUI.IdpConfig.EmailBuilder().build(),
                new AuthUI.IdpConfig.GoogleBuilder().build()
                // new AuthUI.IdpConfig.PhoneBuilder().build()
                //new AuthUI.IdpConfig.FacebookBuilder().build()
        );
        firebaseAuth = FirebaseAuth.getInstance();

        firebaseUser = firebaseAuth.getCurrentUser();
        if(firebaseUser != null)
        {

            startActivity(new Intent(getApplicationContext(),MainActivity.class));
            finish();
        }
        else
        {
            Log.i("click","usernotExist G&m ");
            startActivityForResult(AuthUI.getInstance()
                    .createSignInIntentBuilder()
                    .setAvailableProviders(providers)
                    .setTheme(R.style.loginTheme)
                    .build(),AUTH_REC);
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == AUTH_REC)
        {
            if(requestCode ==RESULT_OK) {
                Log.i("click","Result ok");
                finish();

            }
            else {
                FirebaseUser u = firebaseAuth.getCurrentUser();
                Log.i("click",u.getEmail());
                writeInSharedPreference(u.getEmail());
                Thread s = new Thread()
                {
                    @Override
                    public void run() {
                        super.run();
                        try {
                            sleep(1000);
                            readFireBaseThread.start();
                            //remove Activity
                           //finish();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                };
                s.start();

                FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
                if(firebaseUser != null)
                {

                    Toast.makeText(SignIn.this, "nulll.", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(getApplicationContext(),MainActivity.class));
                    finish();
                }
               finish();
                Toast.makeText(SignIn.this, "nott nulll.", Toast.LENGTH_SHORT).show();
                //MainActivity.progressBar_up.setVisibility(View.VISIBLE);
            }
        }
    }
    public void writeInSharedPreference(String n2){
        SharedPreferences writr = getSharedPreferences("UserInfo", Context.MODE_PRIVATE);
        //FirebaseUser user = mAuth.getCurrentUser();
        SharedPreferences.Editor editor = writr.edit();
        editor.putString("Email",firebaseAuth.getCurrentUser().getEmail());
        editor.commit();
        Log.i("click","email is : "+n2);
    }
    private void cancelAllAlarm() throws ExecutionException, InterruptedException {
        List<Trips> trips = viewModel.getAll();
        for (Trips t : trips) {
            if (t.getStatus() == 0) {
                cancelAlarm(t.getId());

            }

        }
    }
    private void cancelAlarm(int requestCode) {
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent broadcastIntent= new Intent(SignIn.this,NotificationReceiver.class);
        broadcastIntent.putExtra(notificationIntentKey,"say goodbye to your data");
        PendingIntent pendingBroadcastIntent=PendingIntent.getBroadcast(SignIn.this,requestCode,
                broadcastIntent,0);
        alarmManager.cancel(pendingBroadcastIntent);
        pendingBroadcastIntent.cancel();


    }
}