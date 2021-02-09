package com.example.tripreminder.firebase;

import android.os.Message;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.tripreminder.MainActivity;
import com.example.tripreminder.SignIn;
import com.example.tripreminder.beans.Trips;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ReadData implements Runnable {
    @Override
    public void run() {
        DatabaseReference mDatabase;
        ArrayList<Trips> returnedData = new ArrayList<>();
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid() ;
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(userId).child("Trips").child("0");
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Message Msg = Message.obtain();
                for (DataSnapshot ds : snapshot.getChildren()) {
                    Trips user = ds.getValue(Trips.class);
                    //System.out.println(user.getEndPoint());
                    returnedData.add(user);
                }
                System.out.println("the result inside thread :  "+ returnedData.size()+"");
                Msg.obj = returnedData;
                SignIn.fireBaseReadHandler.sendMessage(Msg);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) { }
        });

    }
}
