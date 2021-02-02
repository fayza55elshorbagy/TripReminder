package com.example.tripreminder;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;


import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import static android.app.Activity.RESULT_OK;

public class Upcoming extends Fragment {

    RecyclerView recyclerView;
    UpcomingTripAdapter adapter;
    List<Trip> tripList;
    FloatingActionButton addBtn;
    Button start;
    private  int MY_PERMISSION = 100;
    Upcoming upcoming;
    View v;

    public Upcoming(View v) {
        // Required empty public constructor
        this.v = v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
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
        recyclerView=view.findViewById(R.id.recView);
        adapter=new UpcomingTripAdapter(tripList);
        recyclerView.setAdapter(adapter);
        addBtn=view.findViewById(R.id.addBtn);
        start = v.findViewById(R.id.startBtn);
        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tripList.add(0,new Trip("test","zagazig","ismailia","2021","15:15","one way","upcoming"));
                adapter.notifyDataSetChanged();
            }
        });
        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_upcoming,container,false);
        return view;
    }

}