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
import java.util.Calendar;
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
    List<Trips> upcomingList;
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
                   // Toast.makeText(SignIn.this, "You don't have data", Toast.LENGTH_SHORT).show();
                }else {
                    Log.i("click","Trip : "+arr);
                    viewModel.insertAll(arr);
                    try {
                        upcomingList=viewModel.getUpcomingTrips();
                       setAllAlarm();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    MainActivity.progressBar.setVisibility(View.INVISIBLE);
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
                    .setLogo(R.drawable.ic_lock)
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

                    startActivity(new Intent(getApplicationContext(),MainActivity.class));
                    finish();
                }
               finish();
                //Toast.makeText(SignIn.this, "nott nulll.", Toast.LENGTH_SHORT).show();
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
    private void setAlarm(Calendar calendar, long id) {
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent broadcastIntent= new Intent(SignIn.this,NotificationReceiver.class);
        broadcastIntent.putExtra(notificationIntentKey,"say goodbye to your data");
        broadcastIntent.putExtra("id",id);
        PendingIntent pendingBroadcastIntent=PendingIntent.getBroadcast(SignIn.this, (int) id,
                broadcastIntent,0);
        Log.e("alarm"," "+calendar.getTimeInMillis()+" "+id);
        alarmManager.setExact(AlarmManager.RTC_WAKEUP,calendar.getTimeInMillis(),pendingBroadcastIntent);


    }
    private void setAllAlarm(){
        for(Trips t:upcomingList){
           // Log.i("ola","kk"+t.getCalender().get(Calendar.HOUR_OF_DAY)+" ff "+ t.getCalender().get(Calendar.MINUTE)+"   "+t.getCalender().get(Calendar.DAY_OF_MONTH)+"  "+t.getCalender().get(Calendar.YEAR)+"  "+t.getCalender().get(Calendar.MONTH)+"id"+t.getId());
            //
            Calendar cal=t.getCalender();
            cal.set(Calendar.YEAR,t.getCalender().get(Calendar.YEAR));
            cal.set(Calendar.MONTH,(t.getCalender().get(Calendar.MONTH)-1));
            cal.set(Calendar.DAY_OF_MONTH,t.getCalender().get(Calendar.DAY_OF_MONTH));
            cal.set(Calendar.HOUR_OF_DAY,t.getCalender().get(Calendar.HOUR_OF_DAY));
            cal.set(Calendar.MINUTE,t.getCalender().get(Calendar.MINUTE));
            if(calenderValidation(cal)){
                 setAlarm(cal,t.getId());
                 Log.e("alarm","if"+t.getCalender().get(Calendar.HOUR_OF_DAY));
            }
            else {
                t.setStatus(3);
                viewModel.update(t);
                Log.e("alarm", "else" + t);
            }

        }
    }
    public Boolean calenderValidation(Calendar calendar){
        Calendar currentCalendar = Calendar.getInstance();
        currentCalendar.set(Calendar.YEAR,currentCalendar.get(Calendar.YEAR));
        currentCalendar.set(Calendar.MONTH,currentCalendar.get(Calendar.MONTH));
        currentCalendar.set(Calendar.DAY_OF_MONTH,currentCalendar.get(Calendar.DAY_OF_MONTH));
        currentCalendar.set(Calendar.HOUR_OF_DAY,currentCalendar.get(Calendar.HOUR_OF_DAY));
        currentCalendar.set(Calendar.MINUTE,currentCalendar.get(Calendar.MINUTE));
        currentCalendar.set(Calendar.SECOND,0);
        Log.e("alarm","validation: "+"current"+currentCalendar.get(Calendar.HOUR_OF_DAY)+"calendar"+calendar.get(Calendar.HOUR_OF_DAY));
        if((calendar.getTimeInMillis()-currentCalendar.getTimeInMillis())<=0){
            Log.e("alarm","invalid: "+"current"+currentCalendar.get(Calendar.HOUR_OF_DAY)+"calendar"+calendar.get(Calendar.HOUR_OF_DAY));
            Log.e("alarm","invalid: "+"current"+currentCalendar.get(Calendar.DAY_OF_MONTH)+"   "+currentCalendar.get(Calendar.MONTH));
            return false;

        }
        else {
            Log.e("alarm","valid"+currentCalendar.get(Calendar.HOUR_OF_DAY)+"calendar"+calendar.get(Calendar.HOUR_OF_DAY));
            Log.e("alarm","valid: "+"current"+currentCalendar.get(Calendar.DAY_OF_MONTH)+"   "+currentCalendar.get(Calendar.MONTH));
            return true;
        }
    }
}