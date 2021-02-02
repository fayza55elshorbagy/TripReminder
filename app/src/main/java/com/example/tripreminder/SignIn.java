package com.example.tripreminder;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;

import java.util.Arrays;
import java.util.List;

public class SignIn extends AppCompatActivity {
    FirebaseAuth firebaseAuth;
    private static int AUTH_REC = 12;
    FirebaseAuth.AuthStateListener listener;
    List<AuthUI.IdpConfig> providers;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_sign_in);
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
            //Toast.makeText(SignIn.this, "You already logged in before.", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(getApplicationContext(),MainActivity.class));
            finish();
        }
        else
        {
            startActivityForResult(AuthUI.getInstance()
                    .createSignInIntentBuilder()
                    .setAvailableProviders(providers)
                    .build(),AUTH_REC);
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == AUTH_REC)
        {
            if(requestCode ==RESULT_OK) {
                Toast.makeText(this, "", Toast.LENGTH_SHORT).show();
                finish();

            }
            else {
                finish();
            }
        }
    }
}