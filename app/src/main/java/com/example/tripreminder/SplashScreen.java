package com.example.tripreminder;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SplashScreen extends AppCompatActivity {

    boolean splash_appear = false;
    FirebaseUser currentUser;
    FirebaseAuth firebaseAuth;
    SharedPreferences write;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        firebaseAuth = FirebaseAuth.getInstance();
        currentUser = firebaseAuth.getCurrentUser();
        write = getSharedPreferences("splash", Context.MODE_PRIVATE);

        if(currentUser != null)
        {
            SharedPreferences.Editor editor = write.edit();
            editor.putBoolean("splash_appear",true);
            splash_appear = write.getBoolean("splash_appear",true);
            editor.commit();
        }



        if(splash_appear == false)
        {  Thread splash = new Thread()
        {
            @Override
            public void run() {
                super.run();
                try {
                    sleep(5*1000);
                    Intent i = new Intent(getApplicationContext(),SignUp.class);
                    startActivity(i);
                    //remove Activity
                    finish();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
            splash.start();
        }
        else
        {
            Intent m = new Intent(getApplicationContext(),SignIn.class);
            startActivity(m);
            finish();
        }




    }
}