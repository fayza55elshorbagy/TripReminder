package com.example.tripreminder;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class SignUp extends AppCompatActivity {
    EditText firstName;
    EditText u_phone;
    EditText u_email;
    EditText u_password;
    EditText u_confirmationPassword;
    ProgressBar progressBar_up;

    String nameString;
    String emailString;
    String phoneString;
    String passString;
    String confPassString;

    FirebaseAuth firebaseAuth;
    FirebaseUser currentUser;
    DatabaseReference databaseReference;
    SignInButton signInButton;
    GoogleSignInClient googleSignInClient;
    GoogleSignInOptions googleSignInOptions;
    private int RC_SIGN_IN = 1;


    @Override
    protected void onStart() {
        super.onStart();
        currentUser = firebaseAuth.getCurrentUser();
            if(currentUser != null)
            {
                //SignIn.readFireBaseThread.start();
                startActivity(new Intent(getApplicationContext(),MainActivity.class));
                finish();
            }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        firstName = findViewById(R.id.firstN_et);
        u_phone = findViewById(R.id.phone_et);
        u_email = findViewById(R.id.email_et);
        u_password = findViewById(R.id.pass_et);
        u_confirmationPassword = findViewById(R.id.confpass_et);
        progressBar_up = findViewById(R.id.progressBar_up);
        firebaseAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();

    }



    public void signUp(View view){
         emailString = u_email.getText().toString();
         passString = u_password.getText().toString();
         confPassString = u_confirmationPassword.getText().toString();
         phoneString = u_phone.getText().toString();
         nameString = firstName.getText().toString();
         String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
        //String uid = firebaseAuth.getCurrentUser().getUid();

        if(nameString.isEmpty())
        {
            firstName.setError("Enter valid Name.");
            firstName.requestFocus();
        }
        if(emailString.isEmpty() )
        {
            u_email.setError("Enter Valid Email.");
            u_email.requestFocus();
        }
        if(!emailString.matches(emailPattern))
        {
            u_email.setError("Enter Valid Email.");
            u_email.requestFocus();
        }
        if(passString.isEmpty())
        {
            u_password.setError("Password is Required.");
            u_email.requestFocus();
        }
        if(passString.length() < 6)
        {
            u_password.setError("Password must be more 5 digits");
            u_password.requestFocus();
        }
        if(!passString.equals(confPassString))
        {
            Toast.makeText(this, "Password not matching", Toast.LENGTH_SHORT).show();
            u_confirmationPassword.requestFocus();
        }
        if(phoneString.isEmpty() )
        {
            u_phone.setError("Enter valid Phone Number.");
            u_phone.requestFocus();
        }
        if(phoneString.length() < 11 )
        {
            u_phone.setError("Enter valid Phone Number.");
            u_phone.requestFocus();
        }

        if(!emailString.isEmpty()&&!passString.isEmpty()&&!confPassString.isEmpty()&&!nameString.isEmpty()&&passString.equals(confPassString))
        {
            progressBar_up.setVisibility(view.VISIBLE);
            firebaseAuth.createUserWithEmailAndPassword(emailString,passString).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if((task.isSuccessful()))
                    {
                        Toast.makeText(SignUp.this, "Registeration Done", Toast.LENGTH_SHORT).show();
                        Intent Main = new Intent(getApplicationContext(),MainActivity.class);
                        startActivity(Main);
                        //SendVerificationMail();
                         writeUserData();
                    }
                    else
                    {
                        Toast.makeText(SignUp.this, "Error !"+task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }


                }
            });
        }
        else
        {
            Toast.makeText(this, "Please Recheck your data!", Toast.LENGTH_SHORT).show();
        }

    }

    private void writeUserData() {
        databaseReference.child("Users").child(firebaseAuth.getCurrentUser().getUid()).child("UserName").setValue(nameString);
    }
/*
    private void SendVerificationMail() {
        currentUser.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                if(task.isSuccessful())
                {
                    Intent signin = new Intent(getApplicationContext(),SignIn.class);
                    signin.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(signin);
                }
            }
        });

    }*/

    public void signIn(View view) {
        Intent signin = new Intent(getApplicationContext(),SignIn.class);
        startActivity(signin);
    }
}