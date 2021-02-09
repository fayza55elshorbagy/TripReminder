package com.example.tripreminder.firebase;

import android.os.Message;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.tripreminder.SignIn;
import com.example.tripreminder.beans.Trips;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ReadData implements Runnable {
    @Override
    public void run() {
        DatabaseReference mDatabase;
        ArrayList<Trips> returnedData = new ArrayList<>();
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid() ;
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(userId);
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Trips user;
                Message Msg = Message.obtain();
                DataSnapshot d= snapshot.child("Trips").child("0");
                for (DataSnapshot ds : d.getChildren()){
                     user = ds.getValue(Trips.class);
                    returnedData.add(user);
                }
                Msg.obj = returnedData;
                SignIn.fireBaseReadHandler.sendMessage(Msg);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) { }
        });

    }
}
