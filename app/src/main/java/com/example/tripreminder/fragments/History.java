package com.example.tripreminder.fragments;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.Dialog;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tripreminder.R;
import com.example.tripreminder.adapters.HistoryAdapter;
import com.example.tripreminder.adapters.notesAdapter;
import com.example.tripreminder.beans.HistoryListener;
import com.example.tripreminder.beans.Trips;
import com.example.tripreminder.roomDB.TripsViewModel;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.tripreminder.NotificationReceiver;
import com.example.tripreminder.R;
import com.example.tripreminder.adapters.HistoryAdapter;
import com.example.tripreminder.adapters.UpcomingTripAdapter;
import com.example.tripreminder.adapters.notesAdapter;
import com.example.tripreminder.addTripActivity;
import com.example.tripreminder.beans.HistoryListener;
import com.example.tripreminder.beans.TripListener;
import com.example.tripreminder.beans.Trips;
import com.example.tripreminder.roomDB.TripsViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import static com.example.tripreminder.DialogActivity.notificationIntentKey;

public class History extends Fragment {
    RecyclerView recyclerView;
    HistoryAdapter adapter;
    List<Trips> tripList;
    private TripsViewModel viewModel;
    private HistoryListener itemListener;
    public static Dialog dialog;
    ArrayList<String> noteList = new ArrayList<>();

    public History() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initItemListener();
        tripList=new ArrayList<>();
        noteList=new ArrayList<>();

        recyclerView=view.findViewById(R.id.historyRecView);
        adapter=new HistoryAdapter(itemListener);
        recyclerView.setAdapter(adapter);

        viewModel= ViewModelProviders.of(this).get(TripsViewModel.class);
        viewModel.getAllTrips().observe(getViewLifecycleOwner(), new Observer<List<Trips>>() {
            @Override
            public void onChanged(List<Trips> trips) {
                List<Trips> upcomingTrips=new ArrayList<>();
                for (Trips t:trips) {
                    if(t.getStatus()==1||t.getStatus()==2)
                        upcomingTrips.add(t);
                }
                adapter.saveTrips(upcomingTrips);
                Log.i("note","ol  "+trips.toString());
               // Toast.makeText(getContext(), "Added Successfully", Toast.LENGTH_LONG).show();
            }
        });

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_history, container, false);
    }
    private void initItemListener() {
        itemListener = new HistoryListener() {
            @Override
            public void delete(Trips trip) {
                viewModel.delete(trip);
                AlertDialog diaBox = AskOption(trip);
                diaBox.show();
                cancelAlarm(trip.getId());
            }
            @Override
            public void showNote(Trips trip){
                noteList=trip.getNotesList();
                showDialog(getActivity(),trip);

            }

        };
    }
    public void showDialog(Activity activity, Trips trip){

        dialog = new Dialog(activity);
        dialog.setCancelable(false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setContentView(R.layout.history_note);

        ImageView btnCancel= (ImageView) dialog.findViewById(R.id.img_cancel);
        RecyclerView recyclerView = dialog.findViewById(R.id.recycler);
        notesAdapter adapterRe = new notesAdapter(getContext(),noteList);

        recyclerView.setAdapter(adapterRe);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();

    }

    private AlertDialog AskOption(Trips trip) {
        AlertDialog myQuittingDialogBox = new AlertDialog.Builder(getContext())
                // set message, title, and icon
                .setTitle("Delete")
                .setMessage("Do you want to Delete")
                .setIcon(R.drawable.ic_baseline_delete_forever_24)

                .setPositiveButton("Delete", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int whichButton) {
                        //your deleting code
                        viewModel.delete(trip);
                        dialog.dismiss();
                    }

                })
                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        dialog.dismiss();

                    }
                })
                .create();

        return myQuittingDialogBox;
    }
    private void cancelAlarm(int requestCode) {
        AlarmManager alarmManager = (AlarmManager) getContext().getSystemService(Context.ALARM_SERVICE);
        Intent broadcastIntent= new Intent(getContext(), NotificationReceiver.class);
        broadcastIntent.putExtra(notificationIntentKey,"say goodbye to your data");
        PendingIntent pendingBroadcastIntent=PendingIntent.getBroadcast(getContext(),requestCode,
                broadcastIntent,0);
        alarmManager.cancel(pendingBroadcastIntent);

    }
}