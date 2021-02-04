package com.example.tripreminder;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.tripreminder.adapters.UpcomingTripAdapter;
import com.example.tripreminder.beans.Trip;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class Upcoming extends Fragment {

    RecyclerView recyclerView;
    UpcomingTripAdapter adapter;
    List<Trip> tripList;
    FloatingActionButton addBtn;

    public Upcoming() {
        // Required empty public constructor
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

        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tripList.add(0,new Trip("test","zagazig","ismailia","2021","15:15","one way","upcoming"));
                adapter.notifyDataSetChanged();
            }
        });
    }

    public static Upcoming newInstance(String param1, String param2) {
        Upcoming fragment = new Upcoming();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
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