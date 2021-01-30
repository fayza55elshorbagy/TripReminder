package com.example.tripreminder;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.tripreminder.adapters.UpcomingTripAdapter;
import com.example.tripreminder.beans.Trip;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class UpcomingTrip extends AppCompatActivity {
    RecyclerView recyclerView;
    UpcomingTripAdapter adapter;
    List<Trip> tripList;
    FloatingActionButton addBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upcoming_trip);
        tripList=new ArrayList<>();
        tripList.add(new Trip("work","zagazig","ismailia","2021","15:15","one way","upcoming"));
        tripList.add(new Trip("work","zagazig","ismailia","2021","15:15","one way","upcoming"));
        tripList.add(new Trip("work","zagazig","ismailia","2021","15:15","one way","upcoming"));
        tripList.add(new Trip("work","zagazig","ismailia","2021","15:15","one way","upcoming"));
        tripList.add(new Trip("work","zagazig","ismailia","2021","15:15","one way","upcoming"));
        tripList.add(new Trip("work","zagazig","ismailia","2021","15:15","one way","upcoming"));
        tripList.add(new Trip("work","zagazig","ismailia","2021","15:15","one way","upcoming"));
        tripList.add(new Trip("work","zagazig","ismailia","2021","15:15","one way","upcoming"));
        tripList.add(new Trip("work","zagazig","ismailia","2021","15:15","one way","upcoming"));
        recyclerView=findViewById(R.id.recView);
        adapter=new UpcomingTripAdapter(tripList);
        recyclerView.setAdapter(adapter);
        addBtn=findViewById(R.id.addBtn);

        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tripList.add(0,new Trip("test","zagazig","ismailia","2021","15:15","one way","upcoming"));
                adapter.notifyDataSetChanged();
            }
        });

    }
}