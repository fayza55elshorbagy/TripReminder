package com.example.tripreminder;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class headeractivity extends AppCompatActivity {
    FirebaseAuth firebaseAuth;
    DatabaseReference databaseReference;
    TextView namesider;
    NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.header);
        firebaseAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Users").child(firebaseAuth.getCurrentUser().getUid()).child("UserName");
        navigationView = (NavigationView)findViewById(R.id.nav_view);
        View header=navigationView.getHeaderView(0);
        namesider = (TextView)header.findViewById(R.id.name);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String name = dataSnapshot.getValue().toString();
                Log.i("NAME","*************************************"+name+"&&&&&&&&&&&&&&&&&&&&&&");
                namesider.setText(name);
                System.out.println(name);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

               Log.i("NAME","canclled");
            }
        });
    }
}
