package com.example.tripreminder.fragments;

import android.app.Activity;
import android.app.AlarmManager;
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

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.tripreminder.NotificationReceiver;
import com.example.tripreminder.R;
import com.example.tripreminder.adapters.UpcomingTripAdapter;
import com.example.tripreminder.addTripActivity;
import com.example.tripreminder.beans.TripListener;
import com.example.tripreminder.beans.Trips;
import com.example.tripreminder.adapters.notesAdapter;
import com.example.tripreminder.roomDB.TripsViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static com.example.tripreminder.DialogActivity.notificationIntentKey;

public class Upcoming extends Fragment {

    RecyclerView recyclerView;
    UpcomingTripAdapter adapter;
    List<Trips> tripList;
    FloatingActionButton addBtn;
    private TripsViewModel viewModel;
    private TripListener menuItemListener;
    public static Dialog dialog;
    ArrayList<String> noteList = new ArrayList<>();


    public Upcoming() {
        // Required empty public constructor
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initPopUpMenuItemListener();
        tripList=new ArrayList<>();
        noteList=new ArrayList<>();

        recyclerView=view.findViewById(R.id.recView);

        adapter=new UpcomingTripAdapter(menuItemListener);
        recyclerView.setAdapter(adapter);

        addBtn=view.findViewById(R.id.addBtn);
        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getContext(), addTripActivity.class);
                startActivityForResult(intent,1);
            }
        });

        viewModel= ViewModelProviders.of(this).get(TripsViewModel.class);
        viewModel.getAllTrips().observe(getViewLifecycleOwner(), new Observer<List<Trips>>() {
            @Override
            public void onChanged(List<Trips> trips) {
                List<Trips> upcomingTrips=new ArrayList<>();
                for (Trips t:trips) {
                    if(t.getStatus()==0)
                        upcomingTrips.add(t);
                }
                adapter.saveTrips(upcomingTrips);
                Log.i("note","ol  "+trips.toString());
                Toast.makeText(getContext(), "Added Successfully", Toast.LENGTH_LONG).show();
            }
        });

    }

    @Override
    public void onResume() {
        super.onResume();
        Toast.makeText(getContext(), "elsize "+adapter.getTrips().size(), Toast.LENGTH_SHORT).show();
        for (int i=0;i<adapter.getTrips().size();i++){
            Log.e("Upcoming",adapter.getTrips().get(i).getDate()+"#@#@"+adapter.getTrips().get(i).getTime());
        }

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_upcoming,container,false);
        return view;
    }

    private void initPopUpMenuItemListener() {
        menuItemListener = new TripListener() {

            @Override
            public void edit(Trips trip) {
                Intent intent=new Intent(getContext(),addTripActivity.class);
                intent.putExtra(addTripActivity.TRIP_OBJ, trip);
                intent.putExtra(addTripActivity.TRIP_ID,trip.getId());
                startActivity(intent);
                Toast.makeText(getContext(), "you clicked on edit", Toast.LENGTH_SHORT).show();

            }

            @Override
            public void delete(Trips trip) {
                AlertDialog diaBox = AskOption(trip);
                diaBox.show();


            }

            @Override
            public void startNav(Trips trip) {
                   //start 
            }

            @Override
            public void cancel(Trips trip) {
                trip.setStatus(1);
                cancelAlarm(7);
                viewModel.update(trip);

            }

            @Override
            public void showNote(Trips trip){
                noteList=trip.getNotesList();
                showDialog(getActivity(),trip);

            }

        };
    }
    public void showDialog(Activity activity,Trips trip){

        dialog = new Dialog(activity);
        dialog.setCancelable(false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setContentView(R.layout.note_dialog);

        ImageView btnCancel= (ImageView) dialog.findViewById(R.id.img_cancel);
        Button addNoteBtn=(Button) dialog.findViewById(R.id.addNoteBtn);
        Button saveNoteBtn=(Button) dialog.findViewById(R.id.saveNoteBtn);
        EditText notetxt=(EditText) dialog.findViewById(R.id.type_note_txt);
        RecyclerView recyclerView = dialog.findViewById(R.id.recycler);
        notesAdapter adapterRe = new notesAdapter(getContext(),noteList);
        recyclerView.setAdapter(adapterRe);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                trip.setNotesList(noteList);
                viewModel.update(trip);
            }
        });
        addNoteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addNoteBtn.setVisibility(View.INVISIBLE);
                notetxt.setVisibility(View.VISIBLE);
                saveNoteBtn.setVisibility(View.VISIBLE);
            }
        });
        saveNoteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String note=notetxt.getText().toString().trim();
                if(!note.isEmpty())
                   noteList.add(notetxt.getText().toString());
                Log.i("note","nossssss"+noteList);
                adapterRe.notifyDataSetChanged();
                notetxt.setVisibility(View.INVISIBLE);
                notetxt.setText("");
                saveNoteBtn.setVisibility(View.INVISIBLE);
                addNoteBtn.setVisibility(View.VISIBLE);

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
        Intent broadcastIntent= new Intent(getContext(),NotificationReceiver.class);
        broadcastIntent.putExtra(notificationIntentKey,"say goodbye to your data");
        PendingIntent pendingBroadcastIntent=PendingIntent.getBroadcast(getContext(),requestCode,
                broadcastIntent,0);
        alarmManager.cancel(pendingBroadcastIntent);

    }
   }
