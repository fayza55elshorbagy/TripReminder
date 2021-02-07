package com.example.tripreminder;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import com.example.tripreminder.beans.Trips;
import com.example.tripreminder.firebase.ReadData;
import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SignIn extends AppCompatActivity {
    FirebaseAuth firebaseAuth;
    private static int AUTH_REC = 12;
    FirebaseAuth.AuthStateListener listener;
    List<AuthUI.IdpConfig> providers;
    ArrayList<Trips> arr;
    public static Handler fireBaseReadHandler;
    public static Thread readFireBaseThread;

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_sign_in);
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
                    System.out.println("the result after thread :  " + arr.size() + "");
                    Log.i("click",""+arr);
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

        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        if(firebaseUser != null)
        {
            Toast.makeText(SignIn.this, "not nulll.", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(getApplicationContext(),MainActivity.class));
            finish();
        }
        else
        {
            Toast.makeText(SignIn.this, "signinui.", Toast.LENGTH_SHORT).show();
            startActivityForResult(AuthUI.getInstance()
                    .createSignInIntentBuilder()
                    .setAvailableProviders(providers)
                    //.setIsSmartLockEnabled(false)
                    .build(),AUTH_REC);
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == AUTH_REC)
        {
            if(requestCode ==RESULT_OK) {
                Toast.makeText(SignIn.this, "okk.", Toast.LENGTH_SHORT).show();
                Toast.makeText(this, "", Toast.LENGTH_SHORT).show();
                finish();

            }
            else {

                Toast.makeText(SignIn.this, "notok", Toast.LENGTH_SHORT).show();
                Thread s = new Thread()
                {
                    @Override
                    public void run() {
                        super.run();
                        try {
                            sleep(3*1000);
                            readFireBaseThread.start();
                            //remove Activity
                            //finish();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                };
                s.start();
                finish();
            }
        }
    }
}